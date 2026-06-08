# Eksportformat – Zebra TC21 SOP App

## Formål
Eksporten skal gøre det nemt at overføre SOP-materiale til en PC og viderebearbejde det i f.eks. Word eller PDF.

## Struktur
Hver SOP eksporteres som en mappe.

Eksempel:

```text
SOP_Rengoering_Af_Maskine/
  sop_info.txt
  Trin_01/
    trin_info.txt
    billede_1.jpg
    billede_2.jpg
  Trin_02/
    trin_info.txt
    billede_1.jpg
```

## Indhold

### sop_info.txt
Indeholder:
- SOP-titel
- dato
- antal trin

### trin_info.txt
Indeholder:
- trinnummer
- overskrift
- beskrivelse

## Filnavngivning
- SOP-mapper navngives med titel og evt. dato
- Trin navngives som `Trin_01`, `Trin_02`, osv.
- Billeder navngives som `billede_1.jpg`, `billede_2.jpg`

## Fremtidige udvidelser
- eksport som ZIP
- eksport som PDF
- eksport som JSON/CSV
