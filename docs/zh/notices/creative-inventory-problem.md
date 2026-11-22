---
title: 创造模式背包问题
order: 2
---

# 创造模式背包问题

廖浩龙 2026.3.2

## 影响版本

自本模组适配版本起（1.18）（至少）。  
24w33a（1.21.2 的开发版本）中被修复。

## 分析

创造模式背包 GUI 具有其独特的设计：在客户端中，除了 player\$inventory 中保存了玩家的背包数据，在打开的创造背包 GUI（CreativeInventoryScreen\$CreativeScreenHandler）中同样存在着一份来自变量 trackedStacks 的背包数据；此外，该 GUI 显示的数据来自 player\$inventory，而控制客户端是否向服务端同步背包变化的则是 trackedStacks。

由于 CreativeScreenHandler 中缺少代码来同步初始化后从 player\$inventory 到 trackedStacks 的变化，导致玩家在打开创造模式背包 GUI 之后（即，游戏新建一个 CreativeScreenHandler 实例，并从 player\$inventory 中初始化数据到 trackedStacks 之后），如果通过其他方式修改其背包（无论是本模组还是/item命令等方式），并由服务器同步到客户端的 player\$inventory 中之后，trackedStacks 中并未更新。

假设该其他方式向一个空槽位（设 container.9）中设置了新物品（设 minecraft:stone），此时该玩家客户端的 player\$inventory 受服务器同步，其中的 container.9 中变为 minecraft:stone。而 trackedStacks 缺乏同步，其中的 container.9 中仍为 ItemStack.EMPTY。由于显示的数据来自 player\$inventory，因此该玩家客户端此时的显示并没有问题。

但是当该玩家点击该槽位尝试取出或快捷移动时，客户端首先尝试从 player\$inventory 的 container.9 中取出或移动，显然，player\$inventory 的 container.9 受服务器同步，其中存在 minecraft:stone，故而该物品被成功取出或移动，player\$inventory 的 container.9 变为 ItemStack.EMPTY。

但接下来尝试同步数据时，CreativeScreenHandler 发现 trackedStacks 的 container.9（它认为的原状态）为 ItemStack.EMPTY，而 player\$inventory 的现状态同样为 ItemStack.EMPTY（因为物品被成功取走），此时，客户端的逻辑认为没有槽位发生变化，因此也没有同步事件被发送。

至此，服务端 container.9 中的物品数据仍然存在（这是因为客户端最终并未告知服务端该槽位物品已被取出），而客户端的 container.9（无论是 player\$inventory 还是 trackedStacks）已经变为空槽位，同步出现问题。

分析至此，已经可以让我通过 bugs.mojang.com 查询到若干相关问题，且该问题已解决。

受限于个人精力，未作更多分析（例如，其他 ScreenHandler 同样也有 trackedStacks，为什么没有出问题？）。

如有错漏，欢迎指正。

## 处理

处理必要性：

- 由于本模组的功能如此，触发该问题的概率较原版大大提升。
- 因为受影响的是创造玩家，且最严重的问题仅是物品复制，而这是创造模式玩家本就有的权力（鼠标中键复制），甚至不会产生物品丢失，所以该问题的恶性程度大大降低。
- 这是客户端问题，本模组为仅服务端模组，无法直接修复。
- 这是原版游戏问题，在 23w33a 中已被 Mojang Studios 修复。

综上，反复权衡之后，对该问题不作处理。

## 参考

- Minecraft 1.18 源代码
- <a href="https://bugs.mojang.com/browse/MC/issues/MC-206074" target="_blank" rel="noreferrer" class="vp-external-link-icon">MC-206074</a>
- <a href="https://bugs.mojang.com/browse/MC/issues/MC-219018" target="_blank" rel="noreferrer" class="vp-external-link-icon">MC-219018</a>
