//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.09.30 at 04:23:44 PM MSK 
//


package ru.grinn.diadocsoap.xjs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtendedOrganizationDetails_ManualFilling_Torg2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtendedOrganizationDetails_ManualFilling_Torg2"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="OrgType" use="required" type="{}OrganizationType" /&gt;
 *       &lt;attribute name="Okpo"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;minLength value="1"/&gt;
 *             &lt;maxLength value="10"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="Phone" type="{}string255" /&gt;
 *       &lt;attribute name="Email" type="{}string255" /&gt;
 *       &lt;attribute name="CorrespondentAccount" type="{}string20" /&gt;
 *       &lt;attribute name="BankAccountNumber" type="{}string20" /&gt;
 *       &lt;attribute name="BankName" type="{}string1000" /&gt;
 *       &lt;attribute name="BankId"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;length value="9"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="Department" type="{}string1000" /&gt;
 *       &lt;attribute name="OrganizationAdditionalInfo" type="{}string255" /&gt;
 *       &lt;attribute name="OrganizationOrPersonInfo" type="{}string255" /&gt;
 *       &lt;attribute name="IndividualEntityRegistrationCertificate" type="{}string100" /&gt;
 *       &lt;attribute name="LegalEntityId" type="{}string255" /&gt;
 *       &lt;attribute name="ShortOrgName" type="{}string255" /&gt;
 *       &lt;attribute name="Country" type="{}string255" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendedOrganizationDetails_ManualFilling_Torg2")
@XmlSeeAlso({
    ExtendedOrganizationDetailsTorg2 .class
})
public class ExtendedOrganizationDetailsManualFillingTorg2 {

    @XmlAttribute(name = "OrgType", required = true)
    protected String orgType;
    @XmlAttribute(name = "Okpo")
    protected String okpo;
    @XmlAttribute(name = "Phone")
    protected String phone;
    @XmlAttribute(name = "Email")
    protected String email;
    @XmlAttribute(name = "CorrespondentAccount")
    protected String correspondentAccount;
    @XmlAttribute(name = "BankAccountNumber")
    protected String bankAccountNumber;
    @XmlAttribute(name = "BankName")
    protected String bankName;
    @XmlAttribute(name = "BankId")
    protected String bankId;
    @XmlAttribute(name = "Department")
    protected String department;
    @XmlAttribute(name = "OrganizationAdditionalInfo")
    protected String organizationAdditionalInfo;
    @XmlAttribute(name = "OrganizationOrPersonInfo")
    protected String organizationOrPersonInfo;
    @XmlAttribute(name = "IndividualEntityRegistrationCertificate")
    protected String individualEntityRegistrationCertificate;
    @XmlAttribute(name = "LegalEntityId")
    protected String legalEntityId;
    @XmlAttribute(name = "ShortOrgName")
    protected String shortOrgName;
    @XmlAttribute(name = "Country")
    protected String country;

    /**
     * Gets the value of the orgType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgType() {
        return orgType;
    }

    /**
     * Sets the value of the orgType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgType(String value) {
        this.orgType = value;
    }

    /**
     * Gets the value of the okpo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOkpo() {
        return okpo;
    }

    /**
     * Sets the value of the okpo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOkpo(String value) {
        this.okpo = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the correspondentAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrespondentAccount() {
        return correspondentAccount;
    }

    /**
     * Sets the value of the correspondentAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrespondentAccount(String value) {
        this.correspondentAccount = value;
    }

    /**
     * Gets the value of the bankAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    /**
     * Sets the value of the bankAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankAccountNumber(String value) {
        this.bankAccountNumber = value;
    }

    /**
     * Gets the value of the bankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Sets the value of the bankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankName(String value) {
        this.bankName = value;
    }

    /**
     * Gets the value of the bankId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * Sets the value of the bankId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankId(String value) {
        this.bankId = value;
    }

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the organizationAdditionalInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationAdditionalInfo() {
        return organizationAdditionalInfo;
    }

    /**
     * Sets the value of the organizationAdditionalInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationAdditionalInfo(String value) {
        this.organizationAdditionalInfo = value;
    }

    /**
     * Gets the value of the organizationOrPersonInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationOrPersonInfo() {
        return organizationOrPersonInfo;
    }

    /**
     * Sets the value of the organizationOrPersonInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationOrPersonInfo(String value) {
        this.organizationOrPersonInfo = value;
    }

    /**
     * Gets the value of the individualEntityRegistrationCertificate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndividualEntityRegistrationCertificate() {
        return individualEntityRegistrationCertificate;
    }

    /**
     * Sets the value of the individualEntityRegistrationCertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndividualEntityRegistrationCertificate(String value) {
        this.individualEntityRegistrationCertificate = value;
    }

    /**
     * Gets the value of the legalEntityId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalEntityId() {
        return legalEntityId;
    }

    /**
     * Sets the value of the legalEntityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalEntityId(String value) {
        this.legalEntityId = value;
    }

    /**
     * Gets the value of the shortOrgName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortOrgName() {
        return shortOrgName;
    }

    /**
     * Sets the value of the shortOrgName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortOrgName(String value) {
        this.shortOrgName = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

}
