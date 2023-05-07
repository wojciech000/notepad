package edu.application.notepad.ui.controllers;

import edu.application.notepad.Main;
import edu.application.notepad.ui.NotepadApplication;
import edu.application.notepad.utils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class NotepadAboutController {

    @FXML
    private Text text;

    @FXML
    public void initialize() {
        String content = FileUtils.loadText(Main.class.getResourceAsStream("about.txt"));
        text.setText(content);
    }
}
