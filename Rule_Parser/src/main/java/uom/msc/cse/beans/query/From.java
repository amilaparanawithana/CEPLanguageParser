package uom.msc.cse.beans.query;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class From {

    @XmlElementWrapper(name = "streams")
    @XmlElement(name = "stream")
    private List<Stream> streams;

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }
}
