package ru.grinn.diadocsoap.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.model.UniversalTransferDocument;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Service
@Slf4j
public class UniversalTransferDocumentLoaderService {
    private final DiadocService diadocService;

    @Autowired
    public UniversalTransferDocumentLoaderService(DiadocService diadocService) {
        this.diadocService = diadocService;
    }

    public UniversalTransferDocument getOutgoingDocument(String messageId) throws Exception {
        var document = new UniversalTransferDocument();
        var diadocDocument = diadocService.getDocument(messageId);
        document.setDocumentNumber(diadocDocument.getDocumentNumber());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        document.setDocumentDate(dateFormat.parse(diadocDocument.getDocumentDate()));
        document.setVatAmount(new BigDecimal(diadocDocument.getUniversalTransferDocumentMetadata().getVat()));
        document.setTotalAmount(new BigDecimal(diadocDocument.getUniversalTransferDocumentMetadata().getTotal()));
        document.setSignatureStatus(diadocDocument.getRecipientResponseStatus().toString());
        return document;
    }
}
