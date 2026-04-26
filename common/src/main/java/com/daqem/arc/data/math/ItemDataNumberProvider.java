package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ItemDataProperty;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemDataNumberProvider implements INumberProvider {
    private final ArcItemTarget target;
    private final ItemDataProperty property;

    public ItemDataNumberProvider(ArcItemTarget target, ItemDataProperty property) {
        this.target = target;
        this.property = property;
    }

    @Override
    public double resolve(ActionData actionData) {
        // Using ArcItemTarget handles the logic of extracting the correct item stack!
        ItemStack stack = target.getItemStack(actionData, actionData.getPlayer().arc$getPlayer());

        if (stack != null && !stack.isEmpty()) {
            return property.getValue(stack);
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.item_data",
                Arc.API.translatable("item_target." + target.name().toLowerCase()),
                Arc.API.translatable("item_data_property." + property.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.ITEM_DATA;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<ItemDataNumberProvider> {

        @Override
        public ItemDataNumberProvider fromJson(JsonObject jsonObject) {
            return new ItemDataNumberProvider(
                    getItemTarget(jsonObject, "target", ArcItemTarget.ACTION),
                    getItemDataProperty(jsonObject, "property")
            );
        }

        @Override
        public ItemDataNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new ItemDataNumberProvider(
                    buf.readEnum(ArcItemTarget.class),
                    buf.readEnum(ItemDataProperty.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemDataNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeEnum(type.target);
            buf.writeEnum(type.property);
        }
    }
}