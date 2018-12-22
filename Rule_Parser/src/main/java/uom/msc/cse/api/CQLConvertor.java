package uom.msc.cse.api;

import uom.msc.cse.exceptions.ParserException;

import java.io.File;

/**
 * API methods for converting generic meta language CEP-ML to STREAM-CQL query and vise versa
 *
 * @author Amila Paranawithana
 */
public interface CQLConvertor {

    /**
     * Convert a XML metalanguage to a EPL query
     *
     * @param xml XML as a string
     * @return CQL query
     * @throws ParserException Error in converting XML to CQL
     */
    String XMLToCQL(String xml) throws ParserException;

    /**
     * Convert a XML metalanguage file to a CQL query
     *
     * @param xml Incoming XML file
     * @return CQL query
     * @throws ParserException error in converting XML to CQL
     */
    String XMLToCQL(File xml) throws ParserException;

    /**
     * Convert CQL query to XML metalanguage
     *
     * @param cqlQuery Incoming CQL query string
     * @return CQL query in XML
     * @throws ParserException Error in converting EPL to XML meta language
     */
    String CQLToXML(String cqlQuery) throws ParserException;
}
