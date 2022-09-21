package de.jinx.blogtranslator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;

import static de.jinx.blogtranslator.Converter.generatePDFV2;

public class MainPage {
    public String url;
    public String classPath;
    public String pathFolder;

    public MainPage(String url, String classPath, String pathFolder) {
        this.url = url;
        this.classPath = classPath;
        this.pathFolder = pathFolder;
    }

    public String downloadHTMLBlog() {
        try {
            //Opening Site + Getting Element
            Document doc = Jsoup.connect(url).get();
            Element parentTextHolder = doc.getElementsByClass(classPath).first();

            //File Writer
            String fileName = pathFolder + File.separator + "Blog_NT " + doc.title().replaceAll("[^a-zA-Z0-9]", "") + ".html";
            File file = new File(fileName);
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

            generatePDFV2(fileName, fileName.replace(".html", ".pdf"));

            return "Generated not translated PDF!";
        }
        catch (Exception e) {
           return e.getMessage();
        }
    }
}
