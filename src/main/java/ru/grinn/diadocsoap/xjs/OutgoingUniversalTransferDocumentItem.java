//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.10.12 at 05:04:03 PM MSK 
//


package ru.grinn.diadocsoap.xjs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutgoingUniversalTransferDocumentItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutgoingUniversalTransferDocumentItem"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MeasureUnit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="Price" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="SubTotalWithoutVatAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="VatRate" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="VatAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="SubTotalAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutgoingUniversalTransferDocumentItem", namespace = "http://www.grinn-corp.ru/gestori/edo", propOrder = {
    "id",
    "name",
    "measureUnit",
    "quantity",
    "price",
    "subTotalWithoutVatAmount",
    "vatRate",
    "vatAmount",
    "subTotalAmount"
})
public class OutgoingUniversalTransferDocumentItem {

    @XmlElement(name = "Id", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected String id;
    @XmlElement(name = "Name", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected String name;
    @XmlElement(name = "MeasureUnit", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected String measureUnit;
    @XmlElement(name = "Quantity", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "Price", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected BigDecimal price;
    @XmlElement(name = "SubTotalWithoutVatAmount", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected BigDecimal subTotalWithoutVatAmount;
    @XmlElement(name = "VatRate", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected BigInteger vatRate;
    @XmlElement(name = "VatAmount", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected BigDecimal vatAmount;
    @XmlElement(name = "SubTotalAmount", namespace = "http://www.grinn-corp.ru/gestori/edo", required = true)
    protected BigDecimal subTotalAmount;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the measureUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeasureUnit() {
        return measureUnit;
    }

    /**
     * Sets the value of the measureUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeasureUnit(String value) {
        this.measureUnit = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrice(BigDecimal value) {
        this.price = value;
    }

    /**
     * Gets the value of the subTotalWithoutVatAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSubTotalWithoutVatAmount() {
        return subTotalWithoutVatAmount;
    }

    /**
     * Sets the value of the subTotalWithoutVatAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSubTotalWithoutVatAmount(BigDecimal value) {
        this.subTotalWithoutVatAmount = value;
    }

    /**
     * Gets the value of the vatRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVatRate() {
        return vatRate;
    }

    /**
     * Sets the value of the vatRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVatRate(BigInteger value) {
        this.vatRate = value;
    }

    /**
     * Gets the value of the vatAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    /**
     * Sets the value of the vatAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVatAmount(BigDecimal value) {
        this.vatAmount = value;
    }

    /**
     * Gets the value of the subTotalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSubTotalAmount() {
        return subTotalAmount;
    }

    /**
     * Sets the value of the subTotalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSubTotalAmount(BigDecimal value) {
        this.subTotalAmount = value;
    }

}
