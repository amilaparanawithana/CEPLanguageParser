package uom.msc.cse.impl;

import uom.msc.cse.api.EsperConverter;
import uom.msc.cse.beans.query.*;
import uom.msc.cse.exceptions.ParserException;
import uom.msc.cse.util.QueryKeyWords;
import uom.msc.cse.util.QueryUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ESPER-EPS language parser.
 * This contains the XML to EPL and EPL to XML language parser logic implementation
 *
 * @author Amila Paranawithana
 */
public class EsperConverterImpl implements EsperConverter {

    private static EsperConverterImpl ourInstance = new EsperConverterImpl();

    public static EsperConverterImpl getInstance() {
        return ourInstance;
    }

    private EsperConverterImpl() {
    }

    /**
     * Maps a XML string to a Query object with jaxb and convert to a EPL
     *
     * @param xml incoing xml string
     * @return EPL query
     * @throws ParserException
     */
    @Override
    public String XMLToEPL(String xml) throws ParserException {
        Query query = QueryUtil.convertXMLStringToBean(xml);
        return createSQLWithQuery(query);
    }

    /**
     * Maps a XML file to a Query object with jaxb and convert to a EPL
     *
     * @param xml incoming xml file
     * @return EPL query
     * @throws ParserException
     */
    @Override
    public String XMLToEPL(File xml) throws ParserException {
        Query query = QueryUtil.convertXMLFileToBean(xml);
        return createSQLWithQuery(query);
    }

    public String EPLToXML(String esperQuery) {


        Query query = new Query();
        // set insert into
        String insertInto = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"insert into");
        query.setInsertInto(insertInto.trim());


        // set select
        Select select = new Select();
        List<Attribute> attributes = new ArrayList<>();
        String selectPortion = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"select");
        if(selectPortion.trim().equalsIgnoreCase("*") ) {
            select.setAll(true);
        } else {
            List<String> selectVariables = Arrays.asList(selectPortion.split(","));
            selectVariables.forEach(a-> {
                Attribute att = new Attribute();
                if(a.contains("as")) {
                    String[] s = a.split("as");
                    att.setAttribute(s[0].trim());
                    att.setAs(s[1].trim());
                } else {
                    att.setAttribute(a.trim());
                }

                attributes.add(att);
            });
        }
        select.setAttributes(attributes);
        query.setSelect(select);

        //from
        if(esperQuery.contains(" from ")) {
            From from = new From();
            String fromString = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"from");
            List<Stream> streams = new ArrayList<>();
            if(fromString.contains(",")) {
                List<String> fullFrom = Arrays.asList(fromString.split(","));
                fullFrom.forEach(fromBlk -> {
                    Stream stream = new Stream();
                    setFromStream(fromBlk, stream);
                    streams.add(stream);
                });
            } else {
                Stream stream = new Stream();
                setFromStream(fromString, stream);
                streams.add(stream);
            }

            from.setStreams(streams);
            query.setFrom(from);
        }

        //where - filter
        if(esperQuery.contains(" where ")) {
            String whereByPortion = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"where");
            query.setWhere(whereByPortion.trim());
        }

        // group by
        if(esperQuery.contains(" group by ")) {
            String groupByPortion = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"group by");
            query.setGroupBy(groupByPortion.trim());
        }

        return QueryUtil.convertQueryToXML(query);
    }

    /**
     * Converts a query object to a EPL query
     *
     * @param query xml mapped to query object with jaxb
     * @return converted EPL query
     */
    private String createSQLWithQuery(Query query) {

        StringBuilder queryString = new StringBuilder();

        // set insert into
        QueryUtil.setInsertInto(query.getInsertInto(), queryString).append(QueryKeyWords.SPACE);
        // set select
        QueryUtil.setSelect(query.getSelect(), queryString);

        // set from
        From from = query.getFrom();

        if(query.getFrom()!=null) {
            queryString.append("from ");

            query.getFrom().getStreams().forEach( stream -> {
                queryString.append(stream.getName());
                Window window = stream.getWindow();
                if(window != null) {
                    queryString.append(".").append("win").append(":").append(window.getFunc());
                    queryString.append("(");
                    window.getParameters().forEach( param -> {
                        queryString.append(param.getValue()).append(",");
                    });
                    queryString.setLength(queryString.length() -1);
                    queryString.append(")");
                }
                if(stream.getAs() != null) {
                    queryString.append(" ").append(stream.getAs()).append(",");
                }
            });
        }

        queryString.setLength(queryString.length() - 1);

        // where
        if (query.getWhere() != null && !query.getWhere().isEmpty()) {
            queryString.append(" where ").append(query.getWhere()).append(QueryKeyWords.SPACE);
        }

        // group by
        if (query.getGroupBy() != null) {
            QueryUtil.setGroupBy(query.getGroupBy(), queryString).append(QueryKeyWords.SPACE);
        }
        // having
        if (query.getHaving() != null) {
            QueryUtil.setHaving(query.getHaving(), queryString).append(QueryKeyWords.SPACE);
        }
        return queryString.toString();

    }


    private void setFromStream(String fromBlk, Stream stream) {

        if(fromBlk.contains(".")) {
            stream.setName(fromBlk.split("\\.")[0].trim());
        }
        if(fromBlk.contains("win:")) {
            Window window = new Window();
            window.setFunc(fromBlk.split("win:")[1].split("\\(")[0].trim());
            List<Parameter> windowParams = new ArrayList<>();
            String parametersStrin = fromBlk.split("\\(")[1].split("\\)")[0];

            if(parametersStrin.contains(",")) {
                Arrays.asList(parametersStrin.split(",")).forEach(param -> {
                    windowParams.add(new Parameter(param));
                });
                window.setParameters(windowParams);
            } else {
                windowParams.add(new Parameter(parametersStrin));
                window.setParameters(windowParams);
            }
            stream.setWindow(window);
        }

       if(fromBlk.split("\\)").length > 1) {
           stream.setAs(fromBlk.split("\\)")[1].trim());
       }
    }
}
