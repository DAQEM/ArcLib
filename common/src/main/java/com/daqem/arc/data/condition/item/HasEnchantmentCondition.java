package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcEnchantment;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class HasEnchantmentCondition extends AbstractCondition {

    private final ArcEnchantment enchantment;
    private final ComparisonType comparisonType;

    public HasEnchantmentCondition(boolean inverted, ArcEnchantment enchantment, ComparisonType comparisonType) {
        super(inverted);
        this.enchantment = enchantment;
        this.comparisonType = comparisonType;
    }

    @Override
    public Component getDescription() {
        return getDescription(Enchantment.getFullname(enchantment.enchantment(), enchantment.level()), comparisonType.getSymbol());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = actionData.getData(IActionDataType.ITEM_STACK);
        if (itemStack != null && !itemStack.isEmpty()) {
            int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment.enchantment(), itemStack);
            return comparisonType.compare(enchantmentLevel, enchantment.level());
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.HAS_ENCHANTMENT;
    }

    public static class Serializer implements IConditionSerializer<HasEnchantmentCondition> {

        @Override
        public HasEnchantmentCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new HasEnchantmentCondition(
                    inverted,
                    getEnchantment(jsonObject, "enchantment"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL)
            );
        }

        @Override
        public HasEnchantmentCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new HasEnchantmentCondition(
                    inverted,
                    ArcEnchantment.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, HasEnchantmentCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            ArcEnchantment.STREAM_CODEC.encode(friendlyByteBuf, type.enchantment);
            friendlyByteBuf.writeEnum(type.comparisonType);
        }
    }
}