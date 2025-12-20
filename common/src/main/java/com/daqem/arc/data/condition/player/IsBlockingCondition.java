package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class IsBlockingCondition extends AbstractCondition {

    public IsBlockingCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.isBlocking();
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.IS_BLOCKING;
    }

    public static class Serializer implements IConditionSerializer<IsBlockingCondition> {

        @Override
        public IsBlockingCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new IsBlockingCondition(inverted);
        }

        @Override
        public IsBlockingCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new IsBlockingCondition(inverted);
        }
    }
}