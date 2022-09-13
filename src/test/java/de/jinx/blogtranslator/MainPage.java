package de.jinx.blogtranslator;

import com.codeborne.selenide.SelenideElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    public String url = "https://smartblogger.com/make-money-blogging/";
    public String xpath = "entry-content clear";

    public void createTranslationText() throws IOException {
        //Opening Site + Getting Element
        Document doc = Jsoup.connect(url).get();
        Element parentTextHolder = doc.getElementsByClass(xpath).first();

        //File Writer
        File file = new File("./blogNT.html");
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
        //Ending File
        fileWriter.write("</body>");
        fileWriter.close();
    }
}
