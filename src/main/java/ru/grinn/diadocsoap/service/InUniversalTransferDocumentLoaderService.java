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
import ru.grinn.diadocsoap.xjs.Address;
import ru.grinn.diadocsoap.xjs.ExtendedOrganizationDetails;
import ru.grinn.diadocsoap.xjs.UniversalTransferDocumentWithHyphens;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Service
@Slf4j
@AllArgsConstructor
public class InUniversalTransferDocumentLoaderService {
    private final DiadocService diadocService;
    private final ApplicationConfiguration applicationConfiguration;

    public InUniversalTransferDocumentStatus getIncomingDocuments(String afterIndexKey) throws Exception {
        DocumentsFilter documentsFilter = new DocumentsFilter();
        documentsFilter.setBoxId(diadocService.getMyBoxId());
        documentsFilter.setFilterCategory("Any.InboundWaitingForRecipientSignature");
        if (afterIndexKey != null && afterIndexKey.length() > 0)
            documentsFilter.setAfterIndexKey(afterIndexKey);
        documentsFilter.setFromDocumentDate(applicationConfiguration.getIncomingStartDate());

        var documents = diadocService
                .getApi()
                .getDocumentClient()
                .getDocuments(documentsFilter);

        ArrayList<InUniversalTransferDocument> resultDocuments = new ArrayList<>();
        documents.getDocumentsList()
                .stream()
                .filter(document -> document.getDocumentType().equals(DocumentTypeProtos.DocumentType.UniversalTransferDocument))
                .filter(document -> document.getTypeNamedId().equals(applicationConfiguration.getUtdTypeNameId()))
                .filter(document -> document.getFunction().equals(applicationConfiguration.getUtdFunction()))
                .filter(document -> document.getVersion().endsWith("hyphen"))
                .forEach(document -> resultDocuments.add(getInUniversalTransferDocument(document)));

        InUniversalTransferDocumentStatus result = new InUniversalTransferDocumentStatus(
                documents.getDocumentsList().size() > 0 ? documents.getDocumentsList().get(documents.getDocumentsList().size() - 1).getIndexKey() : "",
                documents.getHasMoreResults(),
                resultDocuments
        );
        return result;
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

            var unmarshaller = JAXBContext.newInstance(UniversalTransferDocumentWithHyphens.class).createUnmarshaller();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(parsedContent);
            UniversalTransferDocumentWithHyphens diadocDocument = (UniversalTransferDocumentWithHyphens)unmarshaller.unmarshal(inputStream);

            resultDocument.setDocumentNumber(diadocDocument.getDocumentNumber());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            resultDocument.setDocumentDate(dateFormat.parse(diadocDocument.getDocumentDate()));

            resultDocument.setBuyer(diadocDocument.getBuyers() != null ? getFirm(diadocDocument.getBuyers().getBuyer().get(0).getOrganizationDetails()) : null);
            resultDocument.setShipper(diadocDocument.getShippers() != null ? getFirm(diadocDocument.getShippers().getShipper().get(0).getOrganizationDetails()) : null);
            resultDocument.setSeller(diadocDocument.getSellers() != null ? getFirm(diadocDocument.getSellers().getSeller().get(0).getOrganizationDetails()) : null);
            resultDocument.setConsignee(diadocDocument.getConsignees() != null ? getFirm(diadocDocument.getConsignees().getConsignee().get(0).getOrganizationDetails()) : null);

            resultDocument.setVatAmount(diadocDocument.getTable().getVat());
            resultDocument.setTotalAmount(diadocDocument.getTable().getTotal());

            if (diadocDocument.getAdditionalInfoId() != null) {
                try {
                    resultDocument.setActNumber(diadocDocument
                            .getAdditionalInfoId()
                            .getAdditionalInfo()
                            .stream()
                            .filter(info -> info.getId().matches("^номер.*акта$"))
                            .map(info -> new BigInteger(info.getValue()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Act number exception")));
                }
                catch (Exception e) {
                    resultDocument.setActNumber(BigInteger.ZERO);
                }
            }
            else {
                resultDocument.setActNumber(BigInteger.ZERO);
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultDocument;
    }

    private Firm getFirm(ExtendedOrganizationDetails diadocFirm) {
        if (diadocFirm == null)
            return null;
        var result = new Firm();
        result.setInn(diadocFirm.getInn());
        result.setKpp(diadocFirm.getKpp());
        result.setName(diadocFirm.getOrgName());
        result.setAddress(getFirmAddress(diadocFirm.getAddress()));
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
        return result;
    }
}
