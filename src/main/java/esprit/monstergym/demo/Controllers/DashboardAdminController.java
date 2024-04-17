package esprit.monstergym.demo.Controllers;


import animatefx.animation.Swing;
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

public class DashboardAdminController {
    @FXML
    private Button btnAddService;

    @FXML
    private Button btnAddUser;

    @FXML
    private Button btnCommande;

    @FXML
    private Button btnEvenementCruds;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnListReservations;

    @FXML
    private Button btnListServices;

    @FXML
    private Button btnListUsers;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnParticipations;

    @FXML
    private Button btnProfil;

    @FXML
    private Button btncommande;

    @FXML
    private Button btnfactureback;

    @FXML
    private Button btnproduit;

    @FXML
    private Button btnreclamation;

    @FXML
    private Label hello;

    @FXML
    private Label hello1;

    @FXML
    private Label lplStatus;

    @FXML
    private Label lplStatusMini;

    @FXML
    private Pane pnlStatus;

    @FXML
    private VBox vBoxDashboardAdmin;

    @FXML
    void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == btnListUsers) {
            lplStatusMini.setText("/Home/List Users");
            lplStatus.setText("List Users");
            pnlStatus.setBackground(new Background(new BackgroundFill(Color.rgb(197, 168, 131), CornerRadii.EMPTY, Insets.EMPTY)));
            vBoxDashboardAdmin.setVisible(true);
            new Shake(pnlStatus).play();
            Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/afficheUsers.fxml"));
            vBoxDashboardAdmin.getChildren().removeAll();
            vBoxDashboardAdmin.getChildren().setAll(fxml);
        } else if (event.getSource() == btnAddUser) {
            lplStatusMini.setText("/Home/Users");
            lplStatus.setText("USERS");
            pnlStatus.setBackground(new Background(new BackgroundFill(Color.rgb(197, 168, 131), CornerRadii.EMPTY, Insets.EMPTY)));
            vBoxDashboardAdmin.setVisible(true);
            new Shake(pnlStatus).play();
            Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/AddUser.fxml"));
            vBoxDashboardAdmin.getChildren().removeAll();
            vBoxDashboardAdmin.getChildren().setAll(fxml);
        } else if (event.getSource() == btnProfil) {
            lplStatusMini.setText("/Home/Profil");
            lplStatus.setText("Profil Settings");
            vBoxDashboardAdmin.setVisible(true);
            pnlStatus.setBackground(new Background(new BackgroundFill(Color.rgb(197, 168, 131), CornerRadii.EMPTY, Insets.EMPTY)));
            new Swing(pnlStatus).play();
            Parent fxml = FXMLLoader.load(getClass().getResource("/esprit/monstergym/demo/Profil.fxml"));
            vBoxDashboardAdmin.getChildren().removeAll();
            vBoxDashboardAdmin.getChildren().setAll(fxml);

        }

    }

    @FXML
    private void handleLogOut(ActionEvent event) {
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
