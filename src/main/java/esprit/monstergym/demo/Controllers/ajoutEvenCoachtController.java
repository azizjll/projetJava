package esprit.monstergym.demo.Controllers;

import esprit.monstergym.demo.Entities.Event;
import esprit.monstergym.demo.Entities.User;
import esprit.monstergym.demo.Service.ServiceEvent;
import esprit.monstergym.demo.Service.UserService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ajoutEvenCoachtController {
    private String imagePath;
    private Image image;
    @FXML
    private AnchorPane principal;

    @FXML
    private ObservableList<Event> TerrainsList;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField des;

    @FXML
    private TextField loclt;

    @FXML
    private TextField nom;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button aj;

    @FXML
    private Button Import_btn;

    @FXML
    private ImageView image_id;
    private UserService userService;
    private final ServiceEvent ST = new ServiceEvent();

    @FXML
    public void Import(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null); // Changed from principal.getScene().getWindow() to null for simplicity
        if (file != null) {
            imagePath = file.toURI().toString();
            // Supprimer le préfixe "file:/"
            if (imagePath.startsWith("file:/")) {
                imagePath = imagePath.substring(6); // Enlève "file:/" du début du chemin
            }
            image_id.setImage(new Image(imagePath));
        }
    }

    @FXML
    private void ajt(ActionEvent event) {
        userService = userService.getInstance();
        User authentificatedUser = userService.getAuthenticatedUser();
        String nomEvent = nom.getText().trim();
        String description = des.getText().trim();
        String lieu = loclt.getText().trim();

        if (nomEvent.length() < 2) {
            showAlert("Erreur de Validation", "Le nom de l'événement doit contenir au moins 2 caractères et non vide.");
            return;
        }
        if (description.length() < 12) {
            showAlert("Erreur de Validation", "La description de l'événement doit contenir au moins 12 caractères et non vide.");
            return;
        }
        if (lieu.length() < 3) {
            showAlert("Erreur de Validation", "Le lieu de l'événement doit contenir au moins 3 caractères et non vide.");
            return;
        }

        // Vérification de la date
        LocalDate dateEvent = datePicker.getValue();
        if (dateEvent == null || dateEvent.isBefore(LocalDate.now())) {
            showAlert("Erreur de Date", "La date de l'événement ne peut pas être dans le passé ou vide.");
            return;
        }

        Event newEvent = new Event();
        newEvent.setNom(nomEvent);
        newEvent.setDescription(description);
        newEvent.setLieu(lieu);
        newEvent.setDate(dateEvent);
        newEvent.setImage(imagePath);
        newEvent.setUser_id(authentificatedUser.getId());

        // Ajout de l'événement à la base de données et à la liste
        ST.ajouter(newEvent);

        showAlert("Succès", "L'événement a été ajouté avec succès.");

        nom.clear();
        des.clear();
        loclt.clear();
        datePicker.setValue(null); // Nettoyez le DatePicker
        image_id.setImage(null);
        imagePath = null;

        // Load the new FXML file and switch to it
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/DashboardClient.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        assert des != null : "fx:id=\"des\" was not injected: check your FXML file 'ajoutEvent.fxml'.";
        assert loclt != null : "fx:id=\"loclt\" was not injected: check your FXML file 'ajoutEvent.fxml'.";
        assert nom != null : "fx:id=\"nom\" was not injected: check your FXML file 'ajoutEvent.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'ajoutEvent.fxml'.";
        assert aj != null : "fx:id=\"ajj\" was not injected: check your FXML file 'ajoutEvent.fxml'.";
        assert Import_btn != null : "fx:id=\"Import_btn\" was not injected: check your FXML file 'ajoutEvent.fxml'.";
        assert image_id != null : "fx:id=\"image_id\" was not injected: check your FXML file 'ajoutEvent.fxml'.";

    }
}

