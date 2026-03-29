public class Arma {

    public enum Tipus { ESPASA, DESTRAL, BASTO, ARC }

    private final String nom;
    private final Tipus tipus;
    private final int dany;      
    private final boolean magica;

    public Arma(String nom, Tipus tipus, int dany, boolean magica) {
        this.nom    = nom;
        this.tipus  = tipus;
        this.dany   = Math.max(1, Math.min(100, dany));
        this.magica = magica;
    }

    //  Getters 
    public String getNom()    { return nom; }
    public Tipus  getTipus()  { return tipus; }
    public int    getDany()   { return dany; }
    public boolean esMagica() { return magica; }

    @Override
    public String toString() {
        return String.format("%s [%s | Dany:%d | %s]",
                nom, tipus, dany, magica ? "Màgica" : "Física");
    }
}
