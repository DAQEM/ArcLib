package com.daqem.arc.neoforge;

import com.daqem.arc.Arc;
import com.daqem.arc.api.event.ArcEntityEvent;
import com.daqem.arc.api.event.ArcItemEvent;
import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.event.EventResult;
import com.daqem.arc.command.argument.ActionArgument;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.brewing.PlayerBrewedPotionEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.mutable.MutableFloat;

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

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        MutableFloat damage = new MutableFloat(event.getContainer().getNewDamage());
        DamageSource damageSource = event.getSource();
        LivingEntity defender = event.getEntity();
        if (defender instanceof ServerPlayer serverPlayer) {
            EventResult eventResult = ArcPlayerEvent.ENTITY_HURT_PLAYER.invoker().onEntityHurtPlayer(serverPlayer, damageSource, damage);
            if (eventResult.cancelsEvent()) {
                event.setCanceled(true);
                return;
            }
        }
        if (damageSource.getEntity() instanceof ServerPlayer serverPlayer) {
            EventResult eventResult = ArcEntityEvent.PLAYER_HURT_ENTITY.invoker().onPlayerHurtEntity(serverPlayer, defender, damageSource, damage);
            if (eventResult.cancelsEvent()) {
                event.setCanceled(true);
                return;
            }
        }
        if (defender instanceof ServerPlayer serverPlayer && damageSource.getEntity() instanceof ServerPlayer attacker) {
            EventResult eventResult = ArcPlayerEvent.PLAYER_HURT_PLAYER.invoker().onPlayerHurtPlayer(attacker, serverPlayer, damageSource, damage);
            if (eventResult.cancelsEvent()) {
                event.setCanceled(true);
            }
        }
        event.getContainer().setNewDamage(damage.getValue());
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        EventResult eventResult = ArcItemEvent.DROP_ITEM.invoker().onDropItem(event.getPlayer(), event.getEntity());
        if (eventResult.cancelsEvent()) {
            event.setCanceled(true);
        }
    }
}
