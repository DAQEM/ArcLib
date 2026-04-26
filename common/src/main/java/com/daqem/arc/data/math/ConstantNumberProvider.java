package com.daqem.arc.data.math;

import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class ConstantNumberProvider implements INumberProvider {
    private final double value;

    public ConstantNumberProvider(double value) {
        this.value = value;
    }

    @Override
    public double resolve(ActionData actionData) {
        return value;
    }

    @Override
    public String toString() {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.CONSTANT;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<ConstantNumberProvider> {
        @Override
        public ConstantNumberProvider fromJson(JsonObject jsonObject) {
            return new ConstantNumberProvider(GsonHelper.getAsDouble(jsonObject, "value"));
        }

        @Override
        public ConstantNumberProvider fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
            return new ConstantNumberProvider(friendlyByteBuf.readDouble());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ConstantNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.value);
        }
    }
}