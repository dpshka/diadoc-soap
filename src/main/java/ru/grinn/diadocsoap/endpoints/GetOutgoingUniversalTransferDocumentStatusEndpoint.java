package ru.grinn.diadocsoap.endpoints;

import java.util.GregorianCalendar;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ru.grinn.diadocsoap.model.UniversalTransferDocument;
import ru.grinn.diadocsoap.service.UniversalTransferDocumentLoaderService;
import ru.grinn.diadocsoap.xjs.*;

import javax.xml.datatype.DatatypeFactory;

@Endpoint
@AllArgsConstructor
public class GetOutgoingUniversalTransferDocumentStatusEndpoint {

    private static final String NAMESPACE_URI = "http://www.grinn-corp.ru/gestori/edo";

    private final UniversalTransferDocumentLoaderService universalTransferDocumentLoaderService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOutgoingUniversalTransferDocumentStatusRequest")
    @ResponsePayload
    public GetOutgoingUniversalTransferDocumentStatusResponse send(@RequestPayload GetOutgoingUniversalTransferDocumentStatusRequest request) {
        var response = new GetOutgoingUniversalTransferDocumentStatusResponse();
        try {
            UniversalTransferDocument document = universalTransferDocumentLoaderService.getOutgoingDocument(request.getMessageId());
            response.setStatusCode("OK");
            response.setStatusMessage("Документ найден");
            var documentStatus = new OutgoingUniversalTransferDocumentStatus();
            documentStatus.setDocumentNumber(document.getDocumentNumber());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(document.getDocumentDate());
            documentStatus.setDocumentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
            documentStatus.setVatAmount(document.getVatAmount());
            documentStatus.setTotalAmount(document.getTotalAmount());
            documentStatus.setSignatureStatus(document.getSignatureStatus());
            response.setDocument(documentStatus);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode("ERROR");
            response.setStatusMessage("Not found");
        }
        return response;
    }

}
