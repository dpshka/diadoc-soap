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
 * <p>Java class for ExtendedOrganizationInfo_ForeignAddress1000 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtendedOrganizationInfo_ForeignAddress1000"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="OrganizationDetails" type="{}ExtendedOrganizationDetails_ForeignAddress1000"/&gt;
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
@XmlType(name = "ExtendedOrganizationInfo_ForeignAddress1000", propOrder = {
    "organizationDetails",
    "organizationReference"
})
public class ExtendedOrganizationInfoForeignAddress1000 {

    @XmlElement(name = "OrganizationDetails")
    protected ExtendedOrganizationDetailsForeignAddress1000 organizationDetails;
    @XmlElement(name = "OrganizationReference")
    protected ExtendedOrganizationReference organizationReference;

    /**
     * Gets the value of the organizationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ExtendedOrganizationDetailsForeignAddress1000 }
     *     
     */
    public ExtendedOrganizationDetailsForeignAddress1000 getOrganizationDetails() {
        return organizationDetails;
    }

    /**
     * Sets the value of the organizationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedOrganizationDetailsForeignAddress1000 }
     *     
     */
    public void setOrganizationDetails(ExtendedOrganizationDetailsForeignAddress1000 value) {
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
