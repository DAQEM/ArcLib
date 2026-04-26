package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

public class CreateExplosionReward extends AbstractReward {

    private final INumberProvider radius;
    private final boolean causesFire;
    private final Level.ExplosionInteraction blockInteraction;

    public CreateExplosionReward(double chance, int priority, INumberProvider radius, boolean causesFire, Level.ExplosionInteraction blockInteraction) {
        super(chance, priority);
        this.radius = radius;
        this.causesFire = causesFire;
        this.blockInteraction = blockInteraction;
    }

    @Override
    public Component getDescription() {
        return getDescription(radius.getDescription(), causesFire, blockInteraction.name());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Level level = actionData.getPlayer().arc$getLevel();
        float resolvedRadius = (float) radius.resolve(actionData);
        level.explode(null, actionData.getPlayer().arc$getPlayer().getX(), actionData.getPlayer().arc$getPlayer().getY(), actionData.getPlayer().arc$getPlayer().getZ(), resolvedRadius, causesFire, blockInteraction);
        return new ActionResult();
    }

    public INumberProvider getRadius() {
        return radius;
    }

    public Level.ExplosionInteraction getBlockInteraction() {
        return blockInteraction;
    }

    public boolean causesFire() {
        return causesFire;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.CREATE_EXPLOSION;
    }

    public static class Serializer implements IRewardSerializer<CreateExplosionReward> {

        @Override
        public CreateExplosionReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new CreateExplosionReward(
                    chance,
                    priority,
                    getNumberProvider(jsonObject, "radius", new ConstantNumberProvider(1.0)),
                    GsonHelper.getAsBoolean(jsonObject, "causes_fire", false),
                    Level.ExplosionInteraction.valueOf(GsonHelper.getAsString(jsonObject, "block_interaction", "NONE").toUpperCase())
            );
        }

        @Override
        public CreateExplosionReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new CreateExplosionReward(
                    chance,
                    priority,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    friendlyByteBuf.readBoolean(),
                    friendlyByteBuf.readEnum(Level.ExplosionInteraction.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, CreateExplosionReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.radius, friendlyByteBuf);
            friendlyByteBuf.writeBoolean(type.causesFire);
            friendlyByteBuf.writeEnum(type.blockInteraction);
        }
    }
}