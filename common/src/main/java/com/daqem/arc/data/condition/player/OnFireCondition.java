package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class OnFireCondition extends AbstractCondition {

    public OnFireCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.isOnFire();
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ON_FIRE;
    }

    public static class Serializer implements IConditionSerializer<OnFireCondition> {

        @Override
        public OnFireCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new OnFireCondition(inverted);
        }

        @Override
        public OnFireCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new OnFireCondition(inverted);
        }
    }
}