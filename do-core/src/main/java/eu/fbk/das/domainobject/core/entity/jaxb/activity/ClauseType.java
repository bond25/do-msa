//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2015.09.01 at 03:11:28 PM CEST
//

package eu.fbk.das.domainobject.core.entity.jaxb.activity;

import eu.fbk.das.domainobject.core.entity.jaxb.GoalType;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ClauseType complex type.
 *
 * <p>The following schema fragments specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ClauseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="point" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="domainProperty" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="dp_name" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClauseType", propOrder = {
        "point"
}, namespace = "http://das.fbk.eu/Process")
@XmlSeeAlso({
        PreconditionType.class,
        GoalType.class,
        CompensationType.class
})
public class ClauseType {

    @XmlElement(required = true, namespace = "http://das.fbk.eu/Annotation")
    protected List<Point> point;

    /**
     * Gets the value of the point properties.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the point properties.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPoint().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClauseType.Point }
     *
     *
     */
    public List<Point> getPoint() {
        if (point == null) {
            point = new ArrayList<Point>();
        }
        return this.point;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragments specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="domainProperty" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="dp_name" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "domainProperty"
    }, namespace = "http://das.fbk.eu/Annotation")
    public static class Point {

        @XmlElement(namespace = "http://das.fbk.eu/Annotation", required = true)
        protected List<DomainProperty> domainProperty;

        /**
         * Gets the value of the domainProperty properties.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the domainProperty properties.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDomainProperty().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ClauseType.Point.DomainProperty }
         *
         *
         */
        public List<DomainProperty> getDomainProperty() {
            if (domainProperty == null) {
                domainProperty = new ArrayList<DomainProperty>();
            }
            return this.domainProperty;
        }


        /**
         * <p>Java class for anonymous complex type.
         *
         * <p>The following schema fragments specifies the expected content contained within this class.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *       &lt;attribute name="dp_name" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "state"
        }, namespace = "http://das.fbk.eu/Annotation")
        public static class DomainProperty {

            @XmlElement(namespace = "http://das.fbk.eu/Annotation", required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NMTOKEN")
            protected List<String> state;
            @XmlAttribute(name = "dp_name", required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NMTOKEN")
            protected String dpName;

            /**
             * Gets the value of the state properties.
             *
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the state properties.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getState().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             *
             *
             */
            public List<String> getState() {
                if (state == null) {
                    state = new ArrayList<String>();
                }
                return this.state;
            }

            /**
             * Gets the value of the dpName properties.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getDpName() {
                return dpName;
            }

            /**
             * Sets the value of the dpName properties.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setDpName(String value) {
                this.dpName = value;
            }

        }

    }

}
