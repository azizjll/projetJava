package esprit.monstergym.demo.Service;

import esprit.monstergym.demo.Entities.Annonce;
import esprit.monstergym.demo.Interfaces.IService;
import esprit.monstergym.demo.Utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AnnonceService implements IService<Annonce> {

    @Override
    public void create(Annonce entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO annonce (titre, description, date,user_id) VALUES (?, ?, ?,?)")) {

            preparedStatement.setString(1, entity.getTitre());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setTimestamp(3, entity.getTimestamp());
            preparedStatement.setInt(4, entity.getUser_id());


            preparedStatement.executeUpdate();
            System.out.println("Annonce created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating annonce: " + e.getMessage());
        }
    }

    @Override
    public void update(Annonce entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE annonce SET titre = ?, description = ?, date = ? ,user_id=? WHERE id = ?")) {

            preparedStatement.setString(1, entity.getTitre());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setString(4, entity.getImageUrl());
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.setInt(6, entity.getUser_id());

            preparedStatement.executeUpdate();
            System.out.println("Annonce updated successfully");
        } catch (SQLException e) {
            System.err.println("Error updating annonce: " + e.getMessage());
        }
    }

    @Override
    public void delete(Annonce entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM annonce WHERE id = ?")) {

            preparedStatement.setInt(1, entity.getId());

            preparedStatement.executeUpdate();
            System.out.println("Annonce deleted successfully");
        } catch (SQLException e) {
            System.err.println("Error deleting annonce: " + e.getMessage());
        }
    }

    @Override
    public List<Annonce> getAll() {
        List<Annonce> annonceList = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM annonce ORDER BY date DESC")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("description");
                Timestamp date = resultSet.getTimestamp("date");

                Annonce annonce = new Annonce(id, titre, description, date);
                annonceList.add(annonce);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all annonces: " + e.getMessage());
        }
        return annonceList;
    }



}
