package com.daqem.arc.data.math;

import com.daqem.arc.api.math.MathOperator;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class CalculateNumberProvider implements INumberProvider {
    private final INumberProvider base;
    private final List<Modifier> modifiers;

    public CalculateNumberProvider(INumberProvider base, List<Modifier> modifiers) {
        this.base = base;
        this.modifiers = modifiers;
    }

    @Override
    public double resolve(ActionData actionData) {
        double result = base.resolve(actionData);
        for (Modifier modifier : modifiers) {
            result = modifier.operator().apply(result, modifier.value().resolve(actionData));
        }
        return result;
    }

    @Override
    public String toString() {
        return "Dynamic";
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.CALCULATE;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public record Modifier(MathOperator operator, INumberProvider value) {}

    public static class Serializer implements INumberProviderSerializer<CalculateNumberProvider> {

        @Override
        public CalculateNumberProvider fromJson(JsonObject jsonObject) {
            INumberProvider base = getNumberProvider(jsonObject, "base");
            List<Modifier> modifiers = new ArrayList<>();
            if (jsonObject.has("modifiers")) {
                JsonArray modArray = jsonObject.getAsJsonArray("modifiers");
                for (JsonElement el : modArray) {
                    JsonObject modObj = el.getAsJsonObject();
                    MathOperator op = getMathOperator(modObj, "operator");
                    INumberProvider val = getNumberProvider(modObj, "value");
                    modifiers.add(new Modifier(op, val));
                }
            }
            return new CalculateNumberProvider(base, modifiers);
        }

        @Override
        public CalculateNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            INumberProvider base = INumberProviderSerializer.fromNetworkStatic(buf);
            int size = buf.readVarInt();
            List<Modifier> modifiers = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                MathOperator op = buf.readEnum(MathOperator.class);
                INumberProvider val = INumberProviderSerializer.fromNetworkStatic(buf);
                modifiers.add(new Modifier(op, val));
            }
            return new CalculateNumberProvider(base, modifiers);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, CalculateNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.base, buf);
            buf.writeVarInt(type.modifiers.size());
            for (Modifier mod : type.modifiers) {
                buf.writeEnum(mod.operator());
                INumberProviderSerializer.toNetwork(mod.value(), buf);
            }
        }
    }
}