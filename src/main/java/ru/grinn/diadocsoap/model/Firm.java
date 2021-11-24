package ru.grinn.diadocsoap.model;

import lombok.Data;

@Data
public class Firm {
    private String inn;
    private String kpp;
    private String name;
    private FirmAddress address;
    private String fnsPartipantId;
}
