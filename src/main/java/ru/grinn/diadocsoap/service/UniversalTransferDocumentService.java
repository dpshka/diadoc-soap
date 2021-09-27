package ru.grinn.diadocsoap.service;

import Diadoc.Api.Proto.OrganizationProtos;
import Diadoc.Api.exceptions.DiadocSdkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.grinn.diadocsoap.model.UniversalTransferDocument;
import ru.grinn.diadocsoap.xjs.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

@Service
public class UniversalTransferDocumentService {
    private DiadocService diadocService;

    @Autowired
    public UniversalTransferDocumentService(DiadocService diadocService) {
        this.diadocService = diadocService;
    }

    public void buildDocument(UniversalTransferDocument document) {
        try {
            UniversalTransferDocumentWithHyphens xmlDocument = getXmlUserDataDocument(document);

            JAXBContext context = JAXBContext.newInstance(UniversalTransferDocumentWithHyphens.class);
            Marshaller m = context.createMarshaller();
            m.marshal(xmlDocument, System.out);

            ByteArrayOutputStream s = new ByteArrayOutputStream();
            m.marshal(xmlDocument, s);

            var generatedTitle = diadocService.getApi().getGenerateClient().generateTitleXml(
                    diadocService.getMyBoxId(),
                    "UniversalTransferDocument",
                    "СЧФДОП",
                    "utd820_05_01_01_hyphen",
                    0,
                    s.toByteArray(),
                    "",
                    "");

            System.out.println(generatedTitle.toString());
            generatedTitle.saveContentToFile("d:/temp/test.xml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UniversalTransferDocumentWithHyphens getXmlUserDataDocument(UniversalTransferDocument document) throws DiadocSdkException {
        var xmlDocument  = new UniversalTransferDocumentWithHyphens();
        xmlDocument.setDocumentNumber(document.getDocumentNumber());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        xmlDocument.setDocumentDate(dateFormat.format(document.getDocumentDate()));
        xmlDocument.setCurrency("643");
        xmlDocument.setFunction("СЧФДОП");
        xmlDocument.setDocumentCreator(diadocService.getMyOrganization().getFullName());

        var xmlSellers = new UniversalTransferDocumentWithHyphens.Sellers();
        xmlSellers.getSeller().add(getXmlOrganizationInfo(diadocService.getMyOrganization().getInn()));
        xmlDocument.setSellers(xmlSellers);

        var xmlBuyers = new UniversalTransferDocumentWithHyphens.Buyers();
        xmlBuyers.getBuyer().add(getXmlOrganizationInfo(document.getBuyerINN()));
        xmlDocument.setBuyers(xmlBuyers);

        var xmlSignerDetails = new ExtendedSignerDetailsSellerTitle();
        xmlSignerDetails.setInn(diadocService.getMyOrganization().getInn());
        xmlSignerDetails.setFirstName("Тимур");
        xmlSignerDetails.setLastName("Коник");
        xmlSignerDetails.setSignerPowers(BigInteger.ONE);
        xmlSignerDetails.setSignerStatus(BigInteger.ONE);
        xmlSignerDetails.setSignerType("1");
        var xmlSigners = new UniversalTransferDocumentWithHyphens.Signers();
        xmlSigners.getSignerReferenceOrSignerDetails().add(xmlSignerDetails);
        xmlDocument.setSigners(xmlSigners);

        var xmlInvoiceTable = new InvoiceTable();
        xmlInvoiceTable.setTotal(BigDecimal.ZERO);
        xmlInvoiceTable.setVat(BigDecimal.ZERO);
        document.getItems().forEach(item -> {
            var xmlInvoiceTableItem = new InvoiceTable.Item();
            xmlInvoiceTableItem.setProduct(item.getName());
            xmlInvoiceTableItem.setPrice(item.getPrice());
            xmlInvoiceTableItem.setQuantity(item.getQuantity());
            xmlInvoiceTableItem.setSubtotal(item.getSubtotal());
            xmlInvoiceTableItem.setHyphenVat("true");
            xmlInvoiceTableItem.setHyphenUnit("true");
            switch (item.getNds()) {
                case 10:
                    xmlInvoiceTableItem.setTaxRate("10/110");
                    break;
                case 20:
                    xmlInvoiceTableItem.setTaxRate("20/120");
                    break;
                default:
                    throw new RuntimeException("Unknown tax rate");
            }
            xmlInvoiceTable.getItem().add(xmlInvoiceTableItem);
        });
        xmlDocument.setTable(xmlInvoiceTable);

        var xmlTransferInfo = new TransferInfo();
        xmlTransferInfo.setOperationInfo("Товары переданы");
        xmlDocument.setTransferInfo(xmlTransferInfo);

        return xmlDocument;
    }
    private Address getXmlAddress(OrganizationProtos.Organization organization) {
        var xmlRussianAddress = new RussianAddress();
        var russianAddress = organization.getAddress().getRussianAddress();
        if (russianAddress.getRegion().length() > 0)
            xmlRussianAddress.setRegion(russianAddress.getRegion());
        if (russianAddress.getTerritory().length() > 0)
            xmlRussianAddress.setTerritory(russianAddress.getTerritory());
        if (russianAddress.getCity().length() > 0)
            xmlRussianAddress.setCity(russianAddress.getCity());
        if (russianAddress.getLocality().length() > 0)
            xmlRussianAddress.setLocality(russianAddress.getLocality());
        if (russianAddress.getStreet().length() > 0)
            xmlRussianAddress.setStreet(russianAddress.getStreet());
        if (russianAddress.getBuilding().length() > 0)
            xmlRussianAddress.setBuilding(russianAddress.getBuilding());
        if (russianAddress.getBlock().length() > 0)
            xmlRussianAddress.setBlock(russianAddress.getBlock());
        if (russianAddress.getApartment().length() > 0)
            xmlRussianAddress.setApartment(russianAddress.getApartment());

        var xmlAddress = new Address();
        xmlAddress.setRussianAddress(xmlRussianAddress);
        return xmlAddress;
    }

    private ExtendedOrganizationInfoWithHyphens getXmlOrganizationInfo(String inn) throws DiadocSdkException {
        var organisation = diadocService.getOrganization(inn);

        var xmlOrganisationDetails = new ExtendedOrganizationDetailsWithHyphens();
        xmlOrganisationDetails.setInn(organisation.getInn());
        xmlOrganisationDetails.setOrgName(organisation.getFullName());
        xmlOrganisationDetails.setOrgType("1");
        xmlOrganisationDetails.setAddress(getXmlAddress(organisation));

        var xmlOrganisationInfo = new ExtendedOrganizationInfoWithHyphens();
        xmlOrganisationInfo.setOrganizationDetails(xmlOrganisationDetails);
        return xmlOrganisationInfo;
    }


}
