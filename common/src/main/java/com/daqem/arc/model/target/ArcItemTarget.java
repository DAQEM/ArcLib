package com.daqem.arc.model.target;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.data.ActionData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum ArcItemTarget {
    HELMET(EquipmentSlot.HEAD),
    CHESTPLATE(EquipmentSlot.CHEST),
    LEGGINGS(EquipmentSlot.LEGS),
    BOOTS(EquipmentSlot.FEET),
    MAIN_HAND(EquipmentSlot.MAINHAND),
    OFF_HAND(EquipmentSlot.OFFHAND),
    BODY(EquipmentSlot.BODY),
    SADDLE(EquipmentSlot.SADDLE),
    ACTION(null);

    private final EquipmentSlot equipmentSlot;

    ArcItemTarget(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public ItemStack getItemStack(ActionData actionData, LivingEntity livingEntity) {
        if (this == ACTION) {
            ItemStack stack = actionData.getData(IActionDataType.ITEM_STACK);
            if (stack == null || stack.isEmpty()) {
                Item item = actionData.getData(IActionDataType.ITEM);
                if (item != null) {
                    return new ItemStack(item);
                }
                return ItemStack.EMPTY;
            }
            return stack;
        }
        return livingEntity.getItemBySlot(this.equipmentSlot);
    }

    public EquipmentSlot getEquipmentSlot(ActionData actionData, LivingEntity livingEntity) {
        if (this == ACTION) {
            ItemStack stack = actionData.getData(IActionDataType.ITEM_STACK);
            if (stack == null || stack.isEmpty()) {
                return null;
            }
            return livingEntity.getEquipmentSlotForItem(stack);
        }
        return this.equipmentSlot;
    }
}
