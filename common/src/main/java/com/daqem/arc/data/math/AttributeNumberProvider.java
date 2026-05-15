package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcEntityTarget;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeNumberProvider implements INumberProvider {

    private final ResourceLocation attributeId;
    private final ArcEntityTarget target;

    public AttributeNumberProvider(ResourceLocation attributeId, ArcEntityTarget target) {
        this.attributeId = attributeId;
        this.target = target;
    }

    @Override
    public double resolve(ActionData actionData) {
        Entity entity = target.getEntity(actionData);
        if (entity instanceof LivingEntity livingEntity) {
            Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(attributeId);
            if (attribute != null && livingEntity.getAttributes().hasAttribute(Holder.direct(attribute))) {
                return livingEntity.getAttributeValue(Holder.direct(attribute));
            }
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.attribute",
                attributeId.getPath(),
                Arc.API.translatable("entity_target." + target.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.ATTRIBUTE;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<AttributeNumberProvider> {
        @Override
        public AttributeNumberProvider fromJson(JsonObject jsonObject) {
            return new AttributeNumberProvider(
                    getResourceLocation(jsonObject, "attribute"),
                    getEntityTarget(jsonObject, "target", ArcEntityTarget.PLAYER)
            );
        }

        @Override
        public AttributeNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new AttributeNumberProvider(buf.readResourceLocation(), buf.readEnum(ArcEntityTarget.class));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, AttributeNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeResourceLocation(type.attributeId);
            buf.writeEnum(type.target);
        }
    }
}