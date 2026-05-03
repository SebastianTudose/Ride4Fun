# 🛴 Ride4Fun

**Ride4Fun** este o platformă web completă dedicată gestionării și închirierii de trotinete electrice. Sistemul conectează utilizatorii care doresc o modalitate rapidă de transport cu o interfață intuitivă de închiriere, oferind în același timp administratorilor un panou de control avansat pentru monitorizarea flotei, a încasărilor și a mentenanței.

---

## 🚀 Scopul Proiectului

Aplicația acoperă nevoile a două tipuri principale de actori:

**Pentru Utilizatori:**
* Vizualizarea trotinetelor disponibile.
* Inițierea și finalizarea unei curse în timp real.
* Calculul automat al costului în funcție de tariful curent (taxă deblocare + timp/distanță).
* Gestionarea profilului și a metodelor de plată.

**Pentru Administratori / Angajați:**
* Vizualizarea și gestionarea întregii flote (nivel baterie, stare tehnică, locație).
* Dashboard interactiv cu statistici și încasări.
* Setarea dinamică a tarifelor.
* Evidența operațiunilor de service și mentenanță pentru fiecare vehicul.

---

## 🧠 Tehnologii Utilizate

| Categorie | Tehnologii |
| :--- | :--- |
| **Frontend** | 🖥️ Vaadin Flow (Java), HTML, CSS |
| **Backend** | ☕ Java 21, Maven, EclipseLink (JPA), Jakarta EE |
| **Bază de date** | 🐘 PostgreSQL |
| **Arhitectură**| 🧱 Monorepo (Multi-module Maven) |
| **DevOps / Deploy**| 🐳 Docker, Render |
| **Versionare** | 🧩 Git & GitHub |

---

## 🗂️ Structura Proiectului

Proiectul este împărțit în două module principale pentru a separa logica de business de interfața utilizatorului:

* 📁 **`backend/`** - Conține entitățile bazei de date (Modele), configurările JPA (`persistence.xml`) și logica de calcul.
* 📁 **`frontend/`** - Conține interfața grafică dezvoltată în Vaadin, serviciile de conectare și layout-urile aplicației (Dashboard, Login, Tabele).

---

## 🌿 Branching Model

Folosim o structură simplă și eficientă pentru organizarea codului:

| Branch | Descriere |
| :--- | :--- |
| `main` | Versiunea finală, stabilă. Codul de aici este **implementat automat (Auto-Deploy)** pe serverul de producție (Render). |
| `dev` | Versiunea de dezvoltare și integrare. Aici se fac testele înainte de lansare. |

🔹 **Reguli:**
* Orice funcționalitate nouă este testată local înainte de a face push.
* `main` primește doar cod funcțional pentru a nu întrerupe serviciul live.

---
## Acesare platforma lansată prin Render
https://ride4fun-vflg.onrender.com

## ⚙️ Cum Rulezi Aplicația Local

Pentru a rula proiectul pe propriul calculator, urmează acești pași:

**1. Clonează proiectul:**
```bash
git clone [https://github.com/SebastianTudose/Ride4Fun.git](https://github.com/SebastianTudose/Ride4Fun.git)
cd Ride4Fun
