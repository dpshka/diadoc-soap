package ru.grinn.diadocsoap.model;

import lombok.Data;

@Data
public class FirmAddress {
    private String zipCode;
    private String region;
    private String territory;
    private String city;
    private String locality;
    private String street;
    private String building;
    private String block;
    private String apartment;
    private String fullAddress;
}
