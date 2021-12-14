package ru.grinn.diadocsoap.service;

import Diadoc.Api.Proto.Events.DiadocMessage_PostApiProtos;
import Diadoc.Api.Proto.Invoicing.SignatureRejectionInfoProtos;
import Diadoc.Api.Proto.Invoicing.SignerProtos;
import Diadoc.Api.httpClient.GeneratedFile;
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

@Service
@Slf4j
@AllArgsConstructor
public class InUniversalTransferDocumentSignerService {
    private final DiadocService diadocService;
    private final CertificateService certificateService;
    private final ApplicationConfiguration applicationConfiguration;

    public InUniversalTransferDocumentSignStatus signUniversalTransferDocument(String messageId, String entityId) throws Exception {
        UniversalTransferDocumentBuyerTitle diadocBuyerTitle = getUniversalTransferDocumentBuyerTitle();
        var marshaller = JAXBContext.newInstance(UniversalTransferDocumentBuyerTitle.class).createMarshaller();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshaller.marshal(diadocBuyerTitle, stream);

        byte[] generatedDocument = diadocService.generateUniversalTransferDocumentBuyerTitle(stream.toByteArray(), messageId, entityId);

        var recipientBuilder = DiadocMessage_PostApiProtos.RecipientTitleAttachment.newBuilder();
        recipientBuilder.setParentEntityId(entityId);
        recipientBuilder.setNeedReceipt(false);
        recipientBuilder.setSignedContent(diadocService.signMessage(generatedDocument));

        var messagePatchToPostBuilder = DiadocMessage_PostApiProtos.MessagePatchToPost.newBuilder();
        messagePatchToPostBuilder.addRecipientTitles(recipientBuilder);
        messagePatchToPostBuilder.setBoxId(diadocService.getMyBoxId());
        messagePatchToPostBuilder.setMessageId(messageId);

        diadocService.getApi().getMessageClient().postMessagePatch(messagePatchToPostBuilder.build());

        return new InUniversalTransferDocumentSignStatus("OK", "Успешно подписан");
    }

    public InUniversalTransferDocumentSignStatus signUniversalCorrectionDocument(String messageId, String entityId) throws Exception {
        UniversalCorrectionDocumentBuyerTitle diadocBuyerTitle = getUniversalCorrectionDocumentBuyerTitle();
        var marshaller = JAXBContext.newInstance(UniversalCorrectionDocumentBuyerTitle.class).createMarshaller();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshaller.marshal(diadocBuyerTitle, stream);

        byte[] generatedDocument = diadocService.generateUniversalCorrectionDocumentBuyerTitle(stream.toByteArray(), messageId, entityId);

        var recipientBuilder = DiadocMessage_PostApiProtos.RecipientTitleAttachment.newBuilder();
        recipientBuilder.setParentEntityId(entityId);
        recipientBuilder.setNeedReceipt(false);
        recipientBuilder.setSignedContent(diadocService.signMessage(generatedDocument));

        var messagePatchToPostBuilder = DiadocMessage_PostApiProtos.MessagePatchToPost.newBuilder();
        messagePatchToPostBuilder.addRecipientTitles(recipientBuilder);
        messagePatchToPostBuilder.setBoxId(diadocService.getMyBoxId());
        messagePatchToPostBuilder.setMessageId(messageId);

        diadocService.getApi().getMessageClient().postMessagePatch(messagePatchToPostBuilder.build());

        return new InUniversalTransferDocumentSignStatus("OK", "Успешно подписан");
    }

    public InUniversalTransferDocumentSignStatus rejectDocument(String messageId, String entityId) throws Exception {
        var signatureRejectionInfo = getSignatureRejectionInfo(messageId, entityId);

        byte[] generatedDocument = signatureRejectionInfo.getContent();

        var xmlSignatureRejectionAttachment = DiadocMessage_PostApiProtos.XmlSignatureRejectionAttachment.newBuilder();
        xmlSignatureRejectionAttachment.setParentEntityId(entityId);
        xmlSignatureRejectionAttachment.setSignedContent(diadocService.signMessage(generatedDocument));

        var messagePatchToPostBuilder = DiadocMessage_PostApiProtos.MessagePatchToPost.newBuilder();
        messagePatchToPostBuilder.addXmlSignatureRejections(xmlSignatureRejectionAttachment);

        messagePatchToPostBuilder.setBoxId(diadocService.getMyBoxId());
        messagePatchToPostBuilder.setMessageId(messageId);

        diadocService.getApi().getMessageClient().postMessagePatch(messagePatchToPostBuilder.build());

        return new InUniversalTransferDocumentSignStatus("OK", "Успешно отказан");
    }

    private UniversalTransferDocumentBuyerTitle getUniversalTransferDocumentBuyerTitle() {
        var diadocDocument = new UniversalTransferDocumentBuyerTitle();

        diadocDocument.setDocumentCreator(diadocService.getMyOrganization().getFullName());
        diadocDocument.setOperationContent("Товары и услуги получены, работы приняты");

        var diadocSigners = new UniversalTransferDocumentBuyerTitle.Signers();
        diadocSigners.getSignerReferenceOrSignerDetails().add(getUniversalTransferDocumentSignerDetails());
        diadocDocument.setSigners(diadocSigners);

        return diadocDocument;
    }

    private UniversalCorrectionDocumentBuyerTitle getUniversalCorrectionDocumentBuyerTitle() {
        var diadocDocument = new UniversalCorrectionDocumentBuyerTitle();

        diadocDocument.setDocumentCreator(diadocService.getMyOrganization().getFullName());
        diadocDocument.setOperationContent("С изменением стоимости согласен");

        var diadocSigners = new UniversalCorrectionDocumentBuyerTitle.Signers();
        diadocSigners.getSignerReferenceOrSignerDetails().add(getUniversalCorrectionDocumentSignerDetails());
        diadocDocument.setSigners(diadocSigners);

        return diadocDocument;
    }

    private GeneratedFile getSignatureRejectionInfo(String messageId, String entityId) throws Exception {

        var signerBuilder = SignerProtos.Signer.newBuilder();
        signerBuilder.setSignerCertificate(ByteString.copyFrom(certificateService.getRawCertificate()));

        var signatureRejectionInfoBuilder = SignatureRejectionInfoProtos.SignatureRejectionInfo.newBuilder();
        signatureRejectionInfoBuilder.setSigner(signerBuilder);
        signatureRejectionInfoBuilder.setErrorMessage("Отказ в подписи");
        return diadocService.getApi().getGenerateClient().generateSignatureRejectionXml(diadocService.getMyBoxId(), messageId, entityId, signatureRejectionInfoBuilder.build());
    }

    private ExtendedSignerDetailsBuyerTitle820 getUniversalTransferDocumentSignerDetails() {
        var signerDetails  = new ExtendedSignerDetailsBuyerTitle820();
        signerDetails.setInn(applicationConfiguration.getOrganizationInn());
        signerDetails.setFirstName(applicationConfiguration.getSignerFirstName());
        signerDetails.setMiddleName(applicationConfiguration.getSignerMiddleName());
        signerDetails.setLastName(applicationConfiguration.getSignerLastName());
        signerDetails.setPosition(applicationConfiguration.getSignerTitle());
        signerDetails.setSignerPowers(BigInteger.valueOf(3));
        signerDetails.setSignerStatus(BigInteger.valueOf(5));
        signerDetails.setSignerType("1");
        signerDetails.setSignerPowersBase("Устав");
        return signerDetails;
    }

    private ExtendedSignerDetailsCorrectionBuyerTitle736 getUniversalCorrectionDocumentSignerDetails() {
        var signerDetails = new ExtendedSignerDetailsCorrectionBuyerTitle736();
        signerDetails.setInn(applicationConfiguration.getOrganizationInn());
        signerDetails.setFirstName(applicationConfiguration.getSignerFirstName());
        signerDetails.setMiddleName(applicationConfiguration.getSignerMiddleName());
        signerDetails.setLastName(applicationConfiguration.getSignerLastName());
        signerDetails.setPosition(applicationConfiguration.getSignerTitle());
        signerDetails.setSignerPowers(BigInteger.valueOf(3));
        signerDetails.setSignerStatus(BigInteger.valueOf(5));
        signerDetails.setSignerType("1");
        signerDetails.setSignerPowersBase("Устав");
        return signerDetails;
    }

}
