package eu.fbk.das.domainobject.core.entity.jaxb.activity;

import javax.xml.bind.annotation.*;


/**
 * Father of all activities
 *
 * <p>Java class for activityType complex type.
 *
 * <p>The following schema fragments specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="activityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="precondition" type="{http://das.fbk.eu/Annotation}PreconditionType" minOccurs="0"/>
 *         &lt;element name="effect" type="{http://das.fbk.eu/Annotation}EffectType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activityType", propOrder = {
        "precondition",
        "effect"
}, namespace = "http://das.fbk.eu/Process")
@XmlSeeAlso({
        AbstractType.class,
        ConcreteType.class,
        SwitchType.class,
        PickType.class,
        ReceiveType.class,
        InvokeType.class
})
public abstract class ActivityType {

    @XmlElement(namespace = "http://das.fbk.eu/Process")
    protected PreconditionType precondition;
    @XmlElement(namespace = "http://das.fbk.eu/Process")
    protected EffectType effect;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the precondition properties.
     *
     * @return
     *     possible object is
     *     {@link PreconditionType }
     *
     */
    public PreconditionType getPrecondition() {
        return precondition;
    }

    /**
     * Sets the value of the precondition properties.
     *
     * @param value
     *     allowed object is
     *     {@link PreconditionType }
     *
     */
    public void setPrecondition(PreconditionType value) {
        this.precondition = value;
    }

    /**
     * Gets the value of the effect properties.
     *
     * @return
     *     possible object is
     *     {@link EffectType }
     *
     */
    public EffectType getEffect() {
        return effect;
    }

    /**
     * Sets the value of the effect properties.
     *
     * @param value
     *     allowed object is
     *     {@link EffectType }
     *
     */
    public void setEffect(EffectType value) {
        this.effect = value;
    }

    /**
     * Gets the value of the name properties.
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
     * Sets the value of the name properties.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

}