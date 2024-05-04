package esprit.monstergym.demo.Entities;

import java.time.LocalDate;

public class Participation {

int id, tel, user_id, event_id;
String nom, prenom, email ;
LocalDate date ;

    public Participation() {
    }

    public Participation(int id, int tel, int user_id, int event_id, String nom, String prenom, String email, LocalDate date) {
        this.id = id;
        this.tel = tel;
        this.user_id = user_id;
        this.event_id = event_id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.date = date;
    }

    public Participation(int tel, int user_id, int event_id, String nom, String prenom, String email, LocalDate date) {
        this.tel = tel;
        this.user_id = user_id;
        this.event_id = event_id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.date = date;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "id=" + id +
                ", tel=" + tel +
                ", user_id=" + user_id +
                ", event_id=" + event_id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                '}';
    }
    public java.sql.Date getSqlDate() {
        return java.sql.Date.valueOf(date);
    }

}
