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
 * <p>Java class for ExtendedSignerDetailsBase736 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtendedSignerDetailsBase736"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="LastName" use="required" type="{}string60" /&gt;
 *       &lt;attribute name="FirstName" use="required" type="{}string60" /&gt;
 *       &lt;attribute name="MiddleName" type="{}string60" /&gt;
 *       &lt;attribute name="Position" type="{}string128" /&gt;
 *       &lt;attribute name="Inn"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;minLength value="10"/&gt;
 *             &lt;maxLength value="12"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="RegistrationCertificate" type="{}string100" /&gt;
 *       &lt;attribute name="SignerType" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="1"/&gt;
 *             &lt;enumeration value="2"/&gt;
 *             &lt;enumeration value="3"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="SignerOrganizationName" type="{}string1000" /&gt;
 *       &lt;attribute name="SignerInfo" type="{}string255" /&gt;
 *       &lt;attribute name="SignerPowersBase" type="{}string255" /&gt;
 *       &lt;attribute name="SignerOrgPowersBase" type="{}string255" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendedSignerDetailsBase736")
@XmlSeeAlso({
    ExtendedSignerDetails736 .class
})
public class ExtendedSignerDetailsBase736 {

    @XmlAttribute(name = "LastName", required = true)
    protected String lastName;
    @XmlAttribute(name = "FirstName", required = true)
    protected String firstName;
    @XmlAttribute(name = "MiddleName")
    protected String middleName;
    @XmlAttribute(name = "Position")
    protected String position;
    @XmlAttribute(name = "Inn")
    protected String inn;
    @XmlAttribute(name = "RegistrationCertificate")
    protected String registrationCertificate;
    @XmlAttribute(name = "SignerType", required = true)
    protected String signerType;
    @XmlAttribute(name = "SignerOrganizationName")
    protected String signerOrganizationName;
    @XmlAttribute(name = "SignerInfo")
    protected String signerInfo;
    @XmlAttribute(name = "SignerPowersBase")
    protected String signerPowersBase;
    @XmlAttribute(name = "SignerOrgPowersBase")
    protected String signerOrgPowersBase;

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleName(String value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

    /**
     * Gets the value of the inn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInn() {
        return inn;
    }

    /**
     * Sets the value of the inn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInn(String value) {
        this.inn = value;
    }

    /**
     * Gets the value of the registrationCertificate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationCertificate() {
        return registrationCertificate;
    }

    /**
     * Sets the value of the registrationCertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationCertificate(String value) {
        this.registrationCertificate = value;
    }

    /**
     * Gets the value of the signerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignerType() {
        return signerType;
    }

    /**
     * Sets the value of the signerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignerType(String value) {
        this.signerType = value;
    }

    /**
     * Gets the value of the signerOrganizationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignerOrganizationName() {
        return signerOrganizationName;
    }

    /**
     * Sets the value of the signerOrganizationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignerOrganizationName(String value) {
        this.signerOrganizationName = value;
    }

    /**
     * Gets the value of the signerInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignerInfo() {
        return signerInfo;
    }

    /**
     * Sets the value of the signerInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignerInfo(String value) {
        this.signerInfo = value;
    }

    /**
     * Gets the value of the signerPowersBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignerPowersBase() {
        return signerPowersBase;
    }

    /**
     * Sets the value of the signerPowersBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignerPowersBase(String value) {
        this.signerPowersBase = value;
    }

    /**
     * Gets the value of the signerOrgPowersBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignerOrgPowersBase() {
        return signerOrgPowersBase;
    }

    /**
     * Sets the value of the signerOrgPowersBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignerOrgPowersBase(String value) {
        this.signerOrgPowersBase = value;
    }

}
