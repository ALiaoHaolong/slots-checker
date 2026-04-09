---
title: Creative Inventory Problem
order: 2
---

# Creative Inventory Problem

廖浩龙 2026.3.2

## Affected Versions

Starting from the version adapted by this mod (1.18) (at least).  
Fixed in 24w33a (the development version of 1.21.2.

## Analysis

The creative inventory GUI has its unique design: on the client side, in addition to the player's inventory data stored in player\$inventory, there is also a copy of the inventory data from the variable trackedStacks in the opened creative inventory GUI (CreativeInventoryScreen\$CreativeScreenHandler); furthermore, the data displayed by the GUI comes from player\$inventory, while the synchronization of inventory changes from the client to the server is controlled by trackedStacks.

Due to the lack of code in CreativeScreenHandler to synchronize changes from player\$inventory to trackedStacks after initialization, if a player's inventory is modified through other means (whether by this mod or by the /item command, etc.) after opening the creative inventory GUI (i.e., after the game creates a new instance of CreativeScreenHandler and initializes data from player\$inventory to trackedStacks), the trackedStacks will not be updated after being synchronized to the client's player\$inventory by the server.

Suppose that the other method sets a new item (let's say minecraft:stone) in an empty slot (let's say container.9). At this point, the player's client-side player\$inventory is synchronized by the server, and container.9 becomes minecraft:stone. However, trackedStacks lacks synchronization, and container.9 still remains ItemStack.EMPTY. Since the displayed data comes from player\$inventory, there is no issue with the display on the player's client at this time.

However, when the player clicks on that slot to try to take out or quick move the item, the client first tries to take out or move from player\$inventory's container.9. Obviously, player\$inventory's container.9 is synchronized by the server and contains minecraft:stone, so the item is successfully taken out or moved, and player\$inventory's container.9 becomes ItemStack.EMPTY.

But when it tries to synchronize data next, CreativeScreenHandler finds that trackedStacks' container.9 (which it considers as the original state) is ItemStack.EMPTY, and player\$inventory's current state is also ItemStack.EMPTY (because the item was successfully taken), at this point, the client's logic thinks that no slot has changed, so no synchronization event is sent.

At this point, the item data in container.9 on the server still exists (because the client ultimately did not inform the server that the item in that slot has been taken out), while the client's container.9 (both player\$inventory and trackedStacks) has become an empty slot, resulting in a synchronization issue.

By now, I have been able to find several related issues on bugs.mojang.com, and the issue has been resolved.

Due to limited personal energy, no further analysis has been made (for example, other ScreenHandlers also have trackedStacks, why haven't they had this issue?).

If there are any errors or omissions, please feel free to point them out.

## Handling

Necessity of handling:

- Due to the functionality of this mod, the probability of triggering this issue is greatly increased compared to the vanilla version.
- Since the affected players are creative mode players, and the most severe issue is only item duplication, which is a power that creative mode players already have (middle-click duplication), and it doesn't even cause item loss, the severity of this issue is greatly reduced.
- This is a client-side issue, and this mod is a server-side only mod, so it cannot be directly fixed.
- This is a vanilla game issue, which has been fixed by Mojang Studios in 23w33a.

In summary, after repeated consideration, I have decided not to handle this issue.

## References

- Minecraft 1.18 source code
- [MC-206074](https://bugs.mojang.com/browse/MC/issues/MC-206074)
- [MC-219018](https://bugs.mojang.com/browse/MC/issues/MC-219018)
