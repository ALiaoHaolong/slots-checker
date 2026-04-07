## Introduction

This mod provides a command to view and modify any player's inventory, hotbar, ender chest, armor, and offhand slots (requires level 4 admin privileges).

When installed on the client, it provides in-game interface translations.

Supports en_us, zh_cn, zh_hk, and zh_tw languages.

Important:

- **Required on the server, optional on the client.**
- For the time being, it is **not supported** to view or modify the data of offline players (offline players refer to those who are not currently online, not to players who log in via offline mode).
- [Creative inventory problem](https://github.com/ALiaoHaolong/slots-checker/blob/master/docs/CreativeInventoryProblem.md).

## Commands

1. `/slots-checker inventory <player>`  
   Open the player's inventory interface.  
   <img src="https://cdn-alt.modrinth.com/data/1wrPNL41/images/3de161eb685822ed53beb4bfd64ddd6064d92c50.png" alt="Inventory" width="400"/>

2. `/slots-checker hotbar <player>`  
   Open the player's hotbar interface.  
   <img src="https://cdn-alt.modrinth.com/data/1wrPNL41/images/38068888fd0a9e0641ef362b563743874a92f8f3.png" alt="Hotbar" width="400"/>

3. `/slots-checker ender <player>`  
   Open the player's ender chest interface.  
   <img src="https://cdn-alt.modrinth.com/data/1wrPNL41/images/14300946feb327cfaf9b554e9f7d7e52079038c0.png" alt="Ender Chest" width="400"/>

4. `/slots-checker armor <player>`  
   Open the player's armor interface.  
   The first four columns are in sequence: helmets, chestplates, leggings, and boots. The last five fields are invalid.  
   This interface has no item restrictions. Any item can be directly placed in the armor slot.  
   <img src="https://cdn-alt.modrinth.com/data/1wrPNL41/images/9048d5224911e9177919fecf555564a3fcac0c23.png" alt="Armor" width="400"/>

5. `/slots-checker offhand <player>`  
   Open the player's offhand interface.  
   The last 8 fields are invalid.  
   <img src="https://cdn-alt.modrinth.com/data/1wrPNL41/images/2ca9a242d457e38d36103ae5c5b156ac98f18898.png" alt="Offhand" width="400"/>
