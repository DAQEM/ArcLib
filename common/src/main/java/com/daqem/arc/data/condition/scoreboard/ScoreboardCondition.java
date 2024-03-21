package com.daqem.arc.data.condition.scoreboard;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardCondition extends AbstractCondition {

    private final String objective;
    private final int min;
    private final int max;

    public ScoreboardCondition(boolean inverted, String objective, int min, int max) {
        super(inverted);
        this.objective = objective;
        this.min = min;
        this.max = max;

        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max for ScoreboardCondition.");
        }
    }

    @Override
    public Component getDescription() {
        return getDescription(objective, min, max);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Scoreboard scoreboard = actionData.getPlayer().arc$getPlayer().getScoreboard();
        Objective objective = scoreboard.getObjective(this.objective);
        if (objective != null) {
            int score = scoreboard.getOrCreatePlayerScore(actionData.getPlayer().arc$getName(), objective).getScore();
            return score >= min && score <= max;
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.SCOREBOARD;
    }

    public static class Serializer implements IConditionSerializer<ScoreboardCondition> {

        @Override
        public ScoreboardCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new ScoreboardCondition(
                    inverted,
                    getString(jsonObject, "objective"),
                    GsonHelper.getAsInt(jsonObject, "min"),
                    GsonHelper.getAsInt(jsonObject, "max"));
        }

        @Override
        public ScoreboardCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ScoreboardCondition(
                    inverted,
                    friendlyByteBuf.readUtf(),
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ScoreboardCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.objective);
            friendlyByteBuf.writeInt(type.min);
            friendlyByteBuf.writeInt(type.max);
        }
    }
}
