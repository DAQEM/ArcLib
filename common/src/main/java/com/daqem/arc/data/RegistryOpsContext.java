package com.daqem.arc.data;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;

public class RegistryOpsContext {
    private static HolderLookup.Provider PROVIDER;

    public static void set(HolderLookup.Provider provider) {
        PROVIDER = provider;
    }

    public static <T> DynamicOps<T> getOps(DynamicOps<T> delegate) {
        if (PROVIDER != null) {
            return PROVIDER.createSerializationContext(delegate);
        }
        return delegate;
    }
}