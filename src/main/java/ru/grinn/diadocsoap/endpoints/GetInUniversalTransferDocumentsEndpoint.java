package ru.grinn.diadocsoap.endpoints;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.grinn.diadocsoap.model.Firm;
import ru.grinn.diadocsoap.model.FirmAddress;
import ru.grinn.diadocsoap.model.InUniversalTransferDocument;
import ru.grinn.diadocsoap.service.InUniversalTransferDocumentLoaderService;
import ru.grinn.diadocsoap.xjs.*;

import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;

@Endpoint
@AllArgsConstructor
@Slf4j
public class GetInUniversalTransferDocumentsEndpoint {

    private static final String NAMESPACE_URI = "http://www.grinn-corp.ru/gestori/edo";

    private final InUniversalTransferDocumentLoaderService incomingUniversalTransferDocumentLoaderService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetIncomingUniversalTransferDocumentsRequest")
    @ResponsePayload
    public GetIncomingUniversalTransferDocumentsResponse send(@RequestPayload GetIncomingUniversalTransferDocumentsRequest request) {
        var response = new GetIncomingUniversalTransferDocumentsResponse();
        try {
            var documentStatus = incomingUniversalTransferDocumentLoaderService.getIncomingDocuments(request.getStartDate().toGregorianCalendar().getTime(), request.getAfterIndexKey());
            IncomingUniversalTransferDocuments responseDocuments = new IncomingUniversalTransferDocuments();
            documentStatus.getDocuments().forEach(document -> responseDocuments.getIncomingDocument().add(getDocument(document)));
            response.setIncomingDocuments(responseDocuments);

            response.setHasMoreResults(documentStatus.isHasMoreResults());
            response.setLastIndexKey(documentStatus.getLastIndexKey());
            response.setStatusCode("OK");
            response.setStatusMessage("OK");
        } catch (Exception e) {
            e.printStackTrace();
            response.setHasMoreResults(false);
            response.setLastIndexKey("");
            response.setStatusCode("ERROR");
            response.setStatusMessage("Error " + e.getMessage());
        }
        return response;
    }

    private IncomingUniversalTransferDocument getDocument(InUniversalTransferDocument document) {
        var result = new IncomingUniversalTransferDocument();
        result.setMessageId(document.getMessageId());
        result.setEntityId(document.getEntityId());
        result.setRecipientResponseStatus(document.getRecipientResponseStatus());
        result.setDocflowStatus(document.getDocflowStatus());
        result.setDocumentType(document.getDocumentType());
        result.setDocumentVersion(document.getDocumentVersion());
        result.setDocumentFunction(document.getDocumentFunction());
        result.setDocumentNumber(document.getDocumentNumber());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(document.getDocumentDate());
        try {
            result.setDocumentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        result.setOriginalDocumentNumber(document.getOriginalDocumentNumber());
        gregorianCalendar.setTime(document.getOriginalDocumentDate());
        try {
            result.setOriginalDocumentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        result.setSeller(getFirm(document.getSeller()));
        result.setShipper(getFirm(document.getShipper()));
        result.setBuyer(getFirm(document.getBuyer()));
        result.setConsignee(getFirm(document.getConsignee()));
        result.setVatAmount(document.getVatAmount());
        result.setTotalAmount(document.getTotalAmount());
        result.setActNumber(document.getActNumber());
        result.setContractNumber(document.getContractNumber());

        if (document.getContractDate() != null) {
            gregorianCalendar.setTime(document.getContractDate());
            try {
                result.setContractDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private IncomingUniversalTransferDocumentFirm getFirm(Firm firm) {
        var result = new IncomingUniversalTransferDocumentFirm();
        if (firm != null) {
            result.setINN(firm.getInn());
            result.setKPP(firm.getKpp());
            result.setName(replaceSpecialFirmName(firm.getName()));
            result.setAddress(getFirmAddress(firm.getAddress()));
        }
        return result;
    }

    private IncomingUniversalTransferDocumentFirmAddress getFirmAddress(FirmAddress address) {
        var result = new IncomingUniversalTransferDocumentFirmAddress();
        if (address != null) {
            result.setZipCode(address.getZipCode());
            result.setRegion(address.getRegion());
            result.setTerritory(address.getTerritory());
            result.setCity(address.getCity());
            result.setLocality(address.getLocality());
            result.setStreet(address.getStreet());
            result.setBuilding(address.getBuilding());
            result.setBlock(address.getBlock());
            result.setApartment(address.getApartment());
            result.setFullAddress(address.getFullAddress());
        }
        return result;
    }

    private String replaceSpecialFirmName(String firmName) {
        firmName = firmName.replaceAll("[«»]", "\"");
        firmName = firmName.replaceAll("№", "N");
        return firmName;
    }
}