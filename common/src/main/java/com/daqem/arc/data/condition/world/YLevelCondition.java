package com.daqem.arc.data.condition.world;

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

public class YLevelCondition extends AbstractCondition {

    private final INumberProvider minY;
    private final INumberProvider maxY;

    public YLevelCondition(boolean inverted, INumberProvider minY, INumberProvider maxY) {
        super(inverted);
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public Component getDescription() {
        return getDescription(minY.getDescription(), maxY.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        double y = actionData.getPlayer().arc$getPlayer().getY();
        double resolvedMin = minY.resolve(actionData);
        double resolvedMax = maxY.resolve(actionData);

        if (resolvedMin > resolvedMax) {
            double temp = resolvedMin;
            resolvedMin = resolvedMax;
            resolvedMax = temp;
        }

        return y >= resolvedMin && y <= resolvedMax;
    }

    public INumberProvider getMaxY() {
        return maxY;
    }

    public INumberProvider getMinY() {
        return minY;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.Y_LEVEL;
    }

    public static class Serializer implements IConditionSerializer<YLevelCondition> {

        @Override
        public YLevelCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new YLevelCondition(
                    inverted,
                    getNumberProvider(jsonObject, "min_y", new ConstantNumberProvider(Integer.MIN_VALUE)),
                    getNumberProvider(jsonObject, "max_y", new ConstantNumberProvider(Integer.MAX_VALUE))
            );
        }

        @Override
        public YLevelCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new YLevelCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, YLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.minY, friendlyByteBuf);
            INumberProviderSerializer.toNetwork(type.maxY, friendlyByteBuf);
        }
    }
}