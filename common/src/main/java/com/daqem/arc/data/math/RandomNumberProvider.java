package com.daqem.arc.data.math;

import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class RandomNumberProvider implements INumberProvider {

    private final INumberProvider min;
    private final INumberProvider max;

    public RandomNumberProvider(INumberProvider min, INumberProvider max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public double resolve(ActionData actionData) {
        double minVal = min.resolve(actionData);
        double maxVal = max.resolve(actionData);

        if (minVal > maxVal) {
            double temp = minVal;
            minVal = maxVal;
            maxVal = temp;
        }

        return minVal + (actionData.getPlayer().arc$nextRandomDouble() * (maxVal - minVal));
    }

    @Override
    public String toString() {
        return "Random";
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.RANDOM;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<RandomNumberProvider> {

        @Override
        public RandomNumberProvider fromJson(JsonObject jsonObject) {
            return new RandomNumberProvider(
                    getNumberProvider(jsonObject, "min", new ConstantNumberProvider(0.0)),
                    getNumberProvider(jsonObject, "max", new ConstantNumberProvider(1.0))
            );
        }

        @Override
        public RandomNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new RandomNumberProvider(
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    INumberProviderSerializer.fromNetworkStatic(buf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, RandomNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.min, buf);
            INumberProviderSerializer.toNetwork(type.max, buf);
        }
    }
}