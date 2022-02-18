package com.solera.gdc.content.manualcheckcrawler.utils;

public class Constants {
    public static final String VEHICLE_INFORMATION_SEARCH_XPATH = "//A[@href='/erwin/showSearch.do']";
    public static final String DISCLAIMER_BUTTON_XPATH = "//*[@id=\"disclaimerModalBox\"]/input";
    public static final String YEAR_SELECTION_XPATH = "//FORM[@name='searchArticleSimpleForm']//SELECT[@id='f_modelYear']";
    public static final String MODEL_SELECTION_XPATH = "//FORM[@name='searchArticleSimpleForm']//SELECT[@id='f_cartypeId']";
    public static final String LOGOUT_BUTTON_XPATH = "//*[@id=\"loginBox\"]/a[1]";

    public static final String LOGIN_BUTTON_CSS_SELECTOR = "#login > div > input.button-main.testing-button-login";

    public static final long SLEEP_TIME = 500;
    public static final long SLEEP_TIME_LONGER = 2000;
}
