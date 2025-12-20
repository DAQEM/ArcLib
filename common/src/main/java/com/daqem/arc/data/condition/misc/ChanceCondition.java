package com.daqem.arc.data.condition.misc;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public class ChanceCondition extends AbstractCondition {

    private final double chance;

    public ChanceCondition(boolean inverted, double chance) {
        super(inverted);
        this.chance = chance;
    }

    @Override
    public Component getDescription() {
        return getDescription(chance + "%");
    }

    @Override
    public boolean isMet(ActionData actionData) {
        return actionData.getPlayer().arc$nextRandomDouble() * 100 <= chance;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.CHANCE;
    }

    public static class Serializer implements IConditionSerializer<ChanceCondition> {

        @Override
        public ChanceCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ChanceCondition(
                    inverted,
                    GsonHelper.getAsDouble(jsonObject, "chance")
            );
        }

        @Override
        public ChanceCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ChanceCondition(
                    inverted,
                    friendlyByteBuf.readDouble()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ChanceCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.chance);
        }
    }
}