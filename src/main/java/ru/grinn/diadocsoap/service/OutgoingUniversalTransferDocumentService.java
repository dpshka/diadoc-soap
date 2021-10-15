package ru.grinn.diadocsoap.service;

import Diadoc.Api.Proto.Events.DiadocMessage_PostApiProtos;
import Diadoc.Api.Proto.Invoicing.Signers.ExtendedSignerProtos;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.configuration.ApplicationConfiguration;
import ru.grinn.diadocsoap.model.*;
import ru.grinn.diadocsoap.xjs.*;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

@Service
@Slf4j
@AllArgsConstructor
public class OutgoingUniversalTransferDocumentService {
    private final DiadocService diadocService;
    private final CertificateService certificateService;
    private final ApplicationConfiguration applicationConfiguration;

    public String sendDocument(UniversalTransferDocument document) throws Exception {
        UniversalTransferDocumentWithHyphens diadocDocument = getUserDataDocument(document);

        var marshaller = JAXBContext.newInstance(UniversalTransferDocumentWithHyphens.class).createMarshaller();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshaller.marshal(diadocDocument, stream);

        byte[] generatedDocument = diadocService.generateUniversalTransferDocumentTitle(stream.toByteArray());
        byte[] signature = certificateService.sign(generatedDocument);

        var documentAttachmentBuilder = DiadocMessage_PostApiProtos.DocumentAttachment.newBuilder();
        documentAttachmentBuilder.setTypeNamedId(applicationConfiguration.getUtdTypeNameId());
        documentAttachmentBuilder.setFunction(applicationConfiguration.getUtdFunction());
        documentAttachmentBuilder.setVersion(applicationConfiguration.getUtdVersion());

        var signedContentBuilder = DiadocMessage_PostApiProtos.SignedContent.newBuilder();
        signedContentBuilder.setContent(ByteString.copyFrom(generatedDocument));
        signedContentBuilder.setSignature(ByteString.copyFrom(signature));
        documentAttachmentBuilder.setSignedContent(signedContentBuilder);

        var messageToPostBuilder = DiadocMessage_PostApiProtos.MessageToPost.newBuilder();
        messageToPostBuilder.addDocumentAttachments(documentAttachmentBuilder);
        messageToPostBuilder.setFromBoxId(diadocService.getMyBoxId());
        String departmentId = diadocService.getMyDepartmentId(document.getShipper().getKpp());
        if (departmentId != null)
            messageToPostBuilder.setFromDepartmentId(departmentId);
        var toBoxId = diadocService.getBoxId(diadocService.getOrganization(document.getBuyer().getInn()));
        messageToPostBuilder.setToBoxId(toBoxId);

        var postMessageResponse = diadocService.getApi().getMessageClient().postMessage(messageToPostBuilder.build());
        return postMessageResponse.getMessageId();
    }

    private UniversalTransferDocumentWithHyphens getUserDataDocument(UniversalTransferDocument document) {
        var userdataDocument  = new UniversalTransferDocumentWithHyphens();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        userdataDocument.setDocumentNumber(document.getDocumentNumber());
        userdataDocument.setDocumentDate(dateFormat.format(document.getDocumentDate()));
        userdataDocument.setCurrency("643");
        userdataDocument.setFunction(applicationConfiguration.getUtdFunction());
        userdataDocument.setDocumentCreator(document.getSeller().getName());

        var diadocSellers = new UniversalTransferDocumentWithHyphens.Sellers();
        diadocSellers.getSeller().add(getDiadocOrganizationInfo(document.getSeller()));
        userdataDocument.setSellers(diadocSellers);

        var diadocShipper = new UniversalTransferDocumentWithHyphens.Shippers.Shipper();
        diadocShipper.setOrganizationDetails(getDiadocOrganizationDetails(document.getShipper()));
        var diadocShippers = new UniversalTransferDocumentWithHyphens.Shippers();
        diadocShippers.getShipper().add(diadocShipper);
        userdataDocument.setShippers(diadocShippers);

        var diadocBuyers = new UniversalTransferDocumentWithHyphens.Buyers();
        diadocBuyers.getBuyer().add(getDiadocOrganizationInfo(document.getBuyer()));
        userdataDocument.setBuyers(diadocBuyers);

        var diadocConsignees = new UniversalTransferDocumentWithHyphens.Consignees();
        diadocConsignees.getConsignee().add(getDiadocOrganizationInfo(document.getConsignee()));
        userdataDocument.setConsignees(diadocConsignees);

        var diadocDocumentShipment = new UniversalTransferDocumentWithHyphens.DocumentShipments.DocumentShipment();
        diadocDocumentShipment.setName("УПД");
        diadocDocumentShipment.setNumber(document.getShipmentDocumentNumber());
        diadocDocumentShipment.setDate(dateFormat.format(document.getShipmentDocumentDate()));
        var diadocDocumentShipments = new UniversalTransferDocumentWithHyphens.DocumentShipments();
        diadocDocumentShipments.getDocumentShipment().add(diadocDocumentShipment);
        userdataDocument.setDocumentShipments(diadocDocumentShipments);

        var diadocSigners = new UniversalTransferDocumentWithHyphens.Signers();
        diadocSigners.getSignerReferenceOrSignerDetails().add(getDoadocSignerDetails(document));
        userdataDocument.setSigners(diadocSigners);

        var diadocInvoiceTable = new InvoiceTable();
        diadocInvoiceTable.setTotal(document.getTotalAmount());
        diadocInvoiceTable.setVat(document.getVatAmount());
        diadocInvoiceTable.setTotalWithVatExcluded(document.getTotalWithoutVatAmount());
        document.getItems().forEach(item -> diadocInvoiceTable.getItem().add(getDiadocInvoiceTableItem(item)));
        userdataDocument.setTable(diadocInvoiceTable);

        var diadocTransferInfo = new TransferInfo();
        diadocTransferInfo.setOperationInfo("Товары переданы");
        userdataDocument.setTransferInfo(diadocTransferInfo);

        return userdataDocument;
    }

    private ExtendedSignerDetailsSellerTitle getDoadocSignerDetails(UniversalTransferDocument document) {
        var diadocSignerDetails = new ExtendedSignerDetailsSellerTitle();
        diadocSignerDetails.setInn(document.getSeller().getInn());
        diadocSignerDetails.setFirstName(applicationConfiguration.getSignerFirstName());
        diadocSignerDetails.setMiddleName(applicationConfiguration.getSignerMiddleName());
        diadocSignerDetails.setLastName(applicationConfiguration.getSignerLastName());
        diadocSignerDetails.setSignerPowers(BigInteger.valueOf(ExtendedSignerProtos.SignerPowers.ResponsibleForOperationAndSignerForInvoice_VALUE));
        diadocSignerDetails.setSignerStatus(BigInteger.valueOf(ExtendedSignerProtos.SignerStatus.SellerEmployee_VALUE));
        diadocSignerDetails.setSignerType(String.valueOf(ExtendedSignerProtos.SignerType.LegalEntity_VALUE));
        return diadocSignerDetails;
    }

    private InvoiceTable.Item getDiadocInvoiceTableItem(UniversalTransferDocumentItem item) {
        var diadocInvoiceTableItem = new InvoiceTable.Item();
        diadocInvoiceTableItem.setItemVendorCode(item.getId());
        diadocInvoiceTableItem.setProduct(item.getName());
        diadocInvoiceTableItem.setUnit(item.getMeasureUnit());
        diadocInvoiceTableItem.setPrice(item.getPrice());
        diadocInvoiceTableItem.setQuantity(item.getQuantity());
        diadocInvoiceTableItem.setSubtotalWithVatExcluded(item.getSubTotalWithoutVatAmount());
        diadocInvoiceTableItem.setTaxRate(String.format("%d%%", item.getVatRate()));
        diadocInvoiceTableItem.setVat(item.getVatAmount());
        diadocInvoiceTableItem.setSubtotal(item.getSubTotalAmount());
        return diadocInvoiceTableItem;
    }

    private ExtendedOrganizationDetailsWithHyphens getDiadocOrganizationDetails(Firm firm) {
        var diadocOrganisationDetails = new ExtendedOrganizationDetailsWithHyphens();
        diadocOrganisationDetails.setInn(firm.getInn());
        diadocOrganisationDetails.setKpp(firm.getKpp() != null && firm.getKpp().length() > 0 ? firm.getKpp() : null);
        diadocOrganisationDetails.setOrgName(firm.getName());
        diadocOrganisationDetails.setOrgType(firm.getKpp() != null && firm.getKpp().length() > 0 ? "1" : "2");
        diadocOrganisationDetails.setAddress(getDiadocAddress(firm.getAddress()));
        return diadocOrganisationDetails;
    }

    private ExtendedOrganizationInfoWithHyphens getDiadocOrganizationInfo(Firm firm) {
        var diadocOrganisationInfo = new ExtendedOrganizationInfoWithHyphens();
        diadocOrganisationInfo.setOrganizationDetails(getDiadocOrganizationDetails(firm));
        return diadocOrganisationInfo;
    }

    private Address getDiadocAddress(FirmAddress address) {
        var diadocRussianAddress = new RussianAddress();
        diadocRussianAddress.setZipCode(address.getZipCode());
        diadocRussianAddress.setRegion(address.getRegion());
        diadocRussianAddress.setTerritory(address.getTerritory());
        diadocRussianAddress.setCity(address.getCity());
        diadocRussianAddress.setLocality(address.getLocality());
        diadocRussianAddress.setStreet(address.getStreet());
        diadocRussianAddress.setBuilding(address.getBuilding());
        diadocRussianAddress.setBlock(address.getBlock());
        diadocRussianAddress.setApartment(address.getApartment());
        var diadocAddress = new Address();
        diadocAddress.setRussianAddress(diadocRussianAddress);
        return diadocAddress;
    }

}
