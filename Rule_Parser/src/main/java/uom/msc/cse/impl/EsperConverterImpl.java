package uom.msc.cse.impl;

import uom.msc.cse.api.EsperConverter;
import uom.msc.cse.beans.query.Attribute;
import uom.msc.cse.beans.query.From;
import uom.msc.cse.beans.query.Query;
import uom.msc.cse.beans.query.Select;
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

        // group by
        if(esperQuery.contains(" group by ")) {
            String groupByPortion = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"group by");
            query.setGroupBy(groupByPortion.trim());
        }

        //where - filter
        if(esperQuery.contains(" where ")) {
            String whereByPortion = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"where");
            query.setFilter(whereByPortion.trim());
        }

        //from
        if(esperQuery.contains(" from ")) {
            String fromPortion = QueryUtil.getStringBetweenEPLBreakers(esperQuery,"from");
            From from = new From();
            if(fromPortion.contains(".")) {
                from.setValue(fromPortion.split("\\.")[0].trim());
            }
            if(fromPortion.contains("as")) {
                from.setAs(fromPortion.split("as")[1].trim());
            }
            query.setFrom(from);
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
        QueryUtil.setFrom(from, queryString).append(QueryKeyWords.SPACE);

        //filter
        if (query.getFilter() != null) {
            queryString.setLength(queryString.length() - 1);
            queryString.append("(").append(query.getFilter()).append(")").append(QueryKeyWords.SPACE);
        }
        // window
        if (query.getWindow() != null) {
            queryString.setLength(queryString.length() - 1);
            queryString.append(".win:").append(query.getWindow().getFunc()).append("(");

            query.getWindow().getParameters().forEach(parameter -> {
                queryString.append(parameter.getValue()).append(",");
            });
            queryString.setLength(queryString.length() - 1);
            queryString.append(")").append(QueryKeyWords.SPACE);
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
}
