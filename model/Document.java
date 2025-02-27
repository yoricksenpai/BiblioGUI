package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Document {
    public String titre;
    public String auteur;
    public int anneePublication;
    public boolean disponible;
    public LocalDate dateInsertion; // Nouvelle propriété

    public Document(String titre, String auteur, int anneePublication, boolean disponible) {
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = true;
        this.dateInsertion = LocalDate.now(); // Date actuelle lors de la création
    }

    public void afficherinfos() {
        System.out.println("Le document " + titre + " a ete ecrit par " + auteur + " en " + anneePublication);
        System.out.println("Disponible: " + (disponible ? "oui" : "non"));
        System.out.println("Date d'insertion: " + dateInsertion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public void emprunterDocument() {
        if (disponible) {
            disponible = false;
            System.out.println("Le document " + titre + " a ete emprunter");
        } else {
            System.out.println("Ce document n'est pas disponible aujourd'hui");
        }
    }

    public void rendreDocument() {
        if (!disponible) {
            disponible = true;
            System.out.println("Le document à ete rendu, merci pour votre collaboration");
        } else {
            System.out.println("Ce document n'a pas été emprunter, veuillez verifier a ce que ce ne soit pas une copie pirater.");
        }
    }

    public static void rechercherDocument(ArrayList<Document> documents, String titre) {
        for (Document document : documents) {
            if (document.titre.equals(titre)) {
                System.out.println("Document trouvé :");
                document.afficherinfos();
                return;
            }
        }
        System.out.println("Document avec le titre " + titre + " non trouver");
    }

    public static void supprimerDocument(ArrayList<Document> documents, String titre) {
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).titre.equals(titre)) {
                documents.remove(i);
                System.out.println("Document avec le titre " + titre + " a ete supprimer");
                return;
            }
        }
        System.out.println("Document avec le titre " + titre + " non trouver");
    }
}