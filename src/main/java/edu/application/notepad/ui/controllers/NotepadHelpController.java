package edu.application.notepad.ui.controllers;

import edu.application.notepad.Main;
import edu.application.notepad.utils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class NotepadHelpController {

    @FXML
    private Text text;

    @FXML
    public void initialize() {
        String content = FileUtils.loadText(Main.class.getResourceAsStream("help.txt"));
        text.setText(content);
    }
}
