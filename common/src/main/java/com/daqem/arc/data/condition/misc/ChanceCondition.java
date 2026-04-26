package com.daqem.arc.data.condition.misc;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ChanceCondition extends AbstractCondition {

    private final INumberProvider chance;

    public ChanceCondition(boolean inverted, INumberProvider chance) {
        super(inverted);
        this.chance = chance;
    }

    @Override
    public Component getDescription() {
        return getDescription(chance.toString() + "%");
    }

    @Override
    public boolean isMet(ActionData actionData) {
        return actionData.getPlayer().arc$nextRandomDouble() * 100 <= chance.resolve(actionData);
    }

    public INumberProvider getChance() {
        return chance;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.CHANCE;
    }

    public static class Serializer implements IConditionSerializer<ChanceCondition> {
        @Override
        public ChanceCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ChanceCondition(inverted, getNumberProvider(jsonObject, "chance", new ConstantNumberProvider(100.0)));
        }

        @Override
        public ChanceCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ChanceCondition(inverted, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ChanceCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.chance, buf);
        }
    }
}