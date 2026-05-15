package com.daqem.arc.data.condition.player;

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
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardCondition extends AbstractCondition {

    private final String objective;
    private final INumberProvider min;
    private final INumberProvider max;

    public ScoreboardCondition(boolean inverted, String objective, INumberProvider min, INumberProvider max) {
        super(inverted);
        this.objective = objective;
        this.min = min;
        this.max = max;
    }

    @Override
    public Component getDescription() {
        return getDescription(objective, min.getDescription(), max.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Scoreboard scoreboard = actionData.getPlayer().arc$getPlayer().level().getScoreboard();
        Objective objective = scoreboard.getObjective(this.objective);
        if (objective != null) {
            int score = scoreboard.getOrCreatePlayerScore((ScoreHolder) actionData.getPlayer(), objective).get();

            double resolvedMin = min.resolve(actionData);
            double resolvedMax = max.resolve(actionData);

            if (resolvedMin > resolvedMax) {
                double temp = resolvedMin;
                resolvedMin = resolvedMax;
                resolvedMax = temp;
            }

            return score >= resolvedMin && score <= resolvedMax;
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.SCOREBOARD;
    }

    public String getObjective() {
        return objective;
    }

    public INumberProvider getMin() {
        return min;
    }

    public INumberProvider getMax() {
        return max;
    }

    public static class Serializer implements IConditionSerializer<ScoreboardCondition> {

        @Override
        public ScoreboardCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new ScoreboardCondition(
                    inverted,
                    getString(jsonObject, "objective"),
                    getNumberProvider(jsonObject, "min", new ConstantNumberProvider(Integer.MIN_VALUE)),
                    getNumberProvider(jsonObject, "max", new ConstantNumberProvider(Integer.MAX_VALUE))
            );
        }

        @Override
        public ScoreboardCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ScoreboardCondition(
                    inverted,
                    friendlyByteBuf.readUtf(),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ScoreboardCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.objective);
            INumberProviderSerializer.toNetwork(type.min, friendlyByteBuf);
            INumberProviderSerializer.toNetwork(type.max, friendlyByteBuf);
        }
    }
}