package com.daqem.arc.data.condition.team;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.daqem.arc.api.player.ArcPlayer;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.Team;

public class TeamCondition extends AbstractCondition {

    private final String team;

    public TeamCondition(boolean inverted, String team) {
        super(inverted);
        this.team = team;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        Team team = player.arc$getPlayer().getTeam();
        return team != null && team.getName().equals(this.team);
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.TEAM;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.TEAM;
    }

    public static class Serializer implements ConditionSerializer<TeamCondition> {

        @Override
        public TeamCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new TeamCondition(
                    inverted,
                    getString(jsonObject, "team"));
        }

        @Override
        public TeamCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new TeamCondition(
                    inverted,
                    friendlyByteBuf.readUtf());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, TeamCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.team);
        }
    }
}
