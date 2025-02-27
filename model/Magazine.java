package model;

public class Magazine extends Document {
    public String moisPubli;
    
    public Magazine(String titre, String auteur, int anneePublication, boolean disponible, String moisPubli) {
        super(titre, auteur, anneePublication, disponible);
        this.moisPubli = moisPubli;
    }
    
    @Override
    public void afficherinfos() {
        System.out.println("Le magazine " + titre + " a ete ecrit par " + auteur + " en " + anneePublication + " precisement le mois de " + moisPubli);
    }
}