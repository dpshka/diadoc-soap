package ru.grinn.diadocsoap.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data @AllArgsConstructor
public class UniversalTransferDocumentItem {
    public static final int NDS_10 = 10;
    public static final int NDS_20 = 20;

    private String name;
    private BigDecimal price;
    private BigDecimal quantity;
    private int nds;

    public BigDecimal getSubtotal() {
        return price.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
    }
}
