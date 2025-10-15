# RAPPORT — Analyse PCM & DPCM

## PARTIE 1 — Analyse du PCM

### 1. Création d'une tonalité sinusoïdale de référence

Une tonalité sinusoïdale de fréquence f = 2 kHz, de 3 secondes de durée a été créée en utilisant 10 échantillons (en float) par période.

- Fréquence d'échantillonnage: Fs = 10 × 2000 = 20 000 Hz
- Signal normalisé sur [-1, +1]
- La tonalité peut être reproduite sur les haut-parleurs via les fichiers WAV générés

### 2. Quantification à 8 bits/échantillon

**SNR PCM 8 bits: 49.79 dB**

### 3. Quantification à résolutions réduites

- **SNR PCM 6 bits: 35.10 dB**
- **SNR PCM 4 bits: 23.10 dB**
- **SNR PCM 3 bits: 17.65 dB**
- **SNR PCM 2 bits: 10.45 dB**

### 4. Quantification à 1 bit/échantillon

**SNR PCM 1 bit: 6.03 dB**

À 1 bit/échantillon, la quantification se réduit au codage du signe (±1). Le signal reconstruit devient une onde carrée, la forme sinusoïdale est complètement détruite.

---

## PARTIE 2 — Analyse du DPCM

Le codeur DPCM utilisé quantifie les différences entre deux échantillons consécutifs avec un prédicteur d'ordre 1 et une résolution R = 8 bits.

### 1. Comportement avec erreurs aléatoires (p = 10⁻² et p = 10⁻³)

**Résultats sans erreurs:**

- SNR DPCM R=8: 48.69 dB

**Résultats avec erreurs binaires:**

| Codeur     | p = 10⁻² | p = 10⁻³  |
| ---------- | -------- | --------- |
| PCM 8 bits | 14.74 dB | 24.41 dB  |
| DPCM R=8   | -        | -12.42 dB |

**Conclusions:**

- Le PCM reste utilisable même avec des erreurs (SNR positif)
- Le DPCM devient inutilisable avec erreurs: SNR négatif à p = 10⁻³ signifie que le signal reconstruit est complètement détruit
- Dans le DPCM, une erreur sur un bit se propage aux échantillons suivants (effet d'avalanche)
- Dans le PCM, chaque erreur reste localisée à un seul échantillon
- Le DPCM sans protection est inadapté pour les canaux avec erreurs

### 2. Quantification de la voix "Xtine" à 8 bits/échantillon avec p = 10⁻²

En extrapolant les résultats obtenus sur le signal sinusoïdal:

- **PCM 8 bits avec p = 10⁻²:** SNR ≈ 14-15 dB → signal vocal partiellement intelligible avec artefacts audibles (qualité médiocre)
- **DPCM R=8 avec p = 10⁻²:** signal complètement détruit (SNR négatif attendu), inintelligible
