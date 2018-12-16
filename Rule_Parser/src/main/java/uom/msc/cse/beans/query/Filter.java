package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author Amila Paranawithana
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Filter {

    @XmlElementWrapper(name = "conditions")
    @XmlElement(name = "condition", type = Condition.class)
    private List<Condition> conditions;

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

}
