package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class IsUnderwaterCondition extends AbstractCondition {

    public IsUnderwaterCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.isUnderWater();
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.IS_UNDERWATER;
    }

    public static class Serializer implements IConditionSerializer<IsUnderwaterCondition> {

        @Override
        public IsUnderwaterCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new IsUnderwaterCondition(inverted);
        }

        @Override
        public IsUnderwaterCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new IsUnderwaterCondition(inverted);
        }
    }
}