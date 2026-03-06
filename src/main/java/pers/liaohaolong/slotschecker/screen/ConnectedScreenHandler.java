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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.liaohaolong.slotschecker.inventory.OffsetPlayerInventory;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

/**
 * <h3>ConnectedScreenHandler</h3>
 *
 * <p>检查界面的屏幕处理器。</p>
 */
public class ConnectedScreenHandler extends GenericContainerScreenHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConnectedScreenHandler.class);

    /**
     * 被检查者
     */
    private final UUID target;

    public ConnectedScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows, UUID target) {
        super(type, syncId, playerInventory, inventory, rows);
        this.target = target;

        // 针对有 未使用的 Slots 的情况，super 中初始化的默认 Slots 无法配置 canInsert 和 canTakeItems，需要重新初始化。
        if (inventory instanceof OffsetPlayerInventory offsetPlayerInventory && offsetPlayerInventory.getSize() < offsetPlayerInventory.getMaxSize()) {
            // 清除默认 Slots
            this.slots.clear();
            try {
                Field trackedStacksField = ScreenHandler.class.getDeclaredField("trackedStacks");
                trackedStacksField.setAccessible(true);
                Optional.ofNullable(trackedStacksField.get(this))
                        .filter(DefaultedList.class::isInstance)
                        .map(DefaultedList.class::cast)
                        .ifPresent(DefaultedList::clear);
                Field previousTrackedStacksFiled = ScreenHandler.class.getDeclaredField("trackedSlots");
                previousTrackedStacksFiled.setAccessible(true);
                Optional.ofNullable(previousTrackedStacksFiled.get(this))
                        .filter(DefaultedList.class::isInstance)
                        .map(DefaultedList.class::cast)
                        .ifPresent(DefaultedList::clear);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error(e.getMessage());
            }

            // 重新初始化
            // 设置 Inventory
            for (int i = 0; i < this.getRows(); i++) {
                for (int j = 0; j < 9; j++) {
                    if (i * 9 + j < offsetPlayerInventory.getSize()) {
                        // 正常使用的 Slot
                        this.addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
                    } else {
                        // 未使用的 Slot
                        this.addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18) {
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
            // 设置 PlayerInventory - Inventory
            int j = 18 + this.getRows() * 18 + 13;
            this.addPlayerSlots(playerInventory, 8, j);
        }
    }

    public UUID getTarget() {
        return target;
    }

}
