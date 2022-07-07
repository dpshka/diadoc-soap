package ru.grinn.diadocsoap.service;

import Diadoc.Api.Proto.DocumentTypeProtos;
import Diadoc.Api.Proto.Documents.DocumentProtos;
import Diadoc.Api.document.DocumentsFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.configuration.ApplicationConfiguration;
import ru.grinn.diadocsoap.model.Firm;
import ru.grinn.diadocsoap.model.FirmAddress;
import ru.grinn.diadocsoap.model.InUniversalTransferDocument;
import ru.grinn.diadocsoap.model.InUniversalTransferDocumentStatus;
import ru.grinn.diadocsoap.xjs.*;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class InUniversalTransferDocumentLoaderService {
    private final DiadocService diadocService;
    private final ApplicationConfiguration applicationConfiguration;

    public InUniversalTransferDocumentStatus getIncomingDocuments(Date startDate, String afterIndexKey) throws Exception {
        DocumentsFilter documentsFilter = new DocumentsFilter();
        documentsFilter.setBoxId(diadocService.getMyBoxId());
        documentsFilter.setFilterCategory("Any.InboundWaitingForRecipientSignature");
        if (afterIndexKey != null && afterIndexKey.length() > 0)
            documentsFilter.setAfterIndexKey(afterIndexKey);
        documentsFilter.setFromDocumentDate(diadocService.dateToString(startDate));

        var documents = diadocService
                .getApi()
                .getDocumentClient()
                .getDocuments(documentsFilter);

        ArrayList<InUniversalTransferDocument> resultDocuments = new ArrayList<>();
        documents.getDocumentsList()
                .stream()
                .filter(document -> document.getDocumentType().equals(DocumentTypeProtos.DocumentType.UniversalTransferDocument) || document.getDocumentType().equals(DocumentTypeProtos.DocumentType.UniversalCorrectionDocument))
                .peek(document -> log.info("Found {} {} {} {} {}", document.getDocumentType(), document.getVersion(), document.getFunction(), document.getDocumentDate(), document.getDocumentNumber()))
                .map(this::getInUniversalTransferDocument)
                .filter(Objects::nonNull)
                .forEach(resultDocuments::add);
        return new InUniversalTransferDocumentStatus(
                documents.getDocumentsList().size() > 0 ? documents.getDocumentsList().get(documents.getDocumentsList().size() - 1).getIndexKey() : "",
                documents.getDocumentsList().size() > 0,
                resultDocuments
        );
    }

    private InUniversalTransferDocument getInUniversalTransferDocument(DocumentProtos.Document document) {
        InUniversalTransferDocument resultDocument = new InUniversalTransferDocument();
        try {
            var entityContent = diadocService.getApi().getDocumentClient().getEntityContent(diadocService.getMyBoxId(), document.getMessageId(), document.getEntityId());
            var parsedContent = diadocService.getApi().getParseClient().parseTitleXml(
                    diadocService.getMyBoxId(),
                    document.getTypeNamedId(),
                    document.getFunction(),
                    document.getVersion(),
                    0,
                    entityContent
            );

            resultDocument.setMessageId(document.getMessageId());
            resultDocument.setEntityId(document.getEntityId());
            resultDocument.setRecipientResponseStatus(document.getRecipientResponseStatus().toString());
            resultDocument.setDocflowStatus(document.getDocflowStatus().getPrimaryStatus().getStatusText());

            resultDocument.setDocumentType(document.getDocumentType().name());
            resultDocument.setDocumentVersion(document.getVersion());
            resultDocument.setDocumentFunction(document.getFunction());

            resultDocument.setOriginalDocumentDate(diadocService.stringToDate("01.01.1980"));

            if (document.getDocumentType().equals(DocumentTypeProtos.DocumentType.UniversalTransferDocument) && document.getVersion().endsWith("hyphen")) {
                fillUniversalTransferDocumentUTD(resultDocument, parsedContent);
                return resultDocument;
            }

            if (document.getDocumentType().equals(DocumentTypeProtos.DocumentType.UniversalCorrectionDocument)) {
                fillUniversalTransferDocumentUCD(resultDocument, parsedContent);
                return resultDocument;
            }

        }
        catch (Exception e) {
            log.error("getInUniversalTransferDocument exception {}", e.toString());
            return null;
        }
        log.error("Unknown document type={} version={} function={}", document.getDocumentType().name(), document.getVersion(), document.getFunction());
        return null;
    }

    private void fillUniversalTransferDocumentUTD(InUniversalTransferDocument resultDocument, byte[] parsedContent) throws Exception {
        var unmarshaller = JAXBContext.newInstance(UniversalTransferDocumentWithHyphens.class).createUnmarshaller();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(parsedContent);
        UniversalTransferDocumentWithHyphens diadocDocument = (UniversalTransferDocumentWithHyphens) unmarshaller.unmarshal(inputStream);

        resultDocument.setDocumentNumber(diadocDocument.getDocumentNumber().trim());
        resultDocument.setDocumentDate(diadocService.stringToDate(diadocDocument.getDocumentDate()));

        resultDocument.setBuyer(diadocDocument.getBuyers() != null ? getFirm(diadocDocument.getBuyers().getBuyer().get(0).getOrganizationDetails()) : null);
        resultDocument.setShipper(diadocDocument.getShippers() != null ? getFirm(diadocDocument.getShippers().getShipper().get(0).getOrganizationDetails()) : null);
        resultDocument.setSeller(diadocDocument.getSellers() != null ? getFirm(diadocDocument.getSellers().getSeller().get(0).getOrganizationDetails()) : null);
        resultDocument.setConsignee(diadocDocument.getConsignees() != null ? getFirm(diadocDocument.getConsignees().getConsignee().get(0).getOrganizationDetails()) : null);

        resultDocument.setVatAmount(diadocDocument.getTable().getVat());
        resultDocument.setTotalAmount(diadocDocument.getTable().getTotal());

        if (diadocDocument.getAdditionalInfoId() != null) {
            resultDocument.setActNumber(getActNumber(diadocDocument.getAdditionalInfoId().getAdditionalInfo()));
        }
        if (diadocDocument.getTransferInfo() != null && diadocDocument.getTransferInfo().getTransferBases().getTransferBase().size() > 0) {
            var transferBase = diadocDocument.getTransferInfo().getTransferBases().getTransferBase().get(0);
            resultDocument.setContractNumber(transferBase.getBaseDocumentNumber());
            if (transferBase.getBaseDocumentDate() != null)
                resultDocument.setContractDate(diadocService.stringToDate(transferBase.getBaseDocumentDate()));
        }
    }

    private void fillUniversalTransferDocumentUCD(InUniversalTransferDocument resultDocument, byte[] parsedContent) throws Exception {
        var unmarshaller = JAXBContext.newInstance(UniversalCorrectionDocument.class).createUnmarshaller();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(parsedContent);
        UniversalCorrectionDocument diadocDocument = (UniversalCorrectionDocument) unmarshaller.unmarshal(inputStream);

        resultDocument.setDocumentNumber(diadocDocument.getDocumentNumber());
        resultDocument.setDocumentDate(diadocService.stringToDate(diadocDocument.getDocumentDate()));

        resultDocument.setBuyer(getForeignFirm(diadocDocument.getBuyer().getOrganizationDetails()));
        resultDocument.setSeller(getForeignFirm(diadocDocument.getSeller().getOrganizationDetails()));

        if (diadocDocument.getInvoices().getInvoice().size() > 0) {
            var invoice = diadocDocument.getInvoices().getInvoice().get(0);
            resultDocument.setOriginalDocumentNumber(invoice.getNumber().trim());
            resultDocument.setOriginalDocumentDate(diadocService.stringToDate(invoice.getDate()));
        }
        BigDecimal vat = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        if (diadocDocument.getTable().getTotalsInc() != null) {
            vat = vat.add(diadocDocument.getTable().getTotalsInc().getVat());
            total = total.add(diadocDocument.getTable().getTotalsInc().getTotal());
        }
        if (diadocDocument.getTable().getTotalsDec() != null) {
            vat = vat.subtract(diadocDocument.getTable().getTotalsDec().getVat());
            total = total.subtract(diadocDocument.getTable().getTotalsDec().getTotal());
        }
        resultDocument.setVatAmount(vat);
        resultDocument.setTotalAmount(total);

        if (diadocDocument.getAdditionalInfoId() != null) {
            resultDocument.setActNumber(getActNumber(diadocDocument.getAdditionalInfoId().getAdditionalInfo()));
        }
    }

    private BigInteger getActNumber(List<AdditionalInfo> additionalInfoList) {
        try {
            return additionalInfoList
                   .stream()
                   .filter(info -> info.getId().matches("^номер.*акта$"))
                   .map(info -> new BigInteger(info.getValue().trim()))
                   .findFirst()
                   .orElse(BigInteger.ZERO);
        } catch (Exception e) {
        }
        return BigInteger.ZERO;
    }

    private Firm getFirm(ExtendedOrganizationDetails diadocFirm) {
        if (diadocFirm == null)
            return null;
        var result = new Firm();
        result.setInn(diadocFirm.getInn());
        result.setKpp(diadocFirm.getKpp());
        result.setName(diadocFirm.getOrgName().trim());
        result.setAddress(getFirmAddress(diadocFirm.getAddress()));
        return result;
    }

    private Firm getForeignFirm(ExtendedOrganizationDetailsForeignAddress1000 diadocFirm) {
        if (diadocFirm == null)
            return null;
        var result = new Firm();
        result.setInn(diadocFirm.getInn());
        result.setKpp(diadocFirm.getKpp());
        result.setName(diadocFirm.getOrgName().trim());
        result.setAddress(getForeignFirmAddress(diadocFirm.getAddress()));
        return result;
    }

    private FirmAddress getFirmAddress(Address address) {
        var result = new FirmAddress();
        if (address.getRussianAddress() != null) {
            result.setZipCode(address.getRussianAddress().getZipCode());
            result.setRegion(address.getRussianAddress().getRegion());
            result.setTerritory(address.getRussianAddress().getTerritory());
            result.setCity(address.getRussianAddress().getCity());
            result.setLocality(address.getRussianAddress().getLocality());
            result.setStreet(address.getRussianAddress().getStreet());
            result.setBuilding(address.getRussianAddress().getBuilding());
            result.setBlock(address.getRussianAddress().getBlock());
            result.setApartment(address.getRussianAddress().getApartment());
        }
        if (address.getForeignAddress() != null) {
            result.setFullAddress(address.getForeignAddress().getAddress());
        }
        return result;
    }

    private FirmAddress getForeignFirmAddress(AddressForeignAddress1000 address) {
        var result = new FirmAddress();
        if (address.getRussianAddress() != null) {
            result.setZipCode(address.getRussianAddress().getZipCode());
            result.setRegion(address.getRussianAddress().getRegion());
            result.setTerritory(address.getRussianAddress().getTerritory());
            result.setCity(address.getRussianAddress().getCity());
            result.setLocality(address.getRussianAddress().getLocality());
            result.setStreet(address.getRussianAddress().getStreet());
            result.setBuilding(address.getRussianAddress().getBuilding());
            result.setBlock(address.getRussianAddress().getBlock());
            result.setApartment(address.getRussianAddress().getApartment());
        }
        if (address.getForeignAddress() != null) {
            result.setFullAddress(address.getForeignAddress().getAddress());
        }
        return result;
    }

}
