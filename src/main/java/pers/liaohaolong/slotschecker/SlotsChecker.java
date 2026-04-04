/*
 * Copyright 2026 廖浩龙
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pers.liaohaolong.slotschecker;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import pers.liaohaolong.slotschecker.inventory.OffsetInventory;
import pers.liaohaolong.slotschecker.screen.SlotsCheckerMenu;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * <h1>Slots Checker</h1>
 *
 * <p>这个模组提供了一个命令，可查看和修改任何玩家的背包、快捷栏、末影箱、护甲以及副手格子（需要 4 级权限等级）。</p>
 *
 * @author 廖浩龙
 */
@SuppressWarnings("unused")
public class SlotsChecker implements ModInitializer {

	@SuppressWarnings("unused")
    public static final String MOD_ID = "slots-checker";

	@Override
	public void onInitialize() {
		// inventory
		LiteralArgumentBuilder<CommandSourceStack> inventoryCommand = literal("inventory")
				.requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
				.then(argument("player", EntityArgument.player())
						.executes(context -> openInventoryChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// hotbar
		LiteralArgumentBuilder<CommandSourceStack> hotbarSubCommand = literal("hotbar")
				.requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
				.then(argument("player", EntityArgument.player())
						.executes(context -> openHotbarChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// ender chest
		LiteralArgumentBuilder<CommandSourceStack> enderSubCommand = literal("ender")
				.requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
				.then(argument("player", EntityArgument.player())
						.executes(context -> openEnderChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// armor
		LiteralArgumentBuilder<CommandSourceStack> armorSubCommand = literal("armor")
				.requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
				.then(argument("player", EntityArgument.player())
						.executes(context -> openArmorChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// offhand
		LiteralArgumentBuilder<CommandSourceStack> inventorySubCommand = literal("offhand")
				.requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
				.then(argument("player", EntityArgument.player())
						.executes(context -> openOffhandChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// slots-checker
		LiteralArgumentBuilder<CommandSourceStack> rootCommand = literal("slots-checker")
				.requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
				.then(enderSubCommand)
				.then(hotbarSubCommand)
				.then(inventoryCommand)
				.then(armorSubCommand)
				.then(inventorySubCommand);

		// register command
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(rootCommand));

		// close all check interfaces when the player being checked is offline
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> server.getPlayerList().getPlayers().forEach(op -> {
            // ignore the player who is being checked
            if (handler.getPlayer().getUUID().equals(op.getUUID()))
                return;
            // is a supervisor
            if (op.containerMenu instanceof SlotsCheckerMenu slotsCheckerMenu) {
                // is checking the disconnected player
                if (slotsCheckerMenu.getTarget().equals(handler.getPlayer().getUUID())) {
                    op.closeContainer();
                }
            }
        }));
	}

	public static ServerPlayer getSourcePlayer(@NotNull CommandContext<CommandSourceStack> context) {
		return context.getSource().getPlayer();
	}

	public static ServerPlayer getTargetPlayer(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		return context.getArgument("player", EntitySelector.class).findSinglePlayer(context.getSource());
	}

	public static int openEnderChecker(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
		source.openMenu(new SimpleMenuProvider(
				(syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x3, syncId, inv, target.getEnderChestInventory(), 3, target.getUUID()),
				Component.translatable("container.ender_checker", target.getName())
		));
		return 1;
	}

	public static int openHotbarChecker(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
		source.openMenu(new SimpleMenuProvider(
				(syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 0, 9), 1, target.getUUID()),
				Component.translatable("container.hotbar_checker", target.getName())
		));
		return 1;
	}

	public static int openInventoryChecker(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
		source.openMenu(new SimpleMenuProvider(
				(syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x3, syncId, inv, new OffsetInventory(target.getInventory(), 27, 9, 36), 3, target.getUUID()),
				Component.translatable("container.inventory_checker", target.getName())
		));
		return 1;
	}

	public static int openArmorChecker(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
		source.openMenu(new SimpleMenuProvider(
				(syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 40, 36), 1, target.getUUID()),
				Component.translatable("container.armor_checker", target.getName())
		));
		return 1;
	}

	public static int openOffhandChecker(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
		source.openMenu(new SimpleMenuProvider(
				(syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 40, 41), 1, target.getUUID()),
				Component.translatable("container.offhand_checker", target.getName())
		));
		return 1;
	}

}
