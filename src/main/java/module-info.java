module esprit.monstergym.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;


    opens esprit.monstergym.demo to javafx.fxml;
    opens esprit.monstergym.demo.Utils to javafx.fxml;


    exports esprit.monstergym.demo;
    exports esprit.monstergym.demo.Entities; // Ajoutez cette ligne pour exporter le package Entities
    exports esprit.monstergym.demo.Service;
    exports esprit.monstergym.demo.Utils;



}