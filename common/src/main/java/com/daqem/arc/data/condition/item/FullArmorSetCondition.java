package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcItemStack;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class FullArmorSetCondition extends AbstractCondition {

    private final List<ArcItemStack> armorItems;
    private final boolean checkComponents;

    public FullArmorSetCondition(boolean inverted, List<ArcItemStack> armorItems, boolean checkComponents) {
        super(inverted);
        this.armorItems = armorItems;
        this.checkComponents = checkComponents;
    }

    @Override
    public Component getDescription() {
        String itemsString = armorItems.stream().map(ArcItemStack::getDisplayName).map(Component::getString).collect(Collectors.joining(", "));
        return getDescription(itemsString);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.isEmpty()) return false;
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestplate.isEmpty()) return false;
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        if (leggings.isEmpty()) return false;
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.isEmpty()) return false;

        List<ItemStack> equippedArmor = List.of(helmet, chestplate, leggings, boots);
        return this.armorItems.stream()
                .allMatch(x -> equippedArmor.stream()
                        .anyMatch(y ->
                                x.matches(y, this.checkComponents)
                        )
                );
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.FULL_ARMOR_SET;
    }

    public static class Serializer implements IConditionSerializer<FullArmorSetCondition> {

        @Override
        public FullArmorSetCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new FullArmorSetCondition(
                    inverted,
                    getItemStackTemplates(jsonObject, "items").stream().map(ArcItemStack::new).toList(),
                    GsonHelper.getAsBoolean(jsonObject, "check_components", true)
            );
        }

        @Override
        public FullArmorSetCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new FullArmorSetCondition(
                    inverted,
                    friendlyByteBuf.readList(object -> ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf)).stream().map(ArcItemStack::new).toList(),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, FullArmorSetCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeCollection(type.armorItems.stream().map(ArcItemStack::getItemStackTemplate).toList(), (buf, itemStack) -> ItemStackTemplate.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, itemStack));
            friendlyByteBuf.writeBoolean(type.checkComponents);
        }
    }
}