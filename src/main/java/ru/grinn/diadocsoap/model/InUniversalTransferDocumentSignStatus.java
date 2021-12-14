package ru.grinn.diadocsoap.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class InUniversalTransferDocumentSignStatus {
    private String status;
    private String message;
}
