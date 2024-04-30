package esprit.monstergym.demo.Controllers;

import esprit.monstergym.demo.Entities.User;
import esprit.monstergym.demo.Service.ResetPasswordService;
import esprit.monstergym.demo.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javax.xml.bind.DatatypeConverter;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfilController implements Initializable {

    @FXML
    private VBox VBoxProfil;

    @FXML
    private Button btnBrowser;

    @FXML
    private Button btnModifProfil;

    @FXML
    private Button btnResetPass;

    @FXML
    private Label hello;

    @FXML
    private Label hello1;

    @FXML
    private Label hello11;

    @FXML
    private Label hello111;

    @FXML
    private Label hello1111;

    @FXML
    private Label hello11111;

    @FXML
    private Label hello1112;

    @FXML
    private Label hello2;

    @FXML
    private Label hello21;

    @FXML
    private ImageView imageView;

    @FXML
    private Label lbConfirmePasswordReset;

    @FXML
    private Label lbEmailProfil;

    @FXML
    private Label lbFullAddresseProfil;

    @FXML
    private Label lbFullNameProfil;

    @FXML
    private Label lbNumTelProfil;

    @FXML
    private Label lbPasswordReset;

    @FXML
    private Pane pane;

    @FXML
    private PasswordField tfConfirmPassword;

    @FXML
    private DatePicker tfDate;

    @FXML
    private TextField tfEmail;


    @FXML
    private TextField tfFullName;

    @FXML
    private TextField tfNumber;

    @FXML
    private PasswordField tfPassword;

    private FileChooser fileChooser;

    private File file;
    private Stage stage;

    private Image image_url;

    private UserService userService;

    private String uploadImgDirectory = "C:\\Users\\Aziz Chahlaoui\\Desktop\\final\\Pi dev\\test\\public\\uploads\\brochures\\esprit.monstergym.demo.Controllers.image\\";

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation de fileChooser
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files","*.*"),
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.gif")
        );
        userService = UserService.getInstance();
        // Your existing initialization code...
        // Example usage:
        User authenticatedUser = userService.getAuthenticatedUser();
        tfEmail.setText(authenticatedUser.getEmail());
        tfDate.setValue(authenticatedUser.getDateNaissance().toLocalDate());
        tfNumber.setText(authenticatedUser.getNumero());
        tfFullName.setText(authenticatedUser.getUsername());
        if(authenticatedUser.getImageUrl() != null && !authenticatedUser.getImageUrl().equals("")){
            Image img = new Image(uploadImgDirectory+authenticatedUser.getImageUrl());
            imageView.setImage(img);
        }

        System.out.println("teqt"+authenticatedUser.toString());
    }


    @FXML
    void ResetPasswordAction(ActionEvent event) throws Exception {
        userService = UserService.getInstance();
        User authenticatedUser = userService.getAuthenticatedUser();
        ResetPasswordService rps = new ResetPasswordService();
        authenticatedUser.setPassword(tfPassword.getText());
        rps.ResetPassword(authenticatedUser);
        tfPassword.setText("");
        tfConfirmPassword.setText("");
        showUpdate("Password Updated succesfully");

    }

    @FXML
    void changeProfilAction(ActionEvent event) {
        userService = UserService.getInstance();
        // Your existing initialization code...
        // Example usage:
        User authenticatedUser = userService.getAuthenticatedUser();
        authenticatedUser.setEmail(tfEmail.getText());
        authenticatedUser.setNumero(tfNumber.getText());
        authenticatedUser.setUsername(tfFullName.getText());
        authenticatedUser.setDateNaissance(Date.valueOf(tfDate.getValue()));
        URL imageUrl = null;
        Path imagePath = null;
        try {
            imageUrl = new URL(imageView.getImage().getUrl());
            imagePath = Paths.get(imageUrl.toURI());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        // Generate new file name based on current time
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String fileNameWithExtension = imagePath.getFileName().toString();
        String extension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf('.')); // Get file extension
        String newFileName = currentTime + extension;

        // Construct new file path
        Path destinationPath = Paths.get(uploadImgDirectory, newFileName);

        try {
            // Copy the esprit.monstergym.demo.Controllers.image file to the upload directory with the new name
            Files.copy(imagePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Update the user's esprit.monstergym.demo.Controllers.image URL with the new file path
            authenticatedUser.setImageUrl(newFileName);

            // Update the user in the database
            userService.update(authenticatedUser);

            showUpdate("User info updated successfully");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle errors
        }

    }

    @FXML
    private void handleBrowser(ActionEvent event)  {
        if (fileChooser == null) {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All files","*.*"),
                    new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.gif")
            );
        }
        stage = (Stage) pane.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        if(file != null){
            System.out.println(""+file.getAbsolutePath());
            image_url = new Image(file.getAbsoluteFile().toURI().toString(),imageView.getFitWidth(),imageView.getFitHeight(),true,true);
            imageView.setImage(image_url);
            imageView.setPreserveRatio(true);
        }
    }

    private void showUpdate(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.setTitle("User Updated");
        alert.setHeaderText(null);
        alert.show();
    }

    // Méthode pour générer un nom de fichier unique à partir du contenu de l'esprit.monstergym.demo.Controllers.image
   /* private String generateUniqueFileName(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String fileExtension = getFileExtension(file);
            String fileHash = md5(fileContent);
            return fileHash + "." + fileExtension;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour calculer le hachage MD5 d'un tableau de bytes
    private String md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour obtenir l'extension de fichier à partir du nom de fichier
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }*/
}

