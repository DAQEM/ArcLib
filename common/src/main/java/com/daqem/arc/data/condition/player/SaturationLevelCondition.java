package com.daqem.arc.data.condition.player;

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

public class SaturationLevelCondition extends AbstractCondition {

    private final float saturationLevel;
    private final ComparisonType comparisonType;

    public SaturationLevelCondition(boolean inverted, float saturationLevel, ComparisonType comparisonType) {
        super(inverted);
        this.saturationLevel = saturationLevel;
        this.comparisonType = comparisonType;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), saturationLevel);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return comparisonType.compare(player.getFoodData().getSaturationLevel(), this.saturationLevel);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.SATURATION_LEVEL;
    }

    public static class Serializer implements IConditionSerializer<SaturationLevelCondition> {

        @Override
        public SaturationLevelCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new SaturationLevelCondition(
                    inverted,
                    GsonHelper.getAsFloat(jsonObject, "saturation_level"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL)
            );
        }

        @Override
        public SaturationLevelCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new SaturationLevelCondition(
                    inverted,
                    friendlyByteBuf.readFloat(),
                    friendlyByteBuf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SaturationLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeFloat(type.saturationLevel);
            friendlyByteBuf.writeEnum(type.comparisonType);
        }
    }
}