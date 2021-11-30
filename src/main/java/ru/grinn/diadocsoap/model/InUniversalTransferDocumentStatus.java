package ru.grinn.diadocsoap.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class InUniversalTransferDocumentStatus {
    private String lastIndexKey;
    private boolean hasMoreResults;
    private List<InUniversalTransferDocument> documents;
}
