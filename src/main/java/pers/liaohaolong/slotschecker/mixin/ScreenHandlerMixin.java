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

package pers.liaohaolong.slotschecker.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("ScreenHandlerMixin");

    @Final
    @Shadow
    public DefaultedList<Slot> slots;

    @Inject(method = "onSlotClick", at = @At("HEAD"))
    private void onSlotClickHead(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (!(player instanceof ServerPlayerEntity))
            return;

        // 获取当前槽位物品（点击前的状态）
        ItemStack stackBefore = ItemStack.EMPTY;
        if (slotIndex >= 0 && slotIndex < this.slots.size()) {
            Slot slot = this.slots.get(slotIndex);
            stackBefore = slot.getStack();
        }

        LOGGER.info("[CLICK-PRE] 玩家 {} 操作槽位 {} | 动作: {} | 按钮: {} | 槽位当前物品: {} x{} | 操作物品: {} x{}",
                player.getName().getString(),
                slotIndex,
                actionType,
                button == 0 ? "左键" : "右键",
                stackBefore.getItem().getName().getString(),
                stackBefore.getCount(),
                ((ServerPlayerEntity) player).currentScreenHandler.getCursorStack().getItem().getName().getString(),
                ((ServerPlayerEntity) player).currentScreenHandler.getCursorStack().getCount()
        );
    }

    @Inject(method = "onSlotClick", at = @At("RETURN"))
    private void onSlotClickReturn(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (!(player instanceof ServerPlayerEntity))
            return;

        // 获取当前槽位物品（点击后的状态）
        ItemStack stackAfter = ItemStack.EMPTY;
        if (slotIndex >= 0 && slotIndex < this.slots.size()) {
            Slot slot = this.slots.get(slotIndex);
            stackAfter = slot.getStack();
        }

        LOGGER.info("[CLICK-POST] 新槽位物品：{} x{}",
                stackAfter.getItem().getName().getString(),
                stackAfter.getCount()
        );
    }

}
