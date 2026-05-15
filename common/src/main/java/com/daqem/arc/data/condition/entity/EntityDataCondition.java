package com.daqem.arc.data.condition.entity;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.EntityDataProperty;
import com.daqem.arc.model.target.ArcEntityTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class EntityDataCondition extends AbstractCondition {

    private final List<EntityDataProperty> properties;
    private final ArcEntityTarget target;

    public EntityDataCondition(boolean inverted, List<EntityDataProperty> properties, ArcEntityTarget target) {
        super(inverted);
        this.properties = properties;
        this.target = target;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Entity entity = target.getEntity(actionData);
        if (entity == null) {
            return false;
        }
        return properties.stream().allMatch(p -> p.matches(entity));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ENTITY_DATA;
    }

    public static class Serializer implements IConditionSerializer<EntityDataCondition> {
        @Override
        public EntityDataCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            List<EntityDataProperty> properties = getEntityDataProperties(jsonObject, "properties");
            ArcEntityTarget target = getEntityTarget(jsonObject, "target", ArcEntityTarget.PLAYER);
            return new EntityDataCondition(inverted, properties, target);
        }

        @Override
        public EntityDataCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf buf, boolean inverted) {
            List<EntityDataProperty> properties = EntityDataProperty.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf);
            ArcEntityTarget target = buf.readEnum(ArcEntityTarget.class);
            return new EntityDataCondition(inverted, properties, target);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EntityDataCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            EntityDataProperty.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, type.properties);
            buf.writeEnum(type.target);
        }
    }
}