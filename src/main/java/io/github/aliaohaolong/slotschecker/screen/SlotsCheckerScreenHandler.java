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

package io.github.aliaohaolong.slotschecker.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import io.github.aliaohaolong.slotschecker.SlotsChecker;
import io.github.aliaohaolong.slotschecker.inventory.OffsetPlayerInventory;

import java.util.UUID;

/**
 * <h3>SlotsCheckerScreenHandler</h3>
 *
 * <p>检查界面的屏幕处理器。</p>
 */
public class SlotsCheckerScreenHandler extends GenericContainerScreenHandler {

    private final UUID targetUUID;

    /**
     * @param type 界面类型
     * @param syncId 同步 ID
     * @param playerInventory 检查者的背包
     * @param inventory 被检查的 Inventory
     * @param rows 用于展示被检查的 Inventory 的行数（一行 9 个槽位）
     * @param targetUUID 被检查者的 UUID
     * @param readOnly 是否为只读模式
     */
    public SlotsCheckerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows, UUID targetUUID, String slotType, boolean readOnly) {
        super(type, syncId, playerInventory, inventory, rows);
        this.targetUUID = targetUUID;

        // 计算可以交互的槽位数
        int size;
        // 查看模式下，所有被检查槽位都禁止交互（除创造模式的中间复制操作）
        if (readOnly)
            size = 0;
        // 编辑模式下，如果使用了 OffsetPlayerInventory，则使用实际可用的槽位数
        else if (inventory instanceof OffsetPlayerInventory offsetPlayerInventory)
            size = offsetPlayerInventory.getSize();
        // 编辑模式下，如果未使用槽位偏移，则所有槽位都可交互
        else
            size = rows * 9;

        // 重新初始化 Slots
        this.slots.clear(); // 唯一的槽位数据
        this.trackedStacks.clear(); // 上次同步给成就系统的缓存
        this.previousTrackedStacks.clear(); // 上次同步给客户端的内容缓存

        // 设置 Inventory
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < 9; j++) {
                int index = j + i * 9;
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                if (index < size) {
                    // 正常使用的 Slot
                    this.addSlot(new Slot(inventory, index, x, y) {
                        @Override
                        public ItemStack getStack() {
                            return pauseInteraction() ? new ItemStack(Items.BARRIER) : super.getStack();
                        }

                        @Override
                        public boolean canInsert(ItemStack stack) {
                            return !pauseInteraction();
                        }

                        @Override
                        public boolean canTakeItems(PlayerEntity playerEntity) {
                            return !pauseInteraction();
                        }

                        private boolean pauseInteraction() {
                            // 从 MinecraftServer 中获取被检查者
                            ServerPlayerEntity target = SlotsChecker.server.getPlayerManager().getPlayer(targetUUID);
                            // 如果被检查者离线，则禁止交互
                            if (target == null || target.isDisconnected())
                                return false;
                            // 若为编辑模式，且查看非 ender 槽位，且被检查者死亡，且未开启死亡不掉落，则暂停交互
                            return !readOnly && !slotType.equals("ender") && target.isDead() && !SlotsChecker.server.getGameRules().getBoolean(GameRules.KEEP_INVENTORY);
                        }
                    });
                } else {
                    // 禁止交互的 Slot
                    this.addSlot(new Slot(inventory, index, x, y) {
                        @Override
                        public boolean canInsert(ItemStack stack) {
                            return false; // 禁止插入
                        }

                        @Override
                        public boolean canTakeItems(PlayerEntity playerEntity) {
                            return false; // 禁止取出
                        }
                    });
                }
            }
        }

        // 设置 PlayerInventory
        int inventoryTop = (this.getRows() - 4) * 18;
        // 设置 PlayerInventory - Inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 103 + i * 18 + inventoryTop));
            }
        }
        // 设置 PlayerInventory - Hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 161 + inventoryTop));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        // 从 MinecraftServer 中获取被检查者
        ServerPlayerEntity target = SlotsChecker.server.getPlayerManager().getPlayer(targetUUID);
        // 如果被检查者离线，则关闭屏幕
        return !(target == null || target.isDisconnected());
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

}
