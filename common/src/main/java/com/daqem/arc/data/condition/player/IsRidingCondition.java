package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class IsRidingCondition extends AbstractCondition {

    public IsRidingCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.isPassenger();
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.IS_RIDING;
    }

    public static class Serializer implements IConditionSerializer<IsRidingCondition> {

        @Override
        public IsRidingCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new IsRidingCondition(inverted);
        }

        @Override
        public IsRidingCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new IsRidingCondition(inverted);
        }
    }
}