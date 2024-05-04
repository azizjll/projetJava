package esprit.monstergym.demo.Controllers;

import esprit.monstergym.demo.Entities.Participation;
import esprit.monstergym.demo.Entities.User;
import esprit.monstergym.demo.Service.JavaMailUtil;
import esprit.monstergym.demo.Service.ServicePartc;
import esprit.monstergym.demo.Service.UserService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;

public class AjoutParticipationControlleur {

    @FXML
    private TextField nomField, prenomField, telField, emailField;
    @FXML
    private DatePicker datePicker;

    private int eventID;
    private UserService userService;
    public void setId(int id) {
        this.eventID = id;
    }

    @FXML
    private void handleParticipation(ActionEvent event) {
        try {
            userService = userService.getInstance();
            User authentificatedUser = userService.getAuthenticatedUser();
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String tel = telField.getText().trim();
            String email = emailField.getText().trim();
            LocalDate date = datePicker.getValue();

            // Validation des champs
            if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || email.isEmpty() || date == null) {
                showNotificationsupp( "Tous les champs doivent être remplis");
                return;
            }

            if (nom.length() < 4 || prenom.length() < 4) {
                showNotificationsupp( "Le nom et le prénom doivent contenir au moins 4 caractères.");
                return;
            }

            if (!tel.matches("\\d{8}")) {
                showNotificationsupp( "Le numéro de téléphone doit être exactement 8 chiffres.");
                return;
            }

            if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                showNotificationsupp("L'adresse email n'est pas valide.");
                return;
            }

            Participation participation = new Participation(Integer.parseInt(tel), authentificatedUser.getId(), eventID, nom, prenom, email, date);
            ServicePartc servicePartc = new ServicePartc();
            servicePartc.ajouter(participation);


            JavaMailUtil.sendMail(emailField.getText().trim(),"Participation","succes participation dans l'evenement boxe");


            // Redirection vers Participation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/DashboardClient.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            showNotification( "Participation ajoutée avec succès !");
        } catch (NumberFormatException e) {
            showNotificationsupp( "Le numéro de téléphone doit être un nombre valide.");
        } catch (Exception e) {
            e.printStackTrace();
            showNotificationsupp("Erreur lors du chargement de la page des participations");
        }
    }

    private void showAlert(AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(header);
        alert.showAndWait();
    }




    private void showNotification(String message) {
        Stage notificationStage = new Stage();
        notificationStage.initStyle(StageStyle.TRANSPARENT);
        notificationStage.setAlwaysOnTop(true);

        Label notificationLabel = new Label(message);
        notificationLabel.getStyleClass().add("notification-label");

        StackPane layout = new StackPane(notificationLabel);
        layout.getStyleClass().add("notification-pane");

        Scene notificationScene = new Scene(layout, 400, 100);
        notificationScene.setFill(null);

        // Load CSS with proper null check
        URL cssUrl = getClass().getResource("/esprit/monstergym/demo/styles.css");
        if (cssUrl != null) {
            notificationScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Failed to load CSS file: check the resource path.");
            // Optionally apply a default style in case CSS file is missing
            notificationLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10;");
        }

        notificationStage.setScene(notificationScene);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        notificationStage.setX(bounds.getMaxX() - 400);
        notificationStage.setY(bounds.getMaxY() - 140);
        notificationStage.show();

        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(5), ev -> {
            notificationStage.close();
        }));
        timeline.play();
    }


    private void showNotificationsupp (String message) {
        Stage notificationStage = new Stage();
        notificationStage.initStyle(StageStyle.TRANSPARENT);
        notificationStage.setAlwaysOnTop(true);

        Label notificationLabel = new Label(message);
        notificationLabel.getStyleClass().add("notification-supp");

        StackPane layout = new StackPane(notificationLabel);
        layout.getStyleClass().add("notification-pane");

        Scene notificationScene = new Scene(layout, 400, 100);
        notificationScene.setFill(null);

        // Load CSS with proper null check
        URL cssUrl = getClass().getResource("/esprit/monstergym/demo/styles.css");
        if (cssUrl != null) {
            notificationScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Failed to load CSS file: check the resource path.");
            // Optionally apply a default style in case CSS file is missing
            notificationLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10;");
        }

        notificationStage.setScene(notificationScene);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        notificationStage.setX(bounds.getMaxX() - 400);
        notificationStage.setY(bounds.getMaxY() - 140);
        notificationStage.show();

        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(5), ev -> {
            notificationStage.close();
        }));
        timeline.play();
    }



}