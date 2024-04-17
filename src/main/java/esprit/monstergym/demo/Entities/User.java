package esprit.monstergym.demo.Entities;

import javafx.scene.control.Button;

import java.sql.Date;
import java.time.LocalDate;

public class User {
    private int id;
    private String username;
    private String email;
    private String roles;
    private String resetToken;
    private String password;
    private boolean is_verified;
    private Date dateNaissance;
    private String numero;
    private Integer cin;
    private Integer etat;
    private String imageUrl;
    private String brochureFilename;

    private Button blockButton;
    public static User Current_User;

    private Button etatButton;




    private boolean isAdmin;


    // Constructors
    public User(String text, String tfEmailText, String tfNumberText, String tfPasswordText, LocalDate tfDate,String role) {
        this.username = text;
        this.email = tfEmailText;
        this.numero = tfNumberText;
        this.password = tfPasswordText;
        this.dateNaissance = Date.valueOf(tfDate);
        this.roles = role;
    }

    public User(int id, String username, String email, String roles, String resetToken, String password, boolean is_verified, Date dateNaissance, String numero, Integer cin, Integer etat, String imageUrl, String brochureFilename) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.resetToken = resetToken;
        this.password = password;
        this.is_verified = is_verified;
        this.dateNaissance = dateNaissance;
        this.numero = numero;
        this.cin = cin;
        this.etat = etat;
        this.imageUrl = imageUrl;
        this.brochureFilename = brochureFilename;
    }

    public User(String username, String email, String password, Date dateNaissance) {
        this.id = id;
        this.username = username;
        this.email = email;

        this.password = password;
        this.dateNaissance = dateNaissance;
        this.numero = numero;



    }
    public User(String numero, String username, String email, boolean is_verified, int etat) {
        this.numero = numero;
        this.username = username;
        this.email = email;
        this.is_verified = is_verified;
        this.etat = etat;
    }
    public User(String numero, String username, String email, Integer etat) {
        this.numero = numero;
        this.username = username;
        this.email = email;
        this.etat = etat;


    }

    public User() {

    }

    public User(String numero, String username, String email,  Button btnBLock) {
        this.numero = numero;
        this.username = username;
        this.email = email;
        this.etat=etat;
    }

    public User(String username, String email, String numTel, boolean isBlocked) {
    }

  /* public User(String username, String email, String numero, boolean etat) {

        this.numero = numero;
        this.username = username;
        this.email = email;
        // Initialiser les autres propriétés
        this.etat = new SimpleStringProperty(etat ? "Deblocked" : "Blocked");
    }*/


    // Getters and Setters
    // id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // roles
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    // resetToken
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    // password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // isVerified
    public boolean getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean verified) {
        is_verified = verified;
    }

    // dateNaissance
    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    // numero
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    // cin
    public Integer getCin() {
        return cin;
    }

    public void setCin(Integer cin) {
        this.cin = cin;
    }

    // etat
    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public Button getEtatButton() {
        return etatButton;
    }

    public void setEtatButton(Button etatButton) {
        this.etatButton = etatButton;
    }


    // imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // brochureFilename
    public String getBrochureFilename() {
        return brochureFilename;
    }

    public Button getBlockButton() {
        return blockButton;
    }

    public void setBlockButton(Button blockButton) {
        this.blockButton = blockButton;
    }

    public void setBrochureFilename(String brochureFilename) {
        this.brochureFilename = brochureFilename;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles='" + roles + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", password='" + password + '\'' +
                ", is_verified=" + is_verified +
                ", dateNaissance=" + dateNaissance +
                ", numero='" + numero + '\'' +
                ", cin=" + cin +
                ", etat=" + etat +
                ", imageUrl='" + imageUrl + '\'' +
                ", borchureFilename='" + brochureFilename + '\'' +
                '}';
    }

    public static void setCurrent_User(User Current_User) {
        User.Current_User = Current_User;
    }

    public boolean hasRole(String role) {
        // Vérifie si l'utilisateur a le rôle spécifié
        return this.roles.contains(role);
    }

}
