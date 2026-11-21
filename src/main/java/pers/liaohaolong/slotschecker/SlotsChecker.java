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

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;
import pers.liaohaolong.slotschecker.inventory.OffsetInventory;
import pers.liaohaolong.slotschecker.screen.SlotsCheckerMenu;

/**
 * <h3>SlotsChecker</h3>
 *
 * <p>注册命令，注册事件监听。</p>
 */
@SuppressWarnings("unused")
public class SlotsChecker implements ModInitializer {

    public static final String MOD_ID = "slots-checker";

    private static final PermissionCheck PERMISSION_VIEW = Commands.LEVEL_GAMEMASTERS;
    private static final PermissionCheck PERMISSION_EDIT = Commands.LEVEL_OWNERS;

    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> SlotsChecker.server = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SlotsChecker.server = null);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                Commands.literal(MOD_ID)
                        .requires(Commands.hasPermission(PERMISSION_VIEW))
                        .then(buildSubCommand("armor"))
                        .then(buildSubCommand("ender"))
                        .then(buildSubCommand("hotbar"))
                        .then(buildSubCommand("inventory"))
                        .then(buildSubCommand("offhand"))
        ));

        ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {
            // 遍历玩家，判断有没有玩家正在观察当前玩家
            server.getPlayerList().getPlayers().forEach(serverPlayer -> {
                // 如果该玩家是观察者
                if (serverPlayer.containerMenu instanceof SlotsCheckerMenu slotsCheckerMenu)
                    // 如果该玩家观察的对象是当前重生的玩家
                    if (slotsCheckerMenu.getTargetUUID().equals(newPlayer.getUUID()))
                        // 如果正在观察的是 OffsetInventory，即 armor、hotbar、inventory 或 offhand 槽位
                        if (slotsCheckerMenu.getContainer() instanceof OffsetInventory offsetInventory)
                            // 更新 inventory 绑定（重生时，旧玩家实体被丢弃，需要换绑至新玩家实体）
                            offsetInventory.setInventory(newPlayer.getInventory());
            });
        }));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> buildSubCommand(String slotType) {
        return Commands.literal(slotType)
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> execute(context, slotType, true))
                        .then(Commands.literal("view")
                                .executes(context -> execute(context, slotType, true))
                        )
                        .then(Commands.literal("edit")
                                .requires(Commands.hasPermission(PERMISSION_EDIT))
                                .executes(context -> execute(context, slotType, false))
                        )
                );
    }

    private static int execute(CommandContext<CommandSourceStack> context, String slotType, boolean readOnly) throws CommandSyntaxException {
        ServerPlayer source = context.getSource().getPlayerOrException();
        ServerPlayer target = context.getArgument("player", EntitySelector.class).findSinglePlayer(context.getSource());

        switch (slotType) {
            case "armor" -> source.openMenu(new SimpleMenuProvider(
                    (syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 40, 36), 1, target.getUUID(), slotType, readOnly),
                    Component.translatable(readOnly ? "container.slots-checker.armor.view" : "container.slots-checker.armor.edit", target.getName())
            ));
            case "ender" -> source.openMenu(new SimpleMenuProvider(
                    (syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x3, syncId, inv, target.getEnderChestInventory(), 3, target.getUUID(), slotType, readOnly),
                    Component.translatable(readOnly ? "container.slots-checker.ender.view" : "container.slots-checker.ender.edit", target.getName())
            ));
            case "hotbar" -> source.openMenu(new SimpleMenuProvider(
                    (syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 0, 9), 1, target.getUUID(), slotType, readOnly),
                    Component.translatable(readOnly ? "container.slots-checker.hotbar.view" : "container.slots-checker.hotbar.edit", target.getName())
            ));
            case "inventory" -> source.openMenu(new SimpleMenuProvider(
                    (syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x3, syncId, inv, new OffsetInventory(target.getInventory(), 27, 9, 36), 3, target.getUUID(), slotType, readOnly),
                    Component.translatable(readOnly ? "container.slots-checker.inventory.view" : "container.slots-checker.inventory.edit", target.getName())
            ));
            case "offhand" -> source.openMenu(new SimpleMenuProvider(
                    (syncId, inv, player) -> new SlotsCheckerMenu(MenuType.GENERIC_9x1, syncId, inv, new OffsetInventory(target.getInventory(), 9, 40, 41), 1, target.getUUID(), slotType, readOnly),
                    Component.translatable(readOnly ? "container.slots-checker.offhand.view" : "container.slots-checker.offhand.edit", target.getName())
            ));
        }

        return 1;
    }

}
