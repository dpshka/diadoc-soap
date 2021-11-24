package ru.grinn.diadocsoap.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "diadoc-soap")
@Data
public class ApplicationConfiguration {
    private String apiClientId;
    private String apiUrl;
    private String login;
    private String password;
    private String certificateAlias;
    private String organizationInn;
    private String keyPassword;
    private String utdTypeNameId;
    private String utdFunction;
    private String utdVersion;
    private String signerFirstName;
    private String signerMiddleName;
    private String signerLastName;
    private String signerTitle;
}
