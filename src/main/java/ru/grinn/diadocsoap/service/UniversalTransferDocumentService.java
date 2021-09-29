package ru.grinn.diadocsoap.service;

import Diadoc.Api.Proto.Events.DiadocMessage_PostApiProtos;
import Diadoc.Api.Proto.Invoicing.InvoiceInfoProtos;
import Diadoc.Api.Proto.Invoicing.Signers.ExtendedSignerProtos;
import Diadoc.Api.documentType.XsdContentType;
import Diadoc.Api.exceptions.DiadocSdkException;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.configuration.ApplicationConfiguration;
import ru.grinn.diadocsoap.model.Firm;
import ru.grinn.diadocsoap.model.FirmAddress;
import ru.grinn.diadocsoap.model.UniversalTransferDocument;
import ru.grinn.diadocsoap.model.UniversalTransferDocumentItem;
import ru.grinn.diadocsoap.xjs.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

@Service
public class UniversalTransferDocumentService {
    private final DiadocService diadocService;
    private final CertificateService certificateService;
    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public UniversalTransferDocumentService(DiadocService diadocService, CertificateService certificateService, ApplicationConfiguration applicationConfiguration) {
        this.diadocService = diadocService;
        this.certificateService = certificateService;
        this.applicationConfiguration = applicationConfiguration;
    }

    public void buildDocument(UniversalTransferDocument document) throws Exception {
        UniversalTransferDocumentWithHyphens xmlDocument = getXmlUserDataDocument(document);

        JAXBContext context = JAXBContext.newInstance(UniversalTransferDocumentWithHyphens.class);
        Marshaller m = context.createMarshaller();

//        FileOutputStream f = new FileOutputStream("d:/temp/userdata.xml");
//        m.marshal(xmlDocument, f);
//        f.close();

        ByteArrayOutputStream s = new ByteArrayOutputStream();
        m.marshal(xmlDocument, s);

        var generatedTitle = diadocService.getApi().getGenerateClient().generateTitleXml(
                diadocService.getMyBoxId(),
                applicationConfiguration.getUtdTypeNameId(),
                applicationConfiguration.getUtdFunction(),
                applicationConfiguration.getUtdVersion(),
                0,
                s.toByteArray(),
                "",
                "");

        byte[] signature = certificateService.sign(generatedTitle.getContent());

        var documentAttachmentBuilder = DiadocMessage_PostApiProtos.DocumentAttachment.newBuilder();
        documentAttachmentBuilder.setTypeNamedId(applicationConfiguration.getUtdTypeNameId());
        documentAttachmentBuilder.setFunction(applicationConfiguration.getUtdFunction());
        documentAttachmentBuilder.setVersion(applicationConfiguration.getUtdVersion());

        var signedContentBuilder = DiadocMessage_PostApiProtos.SignedContent.newBuilder();
        signedContentBuilder.setContent(ByteString.copyFrom(generatedTitle.getContent()));
        signedContentBuilder.setSignature(ByteString.copyFrom(signature));
        documentAttachmentBuilder.setSignedContent(signedContentBuilder);

        var messageToPostBuilder = DiadocMessage_PostApiProtos.MessageToPost.newBuilder();
        messageToPostBuilder.addDocumentAttachments(documentAttachmentBuilder);
        messageToPostBuilder.setFromBoxId(diadocService.getMyBoxId());
        messageToPostBuilder.setToBoxId(diadocService.getTestBoxId());

        var response = diadocService.getApi().getMessageClient().postMessage(messageToPostBuilder.build());
        System.out.println("returned message's id " + response.getMessageId());
        System.out.println("returned message's type " + response.getMessageType().name());
    }

    private UniversalTransferDocumentWithHyphens getXmlUserDataDocument(UniversalTransferDocument document) throws DiadocSdkException {
        var xmlDocument  = new UniversalTransferDocumentWithHyphens();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        xmlDocument.setDocumentNumber(document.getDocumentNumber());
        xmlDocument.setDocumentDate(dateFormat.format(document.getDocumentDate()));
        xmlDocument.setCurrency("643");
        xmlDocument.setFunction(applicationConfiguration.getUtdFunction());
        xmlDocument.setDocumentCreator(document.getSeller().getName());

        var xmlSellers = new UniversalTransferDocumentWithHyphens.Sellers();
        xmlSellers.getSeller().add(getXmlOrganizationInfo(document.getSeller()));
        xmlDocument.setSellers(xmlSellers);

        var xmlShipper = new UniversalTransferDocumentWithHyphens.Shippers.Shipper();
        xmlShipper.setOrganizationDetails(getXmlOrganizationDetails(document.getShipper()));
        var xmlShippers = new UniversalTransferDocumentWithHyphens.Shippers();
        xmlShippers.getShipper().add(xmlShipper);
        xmlDocument.setShippers(xmlShippers);

        var xmlBuyers = new UniversalTransferDocumentWithHyphens.Buyers();
        xmlBuyers.getBuyer().add(getXmlOrganizationInfo(document.getBuyer()));
        xmlDocument.setBuyers(xmlBuyers);

        var xmlConsignees = new UniversalTransferDocumentWithHyphens.Consignees();
        xmlConsignees.getConsignee().add(getXmlOrganizationInfo(document.getConsignee()));
        xmlDocument.setConsignees(xmlConsignees);

        var xmlDocumentShipment = new UniversalTransferDocumentWithHyphens.DocumentShipments.DocumentShipment();
        xmlDocumentShipment.setName("УПД");
        xmlDocumentShipment.setNumber(document.getShipmentDocumentNumber());
        xmlDocumentShipment.setDate(dateFormat.format(document.getShipmentDocumentDate()));
        var xmlDocumentShipments = new UniversalTransferDocumentWithHyphens.DocumentShipments();
        xmlDocumentShipments.getDocumentShipment().add(xmlDocumentShipment);
        xmlDocument.setDocumentShipments(xmlDocumentShipments);

        var xmlSigners = new UniversalTransferDocumentWithHyphens.Signers();
        xmlSigners.getSignerReferenceOrSignerDetails().add(getXmlSignerDetails(document));
        xmlDocument.setSigners(xmlSigners);

        var xmlInvoiceTable = new InvoiceTable();
        xmlInvoiceTable.setTotal(document.getTotalAmount());
        xmlInvoiceTable.setVat(document.getVatAmount());
        document.getItems().forEach(item -> xmlInvoiceTable.getItem().add(getXmlInvoiceTableItem(item)));
        xmlDocument.setTable(xmlInvoiceTable);

        var xmlTransferInfo = new TransferInfo();
        xmlTransferInfo.setOperationInfo("Товары переданы");
        xmlDocument.setTransferInfo(xmlTransferInfo);

        return xmlDocument;
    }

    // TO DO
    private ExtendedSignerDetailsSellerTitle getXmlSignerDetails(UniversalTransferDocument document) {
        var xmlSignerDetails = new ExtendedSignerDetailsSellerTitle();
        xmlSignerDetails.setInn(document.getSeller().getInn());
        xmlSignerDetails.setFirstName("Анастасия");
        xmlSignerDetails.setMiddleName("Геннадьевна");
        xmlSignerDetails.setLastName("Воронина");
        xmlSignerDetails.setSignerPowers(BigInteger.valueOf(ExtendedSignerProtos.SignerPowers.ResponsibleForOperationAndSignerForInvoice_VALUE));
        xmlSignerDetails.setSignerStatus(BigInteger.valueOf(ExtendedSignerProtos.SignerStatus.SellerEmployee_VALUE));
        xmlSignerDetails.setSignerType(String.valueOf(ExtendedSignerProtos.SignerType.LegalEntity_VALUE));
        return xmlSignerDetails;
    }

    private InvoiceTable.Item getXmlInvoiceTableItem(UniversalTransferDocumentItem item) {
        var xmlInvoiceTableItem = new InvoiceTable.Item();
        xmlInvoiceTableItem.setItemVendorCode(item.getId());
        xmlInvoiceTableItem.setProduct(item.getName());
        xmlInvoiceTableItem.setUnit(item.getMeasureUnit());
        xmlInvoiceTableItem.setPrice(item.getPrice());
        xmlInvoiceTableItem.setQuantity(item.getQuantity());
        xmlInvoiceTableItem.setSubtotalWithVatExcluded(item.getSubTotalWithoutVatAmount());
        xmlInvoiceTableItem.setTaxRate(String.format("%d%%", item.getVatRate()));
        xmlInvoiceTableItem.setVat(item.getVatAmount());
        xmlInvoiceTableItem.setSubtotal(item.getSubTotalAmount());
        return xmlInvoiceTableItem;
    }

    private ExtendedOrganizationDetailsWithHyphens getXmlOrganizationDetails(Firm firm) {
        var xmlOrganisationDetails = new ExtendedOrganizationDetailsWithHyphens();
        xmlOrganisationDetails.setInn(firm.getInn());
        xmlOrganisationDetails.setKpp(firm.getKpp());
        xmlOrganisationDetails.setOrgName(firm.getName());
        xmlOrganisationDetails.setOrgType("1"); // TO DO
        xmlOrganisationDetails.setAddress(getXmlAddress(firm.getAddress()));
        return xmlOrganisationDetails;
    }

    private ExtendedOrganizationInfoWithHyphens getXmlOrganizationInfo(Firm firm) throws DiadocSdkException {
        var xmlOrganisationInfo = new ExtendedOrganizationInfoWithHyphens();
        xmlOrganisationInfo.setOrganizationDetails(getXmlOrganizationDetails(firm));
        return xmlOrganisationInfo;
    }

    private Address getXmlAddress(FirmAddress address) {
        var xmlRussianAddress = new RussianAddress();
        xmlRussianAddress.setZipCode(address.getZipCode());
        xmlRussianAddress.setRegion(address.getRegion());
        xmlRussianAddress.setTerritory(address.getTerritory());
        xmlRussianAddress.setCity(address.getCity());
        xmlRussianAddress.setLocality(address.getLocality());
        xmlRussianAddress.setStreet(address.getStreet());
        xmlRussianAddress.setBuilding(address.getBuilding());
        xmlRussianAddress.setBlock(address.getBlock());
        xmlRussianAddress.setApartment(address.getApartment());
        var xmlAddress = new Address();
        xmlAddress.setRussianAddress(xmlRussianAddress);
        return xmlAddress;
    }

    private void saveUserContractXsd(String filename) throws Exception {
        var content = diadocService.getApi().getDocumentTypeClient().getContent(applicationConfiguration.getUtdTypeNameId(), applicationConfiguration.getUtdFunction(), applicationConfiguration.getUtdVersion(), 0,  XsdContentType.UserContractXsd);
        var output = new FileOutputStream(filename);
        output.write(content.getBytes());
        output.close();
    }

}
