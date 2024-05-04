package esprit.monstergym.demo.Controllers;

import esprit.monstergym.demo.Entities.Event;
import esprit.monstergym.demo.Service.ServiceEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

public class ParticipationController {

    ServiceEvent ST =new ServiceEvent();

    @FXML
    private TextField Searchfield;

    @FXML
    private ObservableList<Event> TerrainList;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private AnchorPane NomId;

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
    private HBox hBoxEvents;

    @FXML
    private Label lplStatus;

    @FXML
    private Label lplStatusMini;

    @FXML
    private Pane pnlStatusClient;

    @FXML
    private VBox vBoxDashboardClient;

    @FXML
    void handleClicksClient(ActionEvent event) {

    }

    @FXML
    void handleLogOutClient(ActionEvent event) {

    }
    @FXML
    public void initialize() {
        search(); // Appeler la recherche au chargement initial

        // Ajout d'un écouteur sur la propriété de texte du champ de recherche pour la recherche dynamique.
        Searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
            search(); // Appel de la méthode de recherche à chaque modification du texte.
        });
    }

    private void search() {
        String keyword = Searchfield.getText().toLowerCase().trim();
        hBoxEvents.getChildren().clear(); // Effacer les éléments précédents avant d'ajouter les nouveaux

        try {
            Set<Event> events = ST.afficher();

            for (Event event : events) {
                if (event.getNom().toLowerCase().contains(keyword) || event.getDescription().toLowerCase().contains(keyword) || event.getLieu().toLowerCase().contains(keyword)) {
                    // Créer les éléments visuels pour afficher l'événement
                    String imagePath = event.getImage();
                    Image image = loadImage(imagePath);
                    ImageView imageView = new ImageView(image); // Déclaration et initialisation de imageView

                    // Création des éléments visuels pour afficher les détails de l'événement
                    Label nomLabel = new Label(event.getNom());
                    nomLabel.setStyle("-fx-text-fill: black;");

                    Label descriptionLabel = new Label("Description : " + event.getDescription());
                    descriptionLabel.setStyle("-fx-text-fill: black;");

                    Label lieuLabel = new Label(event.getLieu());
                    lieuLabel.setStyle("-fx-text-fill: black;");

                    Label dateLabel = new Label(event.getDate().toString());
                    dateLabel.setStyle("-fx-text-fill: black;");

                    Button reserverButton = new Button("Participer");
                    reserverButton.setUserData(event.getId());
                    reserverButton.setOnAction(this::ajouter_part);

                    Button detailsButton = new Button("Détails");
                    detailsButton.setUserData(event.getId());
                    detailsButton.setOnAction(e -> showEventDetails(event));

                    VBox eventBox = new VBox(imageView, nomLabel, descriptionLabel, lieuLabel, dateLabel, detailsButton, reserverButton);
                    eventBox.setSpacing(10);

                    hBoxEvents.getChildren().add(eventBox);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void refreshList() {
        hBoxEvents.getChildren().clear(); // Effacer les éléments précédents avant de rafraîchir la liste

        try {
            Set<Event> events = ST.afficher();

            for (Event event : events) {
                // Créer les éléments visuels pour afficher l'événement
                String imagePath = event.getImage();
                Image image = loadImage(imagePath);
                ImageView imageView = new ImageView(image); // Déclaration et initialisation de imageView

                // Création des éléments visuels pour afficher les détails de l'événement
                Label nomLabel = new Label(event.getNom());
                nomLabel.setStyle("-fx-text-fill: black;");

                Label descriptionLabel = new Label("Description : " + event.getDescription());
                descriptionLabel.setStyle("-fx-text-fill: black;");

                Label lieuLabel = new Label(event.getLieu());
                lieuLabel.setStyle("-fx-text-fill: black;");

                Label dateLabel = new Label(event.getDate().toString());
                dateLabel.setStyle("-fx-text-fill: black;");

                Button reserverButton = new Button("Participer");
                reserverButton.setUserData(event.getId());
                reserverButton.setOnAction(this::ajouter_part);

                Button detailsButton = new Button("Détails");
                detailsButton.setUserData(event.getId());
                detailsButton.setOnAction(e -> showEventDetails(event));

                VBox eventBox = new VBox(imageView, nomLabel, descriptionLabel, lieuLabel, dateLabel, detailsButton, reserverButton);
                eventBox.setSpacing(10);

                hBoxEvents.getChildren().add(eventBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    private Image loadImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                String imageUrl = imageFile.toURI().toURL().toString();
                return new Image(imageUrl, 150, 150, false, true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // Optionally, return a placeholder image if the URL is malformed
            }
        }
        // Return a default image or handle the error appropriately
        return new Image("/path/to/placeholder.png"); // Make sure this path is correct
    }







    private void showEventDetails(Event event) {
        Stage detailsStage = new Stage();
        AnchorPane detailsPane = new AnchorPane();
        detailsPane.setPadding(new Insets(10));
        URL stylesheetURL = getClass().getResource("/styles.css");
        if (stylesheetURL != null) {
            detailsPane.getStylesheets().add(stylesheetURL.toExternalForm());
            detailsPane.getStyleClass().add("event-details");
        } else {
            System.out.println("Stylesheet not found.");
        }

        String details = String.format("Détails de l'Événement:\nNom: %s\nDescription: %s\nLieu: %s\nDate: %s\n",
                event.getNom(),
                event.getDescription(),
                event.getLieu(),
                event.getDate().toString());
        Label detailsLabel = new Label(details);
        detailsLabel.setFont(new Font("Arial", 14));
        detailsLabel.setWrapText(true);

        String imagePath = event.getImage();
        Image image = null;
        if (imagePath != null) {
            try {
                File imageFile = new File(imagePath);
                image = new Image(new FileInputStream(imageFile), 200, 200, true, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // Gestion du cas où le chemin de l'image est null ou incorrect
            }
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> detailsStage.close());
        closeButton.getStyleClass().add("close-button");

        detailsPane.getChildren().addAll(imageView, detailsLabel, closeButton);
        AnchorPane.setTopAnchor(imageView, 20.0);
        AnchorPane.setTopAnchor(detailsLabel, 230.0);
        AnchorPane.setTopAnchor(closeButton, 450.0);

        Scene detailsScene = new Scene(detailsPane, 400, 500);
        detailsStage.setScene(detailsScene);
        detailsStage.setTitle("Détails de l'Événement");
        detailsStage.show();
    }




    @FXML
    public void ajouter_part(ActionEvent actionEvent) {
        int idEvenement = (int)((Button) actionEvent.getSource()).getUserData();  // Obtention de l'ID de l'événement depuis le bouton
        try {
            // Chargement du fichier FXML pour la fenêtre d'ajout de participation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/AjoutParticipation.fxml"));
            Parent root = loader.load();

            // Obtention du contrôleur associé et passage de l'ID de l'événement
            AjoutParticipationControlleur controller = loader.getController();
            controller.setId(idEvenement);  // Assurez-vous que la méthode setId existe dans AjoutParticipationControlleur

            // Changement de la racine de la scène pour afficher la nouvelle fenêtre
            NomId.getScene().setRoot(root);  // 'NomId' doit être un élément UI de votre scène actuelle pouvant accéder à 'getScene()'
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void tri(ActionEvent actionEvent) {

    }

    public void listpar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/Userparticipation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/AjoutParticipation.fxml"));
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