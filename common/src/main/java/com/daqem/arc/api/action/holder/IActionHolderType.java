package com.daqem.arc.api.action.holder;

import com.daqem.arc.Arc;
import com.daqem.arc.api.player.holder.PlayerActionHolder;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface IActionHolderType<T extends IActionHolder> {

    IActionHolderType<PlayerActionHolder> PLAYER_ACTION_TYPE = IActionHolderType.register(Arc.getId("player"), new PlayerActionHolder.Serializer());

    static <T extends IActionHolder> IActionHolderType<T> register(final ResourceLocation location, final IActionHolderSerializer<T> serializer) {
        return Registry.register(ArcRegistry.ACTION_HOLDER, location, new IActionHolderType<T>() {

            @Override
            public ResourceLocation getLocation() {
                return location;
            }

            @Override
            public IActionHolderSerializer<T> getSerializer() {
                return serializer;
            }
        });
    }

    static void init() {
    }

    ResourceLocation getLocation();

    IActionHolderSerializer<T> getSerializer();
}
