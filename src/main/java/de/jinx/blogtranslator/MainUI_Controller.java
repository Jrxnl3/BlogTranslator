package de.jinx.blogtranslator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import  de.jinx.blogtranslator.Converter;

import static de.jinx.blogtranslator.Converter.generateHTML;

public class MainUI_Controller {

    @FXML
    private Button btnGenerateBlog;

    @FXML
    private TextArea errorField;

    @FXML
    private TextField folderPathField;

    @FXML
    private TextField idPath;

    @FXML
    private TextField outputFolderPath;

    @FXML
    private TextField pathOfPDFField;

    @FXML
    private TextField urlTextField;

    @FXML
    private Button btnTranslate;

    @FXML
    private TextField outputField;

    @FXML
    private TextField translatedPDFField;


    @FXML
    void generateBlog(ActionEvent event) {

        MainPage mainPage = new MainPage(urlTextField.getText(), idPath.getText(),folderPathField.getText());
        String errorCode = mainPage.downloadHTMLBlog();

        if(!errorCode.equals("")){
            errorField.setText(errorCode);
        }
    }

    @FXML
    void startTranslation(ActionEvent event) {
        TranslateThread translateThread = new TranslateThread(pathOfPDFField.getText(),outputFolderPath.getText());
        translateThread.run();
    }

    @FXML
    void convert(ActionEvent event) {
        generateHTML(translatedPDFField.getText(),outputField.getText());
    }
}