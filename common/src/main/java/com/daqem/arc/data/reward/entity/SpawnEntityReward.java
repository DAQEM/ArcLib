package com.daqem.arc.data.reward.entity;

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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class SpawnEntityReward extends AbstractReward {

    private final EntityType<?> entityType;
    private final INumberProvider count;

    public SpawnEntityReward(double chance, int priority, EntityType<?> entityType, INumberProvider count) {
        super(chance, priority);
        this.entityType = entityType;
        this.count = count;
    }

    @Override
    public Component getDescription() {
        return getDescription(count.toString(), entityType.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        if (player.level() instanceof ServerLevel serverLevel) {
            int resolvedCount = (int) Math.round(count.resolve(actionData));
            for (int i = 0; i < resolvedCount; i++) {
                Entity entity = entityType.create(serverLevel, EntitySpawnReason.EVENT);
                if (entity != null) {
                    entity.moveOrInterpolateTo(player.position(), player.getYRot(), player.getXRot());
                    serverLevel.addFreshEntity(entity);
                }
            }
        }
        return new ActionResult();
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public INumberProvider getCount() {
        return count;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.SPAWN_ENTITY;
    }

    public static class Serializer implements IRewardSerializer<SpawnEntityReward> {

        @Override
        public SpawnEntityReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new SpawnEntityReward(
                    chance,
                    priority,
                    getEntityType(jsonObject, "entity_type"),
                    getNumberProvider(jsonObject, "count", new ConstantNumberProvider(1.0))
            );
        }

        @Override
        public SpawnEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new SpawnEntityReward(
                    chance,
                    priority,
                    EntityType.STREAM_CODEC.decode(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SpawnEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            EntityType.STREAM_CODEC.encode(friendlyByteBuf, type.entityType);
            INumberProviderSerializer.toNetwork(type.count, friendlyByteBuf);
        }
    }
}