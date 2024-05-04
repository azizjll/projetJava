package esprit.monstergym.demo.Entities;

import java.time.LocalDate;

public class Event {
    int id , user_id;
    String 	nom, description, 	image, lieu;
    LocalDate date;

    public Event() {
    }

    public Event(int id, int user_id, String nom, String description, String image, String lieu, LocalDate date) {
        this.id = id;
        this.user_id = user_id;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.lieu = lieu;
        this.date = date;
    }

    public Event(int user_id, String nom, String description, String image, String lieu, LocalDate date) {
        this.user_id = user_id;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.lieu = lieu;
        this.date = date;
    }

    public Event(String nom, String description, String image, String lieu, LocalDate date) {
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.lieu = lieu;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", lieu='" + lieu + '\'' +
                ", date=" + date +
                '}';
    }
    public java.sql.Date getSqlDate() {
        return java.sql.Date.valueOf(date);
    }

}
