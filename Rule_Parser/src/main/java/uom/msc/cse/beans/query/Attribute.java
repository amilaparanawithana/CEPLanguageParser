package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.*;

/**
 * @author Amila Paranawithana
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Attribute {

    @XmlAttribute
    private String as;
    @XmlValue
    private String attribute;

    @XmlAttribute
    private Type type;

    @XmlType
    @XmlEnum(String.class)
    public enum Type {
        DEFAULT, OPERATION
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
