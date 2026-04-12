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

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pers.liaohaolong.slotschecker.screen.SlotsCheckerMenu;

/**
 * <h3>AbstractContainerMenuMixin</h3>
 *
 * <p>修复将屏障物品移动至盔甲槽或副手槽时（使用 Shift + 左键点击操作），该物品将会消失。</p>
 */
@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Redirect(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack insertItemMixin(Slot instance) {
        if (getThis() instanceof SlotsCheckerMenu // 仅针对 Slots Checker 的 GUI
                && !instance.mayPlace(ItemStack.EMPTY)) { // 仅针对禁用插入槽位（实质上就是指禁用槽位）
            return ItemStack.EMPTY; // 返回空槽位，以阻止屏障槽位合并请求
        }
        return instance.getItem();
    }

    @Unique
    private AbstractContainerMenu getThis() {
        return (AbstractContainerMenu)(Object) this;
    }

}
