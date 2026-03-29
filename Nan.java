public class Nan extends Personatge {

    public Nan(String nom, int edat,
               int forca, int destresa, int constitucio,
               int intelligencia, int saviesa, int carisma) {
        super(nom, edat, forca, destresa, constitucio, intelligencia, saviesa, carisma);
    }

    @Override
    protected void aplicarModificadorsRacials() {
        constitucio = Math.min(MAX_CARACTERISTICA, constitucio + 4);
        destresa    = Math.max(MIN_CARACTERISTICA, destresa    - 1);
    }

    /**
     * Defensa millorada: la reducció base és dany/2, però els nans reben
     * un 25% menys que la resta → dany/2 × 0,75 = dany × 0,375.
     */
    @Override
    protected int aplicarDefensa(int dany) {
        return (int) (dany * 0.375);
    }

    /** Regeneració de vida millorada: constitució × 4. */
    @Override
    public void regenerarVida() {
        salut = Math.min(salutMax, salut + constitucio * 4);
    }

    @Override
    public String getRaca() { return "Nan"; }
}
