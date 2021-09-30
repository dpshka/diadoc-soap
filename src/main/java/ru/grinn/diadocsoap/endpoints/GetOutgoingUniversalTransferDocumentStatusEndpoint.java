package ru.grinn.diadocsoap.endpoints;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ru.grinn.diadocsoap.service.OutgoingUniversalTransferDocumentService;
import ru.grinn.diadocsoap.xjs.*;

@Endpoint
public class GetOutgoingUniversalTransferDocumentStatusEndpoint {

    private static final String NAMESPACE_URI = "http://www.grinn-corp.ru/gestori/edo";

    private final OutgoingUniversalTransferDocumentService outgoingUniversalTransferDocumentService;

    @Autowired
    public GetOutgoingUniversalTransferDocumentStatusEndpoint(OutgoingUniversalTransferDocumentService outgoingUniversalTransferDocumentService) {
        this.outgoingUniversalTransferDocumentService = outgoingUniversalTransferDocumentService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOutgoingUniversalTransferDocumentStatusRequest")
    @ResponsePayload
    public GetOutgoingUniversalTransferDocumentStatusResponse send(@RequestPayload GetOutgoingUniversalTransferDocumentStatusRequest request) {
        var response = new GetOutgoingUniversalTransferDocumentStatusResponse();
/*
        try {
            String documentNumber = request.getDocumentNumber();
            Date documentDate = request.getDocumentDate().toGregorianCalendar().getTime();
            var document = getDocument(request.getDocument());
            outgoingUniversalTransferDocumentService.sendDocument(document);
            response.setResult(String.format("Накладная %s от %s отправлена", request.getDocument().getDocumentNumber(), request.getDocument().getDocumentDate().toString()));
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResult("Ошибка отправки накладной " + e.getMessage());
        }
*/
        return response;
    }

}
