# Zebra TC21 SOP App

Android-app til Zebra TC21 til indsamling af tekst og billeder til udarbejdelse af SOP'er (Standard Operating Procedures).

## Formål
Appen skal gøre det nemt at dokumentere arbejdstrin direkte på en Zebra TC21 ved at:
- oprette en SOP
- tilføje trin
- skrive overskrift og beskrivelse pr. trin
- tage et eller flere billeder pr. trin
- gemme alt lokalt på enheden
- eksportere data til en PC til videre bearbejdning

## Målgruppe
Appen er tiltænkt brugere i drift, produktion, lager, kvalitet og service, som skal udarbejde eller opdatere SOP'er.

## MVP-funktioner
- Opret SOP
- Tilføj trin
- Tilføj overskrift og beskrivelse til trin
- Tag flere billeder pr. trin
- Gem lokalt på enheden
- Vis liste over SOP'er
- Eksportér SOP til mappeformat til PC

## Foreslået teknisk løsning
- Android
- Kotlin
- Room database
- CameraX eller systemkamera
- Lokal lagring af billeder
- Eksport som mappe med tekstfiler og JPG-billeder

## Dokumentation
Se mappen `docs/` for kravspecifikation, skærmflow, datamodel og eksportformat.

## Status
Projektet er i planlægningsfasen.
