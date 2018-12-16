package uom.msc.cse.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uom.msc.cse.beans.query.From;
import uom.msc.cse.beans.query.Query;
import uom.msc.cse.beans.query.Select;
import uom.msc.cse.exceptions.ParserException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Amila Paranawithana
 */
public class QueryUtil {

    private static final Logger logger = LogManager.getLogger(QueryUtil.class);

    public static Query convertXMLFileToBean(File xmlFile) throws ParserException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Query.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Query) jaxbUnmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            logger.error("Error while converting the XML file content to JAXB model");
            if (logger.isDebugEnabled()) {
                logger.debug("Error in converting XML string : {} to JAXB object", xmlFile.getName());
            }
            throw new ParserException("Error while converting the XML file content to JAXB model", e);
        }
    }

    public static Query convertXMLStringToBean(String xml) throws ParserException {

        try {
            StringReader reader = new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(Query.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Query) jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            logger.error("Error while converting XML to JAXB object");
            if (logger.isDebugEnabled()) {
                logger.debug("Error in converting XML string : {} to JAXB object", xml);
            }
            throw new ParserException("Error while converting XML to JAXB object", e);
        }
    }

    public static String convertQueryToXML(Query query) throws ParserException {
        try {
            StringWriter sw = new StringWriter();
            JAXBContext jaxbContext = JAXBContext.newInstance(Query.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(query,sw);
            return sw.toString();
        } catch (JAXBException e) {
            logger.error("Error while converting Query object to XML");
            throw new ParserException("Error while converting Query to XML", e);
        }
    }

    public static StringBuilder setSelect(Select select, StringBuilder queryString) {

        queryString.append(QueryKeyWords.SELECT).append(QueryKeyWords.SPACE);
        if (select.getAll()) {
            queryString.append("*");
        } else {
            final int[] numberOfAttributes = {select.getAttributes().size()}; // this is to determine on comma
            select.getAttributes().forEach(attr -> {

                queryString.append(attr.getAttribute()).append(QueryKeyWords.SPACE);
                if (attr.getAs() != null) {
                    queryString.append(QueryKeyWords.AS).append(QueryKeyWords.SPACE).append(attr.getAs()).append(QueryKeyWords.SPACE);
                }
                numberOfAttributes[0] = numberOfAttributes[0] - 1;

                if (numberOfAttributes[0] > 0) {
                    queryString.append(QueryKeyWords.COMMA).append(QueryKeyWords.SPACE);
                }
            });

            final int[] numberOfFunctions = {select.getFunctions().size()}; // this is to determine on comma
            if (numberOfFunctions[0] > 0) {
                queryString.append(QueryKeyWords.COMMA).append(QueryKeyWords.SPACE);
            }

            if(select.getFunctions() != null) {
                select.getFunctions().forEach(function -> {
                    queryString
                            .append(function.getFunc())
                            .append("(")
                            .append(function.getField()).append(")").append(QueryKeyWords.SPACE);

                    if(!function.getAs().isEmpty() || function.getAs() != "") {
                        queryString.append("as").append(QueryKeyWords.SPACE).append(function.getAs()).append(QueryKeyWords.SPACE);
                    }

                    numberOfFunctions[0] = numberOfFunctions[0] - 1;

                    if (numberOfFunctions[0] > 0) {
                        queryString.append(QueryKeyWords.COMMA).append(QueryKeyWords.SPACE);
                    }
                });
            }
        }
        return queryString;
    }

    public static StringBuilder setFrom(From from, StringBuilder queryString) {
        queryString.append(QueryKeyWords.FROM).append(QueryKeyWords.SPACE).append(from);
        if(!from.getAs().isEmpty() || from.getAs() != ""){
            queryString.append("as ").append(from.getAs());
        }
        return queryString;
    }

    public static StringBuilder setGroupBy(String groupBy, StringBuilder queryString) {
        queryString.append(QueryKeyWords.GROUP_BY).append(QueryKeyWords.SPACE).append(groupBy);
        return queryString;
    }

    public static StringBuilder setInsertInto(String insertInto, StringBuilder queryString) {
        queryString.append(QueryKeyWords.INSERT_INTO).append(QueryKeyWords.SPACE).append(insertInto);
        return queryString;
    }

    public static StringBuilder setHaving(String having, StringBuilder queryString) {
        queryString.append(QueryKeyWords.HAVING).append(QueryKeyWords.SPACE).append(having);
        return queryString;
    }

    public static String SiddhigetStringBetweenSiddhiBreakers(String sql, String startPattern) {
        String getHalf = sql.split(startPattern)[1];
        final int[] nextFirstBreakIndx = {10000000};
        QueryKeyWords.breakingKeyWords.forEach(word -> {
            if(getHalf.contains(word)) {
                int indx = getHalf.indexOf(word);
                if(indx< nextFirstBreakIndx[0]) {
                    nextFirstBreakIndx[0] = indx;
                }
            }
        });
        String betweenString = getHalf.substring(0,nextFirstBreakIndx[0]);
        System.out.println(betweenString);
        return betweenString;
    }

    public static String getStringBetweenEPLBreakers(String sql, String startPattern) {
        String getHalf = sql.split(startPattern)[1];
        final int[] nextFirstBreakIndx = {10000000};
        QueryKeyWords.eplBreakingKeyWords.forEach(word -> {
            if(getHalf.contains(word)) {
                int indx = getHalf.indexOf(word);
                if(indx< nextFirstBreakIndx[0]) {
                    nextFirstBreakIndx[0] = indx;
                }
            }
        });
        String betweenString = getHalf.substring(0,nextFirstBreakIndx[0]);
        System.out.println(betweenString);
        return betweenString;
    }

}
