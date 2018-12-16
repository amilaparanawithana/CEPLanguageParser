package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Amila Paranawithana
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Window {

    @XmlAttribute
    private String func;

    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    private List<Parameter> parameters;

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
