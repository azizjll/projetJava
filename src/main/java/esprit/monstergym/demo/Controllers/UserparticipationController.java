package esprit.monstergym.demo.Controllers;


import esprit.monstergym.demo.Entities.Participation;
import esprit.monstergym.demo.Service.ServicePartc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Set;


public class UserparticipationController {



    @FXML
    private TextField tele;
    @FXML
    private Button btnAjoutRec;

    @FXML
    private Button btnEvent;

    @FXML
    private Button btnHomeClient;

    @FXML
    private Button btnListRec;

    @FXML
    private Button btnListReservationsClient;

    @FXML
    private Button btnLogOutClient;

    @FXML
    private Button btnParticiper;

    @FXML
    private Button btnProduit;

    @FXML
    private Button btnProfilClient;

    @FXML
    private Button btnService;

    @FXML
    private Button btncommandee;

    @FXML
    private Button btnfacture;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField even;



    @FXML
    private Label lplStatus;

    @FXML
    private Label lplStatusMini;

    @FXML
    private TextField mail;

    @FXML
    private Button moditr;

    @FXML
    private TextField nom;

    @FXML
    private TextField nomm;

    @FXML
    private Pane pnlStatusClient;

    @FXML
    private TextField pren;

    @FXML
    private Button supres;



    @FXML
    private VBox vBoxDashboardClient;

    @FXML
    void handleClicksClient(ActionEvent event) {

    }

    @FXML
    void handleLogOutClient(ActionEvent event) {

    }

    @FXML
    private ListView<Participation> listReservation;


    @FXML
    void moditr(ActionEvent event) throws IOException {
        Participation selectedParticipation = listReservation.getSelectionModel().getSelectedItem();
        if (selectedParticipation != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/modifierPart.fxml"));
            Parent root = loader.load();

            ModifierPartController modifierController = loader.getController();
            modifierController.setParticipationData(selectedParticipation);

            nomm.getScene().setRoot(root); // Assuming nomm is a component in your current scene
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Participation to modify.");
            alert.showAndWait();
        }
    }

    @FXML
    void suptr(ActionEvent event) {
        Participation selectedReservation = listReservation.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            ServicePartc SR  = new ServicePartc();
            try {
                // Call the method to delete the Reservation, assuming there is a method like deleteReservation
                SR.supprimer(selectedReservation.getId()); // replace getID_Reservation() with your actual method name for getting ID

                // Remove the selected item from the ListView
                listReservation.getItems().remove(selectedReservation);

                // Clear the selection
                listReservation.getSelectionModel().clearSelection();

                // Clear the TextField if needed
                nomm.clear();

                // Show confirmation alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Participation has been successfully deleted.");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                // Show error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to delete Participation");
                alert.setContentText("An error occurred: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Show warning alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Participation to delete.");
            alert.showAndWait();
        }
    }




    @FXML
    void initialize() throws SQLException {
        loadParticipations();
        assert nomm != null : "fx:id=\"nomm\" was not injected: check your FXML file 'AdminParticipation.fxml'.";
        assert supres != null : "fx:id=\"supres\" was not injected: check your FXML file 'AdminParticipation.fxml'.";
        listReservation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nomm.setText(newValue.getNom());
                tele.setText(String.valueOf(newValue.getTel())); // Set telephone number
            }
        });
    }


    public void loadParticipations() throws SQLException {
        ServicePartc servicePartc = new ServicePartc();
        Set<Participation> participations = servicePartc.afficher(); // Récupère les participations
        ObservableList<Participation> participationItems = FXCollections.observableArrayList(participations);

        listReservation.setItems(participationItems);
        listReservation.setCellFactory(param -> new ListCell<Participation>() {
            @Override
            protected void updateItem(Participation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Nom: %s, Prénom: %s, Téléphone: %d, Email: %s, Date: %s",
                            item.getNom(),
                            item.getPrenom(),
                            item.getTel(),  // Now using getTelephone()
                            item.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            item.getEvent_id(),
                            item.getEmail()
                    ));
                }
            }
        });
    }


    public void btnEventParticipation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/AdminEvent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void btnParticiper(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/AdminEvent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
