package model;

public class Article extends Document {
    public String journal;

    public Article(String titre, String auteur, int anneePublication, boolean disponible, String journal) {
        super(titre, auteur, anneePublication, disponible);
        this.journal = journal;
    }

    @Override  
    public void afficherinfos() {
        System.out.println("Le document " + titre + " a été écrit par " + auteur + " en " + anneePublication);
        System.out.println("Journal: " + journal);
        System.out.println("Disponible: " + (disponible ? "oui" : "non"));
    }
}