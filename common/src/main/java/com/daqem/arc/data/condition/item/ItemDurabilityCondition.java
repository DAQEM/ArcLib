package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class ItemDurabilityCondition extends AbstractCondition {

    private final INumberProvider durability;
    private final ComparisonType comparisonType;
    private final boolean isPercentage;
    private final ArcItemTarget target;

    public ItemDurabilityCondition(boolean inverted, INumberProvider durability, ComparisonType comparisonType, boolean isPercentage, ArcItemTarget target) {
        super(inverted);
        this.durability = durability;
        this.comparisonType = comparisonType;
        this.isPercentage = isPercentage;
        this.target = target;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), durability.getDescription(), (isPercentage ? "%" : ""));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = target.getItemStack(actionData, actionData.getPlayer().arc$getPlayer());
        if (itemStack != null && itemStack.isDamageableItem()) {
            double currentDurability = itemStack.getMaxDamage() - itemStack.getDamageValue();
            if (isPercentage) {
                currentDurability = (currentDurability / itemStack.getMaxDamage()) * 100.0;
            }
            return comparisonType.compare(currentDurability, this.durability.resolve(actionData));
        }
        return false;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getDurability() {
        return durability;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public ArcItemTarget getTarget() {
        return target;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM_DURABILITY;
    }

    public static class Serializer implements IConditionSerializer<ItemDurabilityCondition> {
        @Override
        public ItemDurabilityCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ItemDurabilityCondition(
                    inverted,
                    getNumberProvider(jsonObject, "durability", new ConstantNumberProvider(0.0)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false),
                    getItemTarget(jsonObject, "target", ArcItemTarget.ACTION)
            );
        }

        @Override
        public ItemDurabilityCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ItemDurabilityCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class),
                    buf.readBoolean(),
                    buf.readEnum(ArcItemTarget.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemDurabilityCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.durability, buf);
            buf.writeEnum(type.comparisonType);
            buf.writeBoolean(type.isPercentage);
            buf.writeEnum(type.target);
        }
    }
}