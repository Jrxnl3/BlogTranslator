package de.jinx.blogtranslator;

import com.aspose.pdf.Document;
import com.aspose.pdf.HtmlLoadOptions;
import com.aspose.pdf.SaveFormat;
import com.lowagie.text.DocumentException;
import com.sun.tools.javac.Main;
import org.fit.pdfdom.PDFToHTML;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

public class ThreadManager {

    //NT = NOT TRANSLATED | T = TRANSLATED

    public static void main(String[] args) {
        //GET BLOG
        MainPage mainPage = new MainPage();
        try {
            mainPage.createTranslationText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //GENERATE PDF TO TRANSLATE
        generatePDFV2("./blogNT.html", "./blogNT.pdf");
        generateHTML("./blogNT.pdf", "./blogT.html");

        //TRANSLATE
        TranslateThread translateThread = new TranslateThread("./blogNT.pdf");
        //translateThread.run();

        //GENERATE PDF FROM TRANSLATED HTML After thread is done


        //GENERATE HTML FROM TRANSLATED PDF

    }

    public static void generatePDFV2(String inputHtmlPath, String outputPdfPath){
        // Create a HTML LoadOptions
        HtmlLoadOptions options = new HtmlLoadOptions();

        // Initialize document object
        Document document = new Document(inputHtmlPath, options);

        // Save output PDF document
        document.save(outputPdfPath);
    }

    public static void generateHTML(String inputHtmlPath, String outputPdfPath){
        // Open the source PDF document
        Document pdfDocument = new Document(inputHtmlPath);

        // Save the file into MS document format
        pdfDocument.save(outputPdfPath, SaveFormat.Html);
    }

    public static void generatePDF(String inputHtmlPath, String outputPdfPath)
    {
        try {
            String url = new File(inputHtmlPath).toURI().toURL().toString();

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
