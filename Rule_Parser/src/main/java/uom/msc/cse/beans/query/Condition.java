package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Amila Paranawithana
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Condition {

    @XmlAttribute
    private String connect;
    @XmlValue
    private String condition;

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
