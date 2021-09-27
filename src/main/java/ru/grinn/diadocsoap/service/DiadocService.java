package ru.grinn.diadocsoap.service;

import Diadoc.Api.DiadocApi;
import Diadoc.Api.Proto.OrganizationProtos;
import Diadoc.Api.auth.DiadocCredentials;
import Diadoc.Api.exceptions.DiadocSdkException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

@Service @Slf4j
public class DiadocService {
    private final static String API_CLIENT_ID = "Grinn-1e43d899-ada1-4b1d-8271-9b09ad7bc9ca";
    private final static String API_URL = "https://diadoc-api.kontur.ru";

    private final static String LOGIN = "timur.konic@gmail.com";
    private final static String PASSWORD = "ndLMsZxe4Fc4WP";

    private final static String MY_ORGANIZATION_INN = "9681399131";

    private final DiadocApi api;
    private final OrganizationProtos.Organization myOrganization;
    private final String myOrganizationBoxId;

    public DiadocService() throws DiadocSdkException {
        api = new DiadocApi(API_CLIENT_ID, API_URL);
        authentificate();
        myOrganization = getOrganization(MY_ORGANIZATION_INN);
        myOrganizationBoxId = getBoxId(myOrganization);
        saveUserContractXsd();
    }

    private void authentificate() throws DiadocSdkException {
        api.getAuthClient().authenticate(LOGIN, PASSWORD);
        Credentials credentials = api.getAuthManager().getCredentialsProvider().getCredentials(AuthScope.ANY);
        if (credentials instanceof DiadocCredentials diadocCredentials) {
            var token = diadocCredentials.getAuthToken();
            log.info("Successfull authentification, token = {}", token);
        }
        else {
            throw new RuntimeException("token is not DiadocCredentials");
        }
    }

    public DiadocApi getApi() { return api; }

    public OrganizationProtos.Organization getOrganization(String inn) throws DiadocSdkException {
        return api.getOrganizationClient().getOrganizationByInn(inn);
    }

    public String getBoxId(OrganizationProtos.Organization organization) throws DiadocSdkException {
        List<OrganizationProtos.Box> boxes = organization.getBoxesList();
        if (boxes.size() == 0) {
            throw new DiadocSdkException("Box count = 0");
        }
        if (boxes.size() > 1) {
            log.info("Found {} boxes for organisation {}. Getting first...", boxes.size(), organization.getFullName());
        }
        return boxes.get(0).getBoxId();
    }

    public OrganizationProtos.Organization getMyOrganization() throws DiadocSdkException {
        return myOrganization;
    }

    public String getMyBoxId() throws DiadocSdkException {
        return myOrganizationBoxId;
    }

    private void saveUserContractXsd() {
        try {
            var content = api.getDocumentTypeClient().getContent("UniversalTransferDocument", "СЧФДОП", "utd820_05_01_02_hyphen", 0);
            var output = new FileOutputStream("d:/temp/UserContract.xsd");
            output.write(content.getBytes());
            output.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
