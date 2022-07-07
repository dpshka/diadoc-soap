package ru.grinn.diadocsoap.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class InUniversalTransferDocument {

    private String messageId;
    private String entityId;
    private String recipientResponseStatus;
    private String docflowStatus;
    private String documentType;
    private String documentVersion;
    private String documentFunction;
    private String documentNumber;
    private Date documentDate;
    private String originalDocumentNumber;
    private Date originalDocumentDate;
    private Firm seller;
    private Firm shipper;
    private Firm buyer;
    private Firm consignee;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private BigInteger actNumber;
    private String contractNumber;
    private Date contractDate;
}
