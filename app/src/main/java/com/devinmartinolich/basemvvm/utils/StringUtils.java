package com.devinmartinolich.basemvvm.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Name: StringUtils
 * Created by Devin Martinolich on 1/24/18.
 * Modified by
 * Purpose: Contains utility methods to perform common manipulation of strings
 */
public class StringUtils {

    /**
     * Name: isTrimmedEmpty
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: To check that given string is null or empty after trimming
     *
     * @param aString String to check
     * @return true if String is empty even if it is padded with white space
     */
    public static boolean isTrimmedEmpty(String aString) {
        return aString == null || aString.trim().length() == 0;
    }

    /**
     * Name: isValidEmail
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: To check if its a valid email id or not.
     *
     * @param email email address to validate
     * @return true if valid email else false
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Name: validatePattern
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: This method is used to validate pattern
     *
     * @param asPattern : string pattern which we need to map.
     * @param asTarget  : string to compare with map pattern.
     * @return : return true if pattern matches otherwise it will return false.
     */
    public final static boolean validatePattern(String asPattern, String asTarget) {
        Pattern pattern = Pattern.compile(asPattern);
        return pattern.matcher(asTarget).matches();
    }

    /**
     * Name: isEquals
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: This method will compare two strings contents equality with case.
     *
     * @param s  : string 1 which we need to check.
     * @param s1 :  string 2 which we need to check.
     * @return : true if same else false.
     */
    public static boolean isEquals(String s, String s1) {
        return s.equals(s1);
    }

    /**
     * Name: getCommaSeparatedList
     * Created by 12/24/2019
     * Modified by
     * Purpose: Return a List<String> based from a Comma Separated String
     *
     * @return List list
     */
    public static List<String> getCommaSeparatedList(String list) {
        List<String> newlist = new ArrayList<>();

        int count = list.length() - list.replace(",", "").length() - 1;

        for (int i=0; i<=count; i++) {
            final String currentItem = list.split(",")[i];
            newlist.add(currentItem);
        }

        return newlist;
    }
}