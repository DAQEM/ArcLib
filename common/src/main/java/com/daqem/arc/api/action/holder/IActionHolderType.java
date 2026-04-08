package com.daqem.arc.api.action.holder;

import com.daqem.arc.Arc;
import com.daqem.arc.api.player.holder.PlayerActionHolder;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public interface IActionHolderType<T extends IActionHolder> {

    IActionHolderType<PlayerActionHolder> PLAYER_ACTION_TYPE = IActionHolderType.register(Arc.API.getId("player"), new PlayerActionHolder.Serializer());

    static <T extends IActionHolder> IActionHolderType<T> register(final Identifier location, final IActionHolderSerializer<T> serializer) {
        return Registry.register(ArcRegistry.ACTION_HOLDER, location, new IActionHolderType<T>() {

            @Override
            public Identifier getIdentifier() {
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

    Identifier getIdentifier();

    IActionHolderSerializer<T> getSerializer();
}
