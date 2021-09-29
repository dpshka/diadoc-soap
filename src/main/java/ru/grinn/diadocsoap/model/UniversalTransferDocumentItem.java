package ru.grinn.diadocsoap.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UniversalTransferDocumentItem {
    private String id;
    private String name;
    private String measureUnit;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal subTotalWithoutVatAmount;
    private int vatRate;
    private BigDecimal vatAmount;
    private BigDecimal subTotalAmount;
}
