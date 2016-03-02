package common.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by bipibuta1 on 2/23/2016.
 */
public class DateHelper extends CommonHelper {

    public static java.sql.Date utilDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static java.util.Date sqlDateToUtilDate(java.sql.Date date) {
        return new java.util.Date(date.getTime());
    }

    public static java.util.Date stringToUtilDate(String dateIn, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        java.util.Date dateOut = null;

        try {
            dateOut = dateFormat.parse(dateIn);
        } catch (ParseException parseEx) {
            logger.error(parseEx);
        }

        return dateOut;
    }
}
