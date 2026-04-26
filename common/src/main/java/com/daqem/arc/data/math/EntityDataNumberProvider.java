package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcEntityTarget;
import com.daqem.arc.registry.EntityDataRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class EntityDataNumberProvider implements INumberProvider {

    private final ArcEntityTarget target;
    private final Identifier property;

    public EntityDataNumberProvider(ArcEntityTarget target, Identifier property) {
        this.target = target;
        this.property = property;
    }

    @Override
    public double resolve(ActionData actionData) {
        Entity entity = target.getEntity(actionData);

        if (entity != null) {
            return EntityDataRegistry.get(property)
                    .map(resolver -> {
                        Object val = resolver.getDataFetcher().apply(entity);
                        if (val instanceof Number n) return n.doubleValue();
                        if (val instanceof Boolean b) return b ? 1.0 : 0.0;
                        if (val instanceof Optional<?> opt && opt.isPresent()) {
                            Object inner = opt.get();
                            if (inner instanceof Number n) return n.doubleValue();
                            if (inner instanceof Boolean b) return b ? 1.0 : 0.0;
                        }
                        return 0.0;
                    }).orElse(0.0);
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.entity_data",
                Arc.API.translatable("entity_target." + target.name().toLowerCase()),
                Arc.API.translatable("entity_data_property." + property.getPath())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.ENTITY_DATA;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<EntityDataNumberProvider> {

        @Override
        public EntityDataNumberProvider fromJson(JsonObject jsonObject) {
            return new EntityDataNumberProvider(
                    getEntityTarget(jsonObject, "target", ArcEntityTarget.PLAYER),
                    getIdentifier(jsonObject, "property")
            );
        }

        @Override
        public EntityDataNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new EntityDataNumberProvider(
                    buf.readEnum(ArcEntityTarget.class),
                    buf.readIdentifier()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EntityDataNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeEnum(type.target);
            buf.writeIdentifier(type.property);
        }
    }
}