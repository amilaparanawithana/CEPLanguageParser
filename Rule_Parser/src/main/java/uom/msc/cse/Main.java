package uom.msc.cse;

import uom.msc.cse.impl.Parser;

import java.io.File;

/**
 * @author Amila Paranawithana
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("--------- converter running ------------");
        File xmlFile = new File("sample.xml");

        /*String eplQuery = "insert into CombinedEvent\n" +
                "select A.customerId as custId, A.timestamp - B.timestamp as latency\n" +
                "  from EventA.win:time(30 min) A, EventB.win:time(30 min) B\n" +
                " where A.txnId = B.txnId;";*/




        // --------------Siddhi -----------------
//        System.out.println(Parser.getSiddiConverter().XMLToSiddhiQL(xmlFile));
//        System.out.println(Parser.getSiddiConverter().SiddhiQLToXML("from samplefromstream[condition]#window.time(para1,para2) select atr1 as attr1o , atr2 as attr2o, avg(temp) as avgTemp group by grp1 having having1 insert into insert-stream "));
//        System.out.println(Parser.getSiddiConverter().SiddhiQLToXML("from TempStream[temp > 30.0]#window.time(1 min) as T\n" +
//                "  join RegulatorStream[isOn == false]#window.length(1) as R\n" +
//                "  on T.roomNo == R.roomNo\n" +
//                "select T.roomNo, R.deviceID, 'start' as action\n" +
//                "insert into RegulatorActionStream;" ));

        // ------------ ESPER --------------------

        String eplQuery = "insert into CombinedEvent\n" +
                "select A.customerId as custId, A.timestamp - B.timestamp as latency\n" +
                "  from EventA.win:time(30 min) A, EventB.win:time(30 min) B\n" +
                " where A.txnId = B.txnId;";
//        System.out.println(Parser.getEsperConverter().EPLToXML(eplQuery));
//        System.out.println(Parser.getEsperConverter().XMLToEPL(xmlFile));



        // ____________ CQL -----------------------

//        xml to cql
        System.out.println(Parser.getStreamConverter().XMLToCQL(xmlFile));



    }
}
