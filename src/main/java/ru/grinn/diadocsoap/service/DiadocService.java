package ru.grinn.diadocsoap.service;

import Diadoc.Api.DiadocApi;
import Diadoc.Api.Proto.DocumentTypeProtos;
import Diadoc.Api.Proto.Documents.DocumentProtos;
import Diadoc.Api.Proto.OrganizationProtos;
import Diadoc.Api.auth.DiadocCredentials;
import Diadoc.Api.documentType.XsdContentType;
import Diadoc.Api.exceptions.DiadocSdkException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.configuration.ApplicationConfiguration;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DiadocService {
    private final ApplicationConfiguration applicationConfiguration;

    private final DiadocApi api;

    private Date lastAuthDate;
    private static final long AUTH_PERIOD_IN_MILLIS = 1000 * 60 * 60 * 6; // Six hours

    private final OrganizationProtos.Organization myOrganization;
    private final String myOrganizationBoxId;

    private final static String TEST_ORGANIZATION_INN = "9637612488";
    private final OrganizationProtos.Organization testOrganization;
    private final String testOrganizationBoxId;

    public DiadocService(ApplicationConfiguration applicationConfiguration) throws DiadocSdkException {
        this.applicationConfiguration = applicationConfiguration;

        api = new DiadocApi(applicationConfiguration.getApiClientId(), applicationConfiguration.getApiUrl());
        authenticate();

        myOrganization = getOrganization(applicationConfiguration.getOrganizationInn());
        myOrganizationBoxId = getBoxId(myOrganization);

        testOrganization = getOrganization(TEST_ORGANIZATION_INN);
        testOrganizationBoxId = getBoxId(testOrganization);
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

    private void saveUserContractXsd(String filename) throws Exception {
        var content = getApi().getDocumentTypeClient().getContent(applicationConfiguration.getUtdTypeNameId(), applicationConfiguration.getUtdFunction(), applicationConfiguration.getUtdVersion(), 0,  XsdContentType.UserContractXsd);
        var output = new FileOutputStream(filename);
        output.write(content.getBytes());
        output.close();
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

    public DocumentProtos.Document getDocument(String messageId) throws DiadocSdkException {
        var documentList = getApi().getDocumentClient().getDocumentsByMessageId(getMyBoxId(), messageId);
        return documentList.getDocumentsList().stream()
                .filter(document -> document.getDocumentType().equals(DocumentTypeProtos.DocumentType.UniversalTransferDocument))
                .findFirst()
                .orElseThrow(() -> new DiadocSdkException("Universal transfer document not found in message"));
    }

}
