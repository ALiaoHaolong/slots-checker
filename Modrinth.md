<!--suppress HtmlDeprecatedAttribute -->
<div align="center">

# Slots Checker

<img src="https://cdn.jsdelivr.net/gh/ALiaoHaolong/slots-checker@master/icon.png" alt="Logo" width="128" height="128"/>

![Minecraft Support](https://img.shields.io/badge/SUPPORT_FOR_MC-1.18_~_26.3-11304B?style=for-the-badge&labelColor=D0D5DA)

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/1wrPNL41?style=flat-square&logo=modrinth&labelColor=D0D5DA&color=00af5c)](https://modrinth.com/mod/slots-checker)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1489337?style=flat-square&logo=curseforge&labelColor=D0D5DA&color=F16436)](https://www.curseforge.com/minecraft/mc-mods/slots-checker)
[![Modrinth Version](https://img.shields.io/modrinth/v/1wrPNL41?style=flat-square&logo=github&logoColor=010409&labelColor=D0D5DA&color=010409)](https://github.com/ALiaoHaolong/slots-checker)

[![MCMOD](https://img.shields.io/badge/MCMOD-D0D5DA?style=for-the-badge)](https://www.mcmod.cn/class/8936.html)
[![Homepage](https://img.shields.io/badge/HOMEPAGE-D0D5DA?style=for-the-badge)](https://aliaohaolong.github.io/slots-checker)
[![Issues](https://img.shields.io/badge/ISSUES-D0D5DA?style=for-the-badge)](https://github.com/ALiaoHaolong/slots-checker/issues)

</div>

---

The admin can view or edit any player's inventory, hotbar, ender chest, armor, and offhand slots by command.

When installed on the client, it provides in-game interface translations.

Supports en_us, zh_cn, zh_hk, and zh_tw languages.

Important:

- **Required on the server, optional on the client.**
- For the time being, it is **not supported** to view or modify the data of offline players (offline players refer to those who are not currently online, not to players who log in via offline mode).
- [Creative inventory problem](https://aliaohaolong.github.io/slots-checker/notices/creative-inventory-problem.html).
- [Dying player inventory problem](https://aliaohaolong.github.io/slots-checker/notices/dying-player-inventory-problem.html)

## Commands

Grammar: `/slots-checker armor|ender|hotbar|inventory|offhand <player> [view|edit]`

- `<player>` is a required parameter, specifying the target player to be operated on.
- `[view|edit]` is an optional parameter, defaulting to `view` (view mode) when omitted. `view` (view mode) requires 2-level command permission, and `edit` (edit mode) requires 4-level command permission.

Examples:

```command
/slots-checker inventory foo       # Open foo's inventory in view mode
/slots-checker ender foo view      # Open foo's ender chest in view mode
/slots-checker inventory foo edit  # Open foo's inventory in edit mode
```

## In Action

**View An_Night's armor**: `/slots-checker armor An_Night`

- The first four columns are in sequence: helmets, chestplates, leggings, and boots. The last five fields are invalid.
- This interface has no item restrictions. Any item can be directly placed in the armor slot.

<img src="https://cdn.jsdelivr.net/gh/ALiaoHaolong/slots-checker@master/docs/public/images/armor-view.png" alt="Armor" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**View An_Night's ender chest**: `/slots-checker ender An_Night`

<img src="https://cdn.jsdelivr.net/gh/ALiaoHaolong/slots-checker@master/docs/public/images/ender-view.png" alt="Ender Chest" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**View An_Night's hotbar**: `/slots-checker hotbar An_Night`

<img src="https://cdn.jsdelivr.net/gh/ALiaoHaolong/slots-checker@master/docs/public/images/hotbar-view.png" alt="Hotbar" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**View An_Night's inventory**: `/slots-checker inventory An_Night`

<img src="https://cdn.jsdelivr.net/gh/ALiaoHaolong/slots-checker@master/docs/public/images/inventory-edit.png" alt="Inventory" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**View An_Night's offhand**: `/slots-checker offhand An_Night`

- The last 8 fields are invalid.

<img src="https://cdn.jsdelivr.net/gh/ALiaoHaolong/slots-checker@master/docs/public/images/offhand-edit.png" alt="Offhand" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

## License

- Current version (v2 and later): Open source, licensed under the [Apache License 2.0](https://github.com/ALiaoHaolong/slots-checker/blob/master/LICENSE).
- Historical versions (v1 and earlier): Closed-source development; previously declared under the MIT License but source code was not publicly released.
