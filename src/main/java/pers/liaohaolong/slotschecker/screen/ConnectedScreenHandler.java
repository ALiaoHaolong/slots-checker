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

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import java.util.UUID;

/**
 * <h3>ConnectedScreenHandler</h3>
 *
 * <p>检查界面的屏幕处理器。</p>
 */
public class ConnectedScreenHandler extends GenericContainerScreenHandler {

    /**
     * 被检查者
     */
    private final UUID target;

    public ConnectedScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows, UUID target) {
        super(type, syncId, playerInventory, inventory, rows);
        this.target = target;
    }

    public UUID getTarget() {
        return target;
    }

}
