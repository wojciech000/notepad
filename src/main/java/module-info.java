module edu.application.notepad {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.application.notepad to javafx.fxml;
    exports edu.application.notepad;
    exports edu.application.notepad.ui.controllers;
    opens edu.application.notepad.ui.controllers to javafx.fxml;
    exports edu.application.notepad.ui.events;
    opens edu.application.notepad.ui.events to javafx.fxml;
    exports edu.application.notepad.ui;
    opens edu.application.notepad.ui to javafx.fxml;
}