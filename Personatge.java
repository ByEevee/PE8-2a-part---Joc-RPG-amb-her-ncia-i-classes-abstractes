import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe base abstracta per a tots els personatges del joc RPG.
 * Conté les característiques, atributs derivats, inventari i accions comunes.
 */
public abstract class Personatge {

    // Constants 
    protected static final int PUNTS_TOTALS        = 60;
    protected static final int MIN_CARACTERISTICA  = 5;
    protected static final int MAX_CARACTERISTICA  = 20;

    // Atributs bàsics 
    protected String nom;
    protected int    edat;

    // Característiques
    protected int forca;
    protected int destresa;
    protected int constitucio;
    protected int intelligencia;
    protected int saviesa;
    protected int carisma;

    // Atributs derivats
    protected int salut;
    protected int salutMax;
    protected int mana;
    protected int manaMax;

    // Atribut extra: experiència acumulada 
    protected int experiencia;

    // Inventari 
    protected List<Arma> inventari;
    protected Arma armaEquipada;

    // Estat de combat 
    protected boolean defensant;

    // Constructor
    public Personatge(String nom, int edat,
                      int forca, int destresa, int constitucio,
                      int intelligencia, int saviesa, int carisma) {
        this.nom           = nom;
        this.edat          = edat;
        this.forca         = forca;
        this.destresa      = destresa;
        this.constitucio   = constitucio;
        this.intelligencia = intelligencia;
        this.saviesa       = saviesa;
        this.carisma       = carisma;
        this.inventari     = new ArrayList<>();
        this.defensant     = false;
        this.experiencia   = 0;

        aplicarModificadorsRacials();   // cada raça ajusta les característiques

        // Atributs derivats (DESPRÉS dels modificadors)
        this.salutMax = this.constitucio   * 50;
        this.manaMax  = this.intelligencia * 30;
        this.salut    = this.salutMax;
        this.mana     = this.manaMax;
    }

    // ── Mètodes abstractes

    
    protected abstract void aplicarModificadorsRacials();

   
    public abstract String getRaca();

    // Accions

    public boolean equiparArma(Arma arma) {
        if (!inventari.contains(arma)) {
            System.out.println("( ! )  L'arma no és a l'inventari!");
            return false;
        }
        if (arma.esMagica() && intelligencia < 10) {
            System.out.println("( ! )  Cal intel·ligència ≥ 10 per equipar armes màgiques!");
            return false;
        }
        this.armaEquipada = arma;
        System.out.println(nom + " equipa: " + arma.getNom());
        return true;
    }

    /** Desequipa l'arma actual. */
    public void desequiparArma() {
        armaEquipada = null;
        System.out.println(nom + " desequipa l'arma.");
    }

    
    public int atacar(Personatge objectiu) {
        int danyBase;
        if (armaEquipada == null || !armaEquipada.esMagica()) {
            int danyArma = (armaEquipada != null) ? armaEquipada.getDany() : 0;
            danyBase = (int) (forca * (1.0 + danyArma / 100.0));
        } else {
            danyBase = armaEquipada.getDany() * intelligencia / 100;
        }

        // Modificador racial de dany (els orcs fan +10%)
        int danyFinal = aplicarModificadorDany(danyBase);

        // Intentar esquivar
        if (objectiu.esquivar()) {
            System.out.printf(" ( ! ) %s esquiva l'atac de %s!%n", objectiu.getNom(), nom);
            return 0;
        }

        // Aplicar defensa si l'objectiu es defensava
        if (objectiu.defensant) {
            danyFinal = objectiu.aplicarDefensa(danyFinal);
            System.out.printf("( ! )  %s es defensa! Dany reduït a %d%n", objectiu.getNom(), danyFinal);
        }

        objectiu.rebreDany(danyFinal);
        guanyarExperiencia(danyFinal / 2);  // guanya experiència per l'atac
        System.out.printf("⚔  %s ataca %s per %d de dany! (Salut restant: %d/%d)%n",
                nom, objectiu.getNom(), danyFinal,
                objectiu.salut, objectiu.salutMax);
        return danyFinal;
    }

    
    protected int aplicarModificadorDany(int dany) {
        return dany;
    }

    protected int aplicarDefensa(int dany) {
        return dany / 2;
    }

    
    public void rebreDany(int dany) {
        salut = Math.max(0, salut - dany);
    }

    public boolean esquivar() {
        double probabilitat = (destresa - 5) * 3.33;
        int tirada = new Random().nextInt(100) + 1;  // 1–100
        return tirada <= probabilitat;
    }

   
    public void defensar() {
        defensant = true;
        System.out.println("( ! )  " + nom + " es posa en postura defensiva!");
    }

   
    public void regenerarVida() {
        salut = Math.min(salutMax, salut + constitucio * 3);
    }

    public void regenerarMana() {
        mana = Math.min(manaMax, mana + intelligencia * 2);
    }

    // Mètode extra: guanyar experiència

    public void guanyarExperiencia(int punts) {
        experiencia += punts;
    }

    // Inventari 

    public void afegirArma(Arma arma) {
        inventari.add(arma);
    }

    // Utilitats de combat

    public boolean estaViu()      { return salut > 0; }

    public void resetDefensa()    { defensant = false; }

    public void restaurarStats() {
        salut = salutMax;
        mana  = manaMax;
    }

    // Getters 

    public String     getNom()           { return nom; }
    public int        getEdat()          { return edat; }
    public int        getSalut()         { return salut; }
    public int        getSalutMax()      { return salutMax; }
    public int        getMana()          { return mana; }
    public int        getManaMax()       { return manaMax; }
    public int        getForca()         { return forca; }
    public int        getDestresa()      { return destresa; }
    public int        getConstitucio()   { return constitucio; }
    public int        getIntelligencia() { return intelligencia; }
    public int        getSaviesa()       { return saviesa; }
    public int        getCarisma()       { return carisma; }
    public int        getExperiencia()   { return experiencia; }
    public List<Arma> getInventari()     { return inventari; }
    public Arma       getArmaEquipada()  { return armaEquipada; }

    // toString 
    @Override
    public String toString() {
        String armaStr = (armaEquipada != null) ? armaEquipada.getNom() : "Cap";
        return String.format(
            "┌─ %s [%s] ─────────────────────────────┐%n" +
            "│ Edat: %-3d   Exp: %-6d                  │%n" +
            "│ Salut: %4d/%-4d   Maná: %4d/%-4d        │%n" +
            "│ FOR:%2d  DES:%2d  CON:%2d                   │%n" +
            "│ INT:%2d  SAV:%2d  CAR:%2d                   │%n" +
            "│ Arma: %-30s │%n" +
            "└──────────────────────────────────────────┘",
            nom, getRaca(),
            edat, experiencia,
            salut, salutMax, mana, manaMax,
            forca, destresa, constitucio,
            intelligencia, saviesa, carisma,
            armaStr
        );
    }
}
