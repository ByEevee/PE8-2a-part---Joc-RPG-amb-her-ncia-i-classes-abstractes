import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// nota; Al pdf tenint en compte el max dany i la formula de regenaració fan que sigui impossible guanyar, igualment seguiré el criteris del pdf.
public class Joc {

    private String           jugador1;
    private String           jugador2;
    private List<Personatge> personatges;
    private Scanner          sc;

    // Constants (coincideixen amb Personatge)
    private static final int TOTAL_PUNTS = 60;
    private static final int MIN_STAT    = 5;
    private static final int MAX_STAT    = 20;
    private static final int NUM_STATS   = 6;

    private static final Arma[] POOL_ARMES = {
        new Arma("Espasa de ferro",  Arma.Tipus.ESPASA, 70, false),
        new Arma("Espasa llarga",    Arma.Tipus.ESPASA, 85, false),
        new Arma("Excalivur",        Arma.Tipus.ESPASA, 95, true),
        new Arma("Bastó de foc",     Arma.Tipus.BASTO,  80, true),
        new Arma("Bastó de gel",     Arma.Tipus.BASTO,  75, true),
        new Arma("Bastó del llamp",  Arma.Tipus.BASTO,  95, true),
        new Arma("Arc de fusta",     Arma.Tipus.ARC,    60, false),
        new Arma("Arc màgic",        Arma.Tipus.ARC,    70, true),
        new Arma("Arc de caça",      Arma.Tipus.ARC,    55, false),
    };

    // Getters i Setters
    public String           getJugador1()         { return jugador1; }
    public String           getJugador2()         { return jugador2; }
    public List<Personatge> getPersonatges()      { return personatges; }
    public void             setJugador1(String j) { this.jugador1 = j; }
    public void             setJugador2(String j) { this.jugador2 = j; }

    public static void main(String[] args) {
        Joc joc = new Joc();
        joc.Nou();
    }

    // Constructor
    public Joc() {
        personatges = new ArrayList<>();
        sc          = new Scanner(System.in);
    }

    public void Nou() {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║            JOC RPG           ║");
        System.out.println("╚══════════════════════════════╝");

        System.out.print("\nNom del Jugador 1: ");
        jugador1 = llegirNom();
        System.out.print("Nom del Jugador 2: ");
        jugador2 = llegirNomDistint(jugador1);

        System.out.printf("%nBenvinguts, %s i %s!%n", jugador1, jugador2);

        boolean menu = true;
        do {
            System.out.println();
            System.out.println("══════ MENÚ PRINCIPAL ══════");
            System.out.println("1. Crear personatge");
            System.out.printf ("2. Jugar combat 1vs1  [Personatges: %d]%n", personatges.size());
            System.out.println("3. Sortir");
            System.out.print  ("Opció: ");
            int opcio = llegirInt(1, 3);
            switch (opcio) {
                case 1 -> CrearPersonatge();
                case 2 -> {
                    if (personatges.size() < 2) {
                        System.out.println(" ( ⚠ ) - Calen almenys 2 personatges. Crea'n més!");
                    } else {
                        jugar();
                    }
                }
                case 3 -> menu = false;
            }
        } while (menu);
        System.out.println("\nFins aviat!");
    }

    private void CrearPersonatge() {
        System.out.println();
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║       CREAR PERSONATGE       ║");
        System.out.println("╚══════════════════════════════╝");

        System.out.print("Nom: ");
        String nom  = llegirNom();
        System.out.print("Edat (1-1000): ");
        int    edat = llegirInt(1, 1000);

        // Triar raça
        System.out.println("Races disponibles:");
        System.out.println("  1. Humà    (+1 a tot)");
        System.out.println("  2. Elf     (+2 Destresa, +2 Intel·ligència)");
        System.out.println("  3. Orc     (+3 Força, +1 Constitució)");
        System.out.println("  4. Nan     (+4 Constitució, -1 Destresa)");
        System.out.print  ("Tria: ");
        int raca = llegirInt(1, 4);

        // Mode d'assignació
        System.out.println("Mode: 1. Manual   2. Automàtic");
        System.out.print  ("Opció: ");
        int mode = llegirInt(1, 2);
        int[] stats;
        switch (mode) {
            case 1:  stats = crearManual();    break;
            default: stats = crearAutomatic(); break;
        }

        // Construir personatge de la subclasse correcta
        Personatge p = construirPersonatge(raca, nom, edat, stats);

        // Assignar 3 armes aleatòries del pool
        List<Integer> usats = new ArrayList<>();
        while (usats.size() < 3) {
            int idx = (int)(Math.random() * POOL_ARMES.length);
            if (!usats.contains(idx)) {
                usats.add(idx);
                Arma o = POOL_ARMES[idx];
                p.afegirArma(new Arma(o.getNom(), o.getTipus(), o.getDany(), o.esMagica()));
            }
        }

        // Equipar la primera arma per defecte
        p.equiparArma(p.getInventari().get(0));

        personatges.add(p);
        System.out.println("\n ( ✔ ) Personatge creat! [" + p.getRaca() + "]");
        System.out.println(p);
        System.out.println("Armes assignades:");
        for (int i = 0; i < p.getInventari().size(); i++)
            System.out.printf("  %d. %s%n", i + 1, p.getInventari().get(i));
    }

    // Construeix el personatge de la subclasse correcta segons la raça
    private Personatge construirPersonatge(int raca, String nom, int edat, int[] s) {
        switch (raca) {
            case 1: return new Huma(nom, edat, s[0], s[1], s[2], s[3], s[4], s[5]);
            case 2: return new Elf (nom, edat, s[0], s[1], s[2], s[3], s[4], s[5]);
            case 3: return new Orc (nom, edat, s[0], s[1], s[2], s[3], s[4], s[5]);
            case 4: return new Nan (nom, edat, s[0], s[1], s[2], s[3], s[4], s[5]);
            default: return null;
        }
    }

    // Estadístiques manuals
    private int[] crearManual() {
        String[] noms = {"Força", "Destresa", "Constitució",
                         "Intel·ligència", "Saviesa", "Carisma"};
        int[] stats         = new int[NUM_STATS];
        int   puntsRestants = TOTAL_PUNTS;

        System.out.printf("%nRepartiràs %d punts entre %d stats (min %d, max %d)%n",
                TOTAL_PUNTS, NUM_STATS, MIN_STAT, MAX_STAT);

        for (int i = 0; i < NUM_STATS; i++) {
            if (i == NUM_STATS - 1) {
                stats[i] = puntsRestants;
                System.out.printf("  %-18s → assignat: %d%n", noms[i], stats[i]);
                break;
            }
            int reserva   = (NUM_STATS - i - 1) * MIN_STAT;
            int maxPermès = Math.min(MAX_STAT, puntsRestants - reserva);
            System.out.printf("  %-18s (restants: %2d | %d-%d): ",
                    noms[i], puntsRestants, MIN_STAT, maxPermès);
            int val        = llegirInt(MIN_STAT, maxPermès);
            stats[i]       = val;
            puntsRestants -= val;
        }
        return stats;
    }

    // Estadístiques automàtiques
    private int[] crearAutomatic() {
        int[] stats = new int[NUM_STATS];
        int extra   = TOTAL_PUNTS - NUM_STATS * MIN_STAT;
        for (int i = 0; i < NUM_STATS; i++) stats[i] = MIN_STAT;
        while (extra > 0) {
            int idx = (int)(Math.random() * NUM_STATS);
            if (stats[idx] < MAX_STAT) { stats[idx]++; extra--; }
        }
        System.out.printf(
            "%nDistribució automàtica:%n" +
            "  FOR:%d  DES:%d  CON:%d  INT:%d  SAV:%d  CAR:%d%n",
            stats[0], stats[1], stats[2], stats[3], stats[4], stats[5]);
        return stats;
    }

    public Personatge escollirPersonatge(String nomJugador, Personatge excloure) {
        List<Personatge> disponibles = new ArrayList<>();
        for (Personatge p : personatges) {
            if (p != excloure) disponibles.add(p);
        }
        System.out.printf("%n%s, tria el teu personatge:%n", nomJugador);
        for (int i = 0; i < disponibles.size(); i++) {
            Personatge p = disponibles.get(i);
            System.out.printf("  %d. %-12s [%-4s]  ( HP ) %d/%d%n",
                    i + 1, p.getNom(), p.getRaca(),
                    p.getSalut(), p.getSalutMax());
        }
        System.out.print("Opció: ");
        return disponibles.get(llegirInt(1, disponibles.size()) - 1);
    }

    private void jugar() {
        System.out.println();
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║         COMBAT 1vs1          ║");
        System.out.println("╚══════════════════════════════╝");

        Personatge p1 = escollirPersonatge(jugador1, null);
        Personatge p2 = escollirPersonatge(jugador2, p1);

        // Restaurar vida i maná al màxim
        p1.restaurarStats();
        p2.restaurarStats();

        System.out.printf("%n⚔  %s (%s) [%s]  VS  %s (%s) [%s]  ⚔%n",
                jugador1, p1.getNom(), p1.getRaca(),
                jugador2, p2.getNom(), p2.getRaca());

        int torn = 1;
        while (p1.estaViu() && p2.estaViu()) {
            System.out.printf("%n========= TORN %d =========%n", torn);
            mostrarBarra(p1, p2);

            System.out.printf("%n( -> ) Torn de %s (%s) [%s]%n",
                    jugador1, p1.getNom(), p1.getRaca());
            tornJugador(p1, p2);
            if (!p2.estaViu()) break;

            System.out.printf("%n( -> ) Torn de %s (%s) [%s]%n",
                    jugador2, p2.getNom(), p2.getRaca());
            tornJugador(p2, p1);
            if (!p1.estaViu()) break;

            // Regeneració al final del torn
            p1.regenerarVida(); p1.regenerarMana();
            p2.regenerarVida(); p2.regenerarMana();
            p1.resetDefensa();
            p2.resetDefensa();
            System.out.println("  [Fi de torn - vida i maná recuperats parcialment]");
            torn++;
        }

        System.out.println();
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║       FI DEL COMBAT          ║");
        System.out.println("╚══════════════════════════════╝");
        if (p1.estaViu())
            System.out.printf(" ( ! ) Guanyador: %s (%s) [%s]%n",
                    jugador1, p1.getNom(), p1.getRaca());
        else
            System.out.printf(" ( ! ) Guanyador: %s (%s) [%s]%n",
                    jugador2, p2.getNom(), p2.getRaca());
    }

    private void mostrarBarra(Personatge p1, Personatge p2) {
        System.out.printf("  %-12s [%-4s]  ( HP ) %4d/%-4d  ( M ) %4d/%-4d%n",
                p1.getNom(), p1.getRaca(), p1.getSalut(), p1.getSalutMax(),
                p1.getMana(), p1.getManaMax());
        System.out.printf("  %-12s [%-4s]  ( HP ) %4d/%-4d  ( M ) %4d/%-4d%n",
                p2.getNom(), p2.getRaca(), p2.getSalut(), p2.getSalutMax(),
                p2.getMana(), p2.getManaMax());
    }

    private void tornJugador(Personatge atacant, Personatge defensor) {
        // Canviar arma?
        List<Arma> inv = atacant.getInventari();
        if (!inv.isEmpty()) {
            System.out.println("  Canviar d'arma?  1.Sí  2.No");
            System.out.print  ("  Opció: ");
            if (llegirInt(1, 2) == 1) {
                System.out.println("  Tria arma:");
                for (int i = 0; i < inv.size(); i++)
                    System.out.printf("    %d. %s%n", i + 1, inv.get(i));
                System.out.print("  Opció: ");
                atacant.equiparArma(inv.get(llegirInt(1, inv.size()) - 1));
            }
        }

        // Acció
        System.out.println("  Acció:");
        System.out.println("    1. Atacar");
        System.out.println("    2. Defensar-se  (pròxim atac rebut fa ½ dany)");
        System.out.print  ("  Opció: ");

        switch (llegirInt(1, 2)) {
            case 1 -> atacant.atacar(defensor);
            case 2 -> atacant.defensar();
        }
    }

    // Helpers d'entrada
    private String llegirNom() {
        String nom = "";
        while (nom.isEmpty()) {
            nom = sc.nextLine().trim();
            if (nom.isEmpty())
                System.out.print(" ( ⚠ ) - El nom no pot estar buit: ");
        }
        return nom;
    }

    private String llegirNomDistint(String altre) {
        String nom;
        do {
            nom = llegirNom();
            if (nom.equalsIgnoreCase(altre)) {
                System.out.print(" ( ⚠ ) - El nom no pot ser igual al del Jugador 1: ");
                nom = "";
            }
        } while (nom.isEmpty());
        return nom;
    }

    private int llegirInt(int min, int max) {
        int valor = 0;
        while (valor < min || valor > max) {
            try {
                valor = Integer.parseInt(sc.nextLine().trim());
                if (valor < min || valor > max)
                    System.out.printf(" ( ⚠ ) - Entre %d i %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf(" ( ⚠ ) - Entrada no vàlida (%d-%d): ", min, max);
            }
        }
        return valor;
    }
}