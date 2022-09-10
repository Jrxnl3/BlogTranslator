package de.jinx.blogtranslator;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPageTest implements Runnable {
    List<String> translationTexts;

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("https://smartblogger.com/make-money-blogging/");
    }

    public void loadSite() {
        try {
            WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(1));
            wait.until(ExpectedConditions.visibilityOf($x("//div[@class='lmt__translations_as_text__text_btn']")));
        }
        catch (Exception ignored) {}
    }

    public void startTranslation() throws IOException {
        loadSite();

        File file = new File("./blog_translated.txt");
        FileWriter fileWriter = new FileWriter(file);

        for (String text : translationTexts) {
            open("https://www.deepl.com/translator#en/de/"+text);
            loadSite();

            Document doc = Jsoup.parse(WebDriverRunner.getWebDriver().getPageSource());
            fileWriter.write(doc.getElementById("target-dummydiv").text() + "\n");
        }

    }

    @Test
    public void getAllText() throws IOException {
        loadSite();

        Document doc = Jsoup.parse(WebDriverRunner.getWebDriver().getPageSource());
        Element parentTextHolder = doc.getElementsByClass("entry-content clear").first();

        File file = new File("./blog.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("<body>\n");

        for (int i = 0; i < parentTextHolder.children().size(); i++) {
            Element child = parentTextHolder.child(i);
            //if tag a paragh or header then continue
            if (child.tagName().equals("a") || child.tagName().equals("p") || child.tagName().equals("h1") || child.tagName().equals("h2") || child.tagName().equals("h3") || child.tagName().equals("h4") || child.tagName().equals("h5") || child.tagName().equals("h6")) {
                String formattedString = String.format("\t<%s>%s</%s> \n", child.tag(), child.text(), child.tag());

                fileWriter.write(formattedString);
            }
        }
        loadSite();
        fileWriter.write("</body>");
        fileWriter.close();

    }


    @Override
    public void run() {
        try {
            startTranslation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*
    @Test
    public void translate() throws IOException {
        try {
            WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(1));
            wait.until(ExpectedConditions.visibilityOf($x("//div[@class='lmt__translations_as_text__text_btn']")));
        }
        catch (Exception ignored) {}

        copyBlogText();

        File file = new File("./blog_translated.txt");
        FileWriter fileWriter = new FileWriter(file);

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

            fileWriter.write(doc.getElementById("target-dummydiv").text());
        }
    }
*/
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
