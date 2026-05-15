package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.ComparisonType;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class SaturationLevelCondition extends AbstractCondition {

    private final INumberProvider saturationLevel;
    private final ComparisonType comparisonType;

    public SaturationLevelCondition(boolean inverted, INumberProvider saturationLevel, ComparisonType comparisonType) {
        super(inverted);
        this.saturationLevel = saturationLevel;
        this.comparisonType = comparisonType;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), saturationLevel.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return comparisonType.compare(player.getFoodData().getSaturationLevel(), this.saturationLevel.resolve(actionData));
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getSaturationLevel() {
        return saturationLevel;
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
                    getNumberProvider(jsonObject, "saturation_level", new ConstantNumberProvider(0.0)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL)
            );
        }

        @Override
        public SaturationLevelCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new SaturationLevelCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    friendlyByteBuf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SaturationLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.saturationLevel, friendlyByteBuf);
            friendlyByteBuf.writeEnum(type.comparisonType);
        }
    }
}