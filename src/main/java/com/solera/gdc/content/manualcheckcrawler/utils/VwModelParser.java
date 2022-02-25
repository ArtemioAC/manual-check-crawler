package com.solera.gdc.content.manualcheckcrawler.utils;

import com.solera.gdc.content.manualcheckcrawler.models.VwModelGroup;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VwModelParser {
    private final int crawlYear;
    private final Matcher matcher;

    public VwModelParser(final int crawlYear, final String optionString) {
        this.crawlYear = crawlYear;
        String regex = "^" // start
            + "\\s*" // 0-n spaces at start
            + "(.+)" // first match group (models) -- match all chars up
            // until ...
            + "\\s*" // 0-n spaces after models
            + "\\[" // open square bracket
            + "([^\\]]+?)" // second match group (platform) -- contents of
            // square brackets
            + "\\]" // close bracket
            + "\\s*" // 0-n spaces after platform
            + "(\\(.+?\\))" // third match group (dates in parens at end of
            // string)
            + "\\s*" // 0-n spaces
            + "$"; // end

        Pattern pattern = Pattern.compile(regex);
        matcher = pattern.matcher(optionString);
    }

    public VwModelGroup parseModel() {
        VwModelGroup model = new VwModelGroup();
        if (matcher.find()) {
            model.setModelNames(parseModels(matcher.group(1).trim()));
            model.setPlatform(matcher.group(2).trim());
            model.setYear(crawlYear);
        }
        return model;
    }

    private Set<String> parseModels(final String modelString) {
        String separator = (modelString.contains("/")) ? "/" : ",";
        return new TreeSet<>(Arrays.asList(modelString.split(separator)));
    }
}
