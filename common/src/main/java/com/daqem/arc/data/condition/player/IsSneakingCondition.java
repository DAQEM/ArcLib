package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class IsSneakingCondition extends AbstractCondition {

    public IsSneakingCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.isCrouching();
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.IS_SNEAKING;
    }

    public static class Serializer implements IConditionSerializer<IsSneakingCondition> {

        @Override
        public IsSneakingCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new IsSneakingCondition(inverted);
        }

        @Override
        public IsSneakingCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new IsSneakingCondition(inverted);
        }
    }
}