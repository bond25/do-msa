//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2016.01.26 at 09:20:05 AM CET
//

package eu.fbk.das.domainobject.core.entity.jaxb.activity;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * While a condition is true, continue to execute a list of activities
 *
 *
 * <p>
 * Java class for whileType complex type.
 *
 * <p>
 * The following schema fragments specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="whileType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://das.fbk.eu/Process}activityType">
 *       &lt;sequence>
 *         &lt;element name="contextCondition" type="{http://das.fbk.eu/Annotation}PreconditionType"/>
 *         &lt;element name="varCondition">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;sequence>
 *           &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "whileType", propOrder = { "contextCondition", "varCondition",
        "activity" })
public class WhileType extends ActivityType {

    @XmlElement(required = true)
    protected PreconditionType contextCondition;
    @XmlElement(required = true)
    protected WhileType.VarCondition varCondition;
    @XmlElements({ @XmlElement(name = "abstract", type = AbstractType.class),
            @XmlElement(name = "concrete", type = ConcreteType.class),
            @XmlElement(name = "switch", type = SwitchType.class),
            @XmlElement(name = "pick", type = PickType.class),
            @XmlElement(name = "receive", type = ReceiveType.class),
            @XmlElement(name = "invoke", type = InvokeType.class),
            @XmlElement(name = "while", type = WhileType.class),
            @XmlElement(name = "scope", type = ScopeType.class) })
    protected List<ActivityType> activity;

    /**
     * Gets the value of the contextCondition properties.
     *
     * @return possible object is {@link PreconditionType }
     *
     */
    public PreconditionType getContextCondition() {
        return contextCondition;
    }

    /**
     * Sets the value of the contextCondition properties.
     *
     * @param value
     *            allowed object is {@link PreconditionType }
     *
     */
    public void setContextCondition(PreconditionType value) {
        this.contextCondition = value;
    }

    /**
     * Gets the value of the varCondition properties.
     *
     * @return possible object is {@link WhileType.VarCondition }
     *
     */
    public WhileType.VarCondition getVarCondition() {
        return varCondition;
    }

    /**
     * Sets the value of the varCondition properties.
     *
     * @param value
     *            allowed object is {@link WhileType.VarCondition }
     *
     */
    public void setVarCondition(WhileType.VarCondition value) {
        this.varCondition = value;
    }

    /**
     * Gets the value of the activity properties.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the activity properties.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getActivity().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractType } {@link ConcreteType } {@link SwitchType }
     * {@link PickType } {@link ReceiveType } {@link InvokeType } {@link WhileType }
     * {@link ScopeType }
     *
     *
     */
    public List<ActivityType> getActivity() {
        if (activity == null) {
            activity = new ArrayList<ActivityType>();
        }
        return this.activity;
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     *
     * <p>
     * The following schema fragments specifies the expected content contained
     * within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "name", "value" })
    public static class VarCondition {

        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String value;

        /**
         * Gets the value of the name properties.
         *
         * @return possible object is {@link String }
         *
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name properties.
         *
         * @param value
         *            allowed object is {@link String }
         *
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the value properties.
         *
         * @return possible object is {@link String }
         *
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value properties.
         *
         * @param value
         *            allowed object is {@link String }
         *
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

}
