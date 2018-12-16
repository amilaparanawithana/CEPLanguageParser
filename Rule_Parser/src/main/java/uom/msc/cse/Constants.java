package uom.msc.cse;

/**
 * @author Amila Paranawithana
 */
public class Constants {

    public static final String SAMPLE_XML_QUERY = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<query xmlns=\"http://www.example.com\">\n" +
            "<select>\n" +
            "    <attributes>\n" +
            "        <attribute>atr1</attribute>\n" +
            "        <attributer as=\"asat\">atr2</attributer>\n" +
            "    </attributes>\n" +
            "    <operators>\n" +
            "        <operation connectwith=\"AND\">ope1</operation>\n" +
            "    </operators>\n" +
            "</select>\n" +
            "<fromStream>samplefromstream</fromStream>\n" +
            "<filter>\n" +
            "    <conditions>\n" +
            "        <condition>con1</condition>\n" +
            "        <condition connect=\"AND\">con2</condition>\n" +
            "    </conditions>\n" +
            "</filter>\n" +
            "<insertInto>insert-stream</insertInto>\n" +
            "</query>";
}
