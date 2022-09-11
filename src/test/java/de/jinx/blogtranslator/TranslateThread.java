package de.jinx.blogtranslator;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.*;

public class TranslateThread implements Runnable {

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x720";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("https://www.deepl.com/translator/files");
    }

    public void waitSite(int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOf($x("//div[@class='lmt__translations_as_text__text_btn']")));
        }
        catch (Exception ignored) {}
    }

    //Wait until certain Element has loaded
    public void waitElement(int seconds, String cssSelektor) {
        try {
            WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOf($(cssSelektor)));
        }
        catch (Exception ignored) {}
    }
    public void waitElementXPath(int seconds, String xpath) {
        try {
            WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOf($x(xpath)));
        }
        catch (Exception ignored) {}
    }

    public void scroll(int pixelX, int pixelY) {
        ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("window.scrollBy(pixelX,pixelY)");
    }

    public void scrollToClick(String cssSelector) {
        WebElement element = WebDriverRunner.getWebDriver().findElement(By.cssSelector(cssSelector));

        do {
            scroll(0, 100);
            try {
                $(cssSelector).click();
                break;
            }catch (Exception ignored) {}
        }while (!element.isDisplayed() && !element.isEnabled());
    }

    public void scrollToClickXPath(String xPath) {
        WebElement element = WebDriverRunner.getWebDriver().findElement(By.xpath(xPath));

        do {
            scroll(0, 100);
            try {
                $x(xPath).click();
                break;
            }catch (Exception ignored) {}
        }while (!element.isDisplayed() && element.isEnabled());
    }

    public void startTranslation() throws IOException {
        open("https://www.deepl.com/translator/files");

        waitSite(2);

        //Drag blog_to_translate.html into the dropzone
        $("#file-upload_input").uploadFile(new File("./blog_toTranslate.pdf"));

        //Click Datenschutz
        waitElement(3,"button.button--WXFy4:nth-child(2)");
        $("button.button--WXFy4:nth-child(2)").click();

        waitSite(2);

        //Select German |  Function Not working
        scrollToClickXPath("div.lmt__language_select_column:nth-child(1) > button:nth-child(4)");

        //Click on translate
        scrollToClick(".smaller--uk4pu");

        //Wait for Translating
        waitElement(60,".docTrans_document__progress_image--29g9e > img:nth-child(1)");
    }

    @Override
    public void run() {
        try {
            startTranslation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
