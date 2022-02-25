package com.solera.gdc.content.manualcheckcrawler.controller;

import com.solera.gdc.content.manualcheckcrawler.crawler.ManualCheckCrawler;
import com.solera.gdc.content.manualcheckcrawler.service.ManualCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UtilityController {

    private final ManualCheckCrawler manualCheckCrawler;

    @PostMapping("/vw/manual_check")
    String triggerVwManualCheck() {
        manualCheckCrawler.startManualCheck();
        return "Success (manual check report to be added in the response)";
    }
}
