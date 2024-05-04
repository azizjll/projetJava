package esprit.monstergym.demo.Controllers;


import esprit.monstergym.demo.Entities.Event;
import esprit.monstergym.demo.Service.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;

public class AdminEventController {
    private final ServiceEvent ST = new ServiceEvent();

    private String imagePath;
    private Image image;
    @FXML
    private AnchorPane principal;

    @FXML
    private TextField date;
    @FXML
    private Button Import_btn;

    @FXML
    private Button aj;

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
    private DatePicker datePicker;

    @FXML
    private TextField des;

    @FXML
    private Label hello;

    @FXML
    private Label hello1;

    @FXML
    private ImageView image_id;

    @FXML
    private ListView<Event> listTerrain;

    @FXML
    private TextField loclt;

    @FXML
    private Label lplStatus;

    @FXML
    private Label lplStatusMini;

    @FXML
    private Button moditr;

    @FXML
    private TextField nom;

    @FXML
    private TextField nomm;

    @FXML
    private Pane pnlStatus;

    @FXML
    private Button supptr;

    @FXML
    private VBox vBoxDashboardAdmin;

    @FXML
    private TextField Searchfield;

    @FXML
    private ObservableList<Event>TerrainList;

    @FXML
    private Button tri;
    @FXML
    private Button tridate;


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
        try {
            // Charge le FXML pour la vue ajoutEvent
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/ajoutEvent.fxml"));
            Parent root = loader.load();

            // Obtient la scène du bouton actuel et y place le nouveau root
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Met à jour le titre de la fenêtre, si désiré
            stage.setTitle("Ajout d'un Nouvel Événement");

            // Affiche la nouvelle vue
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Log l'exception
            showAlert("Erreur de Chargement", "Impossible de charger la vue d'ajout d'événement.");
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
    void handleClicks(ActionEvent event) {

    }

    @FXML
    void handleLogOut(ActionEvent event) {

    }

    @FXML
    void moditr(ActionEvent event) {
        Event selectedEvent = listTerrain.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Récupération des valeurs des champs de texte
            String noma = nom.getText();
            String description = des.getText();
            String lieu = loclt.getText();
            LocalDate dateEvent = datePicker.getValue();

            if (dateEvent == null || dateEvent.isBefore(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation de la date");
                alert.setHeaderText(null);
                alert.setContentText("La date doit être correctement définie et ne peut pas être dans le passé.");
                alert.showAndWait();
                return;
            }

            // Mise à jour de l'objet Event
            selectedEvent.setNom(noma);
            selectedEvent.setDescription(description);
            selectedEvent.setLieu(lieu);
            selectedEvent.setDate(dateEvent);
            selectedEvent.setImage(imagePath); // Assurez-vous que vous avez un setter pour l'image

            // Mise à jour de l'événement dans la base de données
            try {
                ST.modifier(selectedEvent); // Assurez-vous que la méthode modifier existe et fonctionne correctement
                // Actualisation de la liste
                listTerrain.getItems().set(listTerrain.getSelectionModel().getSelectedIndex(), selectedEvent);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("L'événement a été mis à jour avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de la mise à jour de l'événement");
                alert.setContentText("Une erreur est survenue : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Si aucun événement n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un événement à modifier.");
            alert.showAndWait();
        }
    }


    @FXML
    void supptr(ActionEvent event) {
        Event selectedEvent = listTerrain.getSelectionModel().getSelectedItem(); // Assurez-vous que 'listEvents' est votre ListView
        if (selectedEvent != null) {
            ServiceEvent se = new ServiceEvent();
            try {
                // Appel de la méthode pour supprimer l'événement, en supposant qu'il y ait une méthode comme deleteEvent
                se.supprimer(selectedEvent.getId()); // Utilisez getId ou votre méthode actuelle pour obtenir l'ID

                // Supprimez l'élément sélectionné de la ListView
                listTerrain.getItems().remove(selectedEvent);

                // Effacer la sélection
                listTerrain.getSelectionModel().clearSelection();

                // Effacer les champs textuels si nécessaire
                nomm.clear();
                nom.clear();
                des.clear(); // Supposons que vous avez des champs pour description, etc.
                loclt.clear();
                date.clear();

                // Afficher une alerte de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("L'événement a été supprimé avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                // Afficher une alerte d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de la suppression de l'événement");
                alert.setContentText("Une erreur est survenue : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Afficher une alerte d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un événement à supprimer.");
            alert.showAndWait();
        }
    }



    @FXML
    void initialize() {
        assert listTerrain != null : "fx:id=\"listTerrain\" was not injected: check your FXML file 'AdminEvent.fxml'.";
        // D'autres assertions ici...

        ServiceEvent se = new ServiceEvent();
        try {


            TerrainList = FXCollections.observableArrayList(se.afficher());  // Assurez-vous que TerrainsList est initialisée ici
            listTerrain.setItems(TerrainList);  // Utilisez TerrainsList comme source pour listTerrain




            listTerrain.setCellFactory(listView -> new ListCell<>() {
                @Override
                public void updateItem(Event event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(10); // HBox with spacing of 10
                        Label nameLabel = new Label(event.getNom());
                        nameLabel.setMinWidth(100); // Set minimum width for the label
                        Label descLabel = new Label(event.getDescription());
                        descLabel.setMinWidth(100); // Set minimum width for the label
                        Label locationLabel = new Label(event.getLieu());
                        locationLabel.setMinWidth(100); // Set minimum width for the label
                        Label dateLabel = new Label(event.getDate().toString());
                        dateLabel.setMinWidth(100); // Set minimum width for the label
                        hbox.getChildren().addAll(nameLabel, descLabel, locationLabel, dateLabel);
                        setGraphic(hbox); // Set the custom layout as the graphic of the list cell
                    }
                }
            });



        } catch (SQLException e) {
            e.printStackTrace(); // For debugging purposes, you might want to print the stack trace
            // Consider showing an alert to the user or logging the error to a file
        }


        // Ajout d'un écouteur sur la propriété de texte du champ de recherche pour la recherche dynamique.
        Searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
            search(); // Appel de la méthode de recherche à chaque modification du texte.
        });

        listTerrain.setOnMouseClicked(event -> {
            Event selectedItem = listTerrain.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                nomm.setText(selectedItem.getNom());
                nom.setText(selectedItem.getNom());
                des.setText(selectedItem.getDescription());
                loclt.setText(selectedItem.getLieu());
                datePicker.setValue(selectedItem.getDate()); // Utilisez le DatePicker pour afficher la date

                // Mise à jour de l'image
                if (selectedItem.getImage() != null && !selectedItem.getImage().isEmpty()) {
                    imagePath = selectedItem.getImage();
                    image_id.setImage(new Image(selectedItem.getImage()));
                } else {
                    imagePath = null;
                    image_id.setImage(null);  // Aucune image disponible pour cet événement
                }
            }
        });
    }

    @FXML
    public void tri(ActionEvent event) {
        if (TerrainList != null) {
            TerrainList.sort(Comparator.comparing(Event::getNom));
            listTerrain.setItems(TerrainList);
        } else {
            showAlert("Erreur", "La liste des Evenements est vide ou non initialisée.");
        }
    }


    @FXML
    public void tridate(ActionEvent event) {
        if (TerrainList != null) {
            TerrainList.sort(Comparator.comparing(Event::getDate));
            listTerrain.setItems(TerrainList);
        } else {
            showAlert("Erreur", "La liste des Evenements est vide ou non initialisée.");
        }
    }



    private void search() {
        String keyword = Searchfield.getText().toLowerCase().trim();
        ObservableList<Event> allItems = listTerrain.getItems();
        ObservableList<Event> filteredList = FXCollections.observableArrayList();

        if (!keyword.isEmpty()) {
            for (Event association : allItems) {
                if (association.getNom().toLowerCase().contains(keyword) || association.getDescription().toLowerCase().contains(keyword) || association.getLieu().toLowerCase().contains(keyword)) {
                    filteredList.add(association);
                }
            }
            listTerrain.setItems(filteredList);
        } else {
            refreshList();
        }
    }

    private void refreshList() {
        ServiceEvent service = new ServiceEvent();
        try {
            ObservableList<Event> associations = FXCollections.observableArrayList(service.afficher());
            listTerrain.setItems(associations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}

