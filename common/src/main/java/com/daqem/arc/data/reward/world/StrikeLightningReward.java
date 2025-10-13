package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcPositionTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StrikeLightningReward extends AbstractReward {

    private final ArcPositionTarget positionTarget;
    private final boolean visualOnly;

    public StrikeLightningReward(double chance, int priority, ArcPositionTarget positionTarget, boolean visualOnly) {
        super(chance, priority);
        this.positionTarget = positionTarget;
        this.visualOnly = visualOnly;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        Level level = player.level();
        if (level.isClientSide()) {
            return new ActionResult();
        }
        Vec3 position = positionTarget.getPosition(actionData);
        if (position != null) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.EVENT);
            if (lightningBolt != null) {
                lightningBolt.snapTo(new Vec3(position.x, Math.floor(position.y), position.z));
                lightningBolt.setVisualOnly(visualOnly);
                level.addFreshEntity(lightningBolt);
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.STRIKE_LIGHTNING;
    }

    public static class Serializer implements IRewardSerializer<StrikeLightningReward> {

        @Override
        public StrikeLightningReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new StrikeLightningReward(
                    chance,
                    priority,
                    getPositionTarget(jsonObject, "position"),
                    GsonHelper.getAsBoolean(jsonObject, "visual_only", false)
            );
        }

        @Override
        public StrikeLightningReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new StrikeLightningReward(
                    chance,
                    priority,
                    friendlyByteBuf.readEnum(ArcPositionTarget.class),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, StrikeLightningReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeEnum(type.positionTarget);
            friendlyByteBuf.writeBoolean(type.visualOnly);
        }
    }
}