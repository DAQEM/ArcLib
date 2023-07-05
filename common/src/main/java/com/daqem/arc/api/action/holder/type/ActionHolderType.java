package com.daqem.arc.api.action.holder.type;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ActionHolderType<T extends IActionHolder> extends IActionHolderType<T> {

    static <T extends IActionHolder> ActionHolderType<T> register(final ResourceLocation location) {
        return Registry.register(ArcRegistry.ACTION_HOLDER, location, () -> location);
    }

    static void init() {
    }
}
