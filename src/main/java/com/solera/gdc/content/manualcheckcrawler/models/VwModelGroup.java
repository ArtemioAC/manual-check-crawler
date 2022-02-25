package com.solera.gdc.content.manualcheckcrawler.models;

import lombok.Data;

import java.util.Set;

@Data
public class VwModelGroup {
    private Set<String> modelNames;
    private String platform;
    private int year;
}
