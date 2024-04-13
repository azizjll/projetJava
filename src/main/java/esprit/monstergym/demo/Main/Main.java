package esprit.monstergym.demo.Main;

import esprit.monstergym.demo.Entities.Annonce;
import esprit.monstergym.demo.Entities.Commentaire;


import esprit.monstergym.demo.Service.AnnonceService;
import esprit.monstergym.demo.Service.CommentaireService;



import java.sql.Date;
import java.sql.Timestamp;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Get current date
        java.util.Date currentDate = new java.util.Date();

        // Convert java.util.Date to java.sql.Date
        Date sqlDate = new Date(currentDate.getTime());

        //creating new annonce

        // Instantiate an Annonce object with the necessary details
        Annonce annonce = new Annonce();
        annonce.setTitre("manel annonce");
        annonce.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        annonce.setDate(new Date(System.currentTimeMillis())); // Use the current date

        // Use AnnonceService to add the annonce to the database
        AnnonceService annonceService = new AnnonceService();
        annonceService.create(annonce);

        System.out.println("New annonce added to the database.");

        //show comments

        // Instantiate a CommentaireService
        CommentaireService commentaireService = new CommentaireService();

        // Retrieve all comments from the database
        List<Commentaire> commentaires = commentaireService.getAll();

// Display each comment
        for (Commentaire currentcommentaire : commentaires) {
            System.out.println("Ref: " + currentcommentaire.getRef());
            System.out.println("Message: " + currentcommentaire.getMessage());
            System.out.println("Date: " + currentcommentaire.getTimestamp());
            System.out.println("------------------------------");
        }


        // Retrieve all annonces from the database
        List<Annonce> annonces = annonceService.getAll();

        // Display each annonce
        for (Annonce currentAnnonce : annonces) {
            int annonceId = currentAnnonce.getId(); // Storing the ID in a new variable

            System.out.println("Annonce ID: " + currentAnnonce.getId());
            System.out.println("Titre: " + currentAnnonce.getTitre());
            System.out.println("Description: " + currentAnnonce.getDescription());
            System.out.println("Date: " + currentAnnonce.getDate());
            System.out.println("------------------------------");
        }



        //
        // Find and delete the annonce with ID 22
        int annonceIdToDelete = 24;
        boolean isDeleted = false;
        for (Annonce currentAnnonce : annonces) {
            if (currentAnnonce.getId() == annonceIdToDelete) {
                annonceService.delete(currentAnnonce);
                isDeleted = true;
                break;
            }
        }

        if (isDeleted) {
            System.out.println("Annonce with ID " + annonceIdToDelete + " deleted successfully.");
        } else {
            System.out.println("Annonce with ID " + annonceIdToDelete + " not found.");
        }

        // Create a new Commentaire object
        int ref = 20; // Assuming ref is 1
        String message = "This is a new comment with timestamp";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); // Use the current date
        int annonceId = 25; // Assuming annonce_id is 25
        Commentaire commentaire = new Commentaire(ref, message, timestamp, annonceId);

        // Instantiate a CommentaireService
       // CommentaireService commentaireService = new CommentaireService();

        // Call the create method to insert the new Commentaire into the database
        commentaireService.create(commentaire);

    }

















}





