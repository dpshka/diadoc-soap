package ru.grinn.diadocsoap.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.model.OutUniversalTransferDocument;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Service
@Slf4j
@AllArgsConstructor
public class OutUniversalTransferDocumentLoaderService {
    private final DiadocService diadocService;

    public OutUniversalTransferDocument getOutgoingDocument(String messageId) throws Exception {
        var document = new OutUniversalTransferDocument();
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
