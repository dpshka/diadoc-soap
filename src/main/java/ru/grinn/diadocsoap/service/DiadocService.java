package ru.grinn.diadocsoap.service;

import Diadoc.Api.DiadocApi;
import Diadoc.Api.Proto.DocumentTypeProtos;
import Diadoc.Api.Proto.Documents.DocumentProtos;
import Diadoc.Api.Proto.Events.DiadocMessage_PostApiProtos;
import Diadoc.Api.Proto.OrganizationProtos;
import Diadoc.Api.auth.DiadocCredentials;
import Diadoc.Api.documentType.XsdContentType;
import Diadoc.Api.exceptions.DiadocSdkException;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.configuration.ApplicationConfiguration;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DiadocService {
    private final ApplicationConfiguration applicationConfiguration;
    private final CertificateService certificateService;

    private final DiadocApi api;

    private Date lastAuthDate;
    private static final long AUTH_PERIOD_IN_MILLIS = 1000 * 60 * 60 * 6; // Six hours

    private final OrganizationProtos.Organization myOrganization;
    private final String myOrganizationBoxId;

    private final static String TEST_ORGANIZATION_INN = "9637612488";
    private final OrganizationProtos.Organization testOrganization;
    private final String testOrganizationBoxId;

    private final SimpleDateFormat dateFormat;

    public DiadocService(ApplicationConfiguration applicationConfiguration, CertificateService certificateService) throws DiadocSdkException {
        this.applicationConfiguration = applicationConfiguration;
        this.certificateService = certificateService;

        api = new DiadocApi(applicationConfiguration.getApiClientId(), applicationConfiguration.getApiUrl());
        authenticate();

        myOrganization = getOrganization(applicationConfiguration.getOrganizationInn());
        myOrganizationBoxId = getBoxId(myOrganization);

        testOrganization = getOrganization(TEST_ORGANIZATION_INN);
        testOrganizationBoxId = getBoxId(testOrganization);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        //saveDocumentTypes( "d:/temp/types.dat");
        //saveUserContractXsd("UniversalCorrectionDocument", "КСЧФДИС", "ucd736_05_01_02", 1, "d:/temp/UserContract_UCD_BuyerTitle.xsd");
    }

    private void authenticate() throws DiadocSdkException {
        api.getAuthClient().authenticate(applicationConfiguration.getLogin(), applicationConfiguration.getPassword());
        Credentials credentials = api.getAuthManager().getCredentialsProvider().getCredentials(AuthScope.ANY);
        if (credentials instanceof DiadocCredentials diadocCredentials) {
            var token = diadocCredentials.getAuthToken();
            log.info("Successfull authentification, token = {}", token);
            lastAuthDate = new Date();
        }
        else {
            throw new RuntimeException("Token is not DiadocCredentials");
        }
    }

    public DiadocApi getApi() {
        if (lastAuthDate == null || (new Date().getTime() - lastAuthDate.getTime()) > AUTH_PERIOD_IN_MILLIS) {
            try {
                authenticate();
            }
            catch (DiadocSdkException e) {
                throw new RuntimeException(e);
            }
        }
        return api;
    }

    public OrganizationProtos.Organization getOrganization(String inn) throws DiadocSdkException {
        return getApi().getOrganizationClient().getOrganizationByInn(inn);
    }

    public OrganizationProtos.Organization getOrganizationByFnsParticipantId(String fnsParticipantId) throws DiadocSdkException {
        return getApi().getOrganizationClient().getOrganizationByFnsParticipantId(fnsParticipantId);
    }

    public String getBoxId(OrganizationProtos.Organization organization) throws DiadocSdkException {
        List<OrganizationProtos.Box> boxes = organization.getBoxesList();
        if (boxes.size() > 1) {
            log.info("Found {} boxes for organisation {}. Getting first...", boxes.size(), organization.getFullName());
        }
        return boxes.stream().findFirst().orElseThrow(() -> new DiadocSdkException("Box count = 0")).getBoxId();
    }

    public OrganizationProtos.Organization getMyOrganization() {
        return myOrganization;
    }

    public OrganizationProtos.Organization getTestOrganization() {
        return testOrganization;
    }

    public String getTestBoxId() {
        return testOrganizationBoxId;
    }

    public String getMyDepartmentId(String kpp) {
        try {
            return getApi().getDepartmentClient()
                    .getDepartments(myOrganizationBoxId)
                    .getDepartmentsList()
                    .stream()
                    .filter(department -> department.getKpp().equals(kpp))
                    .findFirst()
                    .orElseThrow(() -> new DiadocSdkException("department not found"))
                    .getId();
        }
        catch (DiadocSdkException e) {
            return null;
        }
    }

    public String getMyBoxId() {
        return myOrganizationBoxId;
    }

    private void saveDocumentTypes(String filename) {
        try {
            var content = getApi().getDocumentTypeClient().getDocumentTypesV2(getMyBoxId());
            var output = new FileOutputStream(filename);
            content.getDocumentTypesList().stream().forEach(documentType -> {
                    try {
                        documentType.writeDelimitedTo(output);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            );
            output.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUserContractXsd(String type, String function, String version, int titleIndex, String filename) {
        try {
            var content = getApi().getDocumentTypeClient().getContent(type, function, version, titleIndex,  XsdContentType.UserContractXsd);
            var output = new FileOutputStream(filename);
            output.write(content.getBytes());
            output.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] generateUniversalTransferDocumentTitle(byte [] document) throws DiadocSdkException {
        var generatedTitle = getApi().getGenerateClient().generateTitleXml(myOrganizationBoxId,
                applicationConfiguration.getUtdTypeNameId(),
                applicationConfiguration.getUtdFunction(),
                applicationConfiguration.getUtdVersion(),
                0,
                document,
                "",
                "");
        return generatedTitle.getContent();
    }

    public byte[] generateUniversalTransferDocumentBuyerTitle(byte [] document, String messageId, String entityId) throws DiadocSdkException {
        var generatedTitle = getApi().getGenerateClient().generateTitleXml(myOrganizationBoxId,
                applicationConfiguration.getUtdTypeNameId(),
                applicationConfiguration.getUtdFunction(),
                applicationConfiguration.getUtdVersion(),
                1,
                document,
                messageId,
                entityId);
        return generatedTitle.getContent();
    }

    public byte[] generateUniversalCorrectionDocumentBuyerTitle(byte [] document, String messageId, String entityId) throws DiadocSdkException {
        var generatedTitle = getApi().getGenerateClient().generateTitleXml(myOrganizationBoxId,
                applicationConfiguration.getUcdTypeNameId(),
                applicationConfiguration.getUcdFunction(),
                applicationConfiguration.getUcdVersion(),
                1,
                document,
                messageId,
                entityId);
        return generatedTitle.getContent();
    }

    public DocumentProtos.Document getUTDDocument(String messageId) throws DiadocSdkException {
        var documentList = getApi().getDocumentClient().getDocumentsByMessageId(getMyBoxId(), messageId);
        return documentList.getDocumentsList().stream()
                .filter(document -> document.getDocumentType().equals(DocumentTypeProtos.DocumentType.UniversalTransferDocument))
                .findFirst()
                .orElseThrow(() -> new DiadocSdkException("Universal transfer document not found in message"));
    }

    public DiadocMessage_PostApiProtos.SignedContent signMessage(byte[] document) throws Exception {
        byte[] signature = certificateService.sign(document);
        var signedContentBuilder = DiadocMessage_PostApiProtos.SignedContent.newBuilder();
        signedContentBuilder.setContent(ByteString.copyFrom(document));
        signedContentBuilder.setSignature(ByteString.copyFrom(signature));
        return signedContentBuilder.build();
    }

    public String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public Date stringToDate(String s) {
        try {
            return dateFormat.parse(s);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
