package com.solera.gdc.content.manualcheckcrawler.crawler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

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
    public void applicationReadyListener(final ApplicationReadyEvent event) throws InterruptedException {
        log.info("Starting manual check crawler");
        crawlWebSite();
    }

    public void crawlWebSite() throws InterruptedException {
        webDriver.get(url);
        //login
        webDriver.findElement(By.id("loginbox_loginName")).sendKeys(username);
        webDriver.findElement(By.id("loginbox_password")).sendKeys(password);
        webDriver.findElement(By.cssSelector("#login > div > input.button-main.testing-button-login")).click();
        //accept both disclaimers
        webDriver.findElement(By.xpath("//*[@id=\"disclaimerModalBox\"]/input")).click();
        webDriver.findElement(By.xpath("//*[@id=\"disclaimerModalBox\"]/input")).click();
        //go to Service Information option
        webDriver.findElement(By.cssSelector("#root > li:nth-child(3) > a")).click();

        Thread.sleep(10000);
        webDriver.quit();
    }
}
