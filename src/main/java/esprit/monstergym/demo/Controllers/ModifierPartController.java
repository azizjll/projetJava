package esprit.monstergym.demo.Controllers;


import esprit.monstergym.demo.Entities.Participation;
import esprit.monstergym.demo.Service.ServicePartc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ModifierPartController {

    private Participation currentParticipation;

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
    private DatePicker datePicker;

    @FXML
    private TextField even;

    @FXML
    private Label lplStatus;

    @FXML
    private Label lplStatusMini;

    @FXML
    private TextField mail;

    @FXML
    private Button modifiertr;

    @FXML
    private TextField nom;

    @FXML
    private Pane pnlStatusClient;

    @FXML
    private TextField pren;

    @FXML
    private TextField tele;

    @FXML
    private VBox vBoxDashboardClient;

    @FXML
    void handleClicksClient(ActionEvent event) {

    }

    @FXML
    void handleLogOutClient(ActionEvent event) {

    }


    public void setParticipationData(Participation participation) {

        this.currentParticipation = participation;

        if (participation != null) {
            nom.setText(participation.getNom());
            pren.setText(participation.getPrenom());
            datePicker.setValue(participation.getDate());
            tele.setText(String.valueOf(participation.getTel()));
            even.setText(String.valueOf(participation.getEvent_id()));
            mail.setText(participation.getEmail());
        }
    }

    @FXML
    void modifiertr(ActionEvent event) {
        try {
            currentParticipation.setNom(nom.getText());
            currentParticipation.setPrenom(pren.getText());
            currentParticipation.setDate(datePicker.getValue());
            currentParticipation.setTel(Integer.parseInt(tele.getText()));
            currentParticipation.setEmail(mail.getText());
            currentParticipation.setUser_id(1);

            ServicePartc servicePartc = new ServicePartc();
            servicePartc.modifier(currentParticipation);

            returnToParticipationListView();
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception, or handle it appropriately
        } catch (NumberFormatException e) {
            // This could happen when converting 'tele.getText()' to an integer
            System.out.println("Error in number formatting: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();  // Handle issues with loading the FXML
        }
    }


    private void returnToParticipationListView() throws IOException {
        // Charger le FXML pour votre vue de liste et l'appliquer
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/Userparticipation.fxml"));
        Parent root = loader.load();
        nom.getScene().setRoot(root);  // 'nom' est un champ existant dans le formulaire actuel, utilisé pour accéder à la scène
    }


    @FXML
    void initialize() {
        assert modifiertr != null : "fx:id=\"modifiertr\" was not injected: check your FXML file 'modifierPart.fxml'.";
        assert pren != null : "fx:id=\"pren\" was not injected: check your FXML file 'modifierPart.fxml'.";
        assert tele != null : "fx:id=\"tele\" was not injected: check your FXML file 'modifierPart.fxml'.";
        assert nom != null : "fx:id=\"nom\" was not injected: check your FXML file 'modifierPart.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'modifierPart.fxml'.";
        assert even != null : "fx:id=\"even\" was not injected: check your FXML file 'modifierPart.fxml'.";
        assert mail != null : "fx:id=\"mail\" was not injected: check your FXML file 'modifierPart.fxml'.";

    }
}




