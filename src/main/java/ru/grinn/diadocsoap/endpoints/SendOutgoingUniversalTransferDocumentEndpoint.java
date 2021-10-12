package ru.grinn.diadocsoap.endpoints;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ru.grinn.diadocsoap.model.*;
import ru.grinn.diadocsoap.service.OutgoingUniversalTransferDocumentService;
import ru.grinn.diadocsoap.xjs.*;

@Endpoint
@AllArgsConstructor
public class SendOutgoingUniversalTransferDocumentEndpoint {

    private static final String NAMESPACE_URI = "http://www.grinn-corp.ru/gestori/edo";

    private final OutgoingUniversalTransferDocumentService outgoingUniversalTransferDocumentService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendOutgoingUniversalTransferDocumentRequest")
    @ResponsePayload
    public SendOutgoingUniversalTransferDocumentResponse send(@RequestPayload SendOutgoingUniversalTransferDocumentRequest request) {
        var response = new SendOutgoingUniversalTransferDocumentResponse();
        try {
            var document = getDocument(request.getDocument());
            String messageId = outgoingUniversalTransferDocumentService.sendDocument(document);
            response.setMessageId(messageId);
            response.setStatusCode("OK");
            response.setStatusMessage(String.format("Накладная %s от %s отправлена", request.getDocument().getDocumentNumber(), request.getDocument().getDocumentDate().toString()));
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode("ERROR");
            response.setStatusMessage("Ошибка отправки накладной " + e.getMessage());
        }
        return response;
    }

    private UniversalTransferDocument getDocument(OutgoingUniversalTransferDocument requestDocument) {
        UniversalTransferDocument document = new UniversalTransferDocument();
        document.setDocumentNumber(requestDocument.getDocumentNumber());
        document.setDocumentDate(requestDocument.getDocumentDate().toGregorianCalendar().getTime());
        document.setShipmentDocumentNumber(requestDocument.getShipmentDocumentNumber());
        document.setShipmentDocumentDate(requestDocument.getShipmentDocumentDate().toGregorianCalendar().getTime());
        document.setSeller(getFirm(requestDocument.getSeller()));
        document.setShipper(getFirm(requestDocument.getShipper()));
        document.setBuyer(getFirm(requestDocument.getBuyer()));
        document.setConsignee(getFirm(requestDocument.getConsignee()));
        document.setVatAmount(requestDocument.getVatAmount());
        document.setTotalAmount(requestDocument.getTotalAmount());
        document.setTotalWithoutVatAmount(requestDocument.getTotalWithoutVatAmount());

        requestDocument.getItems().getItem().forEach(requestItem -> document.addItem(getItem(requestItem)));
        return document;
    }

    private UniversalTransferDocumentItem getItem(OutgoingUniversalTransferDocumentItem requestItem) {
        UniversalTransferDocumentItem item = new UniversalTransferDocumentItem();
        item.setId(requestItem.getId());
        item.setName(requestItem.getName());
        item.setMeasureUnit(requestItem.getMeasureUnit());
        item.setPrice(requestItem.getPrice());
        item.setQuantity(requestItem.getQuantity());
        item.setSubTotalWithoutVatAmount(requestItem.getSubTotalWithoutVatAmount());
        item.setVatAmount(requestItem.getVatAmount());
        item.setVatRate(requestItem.getVatRate().intValue());
        item.setSubTotalAmount(requestItem.getSubTotalAmount());
        return item;
    }

    private FirmAddress getAddress(OutgoingUniversalTransferDocumentFirmAddress requestAddress) {
        FirmAddress address = new FirmAddress();
        if (requestAddress.getZipCode().length() > 0)
            address.setZipCode(requestAddress.getZipCode());
        if (requestAddress.getRegion().length() > 0)
            address.setRegion(requestAddress.getRegion());
        if (requestAddress.getTerritory().length() > 0)
            address.setTerritory(requestAddress.getTerritory());
        if (requestAddress.getCity().length() > 0)
            address.setCity(requestAddress.getCity());
        if (requestAddress.getLocality().length() > 0)
            address.setLocality(requestAddress.getLocality());
        if (requestAddress.getStreet().length() > 0)
            address.setStreet(requestAddress.getStreet());
        if (requestAddress.getBuilding().length() > 0)
            address.setBuilding(requestAddress.getBuilding());
        if (requestAddress.getBlock().length() > 0)
            address.setBlock(requestAddress.getBlock());
        if (requestAddress.getApartment().length() > 0)
            address.setApartment(requestAddress.getApartment());
        return address;
    }

    private Firm getFirm(OutgoingUniversalTransferDocumentFirm requestFirm) {
        Firm firm = new Firm();
        firm.setInn(requestFirm.getINN());
        firm.setKpp(requestFirm.getKPP());
        firm.setName(requestFirm.getName());
        firm.setAddress(getAddress(requestFirm.getAddress()));
        return firm;
    }

}
