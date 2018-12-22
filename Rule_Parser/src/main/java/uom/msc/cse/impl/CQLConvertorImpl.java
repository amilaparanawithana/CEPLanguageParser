package uom.msc.cse.impl;

import uom.msc.cse.api.CQLConvertor;
import uom.msc.cse.beans.query.Query;
import uom.msc.cse.beans.query.Window;
import uom.msc.cse.exceptions.ParserException;
import uom.msc.cse.util.QueryKeyWords;
import uom.msc.cse.util.QueryUtil;

import java.io.File;

public class CQLConvertorImpl extends AbstractConverter implements CQLConvertor {

    private static CQLConvertorImpl ourInstance = new CQLConvertorImpl();

    public static CQLConvertorImpl getInstance() {
        return ourInstance;
    }

    @Override
    public String XMLToCQL(String xml) throws ParserException {
        return null;
    }

    @Override
    public String XMLToCQL(File xml) throws ParserException {
        Query query = QueryUtil.convertXMLFileToBean(xml);
        return createCQLWithQuery(query);
    }

    @Override
    public String CQLToXML(String cqlQuery) throws ParserException {
        return null;
    }

    private String createCQLWithQuery(Query query) {

        StringBuilder queryString = new StringBuilder();

        // select
        QueryUtil.setSelect(query.getSelect(), queryString);

        //from
        if(query.getFrom() != null) {
            queryString.append("from").append(QueryKeyWords.SPACE);
            query.getFrom().getStreams().forEach( stream -> {
                queryString.append(stream.getName());
                 if(stream.getWindow() != null) {
                     Window window = stream.getWindow();
                     queryString.append("[").append(window.getFunc()).append(QueryKeyWords.SPACE);
                     window.getParameters().forEach( wp -> {
                         queryString.append(wp.getValue());
                     });
                     queryString.append("]").append(QueryKeyWords.SPACE);
                 }
                  if(stream.getAs() != null && !stream.getAs().isEmpty()) {
                      queryString.append("as").append(stream.getAs()).append(QueryKeyWords.SPACE);
                  }

                  queryString.append(QueryKeyWords.COMMA);
            });

            queryString.setLength(queryString.length() - 1);
            queryString.append(QueryKeyWords.SPACE);

        }

        //where
        if(query.getWhere() != null) {
            queryString.append("where ").append(query.getWhere()).append(QueryKeyWords.SPACE);
        }

        // group-by
        if(query.getGroupBy() != null && !query.getGroupBy().isEmpty()) {
            queryString.append("group by").append(QueryKeyWords.SPACE).append(query.getGroupBy()).append(QueryKeyWords.SPACE);
        }

        //having
        if(query.getHaving() != null && !query.getHaving().isEmpty()) {
            queryString.append("having").append(QueryKeyWords.SPACE).append(query.getHaving()).append(QueryKeyWords.SPACE);
        }

        return queryString.toString();

    }
}
