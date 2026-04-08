package com.daqem.arc.neoforge;

import com.daqem.arc.Arc;
import com.daqem.arc.command.argument.ActionArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Arc.MOD_ID)
@EventBusSubscriber(modid = Arc.MOD_ID)
public class ArcNeoForge {

    public ArcNeoForge(IEventBus modEventBus) {
        Arc.init();
        registerCommandArgumentTypes(modEventBus);
    }

    private void registerCommandArgumentTypes(IEventBus modEventBus) {
        DeferredRegister<ArgumentTypeInfo<?, ?>> argTypeRegistry = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Arc.MOD_ID);
        argTypeRegistry.register("action", () -> ArgumentTypeInfos.registerByClass(ActionArgument.class, SingletonArgumentInfo.contextFree(ActionArgument::action)));
        argTypeRegistry.register(modEventBus);
    }
}
