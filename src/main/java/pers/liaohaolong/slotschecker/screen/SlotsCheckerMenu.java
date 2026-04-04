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

package pers.liaohaolong.slotschecker.screen;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import pers.liaohaolong.slotschecker.inventory.OffsetInventory;

import java.util.UUID;

/**
 * <h3>SlotsCheckerMenu</h3>
 *
 * <p>检查界面的屏幕处理器。</p>
 */
public class SlotsCheckerMenu extends ChestMenu {

    /**
     * 被检查者
     */
    private final UUID target;

    public SlotsCheckerMenu(MenuType<?> type, int syncId, Inventory inventory, Container container, int rows, UUID target) {
        super(type, syncId, inventory, container, rows);
        this.target = target;

        // 针对有 未使用的 Slots 的情况，super 中初始化的默认 Slots 无法配置 mayPlace 和 mayPickup，需要重新初始化。
        if (container instanceof OffsetInventory offsetInventory && offsetInventory.getSize() < offsetInventory.getMaxSize()) {
            // 清除默认 Slots
            this.slots.clear();
            this.lastSlots.clear();
            this.remoteSlots.clear();

            // 重新初始化
            // 设置 Container
            for (int i = 0; i < this.getRowCount(); i++) {
                for (int j = 0; j < 9; j++) {
                    if (i * 9 + j < offsetInventory.getSize()) {
                        // 正常使用的 Slot
                        this.addSlot(new Slot(container, j + i * 9, 8 + j * 18, 18 + i * 18));
                    } else {
                        // 未使用的 Slot
                        this.addSlot(new Slot(container, j + i * 9, 8 + j * 18, 18 + i * 18) {
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
            int inventoryTop = 18 + this.getRowCount() * 18 + 13;
            this.addStandardInventorySlots(inventory, 8, inventoryTop);
        }
    }

    public UUID getTarget() {
        return target;
    }

}
