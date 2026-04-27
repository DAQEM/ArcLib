package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Optional;

public class EnchantmentLevelNumberProvider implements INumberProvider {

    private final Identifier enchantmentId;
    private final ArcItemTarget target;

    public EnchantmentLevelNumberProvider(Identifier enchantmentId, ArcItemTarget target) {
        this.enchantmentId = enchantmentId;
        this.target = target;
    }

    @Override
    public double resolve(ActionData actionData) {
        ItemStack stack = target.getItemStack(actionData, actionData.getPlayer().arc$getPlayer());
        if (stack != null && !stack.isEmpty()) {
            Optional<Holder.Reference<Enchantment>> enchantment = actionData.getPlayer().arc$getLevel()
                    .registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .get(ResourceKey.create(Registries.ENCHANTMENT, enchantmentId));

            if (enchantment.isPresent()) {
                return EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), stack);
            }
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.enchantment_level",
                enchantmentId.getPath(),
                Arc.API.translatable("item_target." + target.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.ENCHANTMENT_LEVEL;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<EnchantmentLevelNumberProvider> {
        @Override
        public EnchantmentLevelNumberProvider fromJson(JsonObject jsonObject) {
            return new EnchantmentLevelNumberProvider(
                    getIdentifier(jsonObject, "enchantment"),
                    getItemTarget(jsonObject, "target", ArcItemTarget.ACTION)
            );
        }

        @Override
        public EnchantmentLevelNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new EnchantmentLevelNumberProvider(buf.readIdentifier(), buf.readEnum(ArcItemTarget.class));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EnchantmentLevelNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            buf.writeIdentifier(type.enchantmentId);
            buf.writeEnum(type.target);
        }
    }
}