package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcPositionTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.phys.Vec3;

public class DistanceNumberProvider implements INumberProvider {

    private final ArcPositionTarget from;
    private final ArcPositionTarget to;
    private final boolean ignoreY;

    public DistanceNumberProvider(ArcPositionTarget from, ArcPositionTarget to, boolean ignoreY) {
        this.from = from;
        this.to = to;
        this.ignoreY = ignoreY;
    }

    @Override
    public double resolve(ActionData actionData) {
        Vec3 posFrom = from.getPosition(actionData);
        Vec3 posTo = to.getPosition(actionData);

        if (posFrom != null && posTo != null) {
            if (ignoreY) {
                double dx = posFrom.x() - posTo.x();
                double dz = posFrom.z() - posTo.z();
                return Math.sqrt(dx * dx + dz * dz);
            } else {
                return posFrom.distanceTo(posTo);
            }
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.distance",
                Arc.API.translatable("position_target." + from.name().toLowerCase()),
                Arc.API.translatable("position_target." + to.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.DISTANCE;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<DistanceNumberProvider> {
        @Override
        public DistanceNumberProvider fromJson(JsonObject jsonObject) {
            return new DistanceNumberProvider(
                    getPositionTarget(jsonObject, "from", ArcPositionTarget.PLAYER),
                    getPositionTarget(jsonObject, "to", ArcPositionTarget.ENTITY),
                    GsonHelper.getAsBoolean(jsonObject, "ignore_y", false)
            );
        }

        @Override
        public DistanceNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new DistanceNumberProvider(
                    buf.readEnum(ArcPositionTarget.class),
                    buf.readEnum(ArcPositionTarget.class),
                    buf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, DistanceNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeEnum(type.from);
            buf.writeEnum(type.to);
            buf.writeBoolean(type.ignoreY);
        }
    }
}