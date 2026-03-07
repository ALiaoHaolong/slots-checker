# Slots Checker

![](icon.png)

[[中文]](Readme.zh-CN.md)

Modrinth:  
[Slots Checker - Minecraft Mod](https://modrinth.com/mod/slots-checker)

Mod 百科:  
[Slots Checker - MC百科|最大的Minecraft中文MOD百科](https://www.mcmod.cn/class/8936.html)

Source code:  
[Gitee.com - 暗夜/Slots Checker](https://gitee.com/AnNight/slots-checker)

Issues:  
[Issues · 暗夜/Slots Checker - Gitee.com](https://gitee.com/AnNight/slots-checker/issues)

Wiki:  
[Wiki - Gitee.com](https://gitee.com/AnNight/slots-checker/wikis)

## Introduction

This mod provides a command to view and modify any player's inventory, hotbar, ender chest, armor, and offhand slots (requires level 4 admin privileges).

When installed on the client, it provides in-game interface translations.

Supports en_us, zh_cn, zh_hk, and zh_tw languages.

Important:

- **Required on the server, optional on the client.**
- For the time being, it is **not supported** to view or modify the data of offline players (offline players refer to those who are not currently online, not to players who log in via offline mode).
- [Creative inventory problem](CreativeInventoryProblem.md).

## Commands

1. `/slots-checker inventory <player>`  
   Open the player's inventory interface.  
   ![](doc/inventory.png)

2. `/slots-checker hotbar <player>`  
   Open the player's hotbar interface.  
   ![](doc/hotbar.png)

3. `/slots-checker ender <player>`  
   Open the player's ender chest interface.  
   ![](doc/ender.png)

4. `/slots-checker armor <player>`  
   Open the player's armor interface.  
   The first four columns are in sequence: helmets, chestplates, leggings, and boots. The last five fields are invalid.  
   This interface has no item restrictions. Any item can be directly placed in the armor slot.  
   ![](doc/armor.png)

5. `/slots-checker offhand <player>`  
   Open the player's offhand interface.  
   The last 8 fields are invalid.  
   ![](doc/offhand.png)

## Author

廖浩龙

- aliaohaolong@qq.com
- aliaohaolong@gmail.com

## License

- Current version (v2 and later): Open source, licensed under the [Apache License 2.0](LICENSE).
- Historical versions (v1 and earlier): Closed-source development; previously declared under the MIT License but source code was not publicly released.
