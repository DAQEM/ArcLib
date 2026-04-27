package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.BlockDataProperty;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class BlockDataNumberProvider implements INumberProvider {
    private final BlockDataProperty property;

    public BlockDataNumberProvider(BlockDataProperty property) {
        this.property = property;
    }

    @Override
    public double resolve(ActionData actionData) {
        return property.getValue(actionData);
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.block_data",
                Arc.API.translatable("block_data_property." + property.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.BLOCK_DATA;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<BlockDataNumberProvider> {

        @Override
        public BlockDataNumberProvider fromJson(JsonObject jsonObject) {
            return new BlockDataNumberProvider(
                    getBlockDataProperty(jsonObject, "property", BlockDataProperty.HARDNESS)
            );
        }

        @Override
        public BlockDataNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new BlockDataNumberProvider(buf.readEnum(BlockDataProperty.class));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, BlockDataNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeEnum(type.property);
        }
    }
}