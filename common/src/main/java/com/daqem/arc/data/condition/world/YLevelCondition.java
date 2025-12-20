package com.daqem.arc.data.condition.world;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public class YLevelCondition extends AbstractCondition {

    private final int minY;
    private final int maxY;

    public YLevelCondition(boolean inverted, int minY, int maxY) {
        super(inverted);
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public Component getDescription() {
        return getDescription(minY, maxY);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        double y = actionData.getPlayer().arc$getPlayer().getY();
        return y >= minY && y <= maxY;
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
                    GsonHelper.getAsInt(jsonObject, "min_y", Integer.MIN_VALUE),
                    GsonHelper.getAsInt(jsonObject, "max_y", Integer.MAX_VALUE)
            );
        }

        @Override
        public YLevelCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new YLevelCondition(
                    inverted,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readVarInt()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, YLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.minY);
            friendlyByteBuf.writeVarInt(type.maxY);
        }
    }
}