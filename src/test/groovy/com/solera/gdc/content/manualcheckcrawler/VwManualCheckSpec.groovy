package com.solera.gdc.content.manualcheckcrawler

import com.solera.gdc.content.manualcheckcrawler.service.ManualCheckService
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import spock.lang.Specification

class VwManualCheckSpec extends Specification{
    ManualCheckService systemUnderTest
    WebDriver mockWebDriver = Mock(WebDriver)
    WebElement mockWebElement = Mock(WebElement)

    def setup() {
        systemUnderTest = new ManualCheckService("VW", "some-url",
                "someUsername", "somePassword", mockWebDriver)
        mockWebDriver.findElement(_ as By) >> mockWebElement
    }

    def "login is successful" () {
        when:
        systemUnderTest.login()

        then:
        2 * mockWebElement.sendKeys(_)
        1 * mockWebElement.click()
    }

    def "accept disclaimers successfully" () {
        when:
        systemUnderTest.acceptDisclaimers()

        then:
        2 * mockWebElement.click()
    }

    def "logout successfully" () {
        when:
        systemUnderTest.logout()

        then:
        1 * mockWebElement.click()
    }
}
