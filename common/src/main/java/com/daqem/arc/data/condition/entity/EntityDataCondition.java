package com.daqem.arc.data.condition.entity;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.EntityDataProperty;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityDataCondition extends AbstractCondition {

    private final List<EntityDataProperty> properties;
    private final String target;

    public EntityDataCondition(boolean inverted, List<EntityDataProperty> properties, String target) {
        super(inverted);
        this.properties = properties;
        this.target = target;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Entity entity = getTargetEntity(actionData, target);
        if (entity == null) {
            return false;
        }
        return properties.stream().allMatch(p -> p.matches(entity));
    }

    @Nullable
    private Entity getTargetEntity(ActionData actionData, String target) {
        return switch (target) {
            case "player" -> actionData.getPlayer().arc$getPlayer();
            case "entity" -> actionData.getData(IActionDataType.ENTITY);
            default -> null;
        };
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ENTITY_DATA;
    }

    public static class Serializer implements IConditionSerializer<EntityDataCondition> {
        @Override
        public EntityDataCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            List<EntityDataProperty> properties = getEntityDataProperties(jsonObject, "properties");
            String target = getString(jsonObject, "target", "player");
            return new EntityDataCondition(inverted, properties, target);
        }

        @Override
        public EntityDataCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            List<EntityDataProperty> properties = EntityDataProperty.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf);
            String target = buf.readUtf();
            return new EntityDataCondition(inverted, properties, target);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EntityDataCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            EntityDataProperty.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, type.properties);
            buf.writeUtf(type.target);
        }
    }
}