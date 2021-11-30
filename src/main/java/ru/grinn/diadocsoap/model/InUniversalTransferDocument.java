package ru.grinn.diadocsoap.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class InUniversalTransferDocument {

    private String documentNumber;
    private Date documentDate;
    private Firm seller;
    private Firm shipper;
    private Firm buyer;
    private Firm consignee;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private BigInteger ActNumber;
}
