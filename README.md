# NotesApp

## Përshkrimi
**NotesApp** është një aplikacion Android i krijuar në Android Studio duke përdorur Java. Ky aplikacion i lejon përdoruesit të regjistrohen, të bëjnë log in dhe të menaxhojnë shënimet e tyre personale me titull dhe përshkrim. Çdo përdorues ka një hapësirë unike ku mund të krijojë, editojë dhe fshijë shënime që ruhen veçmas.

## Veçoritë Kryesore
- Regjistrimi dhe logimi i përdoruesve.
- Krijimi i shënimeve me titull dhe përshkrim.
- Editimi i shënimeve ekzistuese.
- Fshirja e shënimeve të padëshiruara.
- Ruajtja e të dhënav

## Udhezime per perdorim

- Hapni aplikacionin dhe krijoni një llogari duke u regjistruar.
- Bëni log in me kredencialet tuaja.
- Krijoni shënime të reja duke shtypur titullin dhe përshkrimin.
- Klikoni në një shënim për ta edituar ose përdorni opsionin për fshirje.
- Të gjitha shënimet ruhen dhe janë të personalizuara për secilin përdorues.


## Struktura e Projektit

Projekti është organizuar në mënyrën e mëposhtme:

```
.
├── java/
│   ├── com.example.androidapp/
│   │   ├── activities/         # Activity-t kryesore të aplikacionit
│   │   ├── adapters/           # Adapterat për RecyclerView ose komponentët e tjerë
│   │   ├── db/                 # Klasa për menaxhimin e bazës së të dhënave
│   │   ├── fragments/          # Fragmentet për ndërfaqet e ndryshme
│   │   ├── helpers/            # Funksione ndihmëse ose utilitare
│   │   └── models/             # Modelet e të dhënave (p.sh., për shënimet dhe përdoruesit)
├── res/
│   ├── anim/                   # Animacionet për aplikacionin
│   ├── drawable/               # Imazhet dhe burimet vizuale
│   ├── layout/                 # Skedarët XML për paraqitjen e UI-së
│   ├── menu/                   # Menutë dhe opsionet e aplikacionit
│   ├── navigation/             # Grafi i navigimit midis aktiviteteve/fragmenteve
│   ├── values/                 # Burimet si strings, colors, dhe styles
│   └── xml/                    # Skedarët XML të konfigurimit (p.sh., për të dhënat statike)









