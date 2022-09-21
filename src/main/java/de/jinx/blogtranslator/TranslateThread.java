package de.jinx.blogtranslator;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.*;

public class TranslateThread implements Runnable {

    String fileToTranslate;
    String outputFolderPath;

    public TranslateThread(String fileToTranslate, String outputFolderPath) {
        this.fileToTranslate = fileToTranslate;
        this.outputFolderPath = outputFolderPath;
    }

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
        } catch (Exception ignored) {
        }
    }

    public void scrollToClick(String cssSelector, int pixelX, int pixelY) {
        WebElement element = WebDriverRunner.getWebDriver().findElement(By.cssSelector(cssSelector));

        do {
            ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("window.scrollBy("+pixelX+","+pixelY+")");
            try {
                $(cssSelector).click();
                break;
            }catch (Exception ignored) {}
        }while (!element.isDisplayed() && !element.isEnabled());
    }

    public void startTranslation() throws IOException {
        open("https://www.deepl.com/translator/files");

        //change selenium download folder
        Configuration.downloadsFolder = outputFolderPath;

        waitSite(2);

        //Drag blog_to_translate.html into the dropzone
        $("#file-upload_input").uploadFile(new File(fileToTranslate));

        //Click Datenschutz
        waitElement(3,"button.button--WXFy4:nth-child(2)");
        $("button.button--WXFy4:nth-child(2)").click();

        waitSite(2);

        //Select German
        scrollToClick("div.lmt__language_select_column:nth-child(1) > button:nth-child(4)", 0, 100);

        //Click on translate
        scrollToClick(".smaller--uk4pu", 0, 50);

        waitSite(5);

        //Wait for Translating to be completed
        while ($(".docTrans_document__progress_image--29g9e > img:nth-child(1)").isDisplayed()) {
            waitSite(1);
        }

        waitSite(5);

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
