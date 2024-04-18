package esprit.monstergym.demo.Controllers;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
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

    ObservableList<User> userList = FXCollections.observableArrayList();

    Button blockButton = null;




    @FXML
    void handleChart(ActionEvent event) {

    }

    @FXML
    void handleSaveFile(ActionEvent event) {

    }


    @FXML
    private void initialize() {
        fnReloadData();

    }

    private void fnReloadData(){
        // Associer les colonnes du tableau aux propriétés de l'objet User
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        etatCol.setCellValueFactory(new PropertyValueFactory<>("blockButton"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        isVerifiedCol.setCellValueFactory(new PropertyValueFactory<>("verified"));

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





}
