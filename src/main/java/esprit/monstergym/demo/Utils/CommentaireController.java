    package esprit.monstergym.demo.Utils;

    import esprit.monstergym.demo.Entities.Annonce;
    import esprit.monstergym.demo.Entities.BadWordFilter;
    import esprit.monstergym.demo.Entities.Commentaire;
    import esprit.monstergym.demo.Entities.User;
    import esprit.monstergym.demo.Service.CommentaireService;

    import esprit.monstergym.demo.Service.JavaMailUtil;
    import esprit.monstergym.demo.Service.UserService;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Orientation;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;

    import javax.mail.MessagingException;
    import java.io.IOException;
    import java.sql.*;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Random;
    import esprit.monstergym.demo.Service.AnnonceService;

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


        private UserService userService;


        private CommentaireService commentaireService;
        private int annonce_id;

        public CommentaireController() {
            commentaireService = new CommentaireService();
        }


        @FXML
        public void initialize() {
            loadCommentaires();
        }

        private void loadCommentaires() {
            // Retrieve all commentaires
            List<Commentaire> commentaires = commentaireService.getAllCommentairesByAnnonceId(annonce_id);

            // Clear the container before adding new commentaires
            commentaireContainer.getChildren().clear();

            // Display each commentaire
            for (Commentaire commentaire : commentaires) {

                String user_id = null; // Initialize the user ID variable
                String query = "SELECT user_id FROM commentaire WHERE ref = ?";
                try (Connection connection = ConnectionManager.getConnection();
                     PreparedStatement statement = connection.prepareStatement(query)) {

                    // Set the ID parameter in the query
                    statement.setInt(1, commentaire.getRef());

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            user_id = resultSet.getString("user_id");
                        }
                    }
                } catch (SQLException e) {
                    // Handle any SQL exceptions
                    e.printStackTrace();
                }

                // Create a label for the user ID
                Label userIdLabel = new Label("User ID: " + user_id);
                userIdLabel.setStyle("-fx-font-weight: bold;");

                Label messageLabel = new Label( commentaire.getMessage());
                Label dateLabel = new Label("Date: " + commentaire.getTimestamp());

                userService = userService.getInstance();
                User authentificatedUser = userService.getAuthenticatedUser();

                if (user_id != null && user_id.equals(String.valueOf(authentificatedUser.getId()))) {
                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-text-fill: red;");

                    deleteButton.setOnAction(event -> handleDeleteButtonAction(commentaire));
                    commentaireContainer.getChildren().addAll(deleteButton);


                }
                Separator separator = new Separator(Orientation.HORIZONTAL);
                // Create a ScrollPane and set its content to commentaireContainer
                ScrollPane scrollPane = new ScrollPane(commentaireContainer);
                scrollPane.setFitToWidth(true); // Ensures the ScrollPane adjusts to the width of its content

                commentaireContainer.getChildren().addAll(userIdLabel,messageLabel, dateLabel, separator);
            }
        }

        public void setAnnonce_id(int annonce_id) {
            this.annonce_id = annonce_id;
            loadCommentaires();
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
        public void handleSubmitButtonAction() throws MessagingException {

            userService = userService.getInstance();
            User authentificatedUser = userService.getAuthenticatedUser();

            // Retrieve input from text fields
            int ref = generateRef();
            System.out.println("Generated ref: " + ref);
            int annonceId = Integer.parseInt(annonceIdTextField.getText());
            String message = messageTextArea.getText();



            int user_id = authentificatedUser.getId();



            if (message.isEmpty()) {
                messageLabeladd.setText("Veuillez remplir tous les champs");

                messageLabeladd.setStyle("-fx-text-fill: #d21f1f;-fx-font-weight: bold;");

            }else {

                // Create a new Commentaire object
                Commentaire newCommentaire = new Commentaire(ref, message, getCurrentTimestamp(), annonceId,user_id);

                // Call the create method from CommentaireService to insert the new commentaire into the database
                commentaireService.create(newCommentaire);

                // Reload the commentaires to update the view
                loadCommentaires();

                // Clear input fields
                clearFields();
            }
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
        // Method to set the annonce_id attribute
        public void setannonce_id(int annonce_id) {
            this.annonce_id = annonce_id;
        }


        @FXML
        public void retour(ActionEvent event) throws IOException {
            System.out.println("Retour button clicked."); // Add a debug message

            Parent root = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/DashboardClient.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        }
        ///////
        @FXML
        private Label messageLabeladd;

        @FXML
        public void ajoutcomment() throws MessagingException {
            userService = userService.getInstance();
            User authentificatedUser = userService.getAuthenticatedUser();
            // Retrieve input from text fields
            int ref = generateRef();
            System.out.println("Generated ref: " + ref);
            int annonceId = Integer.parseInt(annonceIdTextField.getText());
            String message = messageTextArea.getText();
            message = BadWordFilter.filterBadWords(message);

            int user_id = authentificatedUser.getId();



            int userId = -1; // Default value if user ID is not found

            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT user_id FROM annonce WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, annonceId); // Set annonceId dynamically
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            userId = resultSet.getInt("user_id");
                            // Print the user ID
                            System.out.println("SQL User ID: " + userId);
                        } else {
                            System.out.println("No user found for annonce ID: " + annonceId);
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving user ID: " + e.getMessage());
            }

            String userEmail = null; // Default value if user email is not found

            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT email FROM user WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, userId); // Set the user_id obtained from the latest post
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            userEmail = resultSet.getString("email");
                            // Print the user email
                            System.out.println("SQL User Email: " + userEmail);
                        } else {
                            System.out.println("No user found with ID: " + userId);
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving user email: " + e.getMessage());
            }





            if (message.isEmpty() || message.length() > 500 ) {
                messageLabeladd.setText("Votre message est vide");
                if (message.isEmpty()) {
                    messageLabeladd.setText("Votre message est vide");
                } else if (message.length() > 500) {
                    messageLabeladd.setText("Trop de caractères dans le message (max. 500)");
                } else {
                    messageLabeladd.setText("Description is empty");
                }

                messageLabeladd.setStyle("-fx-text-fill: #d21f1f;-fx-font-weight: bold;");

            }else {


                // Create a new Commentaire object
                Commentaire newCommentaire = new Commentaire(ref, message, getCurrentTimestamp(), annonceId,user_id);

                // Call the create method from CommentaireService to insert the new commentaire into the database
                commentaireService.ajoutcomment(newCommentaire, annonce_id,user_id);
                // Send email asynchronously to avoid delaying the appearance of the comment
                String finalMessage = message;
                String finalUserEmail = userEmail;
                new Thread(() -> {
                    try {
                        JavaMailUtil.sendMail(finalUserEmail, "nouveau commentaire reçu - MONSTER GYM", "USER a ajouté un nouveau commentaire sur votre annonce : \n\n\"" + finalMessage + "\"");                    } catch (MessagingException e) {
                        e.printStackTrace();
                        // Handle email sending exception
                    }
                }).start();

                // Reload the commentaires to update the view
                loadCommentaires();
                //JavaMailUtil.sendMail("manelfatnassi00@gmail.com","nouveau commentaire reçu - MONSTER GYM","USER a ajouté un nouveau commentaire sur votre annonce ");



                // Clear input fields
                clearFields();

            }
        }
    }

