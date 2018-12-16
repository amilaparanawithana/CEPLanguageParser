package uom.msc.cse.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Amila Paranawithana
 */
public interface QueryKeyWords {

    // breaking keywords
    String SELECT = "select";
    String FROM = "from";
    String INSERT_INTO = "insert into";
    String GROUP_BY = "group by";
    String HAVING = "having";

    //siddhi
    String WINDOW = "#window";

    //epl
    String AS = "as";
    String WHERE = "where";


    String SPACE = " ";
    String COMMA = ",";
    String TERMINATOR = ";";

    // aggregate function keywords
    String AVG = "avg";
    String MAX = "max";
    String SUM = "sum";
    String COUNT = "count";

    List<String> breakingKeyWords = Stream.of(GROUP_BY, HAVING, INSERT_INTO,TERMINATOR).collect(Collectors.toList());

    List<String> eplBreakingKeyWords = Stream.of(GROUP_BY, HAVING, INSERT_INTO, SELECT, FROM, WHERE, TERMINATOR).collect(Collectors.toList());

    List<String> aggFuncs = Stream.of(AVG, MAX, SUM, COUNT).collect(Collectors.toList());





}
