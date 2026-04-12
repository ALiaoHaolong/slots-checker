---
title: 介绍
order: 0
---

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">

# Slots Checker

<img src="/icon.png" alt="Logo" width="128" height="128"/>

![Minecraft Support](https://img.shields.io/badge/SUPPORT_FOR_MC-1.18_~_26.1.1-11304B?style=for-the-badge&labelColor=D0D5DA)

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/1wrPNL41?style=flat-square&logo=modrinth&labelColor=D0D5DA&color=00af5c)](https://modrinth.com/mod/slots-checker)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1489337?style=flat-square&logo=curseforge&labelColor=D0D5DA&color=F16436)](https://www.curseforge.com/minecraft/mc-mods/slots-checker)
[![Modrinth Version](https://img.shields.io/modrinth/v/1wrPNL41?style=flat-square&logo=github&logoColor=010409&labelColor=D0D5DA&color=010409)](https://github.com/ALiaoHaolong/slots-checker)

[![MCMOD](https://img.shields.io/badge/MCMOD-D0D5DA?style=for-the-badge)](https://www.mcmod.cn/class/8936.html)
[![Homepage](https://img.shields.io/badge/HOMEPAGE-D0D5DA?style=for-the-badge)](https://aliaohaolong.github.io/slots-checker/zh)
[![Issues](https://img.shields.io/badge/ISSUES-D0D5DA?style=for-the-badge)](https://github.com/ALiaoHaolong/slots-checker/issues)

**🌍 [English](/guide/introduction) • [简体中文](/zh/guide/introduction)**

</div>

---

这个模组提供了一个命令，可查看和修改任何玩家的背包、快捷栏、末影箱、护甲以及副手格子（需要 4 级权限等级）。

客户端安装后可提供游戏内界面翻译。

支持 en_us、zh_cn、zh_hk 和 zh_tw 语言。

注意事项：

- **服务端必装，客户端选装。**
- 暂**不支持**查看和修改离线玩家的数据（离线玩家指的是当前不在游戏内的玩家，而不是指通过离线登陆方式进入游戏的玩家）。
- [创造模式背包问题](/zh/guide/creative-inventory-problem)。

## 命令

1. `/slots-checker inventory <玩家>`  
   打开玩家的背包界面。  
   <img src="/images/inventory.png" alt="Inventory" width="400"/>

2. `/slots-checker hotbar <玩家>`  
   打开玩家的快捷栏界面。  
   <img src="/images/hotbar.png" alt="Hotbar" width="400"/>

3. `/slots-checker ender <玩家>`  
   打开玩家的末影箱界面。  
   <img src="/images/ender.png" alt="Ender Chest" width="400"/>

4. `/slots-checker armor <玩家>`  
   打开玩家的盔甲槽界面。  
   前 4 个栏位依次为：头盔、胸甲、护腿、靴子；后 5 个栏位无效。  
   此界面没有物品限制，任意物品均可直接放入盔甲栏位。  
   <img src="/images/armor.png" alt="Armor" width="400"/>

5. `/slots-checker offhand <玩家>`  
   打开玩家的副手槽界面。  
   后 8 个栏位无效。  
   <img src="/images/offhand.png" alt="Offhand" width="400"/>

## 开源协议

- 当前版本（v2 起）：开源，采用 <a href="https://github.com/ALiaoHaolong/slots-checker/blob/master/LICENSE" target="_blank" rel="noreferrer" class="vp-external-link-icon">Apache License 2.0</a> 协议。
- 历史版本（v1 及之前）：闭源开发，曾声明 MIT 协议但未公开源码。
