package esprit.monstergym.demo.Utils;

import esprit.monstergym.demo.Entities.Annonce;
import esprit.monstergym.demo.Utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));

        // Load data from the database
        loadDataFromDatabase();

        // Load data from the database (call the method)
        fetchAnnouncementsFromDatabase();
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
                    Date date = resultSet.getDate("date");

                    Annonce annonce = new Annonce(id, titre, description, (java.sql.Date) date);
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
            clearFields(null); // Clear fields after successful addition
        } else {
            messageLabeladd.setText("Failed to add announcement");
        }
    }

    private boolean addAnnouncementToDatabase(String titre, String description) {
        String query = "INSERT INTO annonce (titre, description ) VALUES (?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, titre);
            statement.setString(2, description);

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

}
