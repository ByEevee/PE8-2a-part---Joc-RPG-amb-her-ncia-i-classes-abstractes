# Joc RPG - Pràctica Entregable 08

**Mòdul:** Programació  
**Curs:** DAM1  
**Professor:** Carles Bonet  

---

## Descripció

Implementació d'un joc RPG de fantasia en Java. El projecte se centra en la creació i gestió de personatges de 4 races (Humans, Elfs, Orcs i Nans) i un sistema de combat per torns entre dos jugadors.

---

## Estructura del projecte

```
rpg/
├── Arma.java          # Classe que representa una arma
├── Personatge.java    # Classe abstracta base per a tots els personatges
├── Huma.java          # Raça Humà
├── Elf.java           # Raça Elf
├── Orc.java           # Raça Orc
├── Nan.java           # Raça Nan
└── Joc.java           # Classe principal (menú i combat)
```

---

## Compilació i execució

```bash
# Compilar tots els fitxers
javac *.java

# Executar el joc
java Joc
```

---

## Funcionalitats

### Menú principal
- **Crear personatge** → manual (l'usuari reparteix els punts) o automàtic (aleatori)
- **Combat 1vs1** → els dos jugadors trien un personatge i combaten per torns
- **Llistar personatges** → mostra tots els personatges creats

### Creació de personatges
Cada personatge disposa de **60 punts** a repartir entre 6 característiques (mínim 5, màxim 20 cadascuna):

| Característica | Efecte |
|---|---|
| **Força** | Dany en combat físic |
| **Destresa** | Probabilitat d'esquivar atacs |
| **Constitució** | Salut màxima (`constitució × 50`) |
| **Intel·ligència** | Maná màxim (`intel·ligència × 30`) |
| **Saviesa** | Percepció i intuïció |
| **Carisma** | Influència social |

### Sistema de combat (per torns)
Cada torn el jugador pot:
1. **Canviar d'arma** (opcional)
2. **Atacar** o **Defensar-se**

Al final de cada torn es regeneren automàticament vida i maná.

---

## Races i modificadors (Part 2)

| Raça | Modificadors | Comportament especial |
|---|---|---|
| **Humà** | +1 a totes | Cap especial; raça equilibrada |
| **Elf** | +2 Destresa, +2 Intel·ligència | Regenera maná `intel·ligència × 3` |
| **Orc** | +3 Força, +1 Constitució | No pot equipar armes màgiques; +10% dany |
| **Nan** | +4 Constitució, -1 Destresa | Defensa redueix 25% més; vida `constitució × 4` |

---

## Fórmules del sistema

```
// Atributs derivats
salut màxima  = constitució × 50
maná màxim    = intel·ligència × 30

// Dany en atac
arma física o sense arma → força × (1 + danyArma / 100)
arma màgica              → danyArma × intel·ligència / 100

// Esquivar
probabilitat (%) = (destresa - 5) × 3.33
s'esquiva si random(1-100) ≤ probabilitat

// Defensa
dany rebut = dany / 2
(Nan: dany × 0.375)

// Regeneració per torn (base)
vida = vida + constitució × 3   (Nan: × 4)
maná = maná + intel·ligència × 2  (Elf: × 3)
```

---

## Diagrama de classes (resum)

```
Personatge (abstract)
│  - nom, edat, salut, maná, experiència
│  - forca, destresa, constitucio, intelligencia, saviesa, carisma
│  - inventari: List<Arma>, armaEquipada: Arma
│  + atacar(), esquivar(), defensar()
│  + regenerarVida(), regenerarMana()
│  + equiparArma(), guanyarExperiencia()
│  # aplicarModificadorsRacials() [abstract]
│  # aplicarModificadorDany(), aplicarDefensa()
│
├── Huma    (+1 a tot)
├── Elf     (+2 DES/INT | regenerarMana ×3)
├── Orc     (+3 FOR/+1 CON | no màgia | dany +10%)
└── Nan     (+4 CON/-1 DES | defensa -25% | vida ×4)

Arma
│  - nom, tipus (ESPASA/DESTRAL/BASTO/ARC)
│  - dany (1-100), magica: boolean

Joc
│  - personatges: List<Personatge>
│  + executar(), crearPersonatge(), iniciarCombat()
```

---
