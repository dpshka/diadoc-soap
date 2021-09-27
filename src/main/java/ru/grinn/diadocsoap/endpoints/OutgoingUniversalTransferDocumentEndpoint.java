package ru.grinn.diadocsoap.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ru.grinn.diadocsoap.model.UniversalTransferDocument;
import ru.grinn.diadocsoap.model.UniversalTransferDocumentItem;
import ru.grinn.diadocsoap.service.UniversalTransferDocumentService;
import ru.grinn.diadocsoap.xjs.SendOutgoingUniversalTransferDocumentRequest;
import ru.grinn.diadocsoap.xjs.SendOutgoingUniversalTransferDocumentResponse;

import java.math.BigDecimal;
import java.util.Date;

@Endpoint
public class OutgoingUniversalTransferDocumentEndpoint {

    private static final String NAMESPACE_URI = "http://www.grinn-corp.ru/gestori/edo";

    private final UniversalTransferDocumentService universalTransferDocumentService;

    @Autowired
    public OutgoingUniversalTransferDocumentEndpoint(UniversalTransferDocumentService universalTransferDocumentService) {
        this.universalTransferDocumentService = universalTransferDocumentService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendOutgoingUniversalTransferDocumentRequest")
    @ResponsePayload
    public SendOutgoingUniversalTransferDocumentResponse send(@RequestPayload SendOutgoingUniversalTransferDocumentRequest request) {
        var response = new SendOutgoingUniversalTransferDocumentResponse();

        UniversalTransferDocument document = new UniversalTransferDocument();
        document.setDocumentNumber("1237890");
        document.setDocumentDate(new Date());
        document.setBuyerINN("4629045050");
        document.addItem(new UniversalTransferDocumentItem("Молоко", new BigDecimal("50"), BigDecimal.ONE, UniversalTransferDocumentItem.NDS_10));
        document.addItem(new UniversalTransferDocumentItem("Колбаса", new BigDecimal("50"), BigDecimal.ONE, UniversalTransferDocumentItem.NDS_20));

        universalTransferDocumentService.buildDocument(document);

        response.setResult(String.format("Накладная %s от %s отправлена", request.getDocument().getDocNumber(), request.getDocument().getDocDate().toString()));
        return response;
    }
}
