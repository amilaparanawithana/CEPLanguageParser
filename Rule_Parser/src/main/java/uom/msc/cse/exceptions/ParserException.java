package uom.msc.cse.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amila Paranawithana
 */
public class ParserException extends RuntimeException {

    //String message;
    private String subject;
    private Map<String,String> metaInfoMap = new HashMap<>();
    private String title;
    private ExType exceptionType;
    private Integer errorCode;

    public ParserException() {}

    /**
     * Constructs a ServiceException instance without wrapping an existing exception
     * @see ParserException#ParserException(String, Exception, Object...)
     */
    public ParserException(String message, Object... args) {
        this(message, null, args);
    }

    /**
     * Constructs a ServiceException instance, filling "{}" placeholders in exception messages with values available on
     * varargs array and populating remaining fields with any remaining varargs elements via type checking
     *
     * @param message exception message, possibly containing "{}" placeholders
     * @param e       cause of this exception
     * @param args    holds filler arguments for message placeholders, and any additional exception arguments to be
     *                filled (e.g. exception type or error code)
     */
    public ParserException(String message, Exception e, Object... args) {
        super(processMsg(message, args), e);

        // count arguments already used for parameter filling
        int argNumber = 0;
        int paramPosition = message.indexOf('{');
        while (paramPosition > -1 && argNumber < args.length) {
            argNumber++;
            paramPosition = message.indexOf('{', paramPosition + 1);
        }

        // process remaining arguments, failing if corresponding target fields are already initialized
        for (; argNumber < args.length; argNumber++) {

            Object arg = args[argNumber];
            if (arg instanceof ExType) {            // exception type
                if (this.exceptionType != null) {
                    throw new ExceptionInInitializerError("Multiple occurrences of exceptionType: " + arg + " for " + this);
                }
                this.exceptionType = (ExType) arg;

            } else if (arg instanceof Map) {        // meta info map
                if (this.metaInfoMap != null) {
                    throw new ExceptionInInitializerError("Multiple occurrences of metaInfoMap: " + arg + " for " + this);
                }
                this.metaInfoMap = (Map<String, String>) arg;

            } else if (arg instanceof String) {     // subject
                if (this.subject != null) {
                    throw new ExceptionInInitializerError("Multiple occurrences of subject: " + arg + " for " + this);
                }
                this.subject = (String) arg;

            } else if (arg instanceof Integer) {    // error code
                if (this.errorCode != null) {
                    throw new ExceptionInInitializerError("Multiple occurrences of errorCode: " + arg + " for " + this);
                }
                this.errorCode = (Integer) arg;
            }
        }
    }

    enum ExType {
        WARN, ERROR
    }

    private static String processMsg(String message, Object... args) {
        int index = 0;
        while (message.contains("{}") && index < args.length) {
            message = message.replaceFirst("\\{\\}", String.valueOf(args[index++]));
        }
        return message;
    }

    public ExType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public Map<String, String> getMetaInfoMap() {
        return metaInfoMap;
    }

    public void setMetaInfoMap(Map<String, String> metaInfoMap) {
        this.metaInfoMap = metaInfoMap;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
    
    public boolean hasErrorCode() {
        return errorCode == null ? false : true;
    }
}
