## Analyse du PCM

a. Créer une tonalité sinusoïdale de fréquence f = 2kHz, de 3 secondes de durée, en utilisant 10 échantillons (en float) par période. Reproduire cette tonalité sur les haut-parleurs de votre ordinateur.

b. Quantifier ce signal en (int) à 8 bits/ech.

Ecouter fichier [pcm_8bits.wav](PCM/pcm_8bits.wav)

c. Quantifier ce signal en utilisant une résolution de 6 bits/ech, de 4 bits/ech, de 3 bits/ech et de 2 bits/ech.

- Ecouter fichier [pcm_6bits.wav](PCM/pcm_6bits.wav)
- Ecouter fichier [pcm_4bits.wav](PCM/pcm_4bits.wav)
- Ecouter fichier [pcm_3bits.wav](PCM/pcm_3bits.wav)
- Ecouter fichier [pcm_2bits.wav](PCM/pcm_2bits.wav)

d. Que se passe-t-il quand la résolution du quantificateur devient 1 bit/ech ?

Ecouter fichier [pcm_1bits.wav](PCM/pcm_1bits.wav)

**Analyse :** À 1 bit/échantillon, le quantificateur ne conserve que le signe du signal (positif ou négatif). Le signal reconstruit est une onde carrée alternant entre +1 et -1. Le son devient très mauvais, limite impossible à écouter, car seule la fréquence fondamentale est partiellement préservée avec de nombreuses harmoniques parasites. Le SNR est minimal.

**Graphiques de comparaison :**

- [pcm_comparison.png](PCM/pcm_comparison.png) - Comparaisons individuelles pour chaque résolution
- [pcm_all_comparison.png](PCM/pcm_all_comparison.png) - Superposition de tous les niveaux

**Conclusion PCM :** La résolution de quantification a un impact direct sur la qualité. Le SNR diminue d'environ 6 dB par bit perdu. Pour une qualité acceptable, un minimum de 4-6 bits est nécessaire, tandis que 8 bits garantissent une excellente qualité.

## Analyse du DPCM

1. Comment se porte ce codeur si on est en présence d'erreurs aléatoires avec un taux d'erreur p = 10^-2 et p = 10^-3. Conclusions ?

**Configuration :** Signal sinusoïdal 2 kHz, résolution R = 8 bits

- Ecouter fichier [dpcm_R8_errors_p1e_02.wav](DPCM/dpcm_R8_errors_p1e_02.wav)
- Ecouter fichier [dpcm_R8_errors_p1e_03.wav](DPCM/dpcm_R8_errors_p1e_03.wav)

**Analyse des résultats :**

- **Avec p = 10^-2 :** Le SNR chute fortement. Des artefacts sonores apparaissent. Le signal reste reconnaissable mais la qualité est dégradée de manière notable.

- **Avec p = 10^-3 :** La qualité reste correcte avec un SNR nettement meilleur. Les erreurs sont moins perceptibles et espacées. Le signal est proche de la qualité sans erreur.

**Graphique comparatif :** [dpcm_comparison.png](DPCM/dpcm_comparison.png)

- Graphique 1: Signal original
- Graphique 2: DPCM avec p = 10^-2
- Graphique 3: DPCM avec p = 10^-3

**Conclusions :**

- Le DPCM est sensible aux erreurs binaires car chaque échantillon décodé dépend du précédent
- Notre implémentation utilise le signal original pour éviter l'accumulation catastrophique des erreurs (réinitialisation du prédicteur)
- Comparé au PCM, le DPCM nécessite des mécanismes de protection plus robustes (codes correcteurs, réinitialisation périodique)
- Pour p < 10^-3, le système reste acceptable ; au-delà de 10^-2, la qualité se dégrade rapidement

2. Quantifier la voix de Xtine en utilisant une résolution de 8 bits/ech. Que se passe-t-il si on a un taux d'erreur binaire p = 10^-2 ?

**Configuration :** Signal vocal "XTINE", résolution R = 8 bits, taux d'erreur p = 10^-2

Ecouter fichier [xtine_dpcm_R8_errors_p1e_02.wav](XTINE_DPCM/xtine_dpcm_R8_errors_p1e_02.wav)

**Analyse :**

- **Signal sans erreur :** Le DPCM à 8 bits reconstruit le signal vocal avec une qualité élevée. Le SNR est élevé et la parole reste naturelle.

- **Avec erreurs p = 10^-2 :**
  - La qualité vocale reste intelligible
  - Présence de clics ou artefacts audibles aux points d'erreur
  - La voix conserve son timbre général
  - Les erreurs sont perceptibles mais n'empêchent pas la compréhension

**Graphique comparatif :** [xtine_dpcm_comparison.png](XTINE_DPCM/xtine_dpcm_comparison.png)

- Graphique 1: Signal XTINE original
- Graphique 2: DPCM R=8 sans erreurs
- Graphique 3: DPCM R=8 avec erreurs p = 10^-2

**Comparaison avec le signal sinusoïdal :**

- Le signal vocal est plus complexe (non périodique, large bande spectrale)
- Les erreurs sont potentiellement moins perceptibles car masquées par le contenu vocal
- L'oreille humaine est plus tolérante aux artefacts dans la parole que dans les tons purs

**Conclusion :** Le DPCM à 8 bits est adapté pour la compression de la parole, mais un taux d'erreur de 10^-2 introduit des dégradations audibles. Pour des applications critiques (téléphonie, streaming), un taux d'erreur < 10^-3 est recommandé, avec des mécanismes de protection (codes correcteurs d'erreurs, réinitialisation périodique du prédicteur).

---

## Synthèse générale

**PCM (Pulse Code Modulation) :**

- Simple et robuste
- Qualité directement liée au nombre de bits
- Indépendance entre échantillons = excellente résistance aux erreurs
- Débit élevé (pas de compression)

**DPCM (Differential PCM) :**

- Exploite la corrélation temporelle du signal
- Débit réduit par rapport au PCM
- Sensible aux erreurs de transmission sans protection adéquate
- Nécessite des stratégies de mitigation des erreurs

Le choix entre PCM et DPCM dépend du contexte : qualité requise, fiabilité du canal de transmission, ressources disponibles (bande passante), et nature du signal (parole, musique, données).
