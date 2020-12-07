package com.falcon.assessment.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern alphabeticPattern = Pattern.compile("[a-zA-Z]+");

    public static List<String> alphabeticSubstrings(String content) {
        var result = new ArrayList<String>();

        var matcher = alphabeticPattern.matcher(content);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

}
