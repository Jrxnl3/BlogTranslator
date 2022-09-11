package de.jinx.blogtranslator;

import com.lowagie.text.DocumentException;
import com.sun.tools.javac.Main;
import org.fit.pdfdom.PDFToHTML;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ThreadManager {
    public static void main(String[] args) {
        /*
        MainPage mainPage = new MainPage();
        try {
            mainPage.createTranslationText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        generatePDF("./blog.html", "./blog_toTranslate.pdf");
        PDFToHTML.main(new String[]{"./blog_toTranslate.pdf", "./blog_translated.html"});
        */
        TranslateThread translateThread = new TranslateThread();
        translateThread.run();
    }
    public static void generatePDF(String inputHtmlPath, String outputPdfPath)
    {
        try {
            String url = new File(inputHtmlPath).toURI().toURL().toString();
            System.out.println("URL: " + url);

            OutputStream out = new FileOutputStream(outputPdfPath);

            //Flying Saucer part
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(out);

            out.close();
        } catch (DocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
