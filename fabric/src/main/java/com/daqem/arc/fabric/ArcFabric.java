package com.daqem.arc.fabric;

import com.daqem.arc.Arc;
import com.daqem.arc.command.argument.ActionArgument;
import com.daqem.arc.fabric.data.ActionManagerFabric;
import com.daqem.arc.fabric.data.PlayerActionHolderManagerFabric;
import com.daqem.arc.registry.ArcRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.server.packs.PackType;

public class ArcFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Arc.initCommon();
        ArcRegistry.init();
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ActionManagerFabric());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new PlayerActionHolderManagerFabric());
        registerCommandArgumentTypes();
    }

    private void registerCommandArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(Arc.getId("action"), ActionArgument.class, SingletonArgumentInfo.contextFree(ActionArgument::action));
    }
}
