package esprit.monstergym.demo.Utils;

import esprit.monstergym.demo.Entities.Annonce;
import esprit.monstergym.demo.Service.AnnonceService;
import esprit.monstergym.demo.Service.CommentaireService;
import esprit.monstergym.demo.Utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


import java.util.List;
import java.util.ResourceBundle;

public class AnnonceController {

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField imageUrlTextField;

    @FXML
    private Button submitButton;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<Annonce> tableViewAnnonces;

    @FXML
    private TableColumn<Annonce, String> titreCol;

    ObservableList<Annonce> annonceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize TableColumn for titre

        // Load data from the database

        // Load data from the database (call the method)
        loadAnnonces();
    }

    private void loadDataFromDatabase() {
        // Clear previous data
        annonceList.clear();

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM annonce";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String titre = resultSet.getString("titre");
                    String description = resultSet.getString("description");
                    Date date = resultSet.getTimestamp("date");

                    Annonce annonce = new Annonce(id, titre, description, (java.sql.Timestamp) date);
                    annonceList.add(annonce);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error
        }

        // Set data to TableView
        tableViewAnnonces.setItems(annonceList);
    }

    @FXML
    public void handleSubmitButtonAction() {
        // Method to handle the action when the submit button is clicked
        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        String imageUrl = imageUrlTextField.getText();

        // Perform validation if needed

        // Perform action, e.g., submit annonce to the database, etc.
        System.out.println("Submitting annonce with title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Image URL: " + imageUrl);

        // Clear input fields if needed
        clearFields();
    }

    private void clearFields() {
        // Clear input fields
        titleTextField.clear();
        descriptionTextArea.clear();
        imageUrlTextField.clear();
    }

    @FXML
    private void handleButtonClick() {
        messageLabel.setText("This button has been clicked");
    }


    //////////  add annonce
    @FXML
    private TextField addannonce_titre;

    @FXML
    private TextField addannonce_description;

    @FXML
    private Label messageLabeladd;

    @FXML
    private void clearFields(ActionEvent event) {
        addannonce_titre.clear();
        addannonce_description.clear();
    }

    private Timestamp getCurrentTimestamp() {
        // Get the current timestamp
        return new Timestamp(System.currentTimeMillis());
    }

    @FXML
    private void addAnnonce(ActionEvent event) {
        String titre = addannonce_titre.getText();
        String description = addannonce_description.getText();
        LocalDate date = LocalDate.now(); // Get the current date


        if (titre.isEmpty() || description.isEmpty()) {
            messageLabeladd.setText("Please fill in all fields");
            return;
        }

        if (addAnnouncementToDatabase(titre, description)) {
            messageLabeladd.setText("Announcement added successfully");
            messageLabeladd.setStyle("-fx-text-fill: #00ff48;-fx-font-weight: bold;");
            clearFields(null); // Clear fields after successful addition
            loadAnnonces();
        } else {
            messageLabeladd.setText("Failed to add announcement");
        }

    }

    private boolean addAnnouncementToDatabase(String titre, String description) {
        String query = "INSERT INTO annonce (titre, description, date ) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, titre);
            statement.setString(2, description);
            statement.setTimestamp(3, getCurrentTimestamp());


            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //navigate to comments
    @FXML
    private Button navigateCommentsButton;

    @FXML
    void navigateToComments(ActionEvent event) throws IOException {
        // Load the FXML file for AnotherPage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/commentaire.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML file
        Scene scene = new Scene(root);

        // Get the stage from the current scene
        Stage stage = (Stage) navigateCommentsButton.getScene().getWindow();

        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }




    /////////display annonces in list ////////
    @FXML
    private ListView<String> annonceListView;

    // Method to fetch announcements from the database
    public void fetchAnnouncementsFromDatabase() {
        List<String> annoncesList = new ArrayList<>();

        // Write your database connection code here
        try {
            // Assuming you have a connection object named "connection"
            Connection connection = ConnectionManager.getConnection();

            // Assuming you have a table named "annonces" with columns "titre" and "description"
            String query = "SELECT titre, description FROM annonces";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("description");
                annoncesList.add(titre + " - " + description);
            }

            // Display the fetched announcements in the ListView
            annonceListView.getItems().addAll(annoncesList);

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

    /////load and delete annonces
    @FXML
    private VBox annonceContainer;

    private AnnonceService annonceService;
    public AnnonceController() {
        annonceService = new AnnonceService();
    }


    private void loadAnnonces() {
        // Retrieve all annonces
        List<Annonce> annonces = annonceService.getAll();

        // Clear the container before adding new annonces
        annonceContainer.getChildren().clear();

        // Display each annonce
        for (Annonce annonce : annonces) {
            Separator separator = new Separator(Orientation.HORIZONTAL);
            separator.setPrefWidth(500);

            Label titleLabel = new Label(annonce.getTitre());
            titleLabel.setStyle("-fx-font-weight: bold;");
            Label descriptionLabel = new Label( annonce.getDescription());
            Label dateLabel = new Label("Publié le: " + annonce.getTimestamp());
            Button replyButton = new Button("répondre");
            replyButton.setStyle("-fx-background-color: #ff3c00; -fx-text-fill: #eee5e5;");
            replyButton.setOnAction(event1 -> {
                try {
                    openComments(event1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Button editButton = new Button("Modifier");
            editButton.setStyle(" -fx-background-color: #2a65a9; -fx-text-fill: #ebebeb;");
            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle(" -fx-background-color: #d21f1f; -fx-text-fill: #ebebeb;");

            deleteButton.setOnAction(event -> handleDeleteButtonAction(annonce));
            VBox vboxannoncedetails = new VBox();
            vboxannoncedetails.getChildren().addAll(separator, titleLabel, descriptionLabel, dateLabel, replyButton);

            HBox hboxbuttons = new HBox();
            hboxbuttons.setSpacing(15);
            hboxbuttons.getChildren().addAll( replyButton  ,editButton,deleteButton);


            VBox finalcontainer = new VBox();
            finalcontainer.setSpacing(10);
            finalcontainer.getChildren().addAll(vboxannoncedetails, hboxbuttons);

            annonceContainer.getChildren().addAll(finalcontainer);
            annonceContainer.setSpacing(5);
        }
    }

    //navigate to comments
    @FXML
    void openComments(ActionEvent event) throws IOException {
        // Load the FXML file for commentaire.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/commentaire.fxml"));
        Parent root = loader.load();
        // Access the controller for commentaire.fxml
        CommentaireController commentaireController = loader.getController();
        Annonce annonce = (Annonce) ((Node) event.getSource()).getUserData();


        // Pass the annonceId to the controller
        commentaireController.setannonce_id(annonce.getId());




        // Create a new scene with the loaded FXML file
        Scene scene = new Scene(root);

        // Get the stage from the current scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }


    // Deleting method
    private void handleDeleteButtonAction(Annonce annonce) {
        // Call the service method to delete the annonce
        annonceService.delete(annonce);

        // Reload the annonces to update the view
        loadAnnonces();
    }




}
