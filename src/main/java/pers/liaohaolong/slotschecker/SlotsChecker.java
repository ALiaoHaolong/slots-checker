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
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import pers.liaohaolong.slotschecker.inventory.OffsetInventory;
import pers.liaohaolong.slotschecker.screen.ConnectedScreenHandler;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * <h1>Slots Checker</h1>
 *
 * <p>这个模组提供了一个命令，可查看和修改任何玩家的背包、快捷栏、末影箱、护甲以及副手格子（需要 4 级权限等级）。</p>
 *
 * <p>相关链接：
 *     <a href="https://modrinth.com/mod/slots-checker">主页</a>
 *     <a href="https://gitee.com/AnNight/slots-checker/issues">问题反馈</a>
 * </p>
 *
 * @author 廖浩龙
 */
public class SlotsChecker implements ModInitializer {

	@SuppressWarnings("unused")
    public static final String MOD_ID = "slots-checker";

	@Override
	public void onInitialize() {
		// inventory
		LiteralArgumentBuilder<ServerCommandSource> inventoryCommand = literal("inventory")
				// I am not sure whether this is correct.
				// original: .requires(source -> source.hasPermissionLevel(4))
				.requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS)))
				.then(argument("player", EntityArgumentType.player())
						.executes(context -> openInventoryChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// hotbar
		LiteralArgumentBuilder<ServerCommandSource> hotbarSubCommand = literal("hotbar")
				.requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS)))
				.then(argument("player", EntityArgumentType.player())
						.executes(context -> openHotbarChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// ender chest
		LiteralArgumentBuilder<ServerCommandSource> enderSubCommand = literal("ender")
				.requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS)))
				.then(argument("player", EntityArgumentType.player())
						.executes(context -> openEnderChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// armor
		LiteralArgumentBuilder<ServerCommandSource> armorSubCommand = literal("armor")
				.requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS)))
				.then(argument("player", EntityArgumentType.player())
						.executes(context -> openArmorChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// offhand
		LiteralArgumentBuilder<ServerCommandSource> inventorySubCommand = literal("offhand")
				.requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS)))
				.then(argument("player", EntityArgumentType.player())
						.executes(context -> openOffhandChecker(getSourcePlayer(context), getTargetPlayer(context)))
				);

		// slots-checker
		LiteralArgumentBuilder<ServerCommandSource> rootCommand = literal("slots-checker")
				.requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS)))
				.then(enderSubCommand)
				.then(hotbarSubCommand)
				.then(inventoryCommand)
				.then(armorSubCommand)
				.then(inventorySubCommand);

		// register command
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(rootCommand));

		// close all check interfaces when the player being checked is offline
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> server.getPlayerManager().getPlayerList().forEach(op -> {
            // ignore the player who is being checked
            if (handler.getPlayer().getUuid().equals(op.getUuid()))
                return;
            // is a supervisor
            if (op.currentScreenHandler instanceof ConnectedScreenHandler connectedScreenHandler) {
                // is checking the disconnected player
                if (connectedScreenHandler.getTarget().equals(handler.getPlayer().getUuid())) {
                    op.closeHandledScreen();
                }
            }
        }));
	}

	public static ServerPlayerEntity getSourcePlayer(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return context.getSource().getPlayer();
	}

	public static ServerPlayerEntity getTargetPlayer(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return context.getArgument("player", EntitySelector.class).getPlayer(context.getSource());
	}

	public static int openEnderChecker(@NotNull ServerPlayerEntity source, @NotNull ServerPlayerEntity target) {
		source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
				(syncId, inv, player) -> new ConnectedScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inv, target.getEnderChestInventory(), 3, target.getUuid()),
				Text.translatable("container.ender_checker", target.getName())
		));
		return 1;
	}

	public static int openHotbarChecker(@NotNull ServerPlayerEntity source, @NotNull ServerPlayerEntity target) {
		source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
				(syncId, inv, player) -> new ConnectedScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 0, 9), 1, target.getUuid()),
				Text.translatable("container.hotbar_checker", target.getName())
		));
		return 1;
	}

	public static int openInventoryChecker(@NotNull ServerPlayerEntity source, @NotNull ServerPlayerEntity target) {
		source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
				(syncId, inv, player) -> new ConnectedScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inv, new OffsetInventory(target.getInventory(), 27, 9, 36), 3, target.getUuid()),
				Text.translatable("container.inventory_checker", target.getName())
		));
		return 1;
	}

	public static int openArmorChecker(@NotNull ServerPlayerEntity source, @NotNull ServerPlayerEntity target) {
		source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
				(syncId, inv, player) -> new ConnectedScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 36, 40), 1, target.getUuid()),
				Text.translatable("container.armor_checker", target.getName())
		));
		return 1;
	}

	public static int openOffhandChecker(@NotNull ServerPlayerEntity source, @NotNull ServerPlayerEntity target) {
		source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
				(syncId, inv, player) -> new ConnectedScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 40, 41), 1, target.getUuid()),
				Text.translatable("container.offhand_checker", target.getName())
		));
		return 1;
	}

}
