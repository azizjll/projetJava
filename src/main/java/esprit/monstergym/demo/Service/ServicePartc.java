package esprit.monstergym.demo.Service;

import esprit.monstergym.demo.Entities.Participation;
import esprit.monstergym.demo.Utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ServicePartc implements IProject<Participation> {

    private Connection con = ConnectionManager.getConnection();

    public ServicePartc(){
    }


    @Override
    public Set<Participation> afficher() throws SQLException {
        Set<Participation> participation_list = new HashSet<>();

        String querry;
        {
            participation_list = new HashSet<>();
            querry = "SELECT * FROM `participation`";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(querry);
            while (rs.next()) {
                Participation a = new Participation();
                a.setId(rs.getInt(1));

                a.setNom(rs.getString(2));
                a.setPrenom(rs.getString(3));
                a.setTel(rs.getInt(4)); // This line is causing the issue

                Date sqlDate = rs.getDate(5);
                LocalDate localDate = sqlDate.toLocalDate();

                a.setDate(localDate);

                a.setUser_id(rs.getInt(6));
                a.setEvent_id(rs.getInt(7));
                a.setEmail(rs.getString(8));

                participation_list.add(a);
            }
            return participation_list;
        }
    }


    @Override
    public void ajouter(Participation participation) {
        try {

            String query = "INSERT INTO participation (id, nom, prenom, tel ,date, user_id, event_id, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);

            statement.setInt(1, participation.getId());
            statement.setString(2, participation.getNom());
            statement.setString(3, participation.getPrenom());
            statement.setInt(4, participation.getTel());
            statement.setDate(5, participation.getSqlDate()); // Utilisation de la méthode getSqlDate()

            statement.setInt(6, participation.getUser_id());
            statement.setInt(7, participation.getEvent_id());
            statement.setString(8, participation.getEmail());


            statement.executeUpdate();
            System.out.println("participation ajouté avec succès");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from participation where id = ?";

        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Participation participation) throws SQLException {
        try {




            String query = "UPDATE participation SET nom=?, prenom=?, tel=?, date=?, user_id=?, event_id=?, email=?  WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(query);


            pstmt.setString(1, participation.getNom());
            pstmt.setString(2, participation.getPrenom());
            pstmt.setInt(3, participation.getTel());
            pstmt.setDate(4, participation.getSqlDate()); // Utilisation de la méthode getSqlDate()

            pstmt.setInt(5, participation.getUser_id());
            pstmt.setInt(6, participation.getEvent_id());
            pstmt.setString(7, participation.getEmail());

            pstmt.setInt(8, participation.getId());


            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("participation est modifié !");
            } else {
                System.out.println("Aucun participation n'a été modifié.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
