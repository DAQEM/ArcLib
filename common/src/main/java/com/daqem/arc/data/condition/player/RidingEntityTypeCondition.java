package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class RidingEntityTypeCondition extends AbstractCondition {

    private final EntityType<?> entityType;

    public RidingEntityTypeCondition(boolean inverted, EntityType<?> entityType) {
        super(inverted);
        this.entityType = entityType;
    }

    @Override
    public Component getDescription() {
        return getDescription(entityType.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        Entity vehicle = player.getVehicle();
        return vehicle != null && vehicle.getType() == this.entityType;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.RIDING_ENTITY_TYPE;
    }

    public static class Serializer implements IConditionSerializer<RidingEntityTypeCondition> {

        @Override
        public RidingEntityTypeCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new RidingEntityTypeCondition(inverted, getEntityType(jsonObject, "entity_type"));
        }

        @Override
        public RidingEntityTypeCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new RidingEntityTypeCondition(inverted, EntityType.STREAM_CODEC.decode(friendlyByteBuf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, RidingEntityTypeCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            EntityType.STREAM_CODEC.encode(friendlyByteBuf, type.entityType);
        }
    }
}