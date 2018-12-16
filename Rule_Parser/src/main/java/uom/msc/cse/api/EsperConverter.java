package uom.msc.cse.api;

import uom.msc.cse.exceptions.ParserException;

import java.io.File;

/**
 * API methods for converting generic meta language to ESPER-EPL query and vise versa
 *
 * @author Amila Paranawithana
 */
public interface EsperConverter {

    /**
     * Convert a XML metalanguage to a EPL query
     *
     * @param xml XML as a string
     * @return EPL query
     * @throws ParserException Error in converting XML to EPL
     */
    String XMLToEPL(String xml) throws ParserException;

    /**
     * Convert a XML metalanguage file to a EPL query
     *
     * @param xml Incoming XML file
     * @return EPL query
     * @throws ParserException error in converting XML to EPL
     */
    String XMLToEPL(File xml) throws ParserException;

    /**
     * Convert EPL query to XML metalanguage
     *
     * @param esperQuery Incoming EPL query string
     * @return EPL query in XML
     * @throws ParserException Error in converting EPL to XML meta language
     */
    String EPLToXML(String esperQuery) throws ParserException;
}
