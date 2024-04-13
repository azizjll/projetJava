package esprit.monstergym.demo.Utils;

import esprit.monstergym.demo.Entities.Commentaire;
import esprit.monstergym.demo.Service.CommentaireService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.List;
import java.util.Random;

public class CommentaireController {

    @FXML
    private TextField refTextField;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private TextField annonceIdTextField;

    @FXML
    private Button submitButton;

    @FXML
    private VBox commentaireContainer;



    private CommentaireService commentaireService;

    public CommentaireController() {
        commentaireService = new CommentaireService();
    }


    @FXML
    public void initialize() {
        loadCommentaires();
    }

    private void loadCommentaires() {
        // Retrieve all commentaires
        List<Commentaire> commentaires = commentaireService.getAll();

        // Clear the container before adding new commentaires
        commentaireContainer.getChildren().clear();

        // Display each commentaire
        for (Commentaire commentaire : commentaires) {
            Label messageLabel = new Label( commentaire.getMessage());
            Label dateLabel = new Label("Date: " + commentaire.getTimestamp());
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-text-fill: red;");

            deleteButton.setOnAction(event -> handleDeleteButtonAction(commentaire));





            Separator separator = new Separator(Orientation.HORIZONTAL);
            commentaireContainer.getChildren().addAll(messageLabel, dateLabel,deleteButton, separator);
        }
    }

 //deleting mehod
 private void handleDeleteButtonAction(Commentaire commentaire) {
     // Call the service method to delete the comment
     commentaireService.delete(commentaire);

     // Reload the commentaires to update the view
     loadCommentaires();
 }

    //add comment
    @FXML
    public void handleSubmitButtonAction() {
        // Retrieve input from text fields
        int ref = generateRef();
        System.out.println("Generated ref: " + ref);
        String message = messageTextArea.getText();
        int annonceId = Integer.parseInt(annonceIdTextField.getText());

        // Create a new Commentaire object
        Commentaire newCommentaire = new Commentaire(ref, message, getCurrentTimestamp(), annonceId);

        // Call the create method from CommentaireService to insert the new commentaire into the database
        commentaireService.create(newCommentaire);

        // Reload the commentaires to update the view
        loadCommentaires();

        // Clear input fields
        clearFields();
    }

    private Timestamp getCurrentTimestamp() {
        // Get the current timestamp
        return new Timestamp(System.currentTimeMillis());
    }

    public static int generateRef() {
        int maxRef = 0;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ref) AS max_ref FROM commentaire");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                maxRef = resultSet.getInt("max_ref");
            }
        } catch (SQLException e) {
            System.err.println("Error generating ref: " + e.getMessage());
        }

        return maxRef + 1; // Increment the greatest reference number by 1
    }

    private void clearFields() {
        // Clear input fields
        messageTextArea.clear();
        annonceIdTextField.clear();
    }


}

