package de.jinx.blogtranslator;

import com.aspose.pdf.Document;
import com.aspose.pdf.HtmlLoadOptions;
import com.aspose.pdf.SaveFormat;

public class Converter {

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

}
