package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcEntityTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardNumberProvider implements INumberProvider {

    private final String objectiveName;
    private final ArcEntityTarget target;

    public ScoreboardNumberProvider(String objectiveName, ArcEntityTarget target) {
        this.objectiveName = objectiveName;
        this.target = target;
    }

    @Override
    public double resolve(ActionData actionData) {
        Entity entity = target.getEntity(actionData);
        if (entity instanceof ScoreHolder scoreHolder) {
            Scoreboard scoreboard = entity.level().getScoreboard();
            Objective objective = scoreboard.getObjective(objectiveName);
            if (objective != null) {
                return scoreboard.getOrCreatePlayerScore(scoreHolder, objective).get();
            }
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.scoreboard",
                objectiveName,
                Arc.API.translatable("entity_target." + target.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.SCOREBOARD;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<ScoreboardNumberProvider> {
        @Override
        public ScoreboardNumberProvider fromJson(JsonObject jsonObject) {
            return new ScoreboardNumberProvider(
                    getString(jsonObject, "objective"),
                    getEntityTarget(jsonObject, "target", ArcEntityTarget.PLAYER)
            );
        }

        @Override
        public ScoreboardNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new ScoreboardNumberProvider(buf.readUtf(), buf.readEnum(ArcEntityTarget.class));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ScoreboardNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeUtf(type.objectiveName);
            buf.writeEnum(type.target);
        }
    }
}