package ru.grinn.diadocsoap.endpoints;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.grinn.diadocsoap.service.InUniversalTransferDocumentSignerService;
import ru.grinn.diadocsoap.xjs.*;

@Endpoint
@AllArgsConstructor
@Slf4j
public class SignInUniversalTransferDocumentEndpoint {

    private static final String NAMESPACE_URI = "http://www.grinn-corp.ru/gestori/edo";

    private final InUniversalTransferDocumentSignerService incomingUniversalTransferDocumentSignerService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SignIncomingUniversalTransferDocumentRequest")
    @ResponsePayload
    public SignIncomingUniversalTransferDocumentResponse send(@RequestPayload SignIncomingUniversalTransferDocumentRequest request) {
        var response = new SignIncomingUniversalTransferDocumentResponse();
        try {
            if (request.getSign().intValue() == 1) {
                var documentSignStatus = incomingUniversalTransferDocumentSignerService.signUniversalTransferDocument(request.getMessageId(), request.getEntityId(), request.getAcceptanceDate().toGregorianCalendar().getTime());
                response.setStatusCode(documentSignStatus.getStatus());
                response.setStatusMessage(documentSignStatus.getMessage());
            }
            if (request.getSign().intValue() == 2) {
                var documentSignStatus = incomingUniversalTransferDocumentSignerService.signUniversalCorrectionDocument(request.getMessageId(), request.getEntityId());
                response.setStatusCode(documentSignStatus.getStatus());
                response.setStatusMessage(documentSignStatus.getMessage());
            }
            if (request.getSign().intValue() == 3) {
                var documentSignStatus = incomingUniversalTransferDocumentSignerService.rejectDocument(request.getMessageId(), request.getEntityId());
                response.setStatusCode(documentSignStatus.getStatus());
                response.setStatusMessage(documentSignStatus.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode("ERROR");
            response.setStatusMessage("Ошибка подписания накладной: " + e.getMessage());
        }
        return response;
    }
}
