package com.solera.gdc.content.manualcheckcrawler.service;

import com.solera.gdc.content.manualcheckcrawler.models.ManualDTO;
import com.solera.gdc.content.manualcheckcrawler.models.Model;
import com.solera.gdc.content.manualcheckcrawler.models.VwModelGroup;
import com.solera.gdc.content.manualcheckcrawler.utils.VwModelParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

import static com.solera.gdc.content.manualcheckcrawler.utils.Constants.*;

@Slf4j
@RequiredArgsConstructor
public class ManualCheckService {

    private final String make;
    private final String url;
    private final String username;
    private final String password;

    private final WebDriver webDriver;

    public void crawlWebSite() {
        Map<String, ManualDTO> manuals = new HashMap<>();

        webDriver.get(url);

        login();
        acceptDisclaimers();
        log.info("Login successful!");
        goToServiceInformationTab();

        //Navigate through year,model drop down menus
        Set<String> validYears = getYears();
        log.info("Found years: %s".formatted(validYears.toString()));

        validYears.forEach(year -> {
            Map<String, ManualDTO> modelsForYear = getModelsForYear(year);

            log.info("=======================Models map=========================");
            modelsForYear.forEach((key, value) -> log.info("K: %s | V: %s".formatted(key, value.toString())));
            log.info("================================================");

            modelsForYear.forEach((platform, manual) -> {
                ManualDTO currentManual = modelsForYear.get(platform);

                if (manuals.containsKey(platform)) {
                    ManualDTO targetManual = manuals.get(platform);
                    currentManual.getModels().forEach(it -> targetManual.getModels().add(it));
                }
                else {
                    manuals.put(platform, currentManual);
                }
            });

            log.info("=======================Manuals map=========================");
            manuals.forEach((key, value) -> log.info("K: %s | V: %s".formatted(key, value.toString())));
            log.info("================================================");

        });

        logout();
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
                log.debug("Found this valid year option:%s".formatted(yearString));
            }
        });
        return new TreeSet<>(years);
    }

    public Map<String, ManualDTO> getModelsForYear(String year) {
        List<String> carTypeStrings = getCarTypeStrings(year);
        return getCrawledModelsAndPlatforms(year, carTypeStrings);
    }

    public List<String> getCarTypeStrings(String validYear) {
        List<String> carTypeStrings = new ArrayList<>();

        log.info("Crawling this YEAR : %s".formatted(validYear));
        Select yearOption = new Select(webDriver.findElement(By.xpath(YEAR_SELECTION_XPATH)));
        yearOption.selectByValue(validYear);

        WebElement modelsRootNode = webDriver.findElement(By.xpath(MODEL_SELECTION_XPATH));
        List<WebElement> modelsDropDown = modelsRootNode.findElements(By.xpath("./child::*"));

        modelsDropDown.forEach(element -> {
            String carTypeString = element.getText();
            if (carTypeString.contains("[") && !carTypeString.contains("Model(s)")) {
                log.debug("---Found this valid Model: %s".formatted(carTypeString));
                carTypeStrings.add(removeNewLineFromOption(carTypeString));
            }
        });

        return carTypeStrings;
    }

    public Map<String, ManualDTO> getCrawledModelsAndPlatforms(String year, List<String> carTypeStrings) {
        Map<String, ManualDTO> manuals = new LinkedHashMap<>();

        carTypeStrings.forEach(carTypeString -> {
            log.info("-Model(s): %s".formatted(carTypeString));
            VwModelParser parser = new VwModelParser(Integer.parseInt(year), carTypeString);
            VwModelGroup modelGroup = parser.parseModel();
            log.info("Parsed %s".formatted(modelGroup.toString()));

            if (!modelGroup.getModelNames().isEmpty()) {
                ManualDTO manual = new ManualDTO(make, modelGroup.getPlatform());
                addModelsToManual(Integer.parseInt(year), manual, modelGroup);
                if (manuals.containsKey(manual.getPlatform())) {
                    manuals.get(manual.getPlatform()).getModels().addAll(manual.getModels());
                } else {
                    manuals.put(manual.getPlatform(), manual);
                }
            }
        });
        return manuals;
    }

    private void addModelsToManual(int year, ManualDTO manual, VwModelGroup dropBoxModelGroup) {
        dropBoxModelGroup.getModelNames().forEach(modelName -> {
            manual.getModels().add(
                new Model(modelName.trim(), String.valueOf(year), dropBoxModelGroup.getPlatform(), make)
            );
        });
    }

    private String removeNewLineFromOption(String option) {
        return option.replace("\n", "").trim();
    }
}
