package esprit.monstergym.demo.Entities;

import java.sql.Date;
import java.sql.Timestamp;

public class Annonce {
    private int id;
    private String titre;
    private String description;
    private Timestamp timestamp;
    private String imageUrl;
    private String brochure_filename;

    public Annonce() {
    }

    public Annonce(int id, String titre, String description, Timestamp timestamp ) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.timestamp = timestamp;
    }

    //constructor with image
    public Annonce(int id, String titre, String description, Timestamp timestamp, String brochure_filename ) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.timestamp = timestamp;
        this.brochure_filename = brochure_filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrochureFilename() {
        return brochure_filename;
    }

    public void setBrochureFilename(String brochure_filename) {
        this.brochure_filename = brochure_filename;
    }

    @Override
    public String toString() {
        return "Annonce{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", imageUrl='" + imageUrl + '\'' +
                ", borchure-filename='" + brochure_filename + '\'' +
                '}';
    }
}
