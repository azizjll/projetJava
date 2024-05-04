package esprit.monstergym.demo.Controllers;

import esprit.monstergym.demo.Entities.Participation;
import esprit.monstergym.demo.Entities.Pdf;
import esprit.monstergym.demo.Service.ServicePartc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Set;

public class AfficherParticipationController {

ServicePartc p = new ServicePartc();



    @FXML
    private ListView<Participation> listReservation;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAddService;

    @FXML
    private Button btnAddUser;

    @FXML
    private Button btnCommande;

    @FXML
    private Button btnEvenementCruds;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnListReservations;

    @FXML
    private Button btnListServices;

    @FXML
    private Button btnListUsers;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnParticipations;

    @FXML
    private Button btnProfil;

    @FXML
    private Button btncommande;

    @FXML
    private Button btnfactureback;

    @FXML
    private Button btnproduit;

    @FXML
    private Button btnreclamation;

    @FXML
    private Label hello;

    @FXML
    private Label hello1;




    @FXML
    private Label lplStatus;

    @FXML
    private Label lplStatusMini;

    @FXML
    private TextField nomm;

    @FXML
    private Pane pnlStatus;

    @FXML
    private Button supres;

    @FXML
    private VBox vBoxDashboardAdmin;

    @FXML
    private Button pdf;
    @FXML
    void handleClicks(ActionEvent event) {

    }

    @FXML
    void handleLogOut(ActionEvent event) {

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
    void modtr(ActionEvent event) {

    }

    @FXML
    void initialize() throws SQLException {
        loadParticipations();
        assert nomm != null : "fx:id=\"nomm\" was not injected: check your FXML file 'AdminParticipation.fxml'.";
        assert supres != null : "fx:id=\"supres\" was not injected: check your FXML file 'AdminParticipation.fxml'.";
        listReservation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nomm.setText(newValue.getNom());

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
                    // Formater selon les attributs de Participation
                    setText(String.format("Nom: %s, Prénom: %s, Date: %s",
                            item.getNom(),
                            item.getPrenom(),
                            item.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                }
            }
        });
    }


    @FXML
    void pdf(ActionEvent event) {
        // Créer une instance de la classe Pdf
        Pdf pdfGenerator = new Pdf();

        try {
            // Appeler la méthode generatePdf en passant le nom du fichier PDF
            pdfGenerator.generatePdf("Mes Associations");
            showAlert("Succès", "Le PDF a été généré avec succès.");
        } catch (IOException | SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de la génération du PDF : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
