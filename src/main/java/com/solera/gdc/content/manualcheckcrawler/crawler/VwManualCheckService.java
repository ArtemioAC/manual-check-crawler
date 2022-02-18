package com.solera.gdc.content.manualcheckcrawler.crawler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static com.solera.gdc.content.manualcheckcrawler.utils.Constants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class VwManualCheckService {
    @Value("${vw-manual-check.url}")
    private String url;
    @Value("${vw-manual-check.username}")
    private String username;
    @Value("${vw-manual-check.password}")
    private String password;

    private final WebDriver webDriver;

    @EventListener
    public void applicationReadyListener(final ApplicationReadyEvent event)
        throws InterruptedException {
        log.info("Starting manual check crawler");
        crawlWebSite();
    }

    public void crawlWebSite() throws InterruptedException {
        webDriver.get(url);

        login();
        acceptDisclaimers();

        goToServiceInformationTab();

        //Navigate through year,model drop down menus
        Set<String> validYears = getYears();
        List<String> carTypeStrings = getCarTypeStrings(validYears);

        Thread.sleep(SLEEP_TIME);
        logout();
        webDriver.quit();
    }

    public void login() {
        webDriver.findElement(By.id("loginbox_loginName")).sendKeys(username);
        webDriver.findElement(By.id("loginbox_password")).sendKeys(password);
        webDriver.findElement(By.cssSelector(LOGIN_BUTTON_CSS_SELECTOR)).click();
    }

    public void acceptDisclaimers() {
        webDriver.findElement(By.xpath(DISCLAIMER_BUTTON_XPATH)).click();
        webDriver.findElement(By.xpath(DISCLAIMER_BUTTON_XPATH)).click();
    }

    public void goToServiceInformationTab() {
        webDriver.findElement(By.xpath(VEHICLE_INFORMATION_SEARCH_XPATH)).click();
    }

    public void logout() {
        webDriver.findElement(By.xpath(LOGOUT_BUTTON_XPATH)).click();
    }

    public TreeSet<String> getYears() {
        Set<String> years = new HashSet<>();

        WebElement yearsRootNode = webDriver.findElement(By.xpath(YEAR_SELECTION_XPATH));
        List<WebElement> yearsDropDown = yearsRootNode.findElements(By.xpath("./child::*"));

        yearsDropDown.forEach(element -> {
            String yearString = element.getText();
            if (!yearString.contains("[")) {
                years.add(removeNewLineFromOption(yearString));
                log.info("Found this valid year option:%s".formatted(yearString));
            }
        });
        return new TreeSet<>(years);
    }

    //
    public List<String> getCarTypeStrings(Set<String> validYears) {
        List<String> carTypeStrings = new ArrayList<>();

        validYears.forEach(year -> {
            log.info("Working with this option: %s".formatted(year));
            Select yearOption = new Select(webDriver.findElement(By.xpath(YEAR_SELECTION_XPATH)));
            yearOption.selectByValue(year);

            try {
                Thread.sleep(SLEEP_TIME_LONGER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return carTypeStrings;
    }

    private String removeNewLineFromOption(String option) {
        return option.replace("\n", "").trim();
    }
}
