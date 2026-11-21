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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gamerules.GameRules;
import org.jspecify.annotations.NonNull;
import io.github.aliaohaolong.slotschecker.SlotsChecker;
import io.github.aliaohaolong.slotschecker.inventory.OffsetInventory;

import java.util.UUID;

/**
 * <h3>SlotsCheckerMenu</h3>
 *
 * <p>检查界面的屏幕处理器。</p>
 */
public class SlotsCheckerMenu extends ChestMenu {

    private final UUID targetUUID;

    /**
     * @param type 界面类型
     * @param syncId 同步 ID
     * @param inventory 检查者的背包
     * @param container 被检查的 container
     * @param rows 用于展示被检查的 container 的行数（一行 9 个槽位）
     * @param targetUUID 被检查者的 UUID
     * @param readOnly 是否为只读模式
     */
    public SlotsCheckerMenu(MenuType<?> type, int syncId, Inventory inventory, Container container, int rows, UUID targetUUID, String slotType, boolean readOnly) {
        super(type, syncId, inventory, container, rows);
        this.targetUUID = targetUUID;

        // 计算可以交互的槽位数
        int size;
        // 查看模式下，所有被检查槽位都禁止交互（除创造模式的中间复制操作）
        if (readOnly)
            size = 0;
        // 编辑模式下，如果使用了 OffsetPlayerInventory，则使用实际可用的槽位数
        else if (container instanceof OffsetInventory offsetPlayerInventory)
            size = offsetPlayerInventory.getSize();
        // 编辑模式下，如果未使用槽位偏移，则所有槽位都可交互
        else
            size = rows * 9;

        // 重新初始化 Slots
        this.slots.clear(); // 唯一的槽位数据
        this.lastSlots.clear(); // 上次同步给成就系统的缓存
        this.remoteSlots.clear(); // 上次同步给客户端的内容缓存

        // 设置 Container
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < 9; j++) {
                int index = j + i * 9;
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                if (index < size) {
                    // 正常使用的 Slot
                    this.addSlot(new Slot(container, index, x, y) {
                        @Override
                        public @NonNull ItemStack getItem() {
                            return pauseInteraction() ? new ItemStack(Items.BARRIER) : super.getItem();
                        }

                        @Override
                        public boolean mayPlace(@NonNull ItemStack itemStack) {
                            return !pauseInteraction();
                        }

                        @Override
                        public boolean mayPickup(@NonNull Player player) {
                            return !pauseInteraction();
                        }

                        private boolean pauseInteraction() {
                            // 从 MinecraftServer 中获取被检查者
                            ServerPlayer target = SlotsChecker.server.getPlayerList().getPlayer(targetUUID);
                            // 如果被检查者离线，则禁止交互
                            if (target == null || target.hasDisconnected())
                                return false;
                            // 若为编辑模式，且查看非 ender 槽位，且被检查者死亡，且未开启死亡不掉落，则暂停交互
                            return !readOnly && !slotType.equals("ender") && target.isDeadOrDying() && !SlotsChecker.server.getGameRules().get(GameRules.KEEP_INVENTORY);
                        }
                    });
                } else {
                    // 未使用的 Slot
                    this.addSlot(new Slot(container, index, x, y) {
                        @Override
                        public boolean mayPlace(final @NonNull ItemStack stack) {
                            return false; // 禁止插入
                        }

                        @Override
                        public boolean mayPickup(final @NonNull Player playerEntity) {
                            return false; // 禁止取出
                        }
                    });
                }
            }
        }
        // 设置 Inventory
        this.addStandardInventorySlots(inventory, 8, 18 + this.getRowCount() * 18 + 13);
    }

    @Override
    public boolean stillValid(final @NonNull Player player) {
        // 从 MinecraftServer 中获取被检查者
        ServerPlayer target = SlotsChecker.server.getPlayerList().getPlayer(targetUUID);
        // 如果被检查者离线，则关闭屏幕
        return !(target == null || target.hasDisconnected());
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

}
