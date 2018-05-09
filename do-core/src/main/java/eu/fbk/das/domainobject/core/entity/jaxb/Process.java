package eu.fbk.das.domainobject.core.entity.jaxb;

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2016.01.26 at 09:20:05 AM CET
//

import eu.fbk.das.domainobject.core.entity.jaxb.activity.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Main type of a process with a name, a sequence of activities and a list of
 * variables
 *
 *
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragments specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *         &lt;element name="variable" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="role" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "activity", "variable" })
@XmlRootElement(name = "process", namespace = "http://das.fbk.eu/Process")
public class Process {

    @XmlElements({ @XmlElement(name = "abstract", type = AbstractType.class, namespace = "http://das.fbk.eu/Process"),
            @XmlElement(name = "concrete", type = ConcreteType.class, namespace = "http://das.fbk.eu/Process"),
            @XmlElement(name = "switch", type = SwitchType.class, namespace = "http://das.fbk.eu/Process"),
            @XmlElement(name = "pick", type = PickType.class, namespace = "http://das.fbk.eu/Process"),
            @XmlElement(name = "receive", type = ReceiveType.class, namespace = "http://das.fbk.eu/Process"),
            @XmlElement(name = "invoke", type = InvokeType.class, namespace = "http://das.fbk.eu/Process"),
            @XmlElement(name = "while", type = WhileType.class, namespace = "http://das.fbk.eu/Process"),
            @XmlElement(name = "scope", type = ScopeType.class, namespace = "http://das.fbk.eu/Process") })
    protected List<ActivityType> activity;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected List<String> variable;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "role")
    protected Boolean role;

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
     * Gets the value of the variable properties.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the variable properties.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getVariable().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     */
    public List<String> getVariable() {
        if (variable == null) {
            variable = new ArrayList<String>();
        }
        return this.variable;
    }

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
     * Gets the value of the role properties.
     *
     * @return possible object is {@link Boolean }
     *
     */
    public boolean isRole() {
        if (role == null) {
            return false;
        } else {
            return role;
        }
    }

    /**
     * Sets the value of the role properties.
     *
     * @param value
     *            allowed object is {@link Boolean }
     *
     */
    public void setRole(Boolean value) {
        this.role = value;
    }

}

