package uom.msc.cse.api;

import uom.msc.cse.exceptions.ParserException;

import java.io.File;

/**
 * API methods for converting generic meta language to Siddhi - SiddhiQL query and vise versa
 *
 * @author Amila Paranawithana
 */
public interface SiddhiQLConverter {

    /**
     * Convert a XML metalanguage to a SiddhiQL query
     *
     * @param xml XML as a string
     * @return SiddhiQL query
     * @throws ParserException Error in converting XML to SiddhiQL
     */
    String XMLToSiddhiQL(String xml) throws ParserException;

    /**
     * Convert a XML metalanguage file to a SiddhiQL query
     *
     * @param xmlFile Incoming XML file
     * @return SiddhiQL query
     * @throws ParserException Error in converting XML to SiddhiQL
     */
    String XMLToSiddhiQL(File xmlFile) throws ParserException;

    /**
     * Convert SiddhiQL query to XML metalanguage
     *
     * @param sql Incoming SiddhiQL query string
     * @return Siddhi query in XML
     * @throws ParserException Error in converting SiddhiQL to XML meta language
     */
    String SiddhiQLToXML(String sql) throws ParserException;
}
