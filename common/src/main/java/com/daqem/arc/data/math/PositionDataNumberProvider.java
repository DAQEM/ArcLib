package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.PositionDataProperty;
import com.daqem.arc.model.target.ArcPositionTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;

public class PositionDataNumberProvider implements INumberProvider {

    private final PositionDataProperty property;
    private final ArcPositionTarget target;

    public PositionDataNumberProvider(PositionDataProperty property, ArcPositionTarget target) {
        this.property = property;
        this.target = target;
    }

    @Override
    public double resolve(ActionData actionData) {
        Vec3 pos = target.getPosition(actionData);
        if (pos != null) {
            return property.getValue(actionData, pos);
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.position_data",
                Arc.API.translatable("position_data_property." + property.name().toLowerCase()),
                Arc.API.translatable("position_target." + target.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.POSITION_DATA;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<PositionDataNumberProvider> {
        @Override
        public PositionDataNumberProvider fromJson(JsonObject jsonObject) {
            String propName = getOptionalString(jsonObject, "property");
            PositionDataProperty property = PositionDataProperty.Y;
            if (propName != null) {
                try {
                    property = PositionDataProperty.valueOf(propName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unknown PositionDataProperty: " + propName + ". Options: " + Arrays.toString(PositionDataProperty.values()));
                }
            }
            return new PositionDataNumberProvider(property, getPositionTarget(jsonObject, "target", ArcPositionTarget.PLAYER));
        }

        @Override
        public PositionDataNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new PositionDataNumberProvider(buf.readEnum(PositionDataProperty.class), buf.readEnum(ArcPositionTarget.class));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, PositionDataNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeEnum(type.property);
            buf.writeEnum(type.target);
        }
    }
}