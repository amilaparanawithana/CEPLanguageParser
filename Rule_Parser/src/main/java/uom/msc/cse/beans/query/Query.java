
package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Amila Paranawithana
 */

@XmlRootElement(name = "query")
@XmlAccessorType(XmlAccessType.FIELD)
public class Query {

    @XmlElement(name = "select")
    private Select select;
    @XmlElement(name = "insertInto")
    private String insertInto;
    @XmlElement(name = "group-by")
    private String groupBy;
    @XmlElement(name = "having", type = String.class)
    private String having;
    @XmlElement(name = "filter", type = String.class)
    private String filter;
    @XmlElement(name = "window")
    private Window window;
    @XmlElement(name = "from")
    private From from;

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public String getInsertInto() {
        return insertInto;
    }

    public void setInsertInto(String insertInto) {
        this.insertInto = insertInto;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }
}
