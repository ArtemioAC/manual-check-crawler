package com.solera.gdc.content.manualcheckcrawler.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class SeleniumConfiguration {
    private static final String CHROME_DRIVER = "chromedriver.exe";
    private final ResourceLoader resourceLoader;

    @Bean
    public WebDriver getChromeDriver() throws IOException {
        WebDriverManager.chromedriver().setup();
//        String PATH = Paths.get(getClass().getClassLoader().getResource(CHROME_DRIVER).toURI()).toString();
//        Resource chromeDriver = resourceLoader.getResource("classpath:webdrivers/chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver", chromeDriver.getFile().getAbsolutePath());
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return webDriver;
    }
}
