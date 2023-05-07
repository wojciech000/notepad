package edu.application.notepad.ui.events;

import edu.application.notepad.utils.FileUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

import java.io.File;
import java.util.Map;

public class TabCloseEvent implements EventHandler<Event> {

    private TabPane tabPane;
    private Map<File, String> mapTabFiles;
    private Tab tab;

    private TextArea textArea;

    public TabCloseEvent(Map<File, String> mapTabFiles, TabPane tabPane, Tab tab, TextArea textArea) {
        this.tabPane = tabPane;
        this.mapTabFiles = mapTabFiles;
        this.tab = tab;
        this.textArea = textArea;
    }


    @Override
    public void handle(Event event) {
        var fileClosedTab = new File(tab.getId());
        mapTabFiles.remove(fileClosedTab);

        FileUtils.writeOpenedTabs(mapTabFiles);

        if(!tabPane.getSelectionModel().isEmpty()) {
            var fileSelectedTab = new File(tabPane.getSelectionModel().getSelectedItem().getId());
            textArea.setText(mapTabFiles.get(fileSelectedTab));
        }
    }
}
