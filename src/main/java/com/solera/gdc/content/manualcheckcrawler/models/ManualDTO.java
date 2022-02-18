package com.solera.gdc.content.manualcheckcrawler.models;

import lombok.Data;

import java.util.Set;

@Data
public class ManualDTO {
    private String make;
    private String platform;
    private Set<Model> models;
}
