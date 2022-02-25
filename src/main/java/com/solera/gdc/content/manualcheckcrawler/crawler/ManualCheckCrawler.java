package com.solera.gdc.content.manualcheckcrawler.crawler;

import com.solera.gdc.content.manualcheckcrawler.service.ManualCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.solera.gdc.content.manualcheckcrawler.utils.Constants.AUDI_MAKE;
import static com.solera.gdc.content.manualcheckcrawler.utils.Constants.VW_MAKE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManualCheckCrawler {
    @Value("${vw-manual-check.url}")
    private String vwUrl;
    @Value("${audi-manual-check.url}")
    private String audiUrl;
    @Value("${vw-audi-manual-check.username}")
    private String username;
    @Value("${vw-audi-manual-check.password}")
    private String password;

    private final WebDriver webDriver;

    @EventListener
    public void applicationReadyListener(final ApplicationReadyEvent event)
        throws InterruptedException {
        startManualCheck();
    }

    public void startManualCheck() {
        log.info("Starting manual check crawler");

        ManualCheckService vwManualCheck = new ManualCheckService(
            VW_MAKE, vwUrl, username, password, webDriver);
        vwManualCheck.crawlWebSite();

        ManualCheckService audiManualCheck = new ManualCheckService(
            AUDI_MAKE, audiUrl, username, password, webDriver);
        audiManualCheck.crawlWebSite();

        webDriver.quit();
    }

}
