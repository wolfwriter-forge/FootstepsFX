# FootstepsFX - Particle trails for your server

**Autor:** wolfwriter \
**Version:** FootstepsFX-1.1-1.21.1 \
**Licence:** GNU General Public License version 3 (GPLv3)

## What is FootstepsFX?
FootstepsFX brings movement to your Minecraft server. Players leave a configurable particle trail behind them as they run – from smoke and flames to hearts or musical notes.

## Features
- Ein/Aus pro Spieler
- Effekt‑Typ pro Spieler (z. B. flame, smoke, love hearts, …)
- Intensität (Anzahl Partikel je Schritt)
- Lastschonung: Partikel nur beim Wechsel in einen neuen Block
- Effekt-Sperre pro Spieler: Einzelne Effekte sperren oder freigeben (`/traillock effect` , `/trailunlock effect`)
- Globale Sperre/Freigabe aller Effekte via `*` oder `all`
- Effektverwaltung für andere Spieler: Admins können Effekte für andere Spieler sperren/freigeben
- Tab-Completion für alle Befehle: Dynamische Vorschläge für Effekte, Spieler, Optionen (`effect, player`, `*`, `all`)
- Hilfebefehl: `/trail help` zeigt alle verfügbaren Befehle und deren Beschreibung
- Reload-Funktion: `/trail reload` lädt gespeicherte Daten neu (z. B. nach Dateiänderung)
- Effekt-Validierung: Ungültige Effekt-Namen werden erkannt und mit Hinweis abgefange

## Commands
| Commands                                      | Description                 |
|-----------------------------------------------|-----------------------------|
| `/trail on`                                   | Activates the trail         |
| `trail off`                                   | Deactivates the trail       |
| `/trail effect <effect>`                      | Sets the effect type        |
| `/trail intensity <1-100>`                    | Sets the particle amount    |
| `/trail list [page]`                          | Shows all available effects              | 
| `/trail help`                                 | Displays all available trail commands    | 
| `/trail lock effect <effect>`                 | Locks a specific effect for yourself     | 
| `/trail unlock effect <effect>`               | Unlocks a specific effect for yourself   | 
| `/trail lock effect all` or `*`               | Locks all effects for yourself           |
| `/trail unlock effect all` or `*`             | Unlocks all effects for yourself         | 
| `/trail lock player <name> effect <effect>`   | Locks an effect for another player       | 
| `/trail unlock player <name> effect <effect>` | Unlocks an effect for another player     | 
| `/trail reload`                               | Reloads all saved trail data             | 

- `trail.use`  
  Grants access to all basic trail features: enabling/disabling trails, setting effect type and intensity, viewing available effects, and accessing help.

- `trail.lock`  
  Allows a player to lock specific effects for themselves, including locking all effects via `*` or `all`.

- `trail.unlock`  
  Allows a player to unlock specific effects for themselves, including unlocking all effects via `*` or `all`.

- `trail.reload`  
  Grants access to administrative commands such as `/trail reload`, which reloads all saved trail data from disk.

## Storage
Player settings are stored in `plugins/FootstepsFXfootstepsfx_playerdate.json`:

`````json
{
  "uuid-of-player": {
    "enabled": true,
    "effectType": "flame",
    "intensity": 8
    “lockedEffect”: [],
    “unlockedEffects”: []
  }
}
