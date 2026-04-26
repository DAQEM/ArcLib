package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ActionDataNumberProvider implements INumberProvider {

    private final Identifier dataTypeId;

    public ActionDataNumberProvider(Identifier dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    @Override
    public double resolve(ActionData actionData) {
        IActionDataType<?> type = IActionDataType.TYPES.get(dataTypeId);
        if (type != null) {
            Object val = actionData.getData(type);
            if (val instanceof Number n) return n.doubleValue();
            // QoL feature: Convert booleans (like is_critical_hit) to 1.0 or 0.0 for math operations
            if (val instanceof Boolean b) return b ? 1.0 : 0.0;
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.action_data",
                Arc.API.translatable("action_data." + dataTypeId.getPath())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.ACTION_DATA;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<ActionDataNumberProvider> {

        @Override
        public ActionDataNumberProvider fromJson(JsonObject jsonObject) {
            return new ActionDataNumberProvider(getIdentifier(jsonObject, "data"));
        }

        @Override
        public ActionDataNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new ActionDataNumberProvider(buf.readIdentifier());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ActionDataNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeIdentifier(type.dataTypeId);
        }
    }
}