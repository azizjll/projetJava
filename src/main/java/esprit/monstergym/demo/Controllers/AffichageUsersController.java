package esprit.monstergym.demo.Controllers;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.google.zxing.qrcode.QRCodeWriter;

import com.itextpdf.text.Image;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import esprit.monstergym.demo.Entities.User;
import esprit.monstergym.demo.Service.UserService;
import esprit.monstergym.demo.Utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;



import javafx.util.Callback;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.input.MouseEvent;
import java.util.Optional;




public class AffichageUsersController {

    @FXML
    private VBox VBoxShowUsers;

    @FXML
    private TableColumn<User, Integer> numeroCol;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TableColumn<User, String> emailCol;

    @FXML
    private TableColumn<User, String> etatCol;

    @FXML
    private TableColumn<User, Boolean> isVerifiedCol;
    @FXML
    private TableColumn<User, Date> dateCol;

    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, String> actionsCol;

    @FXML
    private TextField tfSearch;

    @FXML
    private ComboBox comboBox;

    @FXML
    private TableColumn<User, String> role;
    @FXML
    private TableColumn<User, String> brochureFilename;

    ObservableList<User> userList = FXCollections.observableArrayList();

    Button blockButton = null;








    @FXML
    private void initialize() {
        fnReloadData();
        configureBrochureColumn();

    }

    private void fnReloadData(){
        // Associer les colonnes du tableau aux propriétés de l'objet User
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        etatCol.setCellValueFactory(new PropertyValueFactory<>("blockButton"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        isVerifiedCol.setCellValueFactory(new PropertyValueFactory<>("verified"));
        role.setCellValueFactory(new PropertyValueFactory<>("roles"));
        brochureFilename.setCellValueFactory(new PropertyValueFactory<>("brochureFilename"));

        // Charger les données depuis la base de données et les afficher dans le tableau

        tableViewUsers.getItems().clear();
        tableViewUsers.getItems().addAll(loadDataFromDatabase());
        ObservableList<String> listTrier = FXCollections.observableArrayList("username","email","date","numero");
        comboBox.setItems(listTrier);


        FilteredList<User> filtredData = new FilteredList<>(userList, e -> true);
        tfSearch.setOnKeyReleased(e -> {
            tfSearch.textProperty().addListener( (observableValue, oldValue, newValue) ->{
                filtredData.setPredicate((Predicate<? super User>) user ->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFiler = newValue.toLowerCase();
                    if(user.getEmail().contains(lowerCaseFiler)){
                        return true;
                    }else if(user.getUsername().toLowerCase().contains(lowerCaseFiler)){
                        return true;
                    }else if(user.getNumero().toLowerCase().contains(lowerCaseFiler)) {
                        return true;
                    } else if (user.getEtat() != null && user.getEtat().toString().toLowerCase().contains(lowerCaseFiler)) {
                        return true;
                    } else if (user.getDateNaissance() != null && user.getDateNaissance().toString().toLowerCase().contains(lowerCaseFiler)) {
                        return true;

                    }else {
                        // Modification ici pour la comparaison d'un attribut booléen
                        if (Boolean.toString(user.getIs_verified()).contains(lowerCaseFiler)) {
                            return true;
                        }
                    }

                    return false;
                });
            });
            SortedList<User> sortedData = new SortedList<>(filtredData);
            sortedData.comparatorProperty().bind(tableViewUsers.comparatorProperty());
            tableViewUsers.setItems(sortedData);
        });

    }
    private void configureBrochureColumn() {
        brochureFilename.setCellFactory(col -> new TableCell<User, String>() {
            private final Button openPdfButton = new Button("Ouvrir PDF");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(openPdfButton);
                    openPdfButton.setOnAction(e -> openPDF(item));
                }
            }
        });
    }
    private void openPDF(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            try {
                File pdfFile = new File("C:\\Users\\Aziz Chahlaoui\\Desktop\\final\\Pi dev\\test\\public\\uploads\\brochures\\" + fileName);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Le fichier PDF n'existe pas.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Erreur lors de l'ouverture du fichier PDF.");
            }
        }
    }
    private List<User> loadDataFromDatabase() {
        List<User> data= new ArrayList<>();
        UserService us = new UserService();
        for(int i=  0 ; i<us.getAll().size();i++){
            System.out.println(us.getAll().get(i).toString());
            User user = us.getAll().get(i);
            if(user.getIs_verified())
            user.setVerified("Verified");
            else
            user.setVerified("Not Verified");
            Button blockButton = new Button();
            if (user.getEtat() == 1) {
                blockButton.setText("Block");
                blockButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                blockButton.setOnAction(e -> {
                       // Handle button click event here
                    // For example, you can call a method to handle blocking/deblocking
                    user.setEtat(0);
                    us.update(user);
                    fnReloadData();
                });
            } else {
                blockButton.setText("Deblock");
                blockButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                blockButton.setOnAction(e -> {
                    // Handle button click event here
                    // For example, you can call a method to handle blocking/deblocking
                    user.setEtat(1);
                    us.update(user);
                    fnReloadData();
                });
            }
            user.setBlockButton(blockButton);
            data.add(user);


        }
        return data;
    }
    @FXML
    void Select(ActionEvent event) {
        if (comboBox.getSelectionModel().getSelectedItem().toString().equals("username")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY username ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("roles"),
                                resultSet.getString("reset_token"),
                                resultSet.getString("password"),
                                resultSet.getBoolean("is_verified"),
                                resultSet.getDate("date_naissance"),
                                resultSet.getString("numero"),
                                resultSet.getInt("cin"),
                                resultSet.getInt("etat"),
                                resultSet.getString("image_url"),
                                resultSet.getString("borchure_filename")// Ajout de l'état de blocage

                        );
                        if(user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        UserService us = new UserService();
                        Button blockButton = new Button();
                        if (user.getEtat() == 1) {
                            blockButton.setText("Block");
                            blockButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(0);
                                us.update(user);
                                fnReloadData();
                            });
                        } else {
                            blockButton.setText("Deblock");
                            blockButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(1);
                                us.update(user);
                                fnReloadData();
                            });
                        }
                        user.setBlockButton(blockButton);
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        } else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("email")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY numero ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";
                        User user = new User(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("roles"),
                                resultSet.getString("reset_token"),
                                resultSet.getString("password"),
                                resultSet.getBoolean("is_verified"),
                                resultSet.getDate("date_naissance"),
                                resultSet.getString("numero"),
                                resultSet.getInt("cin"),
                                resultSet.getInt("etat"),
                                resultSet.getString("image_url"),
                                resultSet.getString("borchure_filename")// Ajout de l'état de blocage

                        );
                        if(user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        UserService us = new UserService();
                        Button blockButton = new Button();
                        if (user.getEtat() == 1) {
                            blockButton.setText("Block");
                            blockButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(0);
                                us.update(user);
                                fnReloadData();
                            });
                        } else {
                            blockButton.setText("Deblock");
                            blockButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(1);
                                us.update(user);
                                fnReloadData();
                            });
                        }
                        user.setBlockButton(blockButton);
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("numero")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY email ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("roles"),
                                resultSet.getString("reset_token"),
                                resultSet.getString("password"),
                                resultSet.getBoolean("is_verified"),
                                resultSet.getDate("date_naissance"),
                                resultSet.getString("numero"),
                                resultSet.getInt("cin"),
                                resultSet.getInt("etat"),
                                resultSet.getString("image_url"),
                                resultSet.getString("borchure_filename")// Ajout de l'état de blocage

                        );
                        if(user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        UserService us = new UserService();
                        Button blockButton = new Button();
                        if (user.getEtat() == 1) {
                            blockButton.setText("Block");
                            blockButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(0);
                                us.update(user);
                                fnReloadData();
                            });
                        } else {
                            blockButton.setText("Deblock");
                            blockButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(1);
                                us.update(user);
                                fnReloadData();
                            });
                        }
                        user.setBlockButton(blockButton);
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }


        } else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("numero")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY email ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("roles"),
                                resultSet.getString("reset_token"),
                                resultSet.getString("password"),
                                resultSet.getBoolean("is_verified"),
                                resultSet.getDate("date_naissance"),
                                resultSet.getString("numero"),
                                resultSet.getInt("cin"),
                                resultSet.getInt("etat"),
                                resultSet.getString("image_url"),
                                resultSet.getString("borchure_filename")// Ajout de l'état de blocage

                        );
                        if(user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        UserService us = new UserService();
                        Button blockButton = new Button();
                        if (user.getEtat() == 1) {
                            blockButton.setText("Block");
                            blockButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(0);
                                us.update(user);
                                fnReloadData();
                            });
                        } else {
                            blockButton.setText("Deblock");
                            blockButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(1);
                                us.update(user);
                                fnReloadData();
                            });
                        }
                        user.setBlockButton(blockButton);
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("date")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY date_naissance ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("roles"),
                                resultSet.getString("reset_token"),
                                resultSet.getString("password"),
                                resultSet.getBoolean("is_verified"),
                                resultSet.getDate("date_naissance"),
                                resultSet.getString("numero"),
                                resultSet.getInt("cin"),
                                resultSet.getInt("etat"),
                                resultSet.getString("image_url"),
                                resultSet.getString("borchure_filename")// Ajout de l'état de blocage

                        );
                        if(user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        UserService us = new UserService();
                        Button blockButton = new Button();
                        if (user.getEtat() == 1) {
                            blockButton.setText("Block");
                            blockButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(0);
                                us.update(user);
                                fnReloadData();
                            });
                        } else {
                            blockButton.setText("Deblock");
                            blockButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            blockButton.setOnAction(e -> {
                                // Handle button click event here
                                // For example, you can call a method to handle blocking/deblocking
                                user.setEtat(1);
                                us.update(user);
                                fnReloadData();
                            });
                        }
                        user.setBlockButton(blockButton);
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }






    @FXML
    void handleSaveFile() throws FileNotFoundException, DocumentException, BadElementException, IOException, SQLException, WriterException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("User_list.pdf"));
        doc.open();
        String format = "dd/mm/yy hh:mm";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        java.util.Date date = new java.util.Date();
        Paragraph paragraph = new Paragraph("MONSTER GYM");
        paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        doc.add(paragraph);
        doc.add(new Paragraph("\n"));
        doc.add(new Paragraph("All users information in this table :" + "\n"));
        doc.add(new Paragraph("\n"));
        PdfPTable t = new PdfPTable(7); // Augmentez le nombre de colonnes pour inclure le QR code
        PdfPCell cell = new PdfPCell(new Phrase("Username"));
        cell.setBackgroundColor(BaseColor.PINK);
        t.addCell(cell);

        PdfPCell cell1 = new PdfPCell(new Phrase("Email"));
        cell1.setBackgroundColor(BaseColor.PINK);
        t.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase("Date Naissance"));
        cell2.setBackgroundColor(BaseColor.PINK);
        t.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Phrase("Phone Number"));
        cell3.setBackgroundColor(BaseColor.PINK);
        t.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Phrase("IsVerified"));
        cell4.setBackgroundColor(BaseColor.PINK);
        t.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Phrase("etat"));
        cell5.setBackgroundColor(BaseColor.PINK);
        t.addCell(cell5);
        // Set the total width of the table to the width of the page
        t.setTotalWidth(doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin());



        PdfPCell qrCodeCell = new PdfPCell(new Phrase("QR Code"));
        qrCodeCell.setBackgroundColor(BaseColor.PINK); // set the background color of the cell
        t.addCell(qrCodeCell);

        // Récupérez la liste des utilisateurs
        List<User> userList = loadDataFromDatabase();
        for (User user : userList) {
            // Ajoutez les données de l'utilisateur à chaque cellule
            t.addCell(user.getUsername());
            t.addCell(user.getEmail());
            t.addCell(user.getDateNaissance().toString());
            t.addCell(user.getNumero());
            t.addCell(user.getVerified());
            t.addCell(String.valueOf(user.getEtat()));

            // Générez le QR code pour chaque utilisateur
            String qrCodeData = user.getUsername() + "," +
                    user.getEmail() + "," +
                    user.getDateNaissance().toString() + "," +
                    user.getNumero() + "," +
                    user.getVerified() + "," +
                    String.valueOf(user.getEtat());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
            Image qrCodeImage = Image.getInstance(byteArrayOutputStream.toByteArray());

            // Ajoutez le QR code à la cellule QR Code
            qrCodeImage.scaleAbsolute(50, 50); // Ajustez la taille du QR code si nécessaire
            PdfPCell qrCodeImageCell = new PdfPCell(qrCodeImage);
            t.addCell(qrCodeImageCell);
        }
        doc.add(t);
        Desktop.getDesktop().open(new File("User_list.pdf"));
        doc.close();
    }



    @FXML
    void handleChart(ActionEvent event) throws IOException {
        // Load the FXML file for the new stage
        Parent root = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/Chart.fxml"));
        // Create the new stage
        Stage newStage = new Stage();
        // Set the title of the new stage
        newStage.setTitle("BarChart");
        // Create the scene for the new stage
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        // Show the new stage
        newStage.show();

    }






}
