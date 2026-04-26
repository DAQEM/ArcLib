package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
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

    private final INumberProvider amount;
    private final boolean isPercentage;

    public RepairAllArmorReward(double chance, int priority, INumberProvider amount, boolean isPercentage) {
        super(chance, priority);
        this.amount = amount;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount.getDescription(), (isPercentage ? "%" : ""));
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        List<ItemStack> armorItems = Stream.of(
                player.getItemBySlot(EquipmentSlot.HEAD),
                player.getItemBySlot(EquipmentSlot.CHEST),
                player.getItemBySlot(EquipmentSlot.LEGS),
                player.getItemBySlot(EquipmentSlot.FEET)
        ).filter(ItemStack::isDamageableItem).toList();

        double resolvedAmount = amount.resolve(actionData);

        for (ItemStack stack : armorItems) {
            int repairAmount = (int) Math.round(resolvedAmount);
            if (isPercentage) {
                repairAmount = (int) (stack.getMaxDamage() * (resolvedAmount / 100.0));
            }
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - repairAmount));
        }
        return new ActionResult();
    }

    public INumberProvider getAmount() {
        return amount;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.REPAIR_ALL_ARMOR;
    }

    public static class Serializer implements IRewardSerializer<RepairAllArmorReward> {
        @Override
        public RepairAllArmorReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new RepairAllArmorReward(
                    chance, priority,
                    getNumberProvider(jsonObject, "amount", new ConstantNumberProvider(1.0)),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public RepairAllArmorReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new RepairAllArmorReward(
                    chance, priority,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, RepairAllArmorReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.amount, buf);
            buf.writeBoolean(type.isPercentage);
        }
    }
}