package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.Team;

public class TeamCondition extends AbstractCondition {

    private final String team;

    public TeamCondition(boolean inverted, String team) {
        super(inverted);
        this.team = team;
    }

    @Override
    public Component getDescription() {
        return getDescription(team);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        Team team = player.arc$getPlayer().getTeam();
        return team != null && team.getName().equals(this.team);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.TEAM;
    }

    public String getTeam() {
        return team;
    }

    public static class Serializer implements IConditionSerializer<TeamCondition> {

        @Override
        public TeamCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new TeamCondition(
                    inverted,
                    getString(jsonObject, "team"));
        }

        @Override
        public TeamCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new TeamCondition(
                    inverted,
                    friendlyByteBuf.readUtf());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, TeamCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.team);
        }
    }
}
