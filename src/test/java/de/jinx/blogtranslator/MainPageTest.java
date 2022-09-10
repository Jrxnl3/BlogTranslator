package de.jinx.blogtranslator;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLOutput;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPageTest {
    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("https://www.deepl.com/translator#de/en/");
    }

    /*
    public void translatedText() {
        try {
            WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf($x("//div[@class='lmt__translations_as_text__text_btn']")));
        }
        catch (Exception e) {
            System.out.println("Element not found");
        }

        Document doc = Jsoup.parse(WebDriverRunner.getWebDriver().getPageSource());

        System.out.println("Translated: " + doc.getElementById("target-dummydiv").text());
    }
*/

    @Test
    public void translate(){

        try {
            WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(1));
            wait.until(ExpectedConditions.visibilityOf($x("//div[@class='lmt__translations_as_text__text_btn']")));
        }
        catch (Exception ignored) {}


        mainPage.toTranslate.add("Hello how is it going");
        mainPage.toTranslate.add("What are you doing?");

        for(String satz : mainPage.toTranslate){
            //Enter text
            $(".lmt__source_textarea").clear();
            $(".lmt__source_textarea").sendKeys(satz);

            //Wait for translation
            try {
                WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(1));
                wait.until(ExpectedConditions.visibilityOf($x("//div[@class='lmt__translations_as_text__text_btn']")));
            }
            catch (Exception ignored) {}

            //Get translation
            Document doc = Jsoup.parse(WebDriverRunner.getWebDriver().getPageSource());
            System.out.println("Translated: " + doc.getElementById("target-dummydiv").text());
        }
    }
    /*
    @Test
    public void search() {
        mainPage.searchButton.click();

        $("[data-test='search-input']").sendKeys("Selenium");
        $("button[data-test='full-search-button']").click();

        $("input[data-test='search-input']").shouldHave(attribute("value", "Selenium"));
    }

    @Test
    public void toolsMenu() {
        mainPage.toolsMenu.click();

        $("div[data-test='main-submenu']").shouldBe(visible);
    }

    @Test
    public void navigationToAllTools() {
        mainPage.seeAllToolsButton.click();

        $("#products-page").shouldBe(visible);

        assertEquals("All Developer Tools and Products by JetBrains", Selenide.title());
    }
     */
}
