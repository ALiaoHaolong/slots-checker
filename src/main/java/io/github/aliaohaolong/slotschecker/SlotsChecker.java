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

package io.github.aliaohaolong.slotschecker;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import io.github.aliaohaolong.slotschecker.inventory.OffsetPlayerInventory;
import io.github.aliaohaolong.slotschecker.screen.SlotsCheckerScreenHandler;

/**
 * <h3>SlotsChecker</h3>
 *
 * <p>注册命令，注册事件监听。</p>
 */
public class SlotsChecker implements ModInitializer {

    public static final String MOD_ID = "slots-checker";

    private static final int PERMISSION_VIEW = 2;
    private static final int PERMISSION_EDIT = 4;

    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> SlotsChecker.server = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SlotsChecker.server = null);

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(
                CommandManager.literal(MOD_ID)
                        .requires(source -> source.hasPermissionLevel(PERMISSION_VIEW))
                        .then(buildSubCommand("armor"))
                        .then(buildSubCommand("ender"))
                        .then(buildSubCommand("hotbar"))
                        .then(buildSubCommand("inventory"))
                        .then(buildSubCommand("offhand"))
        ));

        ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {
            // 获取服务器实例
            MinecraftServer server = newPlayer.getServer();
            // 遍历玩家，判断有没有玩家正在观察当前玩家
            assert server != null;
            server.getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                // 如果该玩家是观察者
                if (serverPlayerEntity.currentScreenHandler instanceof SlotsCheckerScreenHandler slotsCheckerScreenHandler)
                    // 如果该玩家观察的对象是当前重生的玩家
                    if (slotsCheckerScreenHandler.getTargetUUID().equals(newPlayer.getUuid()))
                        // 如果正在观察的是 OffsetPlayerInventory，即 armor、hotbar、inventory 或 offhand 槽位
                        if (slotsCheckerScreenHandler.getInventory() instanceof OffsetPlayerInventory offsetPlayerInventory)
                            // 更新 inventory 绑定（重生时，旧玩家实体被丢弃，需要换绑至新玩家实体）
                            offsetPlayerInventory.setInventory(newPlayer.getInventory());
            });
        }));
    }

    private static ArgumentBuilder<ServerCommandSource, ?> buildSubCommand(String slotType) {
        return CommandManager.literal(slotType)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> execute(context, slotType, true))
                        .then(CommandManager.literal("view")
                                .executes(context -> execute(context, slotType, true))
                        )
                        .then(CommandManager.literal("edit")
                                .requires(source -> source.hasPermissionLevel(PERMISSION_EDIT))
                                .executes(context -> execute(context, slotType, false))
                        )
                );
    }

    private static int execute(CommandContext<ServerCommandSource> context, String slotType, boolean readOnly) throws CommandSyntaxException {
        ServerPlayerEntity source = context.getSource().getPlayer();
        ServerPlayerEntity target = context.getArgument("player", EntitySelector.class).getPlayer(context.getSource());

        switch (slotType) {
            case "armor" -> source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new SlotsCheckerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, inv, new OffsetPlayerInventory(target.getInventory(), 9, 40, 36), 1, target.getUuid(), slotType, readOnly),
                    new TranslatableText(readOnly ? "container.slots-checker.armor.view" : "container.slots-checker.armor.edit", target.getEntityName())
            ));
            case "ender" -> source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new SlotsCheckerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inv, target.getEnderChestInventory(), 3, target.getUuid(), slotType, readOnly),
                    new TranslatableText(readOnly ? "container.slots-checker.ender.view" : "container.slots-checker.ender.edit", target.getEntityName())
            ));
            case "hotbar" -> source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new SlotsCheckerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, inv, new OffsetPlayerInventory(target.getInventory(), 9, 0, 9), 1, target.getUuid(), slotType, readOnly),
                    new TranslatableText(readOnly ? "container.slots-checker.hotbar.view" : "container.slots-checker.hotbar.edit", target.getEntityName())
            ));
            case "inventory" -> source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new SlotsCheckerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inv, new OffsetPlayerInventory(target.getInventory(), 27, 9, 36), 3, target.getUuid(), slotType, readOnly),
                    new TranslatableText(readOnly ? "container.slots-checker.inventory.view" : "container.slots-checker.inventory.edit", target.getEntityName())
            ));
            case "offhand" -> source.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new SlotsCheckerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, inv, new OffsetPlayerInventory(target.getInventory(), 9, 40, 41), 1, target.getUuid(), slotType, readOnly),
                    new TranslatableText(readOnly ? "container.slots-checker.offhand.view" : "container.slots-checker.offhand.edit", target.getEntityName())
            ));
        }

        return 1;
    }

}
