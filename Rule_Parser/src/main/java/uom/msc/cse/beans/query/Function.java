package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class Function {

    @XmlAttribute
    private String func;
    @XmlAttribute
    private String as;
    @XmlValue
    private String field;

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }
}
