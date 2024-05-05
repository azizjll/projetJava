package esprit.monstergym.demo.Service;

import esprit.monstergym.demo.Entities.Commentaire;
import esprit.monstergym.demo.Interfaces.IService;
import esprit.monstergym.demo.Utils.ConnectionManager;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentaireService implements IService<Commentaire> {



    @Override
    public void create(Commentaire entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO commentaire (ref, message, date, annonce_id) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setInt(1, entity.getRef());
            preparedStatement.setString(2, entity.getMessage());
            preparedStatement.setTimestamp(3, entity.getTimestamp());
            preparedStatement.setInt(4, entity.getAnnonce_id());

            preparedStatement.executeUpdate();
            System.out.println("Commentaire created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating commentaire: " + e.getMessage());
        }
    }


    public void ajoutcomment(Commentaire entity, int annoce_id ,int user_id ) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO commentaire (ref, message, date, annonce_id,user_id) VALUES (?, ?, ?, ?,?)")) {

            preparedStatement.setInt(1, entity.getRef());
            preparedStatement.setString(2, entity.getMessage());
            preparedStatement.setTimestamp(3, entity.getTimestamp());
            preparedStatement.setInt(4, entity.getAnnonce_id());
            preparedStatement.setInt(5, entity.getUser_id());


            preparedStatement.executeUpdate();
            System.out.println("Commentaire created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating commentaire: " + e.getMessage());
        }
    }

    @Override
    public void update(Commentaire entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE commentaire SET message = ? WHERE ref = ?")) {

            preparedStatement.setString(1, entity.getMessage());
            preparedStatement.setInt(2, entity.getRef());

            preparedStatement.executeUpdate();
            System.out.println("Commentaire updated successfully");
        } catch (SQLException e) {
            System.err.println("Error updating commentaire: " + e.getMessage());
        }
    }

    @Override
    public void delete(Commentaire entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM commentaire WHERE ref = ?")) {

            preparedStatement.setInt(1, entity.getRef());

            preparedStatement.executeUpdate();
            System.out.println("Commentaire deleted successfully");
        } catch (SQLException e) {
            System.err.println("Error deleting commentaire: " + e.getMessage());
        }
    }

    @Override
    public List<Commentaire> getAll() {
        List<Commentaire> commentaireList = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM commentaire ORDER BY date DESC")) {
            while (resultSet.next()) {
                int ref = resultSet.getInt("ref");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int annonce_id = resultSet.getInt("annonce_id");
                int user_id = resultSet.getInt("user_id");


                Commentaire commentaire = new Commentaire(ref, message, date, annonce_id,user_id);
                commentaireList.add(commentaire);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all commentaires: " + e.getMessage());
        }
        return commentaireList;
    }

    public List<Commentaire> getAllCommentairesByAnnonceId(int annonce_id) {
        List<Commentaire> commentaireList = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM commentaire WHERE annonce_id = ? ORDER BY date DESC")) {

            preparedStatement.setInt(1, annonce_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ref = resultSet.getInt("ref");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int user_id = resultSet.getInt("user_id");

                // No need to retrieve annonce_id from resultSet as we already know it

                Commentaire commentaire = new Commentaire(ref, message, date, annonce_id,user_id);
                commentaireList.add(commentaire);
            }
        } catch (SQLException e) {
            System.err.println("Error getting commentaires by annonce_id: " + e.getMessage());
        }
        return commentaireList;
    }





}
