package com.daqem.arc.api.math;

import com.daqem.arc.Arc;
import com.daqem.arc.data.math.*;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public interface INumberProviderType<T extends INumberProvider> {

    INumberProviderType<ConstantNumberProvider> CONSTANT = register(Arc.API.getId("constant"), new ConstantNumberProvider.Serializer());
    INumberProviderType<CalculateNumberProvider> CALCULATE = register(Arc.API.getId("calculate"), new CalculateNumberProvider.Serializer());
    INumberProviderType<EntityDataNumberProvider> ENTITY_DATA = register(Arc.API.getId("entity_data"), new EntityDataNumberProvider.Serializer());
    INumberProviderType<ItemDataNumberProvider> ITEM_DATA = register(Arc.API.getId("item_data"), new ItemDataNumberProvider.Serializer());
    INumberProviderType<ActionDataNumberProvider> ACTION_DATA = register(Arc.API.getId("action_data"), new ActionDataNumberProvider.Serializer());
    INumberProviderType<RandomNumberProvider> RANDOM = register(Arc.API.getId("random"), new RandomNumberProvider.Serializer());

    static <T extends INumberProvider> INumberProviderType<T> register(final Identifier location, final INumberProviderSerializer<T> serializer) {
        return Registry.register(ArcRegistry.NUMBER_PROVIDER, location, new INumberProviderType<T>() {

            @Override
            public Identifier getIdentifier() {
                return location;
            }

            @Override
            public INumberProviderSerializer<T> getSerializer() {
                return serializer;
            }

            @Override
            public String toString() {
                return location.toString();
            }
        });
    }

    static void init() {
    }

    Identifier getIdentifier();

    INumberProviderSerializer<T> getSerializer();
}