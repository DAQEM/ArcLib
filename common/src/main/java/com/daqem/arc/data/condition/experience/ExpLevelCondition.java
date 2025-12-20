package com.daqem.arc.data.condition.experience;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public class ExpLevelCondition extends AbstractCondition {

    private final int level;

    public ExpLevelCondition(boolean inverted, int level) {
        super(inverted);
        this.level = level;
    }

    @Override
    public Component getDescription() {
        return getDescription(level);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Integer expLevel = actionData.getData(IActionDataType.EXP_LEVEL);
        return expLevel != null && expLevel == this.level;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.EXP_LEVEL;
    }

    public int getLevel() {
        return level;
    }

    public static class Serializer implements IConditionSerializer<ExpLevelCondition> {

        @Override
        public ExpLevelCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ExpLevelCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "level"));
        }

        @Override
        public ExpLevelCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ExpLevelCondition(
                    inverted,
                    friendlyByteBuf.readVarInt());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ExpLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.level);
        }
    }
}
