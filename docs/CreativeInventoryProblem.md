# 创造模式背包问题（Creative Inventory Problem）

廖浩龙 2026.3.2

## 影响版本（Affected Versions）

自本模组适配版本起（1.18）（至少）。  
24w33a（1.21.2 的开发版本）中被修复。

Starting from the version adapted by this mod (1.18) (at least).  
Fixed in 24w33a (the development version of 1.21.2.

## 分析（Analysis）

创造模式背包 GUI 具有其独特的设计：在客户端中，除了 player\$inventory 中保存了玩家的背包数据，在打开的创造背包 GUI（CreativeInventoryScreen\$CreativeScreenHandler）中同样存在着一份来自变量 trackedStacks 的背包数据；此外，该 GUI 显示的数据来自 player\$inventory，而控制客户端是否向服务端同步背包变化的则是 trackedStacks。

The creative inventory GUI has its unique design: on the client side, in addition to the player's inventory data stored in player\$inventory, there is also a copy of the inventory data from the variable trackedStacks in the opened creative inventory GUI (CreativeInventoryScreen\$CreativeScreenHandler); furthermore, the data displayed by the GUI comes from player\$inventory, while the synchronization of inventory changes from the client to the server is controlled by trackedStacks.

由于 CreativeScreenHandler 中缺少代码来同步初始化后从 player\$inventory 到 trackedStacks 的变化，导致玩家在打开创造模式背包 GUI 之后（即，游戏新建一个 CreativeScreenHandler 实例，并从 player\$inventory 中初始化数据到 trackedStacks 之后），如果通过其他方式修改其背包（无论是本模组还是/item命令等方式），并由服务器同步到客户端的 player\$inventory 中之后，trackedStacks 中并未更新。

Due to the lack of code in CreativeScreenHandler to synchronize changes from player\$inventory to trackedStacks after initialization, if a player's inventory is modified through other means (whether by this mod or by the /item command, etc.) after opening the creative inventory GUI (i.e., after the game creates a new instance of CreativeScreenHandler and initializes data from player\$inventory to trackedStacks), the trackedStacks will not be updated after being synchronized to the client's player\$inventory by the server.

假设该其他方式向一个空槽位（设 container.9）中设置了新物品（设 minecraft:stone），此时该玩家客户端的 player\$inventory 受服务器同步，其中的 container.9 中变为 minecraft:stone。而 trackedStacks 缺乏同步，其中的 container.9 中仍为 ItemStack.EMPTY。由于显示的数据来自 player\$inventory，因此该玩家客户端此时的显示并没有问题。

Suppose that the other method sets a new item (let's say minecraft:stone) in an empty slot (let's say container.9). At this point, the player's client-side player\$inventory is synchronized by the server, and container.9 becomes minecraft:stone. However, trackedStacks lacks synchronization, and container.9 still remains ItemStack.EMPTY. Since the displayed data comes from player\$inventory, there is no issue with the display on the player's client at this time.

但是当该玩家点击该槽位尝试取出或快捷移动时，客户端首先尝试从 player\$inventory 的 container.9 中取出或移动，显然，player\$inventory 的 container.9 受服务器同步，其中存在 minecraft:stone，故而该物品被成功取出或移动，player\$inventory 的 container.9 变为 ItemStack.EMPTY。

However, when the player clicks on that slot to try to take out or quick move the item, the client first tries to take out or move from player\$inventory's container.9. Obviously, player\$inventory's container.9 is synchronized by the server and contains minecraft:stone, so the item is successfully taken out or moved, and player\$inventory's container.9 becomes ItemStack.EMPTY.

但接下来尝试同步数据时，CreativeScreenHandler 发现 trackedStacks 的 container.9（它认为的原状态）为 ItemStack.EMPTY，而 player\$inventory 的现状态同样为 ItemStack.EMPTY（因为物品被成功取走），此时，客户端的逻辑认为没有槽位发生变化，因此也没有同步事件被发送。

But when it tries to synchronize data next, CreativeScreenHandler finds that trackedStacks' container.9 (which it considers as the original state) is ItemStack.EMPTY, and player\$inventory's current state is also ItemStack.EMPTY (because the item was successfully taken), at this point, the client's logic thinks that no slot has changed, so no synchronization event is sent.

至此，服务端 container.9 中的物品数据仍然存在（这是因为客户端最终并未告知服务端该槽位物品已被取出），而客户端的 container.9（无论是 player\$inventory 还是 trackedStacks）已经变为空槽位，同步出现问题。

At this point, the item data in container.9 on the server still exists (because the client ultimately did not inform the server that the item in that slot has been taken out), while the client's container.9 (both player\$inventory and trackedStacks) has become an empty slot, resulting in a synchronization issue.

分析至此，已经可以让我通过 bugs.mojang.com 查询到若干相关问题，且该问题已解决。

By now, I have been able to find several related issues on bugs.mojang.com, and the issue has been resolved.

受限于个人精力，未作更多分析（例如，其他 ScreenHandler 同样也有 trackedStacks，为什么没有出问题？）。

Due to limited personal energy, no further analysis has been made (for example, other ScreenHandlers also have trackedStacks, why haven't they had this issue?).

如有错漏，欢迎指正。

If there are any errors or omissions, please feel free to point them out.

## 处理（Handling）

处理必要性：

- 由于本模组的功能如此，触发该问题的概率较原版大大提升。
- 因为受影响的是创造玩家，且最严重的问题仅是物品复制，而这是创造模式玩家本就有的权力（鼠标中键复制），甚至不会产生物品丢失，所以该问题的恶性程度大大降低。
- 这是客户端问题，本模组为仅服务端模组，无法直接修复。
- 这是原版游戏问题，在 23w33a 中已被 Mojang Studios 修复。

综上，反复权衡之后，对该问题不作处理。

Necessity of handling:

- Due to the functionality of this mod, the probability of triggering this issue is greatly increased compared to the vanilla version.
- Since the affected players are creative mode players, and the most severe issue is only item duplication, which is a power that creative mode players already have (middle-click duplication), and it doesn't even cause item loss, the severity of this issue is greatly reduced.
- This is a client-side issue, and this mod is a server-side only mod, so it cannot be directly fixed.
- This is a vanilla game issue, which has been fixed by Mojang Studios in 23w33a.

In summary, after repeated consideration, I have decided not to handle this issue.

## 参考（References）

- Minecraft 1.18 源代码（Minecraft 1.18 source code）
- [MC-206074](https://bugs.mojang.com/browse/MC/issues/MC-206074)
- [MC-219018](https://bugs.mojang.com/browse/MC/issues/MC-219018)