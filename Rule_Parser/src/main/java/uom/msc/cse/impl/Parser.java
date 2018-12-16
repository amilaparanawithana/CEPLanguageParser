package uom.msc.cse.impl;

import uom.msc.cse.api.EsperConverter;
import uom.msc.cse.api.SiddhiQLConverter;

/**
 * @author Amila Paranawithana
 */
public class Parser {

    public static SiddhiQLConverter getSiddiConverter() {
        return SiddhiQLConverterImpl.getInstance();
    }

    public static EsperConverter getEsperConverter() {
        return EsperConverterImpl.getInstance();
    }
}
