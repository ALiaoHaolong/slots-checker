---
title: Dying Player Inventory Problem
order: 3
---

# Dying Player Inventory Problem

廖浩龙 2026.11.18

## Affected Versions

All versions this mod supports (1.18 through 26.3).  
The vanilla issue is still unresolved; the last update to MC-248983 was on 2025-10-23.

## Analysis

### The Vanilla Issue

When a player dies with the `keepInventory` game rule off, `PlayerInventory.dropAll()` empties every slot at the moment of death and drops the contents as item entities on the ground. The player entity is then discarded and replaced: `PlayerManager.respawnPlayer()` builds a new `ServerPlayerEntity` and calls `copyFrom()` to carry state over from the old one.

`copyFrom()` clones the inventory only when the player is still alive (Like returning from the portal in the middle of the End to the overworld), or when `keepInventory` is on, or when the old player was a spectator. In the rest of the cases, the game skips cloning the inventory (presumably on the grounds that the inventory should already be empty at death, so there's no need to clone it).

Therefore, anything written to the inventory during death is simply discarded. This is MC-248983. 

### Why Does This Mod Need To Handle This Issue

Vanilla offers no way to point a container GUI at another player's inventory, so in an unmodded game the only writers are commands, which almost nobody aims at a player who is currently sitting on the death screen. This mod exists precisely to do that, and it accepts writes through an ordinary GUI. The natural way to hit the bug therefore becomes: open the target's inventory in edit mode, the target dies, `keepInventory` is off, place an item during the window, and watch it disappear after the respawn.

### Why the Ender Chest Is Not Affected

`copyFrom()` transfers the ender chest by reference, unconditionally. The ender chest therefore survives the respawn intact, and writes made during the window persist.

## Handling

Necessity of handling:

- The mod raises the probability of triggering this issue enormously compared to the vanilla version, because it turns a command-line edge case into a GUI action.
- Unlike the creative inventory problem, the consequence here is item loss rather than duplication, which is the severe kind.
- It is a server-side behaviour that this mod can mitigate, unlike the creative inventory problem, which was client-side and unfixable from a server-side-only mod.

Decision: pause interaction during the window, and keep the screen open.

The mod does not change what vanilla does with items. It refuses a write that vanilla would silently destroy. Concretely, in edit mode, for non-ender slots, while the target is dead and `keepInventory` is off:

- The target slots are rendered as barrier items, which is the signal every player already reads as "this slot is unavailable".
- `canInsert` and `canTakeItems` return false, so no click, shift-click, number-key swap, drag or drop key can move anything in or out.

When the target respawns, the mod rebinds the GUI to the new player entity via the 'AFTER_RESPAWN' listener and automatically resumes editing. The admin can simply wait and watch, with no need to re-run the command.

Ender chest slots are exempt from all of the above, for the reason given in the analysis.

## References

- Minecraft 1.18 source code
- <a href="https://bugs.mojang.com/browse/MC/issues/MC-248983" target="_blank" rel="noreferrer" class="vp-external-link-icon">MC-248983</a>
