//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.09.29 at 01:59:19 PM MSK 
//


package ru.grinn.diadocsoap.xjs;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TaxRate_Torg2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TaxRate_Torg2"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NoVat"/&gt;
 *     &lt;enumeration value="ZeroPercent"/&gt;
 *     &lt;enumeration value="TenPercent"/&gt;
 *     &lt;enumeration value="EighteenPercent"/&gt;
 *     &lt;enumeration value="TwentyPercent"/&gt;
 *     &lt;enumeration value="TenFraction"/&gt;
 *     &lt;enumeration value="EighteenFraction"/&gt;
 *     &lt;enumeration value="TwentyFraction"/&gt;
 *     &lt;enumeration value="TaxedByAgent"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TaxRate_Torg2")
@XmlEnum
public enum TaxRateTorg2 {

    @XmlEnumValue("NoVat")
    NO_VAT("NoVat"),
    @XmlEnumValue("ZeroPercent")
    ZERO_PERCENT("ZeroPercent"),
    @XmlEnumValue("TenPercent")
    TEN_PERCENT("TenPercent"),
    @XmlEnumValue("EighteenPercent")
    EIGHTEEN_PERCENT("EighteenPercent"),
    @XmlEnumValue("TwentyPercent")
    TWENTY_PERCENT("TwentyPercent"),
    @XmlEnumValue("TenFraction")
    TEN_FRACTION("TenFraction"),
    @XmlEnumValue("EighteenFraction")
    EIGHTEEN_FRACTION("EighteenFraction"),
    @XmlEnumValue("TwentyFraction")
    TWENTY_FRACTION("TwentyFraction"),
    @XmlEnumValue("TaxedByAgent")
    TAXED_BY_AGENT("TaxedByAgent");
    private final String value;

    TaxRateTorg2(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TaxRateTorg2 fromValue(String v) {
        for (TaxRateTorg2 c: TaxRateTorg2 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
