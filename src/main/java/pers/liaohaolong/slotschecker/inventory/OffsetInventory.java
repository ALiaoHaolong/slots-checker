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

package pers.liaohaolong.slotschecker.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;

/**
 * <h3>OffsetInventory</h3>
 *
 * <p>将 PlayerInventory 的指定部分槽位映射到 OffsetInventory 的槽位。</p>
 */
public class OffsetInventory implements Inventory, Nameable {

    /**
     * 被检查的玩家背包
     */
    private final PlayerInventory inventory;

    /**
     * 此界面的最大槽位数（实际使用时，受 GenericContainerScreenHandler 影响，该值总为 9 的倍数）
     */
    private final int maxSize;

    /**
     * 实际可以使用的槽位数（从左到右，从上到下）
     */
    private final int size;

    /**
     * 此界面对应的玩家背包的起始槽位索引（含）
     */
    private final int start;

    /**
     * 此界面对应的玩家背包的结束槽位索引（不含）
     */
    private final int end;

    public OffsetInventory(PlayerInventory inventory, int maxSize, int start, int end) {
        this.inventory = inventory;
        this.maxSize = maxSize;
        this.size = end - start;
        this.start = start;
        this.end = end;

        if (this.maxSize < size)
            throw new IllegalArgumentException("Max size must be greater than or equal to size.");
    }

    /**
     * 获取背包大小
     * @return 库存的总槽位数。例如：玩家背包返回 41（背包27 + 快捷栏9 + 盔甲4 + 副手1)
     */
    @Override
    public int size() {
        return maxSize;
    }

    /**
     * 判断背包是否为空
     * @return 库存是否为空。所有槽位都没有物品时返回 true
     */
    @Override
    public boolean isEmpty() {
        for (int i = start; i < end; i++) {
            if (!inventory.getStack(i).isEmpty())
                return false;
        }
        return true;
    }

    /**
     * 获取指定槽位的物品（需要偏移）
     * @param slot 槽位索引
     * @return 指定槽位的 ItemStack
     */
    @Override
    public ItemStack getStack(int slot) {
        if (slot >= size)
            return ItemStack.EMPTY;
        return inventory.getStack(slot + start);
    }

    /**
     * 移除指定槽位的指定数量的物品（需要偏移）
     * @param slot 槽位索引
     * @param amount 移除的数量
     * @return 被移除的 ItemStack，原槽位物品数量减少
     */
    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot >= size)
            return ItemStack.EMPTY;
        return inventory.removeStack(slot + start, amount);
    }

    /**
     * 移除指定槽位的完整 ItemStack（需要偏移）
     * @param slot 槽位索引
     * @return 被移除的 ItemStack，原槽位被清空
     */
    @Override
    public ItemStack removeStack(int slot) {
        if (slot >= size)
            return ItemStack.EMPTY;
        return inventory.removeStack(slot + start);
    }

    /**
     * 设置指定槽位的物品（需要偏移）
     * @param slot 槽位索引
     * @param stack 设置的物品
     */
    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot >= size)
            return;
        inventory.setStack(slot + start, stack);
    }

    @Override
    public void markDirty() {
        // 直接调用 PlayerInventory
        inventory.markDirty();
    }

    /**
     * 判断玩家是否可以访问这个 Inventory，通常用来判断玩家是否过远或容器是否被破坏
     * @param player 玩家
     * @return 玩家是否可以访问这个 Inventory
     */
    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        // 较原始 PlayerInventory，移除了距离检测
        return !inventory.player.isRemoved();
    }

    /**
     * 清空受此界面影响的槽位
     */
    @Override
    public void clear() {
        for (int i = start; i < end; i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }
    }

    /**
     * 获取库存名称
     * @return 库存名称
     */
    @Override
    public Text getName() {
        // 直接调用 PlayerInventory
        return inventory.getName();
    }

}
