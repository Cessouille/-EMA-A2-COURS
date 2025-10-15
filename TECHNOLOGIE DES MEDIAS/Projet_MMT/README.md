Projet MMT — Simulation PCM / DPCM

But: simuler un codeur PCM linéaire et un codeur DPCM linéaire, mesurer le SNR et observer l'effet d'erreurs binaires.

Fichiers:

- `simulation.py` : script principal.
- `requirements.txt` : dépendances.

Essai rapide:

# Créer un env virtuel (Windows PowerShell)

py -m venv .venv; .\.venv\Scripts\Activate.ps1
pip install -r requirements.txt

# Lancer la simulation

py simulation.py
