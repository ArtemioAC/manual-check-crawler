package com.solera.gdc.content.manualcheckcrawler.utils;

import com.solera.gdc.content.manualcheckcrawler.models.VwModelGroup;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VwModelParser {
    private final int crawlYear;
    private final Matcher matcher;

    public VwModelParser(int crawlYear, String optionString) {
        this.crawlYear = crawlYear;
        // this.optionString = optionString;
        String regex = "^" + // start
            "\\s*" + // 0-n spaces at start
            "(.+)" + // first match group (models) -- match all chars up
            // until ...
            "\\s*" + // 0-n spaces after models
            "\\[" + // open square bracket
            "([^\\]]+?)" + // second match group (platform) -- contents of
            // square brackets
            "\\]" + // close bracket
            "\\s*" + // 0-n spaces after platform
            "(\\(.+?\\))" + // third match group (dates in parens at end of
            // string)
            "\\s*" + // 0-n spaces
            "$"; // end

        Pattern pattern = Pattern.compile(regex);
        matcher = pattern.matcher(optionString);
    }

    public VwModelGroup parseModel() {
        VwModelGroup model = new VwModelGroup();
        // Map<String, String> map = new HashMap<>();
        if (matcher.find()) {
            model.setModelNames(parseModels(matcher.group(1).trim()));
            //  map.put("model", matcher.group(1).trim());
            model.setPlatform(matcher.group(2).trim());
            // map.put("platform", matcher.group(2).trim());
            model.setYear(crawlYear);
        }
        return model;
    }

    private Set<String> parseModels(String modelString) {
        String separator = (modelString.contains("/")) ? "/" : ",";
        return new TreeSet<>(Arrays.asList(tokenize(modelString, separator)));
    }

    private String[] tokenize(String string, String delim) {
        List<String> tokens = new ArrayList<>();
        int end;
        if (string != null) {
            for(int start = 0; start < string.length(); start = end + delim.length()) {
                end = string.indexOf(delim, start);
                if (end < 0) {
                    end = string.length();
                }

                tokens.add(string.substring(start, end).trim());
            }
        }

        return (String[])tokens.toArray(new String[0]);
    }
}
