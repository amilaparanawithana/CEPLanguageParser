package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by amila on 9/12/16.
 */
@XmlRootElement(name = "querys")
@XmlAccessorType(XmlAccessType.NONE)
public class Querys {

    @XmlElement(name = "connection")
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
