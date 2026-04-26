package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcEnchantment;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class HasEnchantmentCondition extends AbstractCondition {

    private final ArcEnchantment enchantment;
    private final ComparisonType comparisonType;
    private final ArcItemTarget target;

    public HasEnchantmentCondition(boolean inverted, ArcEnchantment enchantment, ComparisonType comparisonType, ArcItemTarget target) {
        super(inverted);
        this.enchantment = enchantment;
        this.comparisonType = comparisonType;
        this.target = target;
    }

    @Override
    public Component getDescription() {
        return getDescription(Enchantment.getFullname(enchantment.enchantment(), enchantment.level()), comparisonType.getSymbol());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = target.getItemStack(actionData, actionData.getPlayer().arc$getPlayer());
        if (itemStack != null && !itemStack.isEmpty()) {
            int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment.enchantment(), itemStack);
            return comparisonType.compare(enchantmentLevel, enchantment.level());
        }
        return false;
    }

    public ArcEnchantment getEnchantment() {
        return enchantment;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public ArcItemTarget getTarget() {
        return target;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.HAS_ENCHANTMENT;
    }

    public static class Serializer implements IConditionSerializer<HasEnchantmentCondition> {

        @Override
        public HasEnchantmentCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new HasEnchantmentCondition(
                    inverted,
                    getEnchantment(jsonObject, "enchantment"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    getItemTarget(jsonObject, "target", ArcItemTarget.ACTION)
            );
        }

        @Override
        public HasEnchantmentCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new HasEnchantmentCondition(
                    inverted,
                    ArcEnchantment.STREAM_CODEC.decode(buf),
                    buf.readEnum(ComparisonType.class),
                    buf.readEnum(ArcItemTarget.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, HasEnchantmentCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            ArcEnchantment.STREAM_CODEC.encode(buf, type.enchantment);
            buf.writeEnum(type.comparisonType);
            buf.writeEnum(type.target);
        }
    }
}