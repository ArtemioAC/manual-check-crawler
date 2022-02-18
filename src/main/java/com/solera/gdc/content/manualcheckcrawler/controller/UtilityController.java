package com.solera.gdc.content.manualcheckcrawler.controller;

import com.solera.gdc.content.manualcheckcrawler.crawler.VwManualCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UtilityController {

    private final VwManualCheckService vwManualCheckService;

    @PostMapping("/vw/manual_check")
    String triggerVwManualCheck() throws InterruptedException {
        vwManualCheckService.crawlWebSite();
        return "Success (manual check report to be added in the response)";
    }
}
