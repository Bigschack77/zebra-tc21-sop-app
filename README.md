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
- Forbered flere billeder pr. trin via billedpladsholdere
- Lokal lagringsarkitektur via repository-lag (midlertidigt in-memory)
- Vis liste over SOP'er
- Navigation klar til senere eksportfunktion

## Projektstatus
Repository'et indeholder nu et første Android MVP-skelet i Kotlin med Jetpack Compose:
- `app/`-modul, så projektet kan åbnes i Android Studio
- SOP-liste
- SOP-detalje
- Opret/rediger trin
- Datamodeller for `Sop`, `Step` og `StepImage`
- `SopRepository` + `InMemorySopRepository`, klar til senere udskiftning med Room eller anden lokal lagring

## Åbn projektet i Android Studio
1. Åbn mappen `/tmp/workspace/Bigschack77/zebra-tc21-sop-app` i Android Studio.
2. Lad Gradle sync køre færdig.
3. Brug Android SDK 35 og JDK 17, hvis Android Studio spørger.
4. Kør appen på en emulator eller en Zebra TC21-enhed.

## Byg fra kommandolinje
```bash
./gradlew :app:assembleDebug
```

Første Gradle-sync/build kræver adgang til Google Maven og Maven Central for at hente Android-afhængigheder.

## Næste oplagte skridt
- Erstat in-memory repository med Room.
- Tilknyt kamera eller filvælger til `StepImage`.
- Gem billeder i app-specifik lokal lagring.
- Tilføj reel eksport til mappeformat til PC.

## Dokumentation
Se mappen `docs/` for kravspecifikation, skærmflow, datamodel og eksportformat.
