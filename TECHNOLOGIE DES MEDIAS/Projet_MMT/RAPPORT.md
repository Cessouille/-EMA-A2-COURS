RAPPORT — Analyse PCM & DPCM (Projet MMT)

## Résumé des résultats expérimentaux

### Paramètres de simulation

- Fréquence d'échantillonnage: Fs = 20 000 Hz
- Signal de référence: sinusoïde f = 2 kHz, durée = 3 s
- Échantillonnage: 10 échantillons par période
- Signal normalisé sur [-1, +1]

### Résultats PCM (quantification uniforme)

- SNR PCM 8 bits: 49.79 dB
- SNR PCM 6 bits: 35.10 dB
- SNR PCM 4 bits: 23.10 dB
- SNR PCM 3 bits: 17.65 dB
- SNR PCM 2 bits: 10.45 dB
- SNR PCM 1 bit: 6.03 dB

### Résultats DPCM (prédicteur d'ordre 1, R = 8 bits)

- SNR DPCM R=8 (sans erreurs): 48.69 dB

### Résultats avec erreurs binaires (modèle BSC)

**PCM 8 bits:**

- p = 0.01 (10⁻²) → SNR: 14.74 dB
- p = 0.001 (10⁻³) → SNR: 24.41 dB

**DPCM R=8:**

- p = 0.001 (10⁻³) → SNR: -12.42 dB

---

RAPPORT — Analyse PCM & DPCM (Projet MMT)

Ce rapport répond explicitement aux questions du sujet (Partie 1 et Partie 2) en s'appuyant sur les résultats de la simulation.

---

## PARTIE 1 — Analyse du PCM

### Contexte

L'objectif est d'étudier l'impact des erreurs induites par un réseau sur un codeur PCM (Pulse Code Modulation) linéaire avec une résolution de R bits/échantillon.

### Question 1.1 — Création et reproduction de la tonalité de référence

**Consigne:** Créer une tonalité sinusoïdale de fréquence f = 2kHz, de 3 sec de durée, en utilisant 10 échantillons (en float) par période. Reproduire cette tonalité sur les haut-parleurs.

**Réponse:**

- Signal créé: sinusoïde de fréquence f = 2 kHz, durée = 3 secondes
- Échantillonnage: 10 échantillons par période → Fs = 10 × 2000 = 20 000 Hz
- Signal normalisé sur l'intervalle [-1, +1] en virgule flottante
- Le script `simulation.py` génère des fichiers WAV (`pcm_*bits.wav`) qui peuvent être reproduits sur les haut-parleurs de l'ordinateur

### Question 1.2 — Quantification à 8 bits/échantillon

**Consigne:** Quantifier ce signal en (int) à 8 bits/éch.

**Résultat:**

- **SNR PCM 8 bits: 49.79 dB**

**Analyse:**

- Une résolution de 8 bits fournit une très bonne qualité pour un signal sinusoïdal pur
- Le bruit de quantification reste faible, ce qui explique le SNR élevé (~50 dB)
- La formule théorique SNR ≈ 6.02R + 1.76 dB prédit environ 49.9 dB pour R=8, ce qui correspond bien à la mesure

### Question 1.3 — Quantification à résolutions réduites

**Consigne:** Quantifier ce signal en utilisant une résolution de 6 bits/éch, de 4 bits/éch, de 3 bits/éch et de 2 bits/éch.

**Résultats:**

- **SNR PCM 6 bits: 35.10 dB**
- **SNR PCM 4 bits: 23.10 dB**
- **SNR PCM 3 bits: 17.65 dB**
- **SNR PCM 2 bits: 10.45 dB**

**Analyse:**

- On observe une décroissance systématique du SNR lorsque le nombre de bits diminue
- La relation approximative SNR ≈ 6 dB/bit est vérifiée:
  - De 8 bits à 6 bits (2 bits de moins): 49.79 - 35.10 ≈ 14.7 dB (≈ 7.3 dB/bit)
  - De 6 bits à 4 bits (2 bits de moins): 35.10 - 23.10 = 12 dB (6 dB/bit)
  - De 4 bits à 2 bits (2 bits de moins): 23.10 - 10.45 ≈ 12.6 dB (6.3 dB/bit)
- En régime de haute résolution, le gain théorique de ~6 dB/bit est bien respecté

### Question 1.4 — Effet d'une résolution de 1 bit/échantillon

**Consigne:** Que se passe-t-il quand la résolution du quantificateur devient 1 bit/éch ?

**Résultat:**

- **SNR PCM 1 bit: 6.03 dB**

**Explication:**

- Avec une résolution de 1 bit, la quantification se réduit au codage du signe de chaque échantillon (±1)
- Le signal reconstruit ne peut prendre que deux valeurs discrètes
- La forme sinusoïdale originale est complètement détruite, le signal devient une onde carrée
- La distortion est extrêmement importante, d'où un SNR très faible (~6 dB)
- La qualité audio perçue est désastreuse: le son devient totalement distordu et non reconnaissable

---

## PARTIE 2 — Analyse du DPCM et impact des erreurs de transmission

### Contexte

On simule maintenant un codeur DPCM (Differential Pulse Code Modulation) linéaire avec une résolution R bits/échantillon. Le principe consiste à quantifier les **différences** entre deux échantillons consécutifs plutôt que les valeurs absolues.

**Description du codeur DPCM utilisé:**

- Prédicteur: ordre 1 simple (prédiction = valeur précédente)
- Résolution des différences: R = 8 bits
- Quantification uniforme des différences

### Question 2.1 — Comportement en présence d'erreurs aléatoires

**Consigne:** Comment se porte ce codeur si on est en présence d'erreurs aléatoires avec un taux d'erreur p=10⁻² et p=10⁻³. Conclusions ?

**Résultats expérimentaux:**

**Sans erreurs de transmission:**

- SNR DPCM R=8: **48.69 dB**
- SNR PCM 8 bits: **49.79 dB**

**Avec erreurs binaires (modèle BSC: Binary Symmetric Channel):**

| Codeur     | Taux d'erreur p  | SNR (dB)      |
| ---------- | ---------------- | ------------- |
| PCM 8 bits | p = 0.01 (10⁻²)  | **14.74 dB**  |
| PCM 8 bits | p = 0.001 (10⁻³) | **24.41 dB**  |
| DPCM R=8   | p = 0.001 (10⁻³) | **-12.42 dB** |

**Analyse et conclusions:**

1. **Sans erreurs:** Le DPCM R=8 offre une performance comparable au PCM 8 bits (48.69 dB vs 49.79 dB) pour le signal sinusoïdal testé.

2. **Avec erreurs - PCM:**

   - Le PCM se dégrade significativement mais reste utilisable
   - À p=0.01: SNR chute de 49.79 dB → 14.74 dB (dégradation de 35 dB)
   - À p=0.001: SNR = 24.41 dB (dégradation de 25 dB)
   - L'effet des erreurs est **localisé**: une erreur sur un bit n'affecte qu'un seul échantillon

3. **Avec erreurs - DPCM:**

   - Le DPCM devient **extrêmement vulnérable** aux erreurs de transmission
   - À p=0.001: SNR = **-12.42 dB** (SNR négatif!)
   - Un SNR négatif signifie que l'erreur quadratique moyenne est **supérieure à la puissance du signal** reconstruit → le signal est totalement détruit

4. **Mécanisme de propagation d'erreur dans le DPCM:**
   - Une erreur sur un bit codant une différence introduit une erreur sur la valeur reconstruite à l'instant t
   - La prédiction suivante (instant t+1) s'appuie sur cette valeur erronée
   - L'erreur se **propage** aux échantillons suivants (effet d'avalanche/accumulation)
   - Contrairement au PCM, l'erreur n'est pas confinée à un seul échantillon

**Conclusion générale:**

- Le **DPCM sans protection est inadapté** pour les canaux bruités ou peu fiables
- Le PCM est beaucoup plus robuste aux erreurs de transmission car les erreurs restent localisées
- Pour utiliser DPCM sur un réseau avec erreurs, il est **impératif** de mettre en place des mécanismes de protection

### Question 2.2 — Quantification de la voix "Xtine" en 8 bits

**Consigne:** Quantifier la voix de Xtine en utilisant une résolution de 8 bits/éch. Que se passe-t-il si on a un taux d'erreur binaire p=10⁻²?

**Réponse:**

_Note: La simulation réalisée utilisait un signal sinusoïdal de référence. Aucune piste audio nommée "Xtine" n'était disponible dans les fichiers fournis. Néanmoins, nous pouvons extrapoler les résultats:_

**Pour PCM 8 bits sur signal vocal:**

- Si la voix est normalisée sur [-1, +1], on s'attend à un SNR similaire au signal sinusoïdal en l'absence d'erreurs (≈ 45-50 dB selon le contenu)
- La parole contient des composantes spectrales plus complexes (transitoires, silences, consonnes), donc le SNR effectif peut varier
- **Avec p = 10⁻²:** En se basant sur les résultats du sinus, le SNR chuterait à environ **14-15 dB**
  - Le signal vocal resterait partiellement intelligible mais avec de nombreux artefacts audibles (clics, pops, distorsions)
  - Qualité perçue: médiocre à mauvaise

**Pour DPCM R=8 sur signal vocal:**

- Sans erreurs: performance comparable au PCM 8 bits
- **Avec p = 10⁻²:** Compte tenu de la propagation d'erreur observée (SNR négatif même à p=10⁻³), le signal vocal serait **complètement détruit**
  - Accumulation des erreurs rendrait le signal inintelligible
  - Effet catastrophique comparé au PCM

---

## Recommandations pratiques

Pour utiliser DPCM sur des canaux avec erreurs, il est nécessaire de mettre en place des mécanismes de protection:

1. **Codes correcteurs d'erreurs (FEC):**

   - Codes de Reed-Solomon, codes convolutifs, Turbo codes, LDPC
   - Permettent de détecter et corriger les erreurs avant décodage

2. **Contrôle d'intégrité + retransmission (ARQ):**

   - CRC (Cyclic Redundancy Check) + demande de retransmission
   - Adapté aux canaux bidirectionnels

3. **Techniques spécifiques DPCM:**

   - **Points de rafraîchissement périodiques:** insérer des trames PCM complètes (non différentielles) pour resynchroniser
   - **Limitation de la propagation:** prédicteurs adaptatifs avec réinitialisation
   - **Codage hybride:** alterner trames intra-codées (PCM) et inter-codées (DPCM)

4. **Fragmentation et resynchronisation:**
   - Découper le flux en trames indépendantes avec en-têtes de synchronisation

---

## Procédure pour reproduire les mesures

### Dans PowerShell:

```powershell
cd "c:\Users\chaussoc\Desktop\-EMA-A2-COURS\TECHNOLOGIE DES MEDIAS\Projet_MMT"
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
python simulation.py
```

### Fichiers générés

- `pcm_8bits.wav`, `pcm_6bits.wav`, `pcm_4bits.wav`, `pcm_3bits.wav`, `pcm_2bits.wav`, `pcm_1bits.wav`
- `dpcm_R8.wav`

Ces fichiers WAV peuvent être lus directement sur les haut-parleurs pour évaluer la qualité audio perçue.

---

## Limitations et perspectives

1. **Prédicteur simple:**

   - Le prédicteur DPCM utilisé est d'ordre 1 (très basique)
   - Des prédicteurs d'ordre supérieur ou adaptatifs amélioreraient les performances en l'absence d'erreurs

2. **Signal de test:**

   - Les mesures SNR proviennent d'un signal sinusoïdal pur
   - Pour la parole ou la musique complexe, les valeurs numériques absolues changeront
   - Néanmoins, les **tendances et conclusions qualitatives** restent valables

3. **Modèle d'erreurs:**
   - Le modèle BSC (Binary Symmetric Channel) est simplifié
   - Les canaux réels peuvent présenter des erreurs en rafales (burst errors) qui seraient encore plus dévastateurs pour DPCM

---

**Fin du rapport**
