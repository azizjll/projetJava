package esprit.monstergym.demo.Controllers;

import animatefx.animation.Shake;
import animatefx.animation.Swing;
import esprit.monstergym.demo.Entities.User;
import esprit.monstergym.demo.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.layout.BackgroundFill;
import javafx.fxml.FXMLLoader;
import animatefx.animation.Shake;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;

import java.io.IOException;

public class DashboardClientController   {

    @FXML
    private Label lplStatus;
    @FXML
    private Label lplStatusMini;
    @FXML
    private Button btnHomeClient;
    @FXML
    private Button btnProfilClient;
    @FXML
    private Button btnLogOutClient;
    @FXML
    private Pane pnlStatusClient;
    @FXML
    private VBox vBoxDashboardClient;
    @FXML
    private Button btnEvent;

    private Button btnAddReservationClient;
    @FXML
    private Button btnService;
    @FXML
    private Button btnListReservationsClient;
    @FXML
    private Button btnAjoutRec;
    @FXML
    private Button btnListRec;
    @FXML
    private Button btnProduit;
    @FXML
    private Button btncommandee;
    @FXML
    private Button btnfacture;
    @FXML
    private Button btnParticiper;

    @FXML
    private Button btnEventCoach;

    @FXML
    private Button btnListRec1;
    private UserService userService;




    @FXML
    void handleClicksClient(ActionEvent event) throws IOException {
        if(event.getSource() == btnProfilClient){
            lplStatusMini.setText("/Home/Profil");
            lplStatus.setText("Profil Settings");
            vBoxDashboardClient.setVisible(true);
            pnlStatusClient.setBackground(new Background(new BackgroundFill(Color.rgb(201, 179, 150),CornerRadii.EMPTY,Insets.EMPTY)));
            new Swing(pnlStatusClient).play();
            Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/Profil.fxml"));
            vBoxDashboardClient.getChildren().removeAll();
            vBoxDashboardClient.getChildren().setAll(fxml);
        }else if (event.getSource() == btnEvent) {
            userService = userService.getInstance();
            User authentificatedUser = userService.getAuthenticatedUser();
            if (authentificatedUser != null && authentificatedUser.hasRole("[\"ROLE_COACH\"]")) {
                lplStatusMini.setText("/Home/Profil");
                lplStatus.setText("Profil Settings");
                vBoxDashboardClient.setVisible(true);
                pnlStatusClient.setBackground(new Background(new BackgroundFill(Color.rgb(201, 179, 150), CornerRadii.EMPTY, Insets.EMPTY)));
                new Swing(pnlStatusClient).play();
                Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/AdminEvent.fxml"));
                vBoxDashboardClient.getChildren().removeAll();
                vBoxDashboardClient.getChildren().setAll(fxml);
            }
        }
        else if (event.getSource() == btnParticiper) {
            lplStatusMini.setText("/Home/Profil");
            lplStatus.setText("Profil Settings");
            vBoxDashboardClient.setVisible(true);
            pnlStatusClient.setBackground(new Background(new BackgroundFill(Color.rgb(201, 179, 150), CornerRadii.EMPTY, Insets.EMPTY)));
            new Swing(pnlStatusClient).play();
            Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/AjoutParticipation.fxml"));
            vBoxDashboardClient.getChildren().removeAll();
            vBoxDashboardClient.getChildren().setAll(fxml);
        } else if (event.getSource() == btnEventCoach) {
            userService = userService.getInstance();
            User authentificatedUser = userService.getAuthenticatedUser();
            if (authentificatedUser != null && authentificatedUser.hasRole("[\"ROLE_CLIENT\"]")) {
                lplStatusMini.setText("/Home/Profil");
                lplStatus.setText("Profil Settings");
                vBoxDashboardClient.setVisible(true);
                pnlStatusClient.setBackground(new Background(new BackgroundFill(Color.rgb(201, 179, 150), CornerRadii.EMPTY, Insets.EMPTY)));
                new Swing(pnlStatusClient).play();
                Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/Participation.fxml"));
                vBoxDashboardClient.getChildren().removeAll();
                vBoxDashboardClient.getChildren().setAll(fxml);
            }
        }else if (event.getSource() == btnListRec1) {
            userService = userService.getInstance();
            User authentificatedUser = userService.getAuthenticatedUser();
            if (authentificatedUser != null && authentificatedUser.hasRole("[\"ROLE_COACH\"]")) {
                lplStatusMini.setText("/Home/Profil");
                lplStatus.setText("Profil Settings");
                vBoxDashboardClient.setVisible(true);
                pnlStatusClient.setBackground(new Background(new BackgroundFill(Color.rgb(201, 179, 150), CornerRadii.EMPTY, Insets.EMPTY)));
                new Swing(pnlStatusClient).play();
                Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/Userparticipation.fxml"));
                vBoxDashboardClient.getChildren().removeAll();
                vBoxDashboardClient.getChildren().setAll(fxml);
            }
        }

    }



    @FXML
    private void handleLogOutClient(ActionEvent event) {
        try {
            // Load the new page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/monstergym/demo/Main.fxml"));
            Parent root = loader.load();


            // Create a new stage for the new scene
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.show();

            // Close the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
