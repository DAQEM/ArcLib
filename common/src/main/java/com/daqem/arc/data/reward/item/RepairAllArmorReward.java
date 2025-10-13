package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public class RepairAllArmorReward extends AbstractReward {

    private final int amount;
    private final boolean isPercentage;

    public RepairAllArmorReward(double chance, int priority, int amount, boolean isPercentage) {
        super(chance, priority);
        this.amount = amount;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount + (isPercentage ? "%" : ""));
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        List<ItemStack> armorItems = Stream.of(helmet, chestplate, leggings, boots)
                .filter(ItemStack::isDamageableItem)
                .toList();
        for (ItemStack stack : armorItems) {
            int repairAmount = amount;
            if (isPercentage) {
                repairAmount = (int) (stack.getMaxDamage() * (amount / 100.0));
            }
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - repairAmount));
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.REPAIR_ALL_ARMOR;
    }

    public static class Serializer implements IRewardSerializer<RepairAllArmorReward> {

        @Override
        public RepairAllArmorReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new RepairAllArmorReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "amount"),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public RepairAllArmorReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new RepairAllArmorReward(
                    chance,
                    priority,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, RepairAllArmorReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.amount);
            friendlyByteBuf.writeBoolean(type.isPercentage);
        }
    }
}