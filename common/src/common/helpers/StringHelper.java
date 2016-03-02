package common.helpers;

/**
 * Created by bipibuta1 on 2/19/2016.
 */
public class StringHelper extends CommonHelper {

    //notes:    this checks a string if it is null or empty returns TRUE, otherwise returns FALSE
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

}
