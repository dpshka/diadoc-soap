package ru.grinn.diadocsoap.service;

import Diadoc.Api.DiadocApi;
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
import java.util.List;

@Service @Slf4j
public class DiadocService {
    private final static String LOGIN = "timur.konic@gmail.com";
    private final static String PASSWORD = "ndLMsZxe4Fc4WP";

    private final static String MY_ORGANIZATION_INN = "9681399131";
    private final static String TEST_ORGANIZATION_INN = "9637612488";

    private final ApplicationConfiguration applicationConfiguration;

    private final DiadocApi api;
    private final OrganizationProtos.Organization myOrganization;
    private final String myOrganizationBoxId;
    private final OrganizationProtos.Organization testOrganization;
    private final String testOrganizationBoxId;

    public DiadocService(ApplicationConfiguration applicationConfiguration) throws DiadocSdkException {
        this.applicationConfiguration = applicationConfiguration;

        api = new DiadocApi(applicationConfiguration.getApiClientId(), applicationConfiguration.getApiUrl());
        authentificate();
        myOrganization = getOrganization(MY_ORGANIZATION_INN);
        myOrganizationBoxId = getBoxId(myOrganization);
        testOrganization = getOrganization(TEST_ORGANIZATION_INN);
        testOrganizationBoxId = getBoxId(testOrganization);
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

    public OrganizationProtos.Organization getTestOrganization() throws DiadocSdkException {
        return testOrganization;
    }

    public String getTestBoxId() throws DiadocSdkException {
        return testOrganizationBoxId;
    }

}
