package esprit.monstergym.demo.Service;

import esprit.monstergym.demo.Entities.User;
import esprit.monstergym.demo.Interfaces.IService;
import esprit.monstergym.demo.Utils.ConnectionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.control.Alert;



public class UserService implements IService<User> {

    public User userConnect;
    private static UserService instance;
    private User authenticatedUser; // Store authenticated user here
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    @Override
    public void create(User entity) {
        // Implement the logic to insert a new user into the database
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user ( username, email, roles, reset_token, password, is_verified, date_naissance, numero, cin, etat, image_url, borchure_filename) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getRoles());
            preparedStatement.setString(4, entity.getResetToken());
            preparedStatement.setString(5, entity.getPassword());
            preparedStatement.setBoolean(6, entity.getIs_verified());
            preparedStatement.setTimestamp(7, new Timestamp(entity.getDateNaissance().getTime()));
            preparedStatement.setString(8, entity.getNumero());
            preparedStatement.setInt(9, entity.getCin());
            preparedStatement.setInt(10, entity.getEtat());
            preparedStatement.setString(11, entity.getImageUrl());
            preparedStatement.setString(12, entity.getBrochureFilename());

            preparedStatement.executeUpdate();
            System.out.println("User created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        // Implement the logic to insert a new user into the database
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE  user SET  username = ?, email = ?, roles = ?, reset_token = ?, password = ?, is_verified = ?, date_naissance = ?, numero = ?, cin = ?, etat = ?, image_url = ?, borchure_filename = ? WHERE id = ?")) {

            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getRoles());
            preparedStatement.setString(4, entity.getResetToken());
            preparedStatement.setString(5, entity.getPassword());
            preparedStatement.setBoolean(6, entity.getIs_verified());
            preparedStatement.setTimestamp(7, new Timestamp(entity.getDateNaissance().getTime()));
            preparedStatement.setString(8, entity.getNumero());
            preparedStatement.setInt(9, entity.getCin());
            preparedStatement.setInt(10, entity.getEtat());
            preparedStatement.setString(11, entity.getImageUrl());
            preparedStatement.setString(12, entity.getBrochureFilename());
            preparedStatement.setInt(13, entity.getId());

            preparedStatement.executeUpdate();
            System.out.println("User created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    @Override
    public void delete(User entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id =  ?")) {

            preparedStatement.setInt(1, entity.getId());


            preparedStatement.executeUpdate();
            System.out.println("User deleted successfully");
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        // Implement the logic to retrieve all users from the database and populate the userList
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM user")) {
            while (resultSet.next()) {
                // Retrieve user attributes from the resultSet and create a User object
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String roles = resultSet.getString("roles");
                String resetToken = resultSet.getString("reset_token");
                String password = resultSet.getString("password");
                boolean isVerified = resultSet.getBoolean("is_verified");
                Date dateNaissance = resultSet.getDate("date_naissance");
                String numero = resultSet.getString("numero");
                Integer cin = resultSet.getInt("cin");
                Integer etat = resultSet.getInt("etat");
                String imageUrl = resultSet.getString("image_url");
                String borchureFilename = resultSet.getString("borchure_filename");

                User user = new User(id, username, email, roles, resetToken, password, isVerified,
                        dateNaissance, numero, cin, etat, imageUrl, borchureFilename);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        return userList;
    }

    private Connection conn;



    public boolean SignUpUser(User user) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean success = false;

      //  String query = "INSERT INTO user ( username, email, roles, reset_token, password, is_verified, date_naissance, numero, cin, etat, image_url, borchure_filename) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String query = "INSERT INTO user ( username, email, password,date_naissance, numero,roles) VALUES ( ?, ?, ?, ?, ?,?)";
        try {
            conn = ConnectionManager.getConnection(); // Initialize conn

            // Vérifier si l'e-mail existe déjà dans la base de données
            preparedStatement = conn.prepareStatement("SELECT email FROM user WHERE email = ?");
            preparedStatement.setString(1, user.getEmail());
            resultSet = preparedStatement.executeQuery();

            // Si l'e-mail n'existe pas, insérer l'utilisateur dans la base de données
            if (!resultSet.isBeforeFirst()) {
                // Hasher le mot de passe avant de l'enregistrer dans la base de données
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(user.getPassword());

                // Préparer la requête d'insertion avec les données de l'utilisateur
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getEmail());
              //  preparedStatement.setString(3, user.getRoles());
              //  preparedStatement.setString(4, user.getResetToken());
                preparedStatement.setString(3, hashedPassword);
               // preparedStatement.setBoolean(6, user.isVerified());
                preparedStatement.setDate(4, user.getDateNaissance());
                preparedStatement.setInt(5, Integer.parseInt(user.getNumero()));
                preparedStatement.setString(6,user.getRoles());
               // preparedStatement.setInt(10, user.getEtat());
                //preparedStatement.setString(11, user.getImageUrl());
               // preparedStatement.setString(12, user.getBrochureFilename());


                // Exécuter la requête d'insertion
                preparedStatement.executeUpdate();

                // L'enregistrement de l'utilisateur est un succès
                success = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Fermer les ressources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (conn != null) {
                    conn.close(); // Close connection
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public User authenticate(String email, String password) {
        User user = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = ConnectionManager.getConnection();

            String query = "SELECT * FROM user WHERE email = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("password");

                // Compare hashed password from DB with provided password
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                if (passwordEncoder.matches(password, hashedPasswordFromDB)) {
                    // Passwords match, create User object
                    user = new User(
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
                            resultSet.getString("borchure_filename")
                    );

                    // Check if the user is an admin or a standard user
                    if (user.hasRole("ROLE_ADMIN")) {
                        user.setAdmin(true);
                    } else if (user.hasRole("ROLE_USER")) {
                        user.setAdmin(false);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Close resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        authenticatedUser = user;
        return user;
    }


    public Boolean ForgetPassUser(String email , String password) throws Exception{

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean res = false;
        try {

            preparedStatement = conn.prepareStatement("SELECT password , email FROM user WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("User not found in the database!!");

                res=false;
            }else{

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                String query = "UPDATE user SET password = '" + hashedPassword + "' WHERE email = '" + email + "'";
                PreparedStatement pst=conn.prepareStatement(query);
                pst.executeUpdate();
                //System.out.println("Login succes!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Password changed successfully !!");
                alert.setHeaderText(null);
                alert.setTitle("Succes");
                alert.show();
                res=true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean isEmailExist(String email) {
        boolean emailExists = false;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM user WHERE email = ?")) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                emailExists = resultSet.next(); // If there's a result, email exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        return emailExists;
    }

    // Method to authenticate user

    // Method to get authenticated user
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
}
