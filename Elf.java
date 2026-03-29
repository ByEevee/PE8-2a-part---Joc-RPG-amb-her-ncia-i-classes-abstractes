public class Elf extends Personatge {

    public Elf(String nom, int edat,
               int forca, int destresa, int constitucio,
               int intelligencia, int saviesa, int carisma) {
        super(nom, edat, forca, destresa, constitucio, intelligencia, saviesa, carisma);
    }

    @Override
    protected void aplicarModificadorsRacials() {
        destresa      = Math.min(MAX_CARACTERISTICA, destresa      + 2);
        intelligencia = Math.min(MAX_CARACTERISTICA, intelligencia + 2);
    }

    /** Regeneració de maná millorada: intel·ligència × 3. */
    @Override
    public void regenerarMana() {
        mana = Math.min(manaMax, mana + intelligencia * 3);
    }

    @Override
    public String getRaca() { return "Elf"; }
}
