//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.10.12 at 05:04:03 PM MSK 
//


package ru.grinn.diadocsoap.xjs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Address_ForeignAddress1000 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Address_ForeignAddress1000"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="RussianAddress" type="{}RussianAddress"/&gt;
 *         &lt;element name="ForeignAddress" type="{}ForeignAddress1000"/&gt;
 *         &lt;element name="AddressCode" type="{}string36"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Address_ForeignAddress1000", propOrder = {
    "russianAddress",
    "foreignAddress",
    "addressCode"
})
public class AddressForeignAddress1000 {

    @XmlElement(name = "RussianAddress")
    protected RussianAddress russianAddress;
    @XmlElement(name = "ForeignAddress")
    protected ForeignAddress1000 foreignAddress;
    @XmlElement(name = "AddressCode")
    protected String addressCode;

    /**
     * Gets the value of the russianAddress property.
     * 
     * @return
     *     possible object is
     *     {@link RussianAddress }
     *     
     */
    public RussianAddress getRussianAddress() {
        return russianAddress;
    }

    /**
     * Sets the value of the russianAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link RussianAddress }
     *     
     */
    public void setRussianAddress(RussianAddress value) {
        this.russianAddress = value;
    }

    /**
     * Gets the value of the foreignAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ForeignAddress1000 }
     *     
     */
    public ForeignAddress1000 getForeignAddress() {
        return foreignAddress;
    }

    /**
     * Sets the value of the foreignAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForeignAddress1000 }
     *     
     */
    public void setForeignAddress(ForeignAddress1000 value) {
        this.foreignAddress = value;
    }

    /**
     * Gets the value of the addressCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressCode() {
        return addressCode;
    }

    /**
     * Sets the value of the addressCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressCode(String value) {
        this.addressCode = value;
    }

}
