package com.daqem.arc.data.condition.scoreboard;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
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
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Scoreboard scoreboard = actionData.getPlayer().getPlayer().getScoreboard();
        Objective objective = scoreboard.getObjective(this.objective);
        if (objective != null) {
            int score = scoreboard.getOrCreatePlayerScore(actionData.getPlayer().name(), objective).getScore();
            return score >= min && score <= max;
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.SCOREBOARD;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.SCOREBOARD;
    }

    public static class Serializer implements ConditionSerializer<ScoreboardCondition> {

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
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.objective);
            friendlyByteBuf.writeInt(type.min);
            friendlyByteBuf.writeInt(type.max);
        }
    }
}
