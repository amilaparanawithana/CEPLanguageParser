package uom.msc.cse.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uom.msc.cse.api.SiddhiQLConverter;
import uom.msc.cse.beans.query.*;
import uom.msc.cse.exceptions.ParserException;
import uom.msc.cse.util.QueryKeyWords;
import uom.msc.cse.util.QueryUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Siddhi-SiddhiQl language parser.
 * This contains the XML to SiddhiQL and SiddhiQL to XML language parser logic implementation
 *
 * @author Amila Paranawithana
 */
public class SiddhiQLConverterImpl extends AbstractConverter implements SiddhiQLConverter {

    private static final Logger logger = LogManager.getLogger(SiddhiQLConverterImpl.class);
    private static SiddhiQLConverterImpl ourInstance = new SiddhiQLConverterImpl();

    public static SiddhiQLConverterImpl getInstance() {
        return ourInstance;
    }

    private SiddhiQLConverterImpl() {
    }

    /**
     * Maps a XML string to a Query object with jaxb and convert to a SiddhiQL
     *
     * @param xml incoing xml string
     * @return SiddhiQL query
     * @throws ParserException
     */
    public String XMLToSiddhiQL(String xml) throws ParserException {
        String query = "";
        logger.info("Converting the XML to SiddhiQL query..");
        Query queryObj = QueryUtil.convertXMLStringToBean(xml);
        query = createSQLWithQuery(queryObj);
        return query;
    }

    /**
     * Maps a XML file to a Query object with jaxb and convert to a SiddhiQL
     *
     * @param xmlFile incoming xml file
     * @return SiddhiQL query
     * @throws ParserException
     */
    public String XMLToSiddhiQL(File xmlFile) throws ParserException {
        String query = "";
        //TODO validate the incoming xml request over a XSD
        //validateXML();
        logger.info("Converting the XML to SiddhiQL query..");
        Query queryObj = QueryUtil.convertXMLFileToBean(xmlFile);
        query = createSQLWithQuery(queryObj);
        return query;
    }

    /**
     * Create the Siddhi query from the Query object created from the XML
     *
     * @param query query object created from the XML
     * @return Siddhi query
     */
    private String createSQLWithQuery(Query query) {

        StringBuilder queryString = new StringBuilder();
        // from
        QueryUtil.setFrom(query.getFrom(), queryString).append(QueryKeyWords.SPACE);
        // filter
        if (query.getFilter() != null) {
            queryString.setLength(queryString.length() - 1);
            queryString.append("[").append(query.getFilter()).append("]").append(QueryKeyWords.SPACE);
        }
        // window
        if (query.getWindow() != null) {
            queryString.setLength(queryString.length() - 1);
            queryString.append("#window.").append(query.getWindow().getFunc()).append("(");

            query.getWindow().getParameters().forEach(parameter -> {
                queryString.append(parameter.getValue()).append(",");
            });
            queryString.setLength(queryString.length() - 1);
            queryString.append(")").append(QueryKeyWords.SPACE);
        }
        // select
        QueryUtil.setSelect(query.getSelect(), queryString);
        // group by
        if (query.getGroupBy() != null) {
            QueryUtil.setGroupBy(query.getGroupBy(), queryString).append(QueryKeyWords.SPACE);
        }
        // having
        if (query.getHaving() != null) {
            QueryUtil.setHaving(query.getHaving(), queryString).append(QueryKeyWords.SPACE);
        }
        //INSERT-INTO
        QueryUtil.setInsertInto(query.getInsertInto(), queryString).append(QueryKeyWords.SPACE);

        return queryString.toString();
    }

    public String SiddhiQLToXML(String sql) throws ParserException {
        Query query = new Query();
        List<String> splitList =  Arrays.asList(sql.split(" "));

        //fromstream
        int fromIndx = splitList.indexOf(QueryKeyWords.FROM);
        String stringAfterFrom = splitList.get(fromIndx + 1);

        String fromStream;
        if(stringAfterFrom.contains("[")) {
            fromStream = splitList.get(fromIndx + 1).split("\\[")[0];
        } else {
            fromStream = splitList.get(fromIndx + 1);
        }
        query.setFrom(new From(fromStream));

        // filter
        if(stringAfterFrom.contains("[")) {
            String filterString = splitList.get(fromIndx + 1).split("\\[")[1].split("\\]")[0];
            query.setFilter(filterString);
        } else if(splitList.get(fromIndx + 2).startsWith("[")) {
            query.setFilter(splitList.get(fromIndx + 2).split("\\[")[1].split("\\]")[0]);
        }

        //window
        if(sql.contains("#window")) {
            String windowFunc = sql.split("#window.")[1].split(" ")[0].split("\\(")[0];
            Window window = new Window();
            window.setFunc(windowFunc);
            String windowParams = sql.split("#window.")[1].split("\\(")[1].split("\\)")[0];
            List<Parameter> paramList = new ArrayList<>();
            Arrays.asList(windowParams.split(",")).forEach( a -> {
                paramList.add(new Parameter(a));
            });
            window.setParameters(paramList);
            query.setWindow(window);
        }

        //select
        if(sql.contains("select")) {
            String selectPortion = QueryUtil.SiddhigetStringBetweenSiddhiBreakers(sql,"select");
            List<Attribute> attributes = new ArrayList<>();
            Arrays.asList(selectPortion.split(",")).forEach(a -> {
                Attribute at = new Attribute();
                if(a.contains("as")) {
                    at.setAttribute(a.split("as")[0]);
                    at.setAs(a.split("as")[1].trim());
                } else {
                    at.setAttribute(a.trim());
                }
                attributes.add(at);
            });


            List<Function> funcList = new ArrayList<>();
            // aggregate functions of select
            QueryKeyWords.aggFuncs.forEach( func -> {
                if(selectPortion.contains(func)) {
                    String s = selectPortion.split(func+ "\\(")[1];
                    Function f = new Function();
                    f.setFunc(func);
                    f.setField(s.split("\\)")[0]);
                    f.setAs(s.split("as ")[1].trim());
                    funcList.add(f);
                }
            });

            query.setSelect(new Select(attributes, funcList));

        }




        //group by
        if(sql.contains("group by")) {
            String selectPortion = QueryUtil.SiddhigetStringBetweenSiddhiBreakers(sql,"group by");
            query.setGroupBy(selectPortion.trim());
        }


        return QueryUtil.convertQueryToXML(query);
    }
}
