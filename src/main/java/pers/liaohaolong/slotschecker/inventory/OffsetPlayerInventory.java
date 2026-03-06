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
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;

import java.util.stream.IntStream;

/**
 * <h3>OffsetPlayerInventory</h3>
 *
 * <p>将 PlayerInventory 的指定范围的槽位映射到 OffsetPlayerInventory 的槽位。</p>
 */
@SuppressWarnings("unused")
public class OffsetPlayerInventory implements Inventory, Nameable {

    /**
     * 被映射的 PlayerInventory
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
     * 是正序映射
     */
    private final boolean isPositiveSequence;

    /**
     * 被映射的 PlayerInventory 的起始槽位索引
     */
    private final int start;

    /**
     * 被映射的 PlayerInventory 的结束槽位索引
     */
    private final int end;

    /**
     * <p>映射时，除了可以使用正序映射参数（start > end），还可以使用逆序映射参数（start < end）。</p>
     * <ul>
     *     <li>使用正序参映射数时，被映射区间为 [start, end)</li>
     *     <li>使用逆序映射参数时，被映射区间为 [end, start)</li>
     * </ul>
     *
     * <p>示例：</p>
     * <ul>
     *     <li>当 maxSize = 27, start = 9, end = 36 时，thisInventory 的 [0,27) 对应 [9,36)</li>
     *     <li>当 maxSize = 9, start = 40, end = 36 时，thisInventory 的 [0,4) 对应 [36,40)</li>
     * </ul>
     *
     * @param inventory {@link #inventory}
     * @param maxSize {@link #maxSize}
     * @param start {@link #start}
     * @param end {@link #end}
     */
    public OffsetPlayerInventory(PlayerInventory inventory, int maxSize, int start, int end) {
        this.inventory = inventory;
        this.maxSize = maxSize;
        this.size = Math.abs(end - start);
        this.isPositiveSequence = start < end;
        this.start = start;
        this.end = end;

        if (size <= 0)
            throw new IllegalArgumentException("Size must be greater than 0.");
        if (this.maxSize < size)
            throw new IllegalArgumentException("Max size must be greater than or equal to size.");
    }

    /**
     * 此界面的槽位数量
     * @return {@link #maxSize}
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
        return getForeachStream().allMatch(i -> inventory.getStack(i).isEmpty());
    }

    /**
     * 获取指定槽位的物品（需要偏移）
     * @param slot 槽位索引
     * @return 指定槽位的 ItemStack
     */
    @Override
    public ItemStack getStack(int slot) {
        if (slot >= size)
            return new ItemStack(Items.BARRIER);
        return inventory.getStack(getOffsetSlotIndex(slot));
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
        return inventory.removeStack(getOffsetSlotIndex(slot), amount);
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
        return inventory.removeStack(getOffsetSlotIndex(slot));
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
        inventory.setStack(getOffsetSlotIndex(slot), stack);
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
        getForeachStream().forEach(i -> inventory.setStack(i, ItemStack.EMPTY));
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

    public int getMaxSize() {
        return maxSize;
    }

    public int getSize() {
        return size;
    }

    /**
     * 获取一个 IntStream，包含所有受此界面影响的 PlayerInventory 的原始槽位索引
     * @return 受此界面影响的 PlayerInventory 的原始槽位索引的 IntStream
     */
    private IntStream getForeachStream() {
        return IntStream.range(isPositiveSequence ? start : end, isPositiveSequence ? end : start);
    }

    /**
     * 根据界面槽位索引获取 PlayerInventory 的原始槽位索引
     * @param slot 界面槽位索引
     * @return PlayerInventory 的原始槽位索引
     */
    private int getOffsetSlotIndex(int slot) {
        return isPositiveSequence ? slot + start : start - slot - 1;
    }

}
