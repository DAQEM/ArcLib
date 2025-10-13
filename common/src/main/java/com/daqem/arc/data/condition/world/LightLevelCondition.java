package com.daqem.arc.data.condition.world;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

public class LightLevelCondition extends AbstractCondition {

    private final int lightLevel;
    private final ComparisonType comparisonType;

    public LightLevelCondition(boolean inverted, int lightLevel, ComparisonType comparisonType) {
        super(inverted);
        this.lightLevel = lightLevel;
        this.comparisonType = comparisonType;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), lightLevel);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        int currentLightLevel = player.level().getLightEmission(player.blockPosition());
        return comparisonType.compare(currentLightLevel, this.lightLevel);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.LIGHT_LEVEL;
    }

    public static class Serializer implements IConditionSerializer<LightLevelCondition> {

        @Override
        public LightLevelCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new LightLevelCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "light_level"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL)
            );
        }

        @Override
        public LightLevelCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new LightLevelCondition(
                    inverted,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, LightLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.lightLevel);
            friendlyByteBuf.writeEnum(type.comparisonType);
        }
    }
}