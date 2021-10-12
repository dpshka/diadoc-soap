package ru.grinn.diadocsoap.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UniversalTransferDocument {

    private String documentNumber;
    private Date documentDate;
    private String shipmentDocumentNumber;
    private Date shipmentDocumentDate;
    private Firm seller;
    private Firm shipper;
    private Firm buyer;
    private Firm consignee;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalWithoutVatAmount;
    private String signatureStatus;

    private List<UniversalTransferDocumentItem> items;

    public UniversalTransferDocument() {
        items = new ArrayList<>();
    }

    public void addItem(UniversalTransferDocumentItem item) {
        items.add(item);
    }

}
