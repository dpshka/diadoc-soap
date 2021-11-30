package ru.grinn.diadocsoap.endpoints;

import lombok.AllArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ru.grinn.diadocsoap.model.*;
import ru.grinn.diadocsoap.service.OutUniversalTransferDocumentService;
import ru.grinn.diadocsoap.xjs.*;

@Endpoint
@AllArgsConstructor
public class SendOutUniversalTransferDocumentEndpoint {

    private static final String NAMESPACE_URI = "http://www.grinn-corp.ru/gestori/edo";

    private final OutUniversalTransferDocumentService outgoingUniversalTransferDocumentService;

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

    private OutUniversalTransferDocument getDocument(OutgoingUniversalTransferDocument document) {
        var result = new OutUniversalTransferDocument();
        result.setDocumentNumber(document.getDocumentNumber());
        result.setDocumentDate(document.getDocumentDate().toGregorianCalendar().getTime());
        result.setShipmentDocumentNumber(document.getShipmentDocumentNumber());
        result.setShipmentDocumentDate(document.getShipmentDocumentDate().toGregorianCalendar().getTime());
        result.setSeller(getFirm(document.getSeller()));
        result.setShipper(getFirm(document.getShipper()));
        result.setBuyer(getFirm(document.getBuyer()));
        result.setConsignee(getFirm(document.getConsignee()));
        result.setTransferEmployee(getTransferEmployee(document.getTransferEmployee()));
        result.setVatAmount(document.getVatAmount());
        result.setTotalAmount(document.getTotalAmount());
        result.setTotalWithoutVatAmount(document.getTotalWithoutVatAmount());

        document.getItems().getItem().forEach(requestItem -> result.addItem(getItem(requestItem)));
        return result;
    }

    private OutUniversalTransferDocumentItem getItem(OutgoingUniversalTransferDocumentItem item) {
        var result = new OutUniversalTransferDocumentItem();
        result.setId(item.getId());
        result.setName(item.getName());
        result.setMeasureUnit(item.getMeasureUnit());
        result.setPrice(item.getPrice());
        result.setQuantity(item.getQuantity());
        result.setSubTotalWithoutVatAmount(item.getSubTotalWithoutVatAmount());
        result.setVatAmount(item.getVatAmount());
        result.setVatRate(item.getVatRate().intValue());
        result.setSubTotalAmount(item.getSubTotalAmount());
        return result;
    }

    private FirmAddress getAddress(OutgoingUniversalTransferDocumentFirmAddress address) {
        var result = new FirmAddress();
        if (address.getZipCode().length() > 0)
            result.setZipCode(address.getZipCode());
        if (address.getRegion().length() > 0)
            result.setRegion(address.getRegion());
        if (address.getTerritory().length() > 0)
            result.setTerritory(address.getTerritory());
        if (address.getCity().length() > 0)
            result.setCity(address.getCity());
        if (address.getLocality().length() > 0)
            result.setLocality(address.getLocality());
        if (address.getStreet().length() > 0)
            result.setStreet(address.getStreet());
        if (address.getBuilding().length() > 0)
            result.setBuilding(address.getBuilding());
        if (address.getBlock().length() > 0)
            result.setBlock(address.getBlock());
        if (address.getApartment().length() > 0)
            result.setApartment(address.getApartment());
        return result;
    }

    private Firm getFirm(OutgoingUniversalTransferDocumentFirm firm) {
        var result = new Firm();
        result.setInn(firm.getINN());
        result.setKpp(firm.getKPP());
        result.setName(firm.getName());
        result.setAddress(getAddress(firm.getAddress()));
        result.setFnsPartipantId(firm.getFnsParticipantId());
        return result;
    }

    private TransferEmployee getTransferEmployee(OutgoingUniversalTransferDocumentEmployee employee) {
        var result = new TransferEmployee();
        result.setFirstName(employee.getFirstName());
        result.setMiddleName(employee.getMiddleName());
        result.setLastName(employee.getLastName());
        result.setPosition(employee.getPosition());
        return result;
    }

}
