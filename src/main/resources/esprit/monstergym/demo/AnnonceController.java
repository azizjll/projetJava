package esprit.monstergym.demo.Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import esprit.monstergym.demo.Entities.Annonce;
import esprit.monstergym.demo.Entities.BadWordFilter;
import esprit.monstergym.demo.Service.AnnonceService;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Label messageLabeladdpdf;

    @FXML
    private TableView<Annonce> tableViewAnnonces;

    @FXML
    private TableColumn<Annonce, String> titreCol;

    private int id;
    private Annonce annonce;



    //pdf


    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
        this.id = annonce.getId();

    }


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
    private void addAnnonce(ActionEvent event) throws MessagingException {
        String titre = addannonce_titre.getText();
        String description = addannonce_description.getText();
        LocalDate date = LocalDate.now(); // Get the current date

        // Filter bad words in the title and description
        titre = BadWordFilter.filterBadWords(titre);
        description = BadWordFilter.filterBadWords(description);
        //String randomString = "hi";

        //JavaMailUtil.sendMail("manelfatnassi00@gmail.com","helllooooooooo","if you forget your password pls use this code to reset it: ");


        if (titre.isEmpty() || titre.length() > 100 || description.isEmpty()|| description.length() > 500) {
            // Display appropriate error messages for empty title, long title, or empty description
            if (titre.isEmpty()) {
                messageLabeladd.setText("Veuillez remplir tous les champs");
            } else if (titre.length() > 100) {
                messageLabeladd.setText("Trop de caractères dans le titre (max. 100)");
            } else if (description.length() > 500) {
                messageLabeladd.setText("Trop de caractères dans la description (max. 500)");
            } else {
                messageLabeladd.setText("Veuillez remplir tous les champs");
            }
            messageLabeladd.setStyle("-fx-text-fill: #d21f1f;-fx-font-weight: bold;");
        } else {
            // Proceed with adding the announcement to the database
            if (addAnnouncementToDatabase(titre, description)) {
                messageLabeladd.setText("Annonce ajoutée avec succés");
                messageLabeladd.setStyle("-fx-text-fill: #00ff48;-fx-font-weight: bold;");
                clearFields(null); // Clear fields after successful addition
                loadAnnonces();
            } else {
                messageLabeladd.setText("Failed to add announcement");
            }
        }


    }



   /* @FXML
    private void addAnnonce(ActionEvent event) {
        String titre = addannonce_titre.getText();
        String description = addannonce_description.getText();
        LocalDate date = LocalDate.now(); // Get the current date


        if (titre.isEmpty() || description.isEmpty()|| titre.length() > 100  ) {
            messageLabeladd.setText("Veuillez remplir tous les champs");
            messageLabeladd.setStyle("-fx-text-fill: #d21f1f;-fx-font-weight: bold;");
        } else if (titre.length() > 100) {
            messageLabeladd.setText("Trop de caractères dans le titre (max. 100)");
            messageLabeladd.setStyle("-fx-text-fill: #d21f1f;-fx-font-weight: bold;");


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

    }*/

    public void generateAnnouncementsPDF() {
        Document document = new Document();


        try {
            PdfWriter.getInstance(document, new FileOutputStream("liste_annonces.pdf"));
            document.open();
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, new BaseColor(255, 51, 51));
            Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);

            Font contentFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(0, 102, 204));

            Paragraph pdfparagraph = new Paragraph("MONSTER GYM \n Liste des annonces et des avis \n",boldFont);

            pdfparagraph.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(pdfparagraph);

            // Fetch announcements from the database
            String query = "SELECT titre, description, user_id FROM annonce";
            try (Connection connection = ConnectionManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String user_id = resultSet.getString("user_id");
                    String titre = resultSet.getString("titre");
                    String description = resultSet.getString("description");

                    // Add each announcement to the PDF document

                    document.add(new Paragraph("User ID : " + user_id,contentFont));
                    document.add(new Paragraph("Titre: ",contentFont));
                    document.add(new Paragraph( titre));
                    document.add(new Paragraph("Description: " ,contentFont));
                    document.add(new Paragraph( description));
                    //document.add(new Paragraph("Description: " + description));

                    document.add(new Paragraph("--------------------------------------------------------------------------------------------------------------------------------")); // Add some space between announcements
                    document.add(new Paragraph("\n"));


                }
            }

            document.close();
            messageLabeladdpdf.setText("PDF téléchargé avec succés !");

            System.out.println("PDF file generated successfully.");
        } catch (DocumentException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
            int id = annonce.getId(); // Store the id locally

            replyButton.setOnAction(event -> {
                try {
                    Accedercommentaires2(event, id); // Pass the event and id
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }); // Pass the event and annonce ID

            Button editButton = new Button("Modifier");
            editButton.setStyle(" -fx-background-color: #2a65a9; -fx-text-fill: #ebebeb;");
            editButton.setOnAction(event -> {
                // Call the handleEditButtonAction method
                handleEditButtonAction(annonce);
            });


            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle(" -fx-background-color: #d21f1f; -fx-text-fill: #ebebeb;");

            deleteButton.setOnAction(event -> handleDeleteButtonAction(annonce));
            VBox vboxannoncedetails = new VBox();
            vboxannoncedetails.getChildren().addAll(separator, titleLabel, descriptionLabel, dateLabel, replyButton);

            HBox hboxbuttons = new HBox();
            hboxbuttons.setSpacing(15);
            hboxbuttons.getChildren().addAll( replyButton  ,deleteButton);


            VBox finalcontainer = new VBox();
            finalcontainer.setSpacing(10);
            finalcontainer.getChildren().addAll(vboxannoncedetails, hboxbuttons);

            annonceContainer.getChildren().addAll(finalcontainer);
            annonceContainer.setSpacing(5);
        }
    }

//
@FXML
public void Accedercommentaires2(ActionEvent event, int id) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/commentaire2.fxml"));
    Parent root = loader.load();

    CommentaireController controller = loader.getController();
    controller.setAnnonce_id(id);
    System.out.println("Opening comments for annonce with id: " + id);



    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);

    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.close();

    stage.show();
}

    //
    @FXML
    public void Accedercommentaires(ActionEvent event ) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/commentaire.fxml"));
        Parent root = loader.load();

        CommentaireController controller = loader.getController();
        controller.setAnnonce_id(id);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        stage.show();
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


    // Update method
    @FXML
    private void handleEditButtonAction(ActionEvent event) {
        // Get the selected annonce from the TableView
        Annonce selectedAnnonce = tableViewAnnonces.getSelectionModel().getSelectedItem();

        // Check if an annonce is selected
        if (selectedAnnonce == null) {
            // No annonce selected, show error message or handle accordingly
            System.out.println("No annonce selected for editing.");
            return;
        }

        // Populate the input fields with the data of the selected annonce
        titleTextField.setText(selectedAnnonce.getTitre());
        descriptionTextArea.setText(selectedAnnonce.getDescription());
        imageUrlTextField.setText(selectedAnnonce.getImageUrl());
        this.id = selectedAnnonce.getId(); // Store the id locally
    }

    @FXML
    private void handleEditButtonAction(Annonce annonce) {
        // Check if a specific announcement is selected
        if (annonce == null) {
            System.out.println("No annonce selected for edit.");
            return;
        }

        // Retrieve updated values from input fields or dialogs
        String updatedTitle = titleTextField.getText();
        String updatedDescription = descriptionTextArea.getText();

        // Perform validation if needed

        // Update the selected annonce with new values
        annonce.setTitre(updatedTitle);
        annonce.setDescription(updatedDescription);

        // Call the update method of the AnnonceService
        annonceService.update(annonce);

        // Reload the annonces to update the view
        loadAnnonces();

        // Clear input fields if needed
        clearFields();
    }






}
