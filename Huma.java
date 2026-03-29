public class Huma extends Personatge {

    public Huma(String nom, int edat,
                int forca, int destresa, int constitucio,
                int intelligencia, int saviesa, int carisma) {
        super(nom, edat, forca, destresa, constitucio, intelligencia, saviesa, carisma);
    }

    @Override
    protected void aplicarModificadorsRacials() {
        forca         = Math.min(MAX_CARACTERISTICA, forca         + 1);
        destresa      = Math.min(MAX_CARACTERISTICA, destresa      + 1);
        constitucio   = Math.min(MAX_CARACTERISTICA, constitucio   + 1);
        intelligencia = Math.min(MAX_CARACTERISTICA, intelligencia + 1);
        saviesa       = Math.min(MAX_CARACTERISTICA, saviesa       + 1);
        carisma       = Math.min(MAX_CARACTERISTICA, carisma       + 1);
    }

    @Override
    public String getRaca() { return "Humà"; }
}
