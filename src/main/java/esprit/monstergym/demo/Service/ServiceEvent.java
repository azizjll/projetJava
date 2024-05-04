package esprit.monstergym.demo.Service;

import esprit.monstergym.demo.Entities.Event;
import esprit.monstergym.demo.Utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ServiceEvent implements IProject<Event> {
    private Connection con = ConnectionManager.getConnection();
    public ServiceEvent(){
    }

    @Override
    public Set<Event> afficher() throws SQLException {
        Set<Event> events_list = new HashSet<>();

        String querry;
        {
            events_list = new HashSet<>();
            querry = "SELECT * FROM `event`";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(querry);
            while (rs.next()) {
                Event a = new Event();
                a.setId(rs.getInt(1));

                a.setNom(rs.getString(2));
                a.setDescription(rs.getString(3));
                a.setImage(rs.getString(4));
                a.setLieu(rs.getString(5));


                Date sqlDate = rs.getDate(6);
                LocalDate LocalDate = sqlDate.toLocalDate();

                a.setDate(LocalDate);

                a.setUser_id(rs.getInt(7));


                events_list.add(a);
            }


            return events_list;
        }


    }

    @Override
    public void ajouter(Event event) {
        try {

            String query = "INSERT INTO event (id, nom, description, image , lieu, date, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);

            statement.setInt(1, event.getId());
            statement.setString(2, event.getNom());
            statement.setString(3, event.getDescription());
            statement.setString(4, event.getImage());
            statement.setString(5, event.getLieu());

            statement.setDate(6, event.getSqlDate()); // Utilisation de la méthode getSqlDate()

            statement.setInt(7, event.getUser_id());


            statement.executeUpdate();
            System.out.println("Evenement ajouté avec succès");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from event where id = ?";

        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Event event) throws SQLException {
        try {




            String query = "UPDATE event SET nom=?, description=?, image=?, lieu=?, date=?, user_id=?  WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(query);


            pstmt.setString(1, event.getNom());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getImage());
            pstmt.setString(4, event.getLieu());

            pstmt.setDate(5, event.getSqlDate()); // Utilisation de la méthode getSqlDate()

            pstmt.setInt(6, event.getUser_id());

            pstmt.setInt(7, event.getId());


            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Evenement est modifié !");
            } else {
                System.out.println("Aucun Evenement n'a été modifié.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
