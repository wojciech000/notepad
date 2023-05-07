package edu.application.notepad.ui.controllers;

import edu.application.notepad.Main;
import edu.application.notepad.ui.NotepadApplication;
import edu.application.notepad.utils.FileUtils;
import edu.application.notepad.ui.events.TabCloseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class NotepadController {

    private static int untitledCounter = 1;
    private Map<File, String> mapTabFiles;


    @FXML
    private TextArea textArea;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label pathFile;

    @FXML
    protected void performActionMenuFileNew() {
        String fileName = "untitled " + untitledCounter++;
        File file = new File(fileName);

        Tab tab = new Tab(fileName);
        tab.setId(file.getAbsolutePath());
        tab.setOnClosed(new TabCloseEvent(mapTabFiles, tabPane, tab, textArea));

        try {
            mapTabFiles.put(file.getCanonicalFile(), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tabPane.getTabs().add(tab);

        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    protected void performActionMenuFileOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        File file = fileChooser.showOpenDialog(tabPane.getScene().getWindow());

        if(file != null) {
            if(!mapTabFiles.containsKey(file)) {
                Tab tab = new Tab(file.getName());
                tab.setId(file.getAbsolutePath());

                tabPane.getTabs().add(tab);
                mapTabFiles.put(file, FileUtils.loadText(file));

                tabPane.getSelectionModel().select(tab);

                tab.setOnClosed(new TabCloseEvent(mapTabFiles, tabPane, tab, textArea));

                FileUtils.writeOpenedTabs(mapTabFiles);
            }
            else {
                Tab tab = tabPane.getTabs().stream().filter(x -> x.getId().equals(file.getAbsolutePath())).findFirst().get();
                tabPane.getSelectionModel().select(tab);
            }
        }
    }

    @FXML
    protected void performActionMenuFileSave() {
        File file = new File(pathFile.getText());

        if(file.exists()) {
            FileUtils.save(file, textArea.getText());
        } else {
            performActionMenuFileSaveAs();
        }
    }

    @FXML
    protected void performActionMenuFileSaveAs() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");

        File file = fileChooser.showSaveDialog(tabPane.getScene().getWindow());

        if(file == null) return;

        FileUtils.save(file, textArea.getText());

        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        mapTabFiles.remove(new File(tab.getId()));

        tab.setText(file.getName());
        tab.setId(file.getAbsolutePath());
        tab.setOnClosed(new TabCloseEvent(mapTabFiles, tabPane, tab, textArea));

        mapTabFiles.put(file, textArea.getText());

        pathFile.setText(tab.getId());

        FileUtils.writeOpenedTabs(mapTabFiles);
    }

    @FXML
    protected void performActionMenuFileExit() {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        tabPane.getTabs().remove(tab);

        mapTabFiles.remove(new File(tab.getId()));
        FileUtils.writeOpenedTabs(mapTabFiles);
    }

    @FXML
    protected void performActionMenuEditCopy() {
        Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{ putString(textArea.getSelectedText()); }} );
    }

    @FXML
    protected void performActionMenuEditCut() {
        textArea.replaceSelection("");
    }

    @FXML
    protected void performActionMenuEditPaste() {
        textArea.replaceSelection(Clipboard.getSystemClipboard().getString());
    }

    @FXML
    protected void performActionMenuEditFindAndReplace() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("notepad-findandreplace-view.fxml"));
            Parent loader = fxmlLoader.load();

            NotePadFindAndReplaceController controller = fxmlLoader.getController();
            controller.setTextArea(textArea);

            Scene scene = new Scene(loader);
            Stage stage = new Stage();

            stage.setTitle("Notepad Find And Replace");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void performActionMenuHelpAbout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("notepad-about-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();

            stage.setTitle("Notepad About");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void performActionMenuHelpHelp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("notepad-help-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();

            stage.setTitle("Notepad Help");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            /*int counter = 1;
            for(Map.Entry<File, String> entry : mapTabFiles.entrySet()) {
                System.out.println(counter++ + ". "  + entry.getKey());
            }

            System.out.println();*/

            if(oldValue != null) {
                File oldTabFile = new File(oldValue.getId());
                String content = textArea.getText();

                mapTabFiles.put(oldTabFile, content);

            }
            if(newValue != null) {
                File newTabFile = new File(newValue.getId());
                String content = mapTabFiles.get(newTabFile);
                textArea.setText(content);

                if(newTabFile.exists()) pathFile.setText(newTabFile.getAbsolutePath());
                else pathFile.setText("Save As");
            }
            else textArea.setText("");
        }));

        createTabsAtStart();
    }

    private void createTabsAtStart() {
        mapTabFiles = FileUtils.loadOpenedTabs();

        if(mapTabFiles.size() == 0) {
            String fileName = "untitled " + untitledCounter++;
            File file = new File(fileName);

            Tab tab = new Tab(fileName);
            tab.setId(file.getAbsolutePath());

            tabPane.getTabs().add(tab);
            try {
                mapTabFiles.put(file.getCanonicalFile(), "");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            for(Map.Entry<File, String> entry : mapTabFiles.entrySet()) {

                Tab tab = new Tab(entry.getKey().getName());
                tab.setId(entry.getKey().getAbsolutePath());
                tab.setOnClosed(new TabCloseEvent(mapTabFiles, tabPane, tab, textArea));

                tabPane.getTabs().add(tab);
            }
        }
    }
}