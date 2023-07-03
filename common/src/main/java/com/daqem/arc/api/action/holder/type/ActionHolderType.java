package com.daqem.arc.api.action.holder.type;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ActionHolderType<T extends IActionHolder> extends IActionHolderType<T> {

    static <T extends IActionHolder> ActionHolderType<T> register(final ResourceLocation location, final List<IActionHolder> actionHolders) {
        return Registry.register(ArcRegistry.ACTION_HOLDER, location, new ActionHolderType<T>() {
            @Override
            public ResourceLocation getLocation() {
                return location;
            }

            @Override
            public List<IActionHolder> getActionHolders() {
                return actionHolders;
            }
        });
    }

    static void init() {
    }
}
