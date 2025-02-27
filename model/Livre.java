package model;

public class Livre extends Document {
    public int nombrePage;
    
    public Livre(String titre, String auteur, int anneePublication, boolean disponible, int nombrePage) {
        super(titre, auteur, anneePublication, disponible);
        this.nombrePage = nombrePage;
    }
    
    @Override
    public void afficherinfos() {
        System.out.println("Le livre " + titre + " a ete ecrit par " + auteur + " en " + anneePublication + ", il est dotee de " + nombrePage + " pages.");
    }
}