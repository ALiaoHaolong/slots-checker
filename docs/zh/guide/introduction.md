---
title: 介绍
order: 0
---

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">

# Slots Checker

<img src="/icon.png" alt="Logo" width="128" height="128"/>

![Minecraft Support](https://img.shields.io/badge/SUPPORT_FOR_MC-1.18_~_26.3-11304B?style=for-the-badge&labelColor=D0D5DA)

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/1wrPNL41?style=flat-square&logo=modrinth&labelColor=D0D5DA&color=00af5c)](https://modrinth.com/mod/slots-checker)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1489337?style=flat-square&logo=curseforge&labelColor=D0D5DA&color=F16436)](https://www.curseforge.com/minecraft/mc-mods/slots-checker)
[![Modrinth Version](https://img.shields.io/modrinth/v/1wrPNL41?style=flat-square&logo=github&logoColor=010409&labelColor=D0D5DA&color=010409)](https://github.com/ALiaoHaolong/slots-checker)

[![MCMOD](https://img.shields.io/badge/MCMOD-D0D5DA?style=for-the-badge)](https://www.mcmod.cn/class/8936.html)
[![Homepage](https://img.shields.io/badge/HOMEPAGE-D0D5DA?style=for-the-badge)](https://aliaohaolong.github.io/slots-checker/zh)
[![Issues](https://img.shields.io/badge/ISSUES-D0D5DA?style=for-the-badge)](https://github.com/ALiaoHaolong/slots-checker/issues)

**🌍 [English](/guide/introduction) • [简体中文](/zh/guide/introduction)**

</div>

---

管理员可以通过命令查看或编辑任意玩家的背包、快捷栏、末影箱、护甲以及副手格子。

客户端安装后可提供游戏内界面翻译。

支持 en_us、zh_cn、zh_hk 和 zh_tw 语言。

注意事项：

- **服务端必装，客户端选装。**
- 暂**不支持**查看和编辑离线玩家的数据（离线玩家指的是当前不在游戏内的玩家，而不是指通过离线登陆方式进入游戏的玩家）。
- [创造模式背包问题](/zh/notices/creative-inventory-problem)。
- [死亡玩家背包问题](/zh/notices/dying-player-inventory-problem)。

## 命令

语法：`/slots-checker armor|ender|hotbar|inventory|offhand <玩家> [view|edit]`

- 子命令与目标界面的对应关系：`armor` 护甲槽、`ender` 末影箱、`hotbar` 快捷栏、`inventory` 背包、`offhand` 副手槽。
- `<玩家>` 为必填参数，指定要操作的目标玩家。
- `[view|edit]` 为可选参数，省略时默认为 `view`（查看模式）。`view`（查看模式）需要 2 级命令权限，`edit`（编辑模式）需要 4 级命令权限。

示例：

```command
/slots-checker inventory foo       # 以查看模式打开 foo 的背包
/slots-checker ender foo view      # 以查看模式打开 foo 的末影箱
/slots-checker inventory foo edit  # 以编辑模式打开 foo 的背包
```

## 效果展示

**查看 An_Night 的盔甲槽**：`/slots-checker armor An_Night`

- 前 4 个栏位依次为：头盔、胸甲、护腿、靴子；后 5 个栏位无效。
- 此界面没有物品限制，任意物品均可直接放入盔甲栏位。

<img src="/images/armor-view.zh-CN.png" alt="Armor" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**查看 An_Night 的末影箱**：`/slots-checker ender An_Night`

<img src="/images/ender-view.zh-CN.png" alt="Ender Chest" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**查看 An_Night 的快捷栏**：`/slots-checker hotbar An_Night`

<img src="/images/hotbar-view.zh-CN.png" alt="Hotbar" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**编辑 An_Night 的背包**：`/slots-checker inventory An_Night edit`

<img src="/images/inventory-edit.zh-CN.png" alt="Inventory" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

---

**编辑 An_Night 的副手槽**：`/slots-checker offhand An_Night edit`

- 后 8 个栏位无效。

<img src="/images/offhand-edit.zh-CN.png" alt="Offhand" width="400" style="border: 2px solid #000000; border-radius: 8px;"/>

## 开源协议

- 当前版本（v2 起）：开源，采用 <a href="https://github.com/ALiaoHaolong/slots-checker/blob/master/LICENSE" target="_blank" rel="noreferrer" class="vp-external-link-icon">Apache License 2.0</a> 协议。
- 历史版本（v1 及之前）：闭源开发，曾声明 MIT 协议但未公开源码。
