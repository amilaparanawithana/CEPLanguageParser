package uom.msc.cse.impl;

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
        queryString.append("from ");
        query.getFrom().getStreams().forEach(stream -> {
            queryString.append(stream.getName());
            if (stream.getFilter() != null && !stream.getFilter().isEmpty()) {
                queryString.append("[" + stream.getFilter() + "]");
            }
            if (stream.getWindow() != null) {
                queryString.append("#window.").append(stream.getWindow().getFunc()).append("(");

                stream.getWindow().getParameters().forEach(parameter -> {
                    queryString.append(parameter.getValue()).append(",");
                });
                queryString.setLength(queryString.length() - 1);
                queryString.append(")").append(QueryKeyWords.SPACE);
            }

            if (stream.getAs() != null && (!stream.getAs().isEmpty())) {
                queryString.append(QueryKeyWords.SPACE).append(QueryKeyWords.AS).append(QueryKeyWords.SPACE).append(stream.getAs());
            }

            queryString.append(QueryKeyWords.SPACE).append(QueryKeyWords.COMMA);

        });

        queryString.setLength(queryString.length() - 1);

        //on
        if (!query.getWhere().isEmpty()) {
            queryString.append(QueryKeyWords.SPACE).append("on").append(QueryKeyWords.SPACE).append(query.getWhere()).append(QueryKeyWords.SPACE);
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

        //fromstream
        From from = new From();
        List<Stream> streams = new ArrayList<>();
        String fromString = QueryUtil.SiddhigetStringBetweenSiddhiBreakers(sql, "from");
        if (fromString.contains("join")) {

            List<String> fullFrom = Arrays.asList(fromString.split("join"));
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

        // on
        String onPortion = QueryUtil.SiddhigetStringBetweenSiddhiBreakers(sql, "on");
        query.setWhere(onPortion.trim());

        //select
        if (sql.contains("select")) {
            String selectPortion = QueryUtil.SiddhigetStringBetweenSiddhiBreakers(sql, "select");
            List<Attribute> attributes = new ArrayList<>();
            Arrays.asList(selectPortion.split(",")).forEach(a -> {
                Attribute at = new Attribute();
                if (a.contains("as")) {
                    at.setAttribute(a.split("as")[0]);
                    at.setAs(a.split("as")[1].trim());
                } else {
                    at.setAttribute(a.trim());
                }
                attributes.add(at);
            });


            List<Function> funcList = new ArrayList<>();
            // aggregate functions of select
            QueryKeyWords.aggFuncs.forEach(func -> {
                if (selectPortion.contains(func)) {
                    String s = selectPortion.split(func + "\\(")[1];
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
        if (sql.contains("group by")) {
            String selectPortion = QueryUtil.SiddhigetStringBetweenSiddhiBreakers(sql, "group by");
            query.setGroupBy(selectPortion.trim());
        }


        return QueryUtil.convertQueryToXML(query);
    }


    private void setFromStream(String fromBlk, Stream stream) {
        // name
        if (fromBlk.contains("[")) {
            stream.setName(fromBlk.split("\\[")[0].trim());
        } else if (fromBlk.contains("#window")) {
            stream.setName(fromBlk.split("#window")[0].trim());
        } else if (fromBlk.contains("as")) {
            stream.setName(fromBlk.split("as")[0].trim());
            stream.setAs(fromBlk.split("as")[1].trim());
        } else {
            stream.setName(fromBlk.trim());
        }

        //filter
        if (fromBlk.contains("[")) {
            String filter = fromBlk.split("\\[")[1].split("\\]")[0].trim();
            stream.setFilter(filter);
        }

        //window
        if (fromBlk.contains("#window")) {
            String windowFunc = fromBlk.split("#window.")[1].split(" ")[0].split("\\(")[0];
            Window window = new Window();
            window.setFunc(windowFunc);
            String windowParams = fromBlk.split("#window.")[1].split("\\(")[1].split("\\)")[0];
            List<Parameter> paramList = new ArrayList<>();
            Arrays.asList(windowParams.split(",")).forEach(a -> {
                paramList.add(new Parameter(a));
            });
            window.setParameters(paramList);
            stream.setWindow(window);
        }
    }

    private void appendFrom(String query, StringBuilder sb) {

    }

}
