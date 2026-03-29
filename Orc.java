public class Orc extends Personatge {

    public Orc(String nom, int edat,
               int forca, int destresa, int constitucio,
               int intelligencia, int saviesa, int carisma) {
        super(nom, edat, forca, destresa, constitucio, intelligencia, saviesa, carisma);
    }

    @Override
    protected void aplicarModificadorsRacials() {
        forca       = Math.min(MAX_CARACTERISTICA, forca       + 3);
        constitucio = Math.min(MAX_CARACTERISTICA, constitucio + 1);
    }

    /** Els orcs no poden equipar armes màgiques. */
    @Override
    public boolean equiparArma(Arma arma) {
        if (arma.esMagica()) {
            System.out.println("( ! )  Els orcs no poden equipar armes màgiques!");
            return false;
        }
        return super.equiparArma(arma);
    }

    /** Bonus racial: +10% de dany en atac. */
    @Override
    protected int aplicarModificadorDany(int dany) {
        return (int) (dany * 1.10);
    }

    @Override
    public String getRaca() { return "Orc"; }
}
