//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.09.30 at 04:23:44 PM MSK 
//


package ru.grinn.diadocsoap.xjs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtendedOrganizationInfoWithHyphens complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtendedOrganizationInfoWithHyphens"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="OrganizationDetails" type="{}ExtendedOrganizationDetailsWithHyphens"/&gt;
 *         &lt;element name="OrganizationReference" type="{}ExtendedOrganizationReference"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendedOrganizationInfoWithHyphens", propOrder = {
    "organizationDetails",
    "organizationReference"
})
public class ExtendedOrganizationInfoWithHyphens {

    @XmlElement(name = "OrganizationDetails")
    protected ExtendedOrganizationDetailsWithHyphens organizationDetails;
    @XmlElement(name = "OrganizationReference")
    protected ExtendedOrganizationReference organizationReference;

    /**
     * Gets the value of the organizationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ExtendedOrganizationDetailsWithHyphens }
     *     
     */
    public ExtendedOrganizationDetailsWithHyphens getOrganizationDetails() {
        return organizationDetails;
    }

    /**
     * Sets the value of the organizationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedOrganizationDetailsWithHyphens }
     *     
     */
    public void setOrganizationDetails(ExtendedOrganizationDetailsWithHyphens value) {
        this.organizationDetails = value;
    }

    /**
     * Gets the value of the organizationReference property.
     * 
     * @return
     *     possible object is
     *     {@link ExtendedOrganizationReference }
     *     
     */
    public ExtendedOrganizationReference getOrganizationReference() {
        return organizationReference;
    }

    /**
     * Sets the value of the organizationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedOrganizationReference }
     *     
     */
    public void setOrganizationReference(ExtendedOrganizationReference value) {
        this.organizationReference = value;
    }

}
