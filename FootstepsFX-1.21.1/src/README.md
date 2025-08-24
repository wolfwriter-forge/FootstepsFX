# FootstepsFX - Particle trails for your server

**Autor:** wolfwriter \
**Version:** FootstepsFX-1.0-1.21.1 \
**Lizenz:** MIT (adapted for plugins)

## What is FootstepsFX?
FootstepsFX brings movement to your Minecraft server. Players leave a configurable particle trail behind them as they run – from smoke and flames to hearts or musical notes.

## Features
- Can be turned on/off for each player
- Choice of many particle effects (`flame`, `smoke`, `heart`, `note`, ...)
- Adjustable intensity (1-100 particles per step)
- Lag-friendly: effects only when changing blocks
- Storage in JSON: settings are retained after restarts
- Easy to use with clear commands

## Commands
| Befehlt                   | Beschreibung                |
|---------------------------|-----------------------------|
| `/trailon`                | Activates the trail         |
| `trailoff`                | Deactivates the trail       |
| `/trailtype <effekt>`     | Sets the effect type        |
| `/trailintensity <1-100>` | Sets the particle amount    |
| `/trailtypelist`          | Shows all available effects |

All commands require the `trail.use` permission

## Storage
Player settings are stored in `plugins/FootstepsFXfootstepsfx_playerdate.json`:

`````json
{
    “uuid-of-the-player”: {
      “enabled”: true / false,
      “effectType”: “effect name”
      “intensity”: number
    },
    [...]
}