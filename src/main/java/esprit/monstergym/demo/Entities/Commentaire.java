package esprit.monstergym.demo.Entities;

import java.sql.Date;
import java.sql.Timestamp;


public class Commentaire {
    private int ref;
    private String message;
    private Timestamp timestamp;
    private int annonce_id;

    public Commentaire(int ref, String message, Timestamp timestamp, int annonce_id) {
        this.ref = ref;
        this.message = message;
        this.timestamp = timestamp;
        this.annonce_id = annonce_id;
    }


    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getAnnonce_id() {
        return annonce_id;
    }

    public void setAnnonce_id(int annonce_id) {
        this.annonce_id = annonce_id;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "ref=" + ref +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", annonce_id=" + annonce_id +
                '}';
    }
}
