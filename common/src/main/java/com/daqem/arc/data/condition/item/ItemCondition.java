package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ItemCondition extends AbstractCondition {

    private final ItemStackTemplate itemStackTemplate;
    private final boolean checkComponents;
    private final boolean checkCount;
    private final INumberProvider count;
    private final ComparisonType comparisonType;
    private final ArcItemTarget target;
    private ItemStack cachedItemStack;

    public ItemCondition(boolean inverted, ItemStackTemplate itemStackTemplate, boolean checkComponents, boolean checkCount, INumberProvider count, ComparisonType comparisonType, ArcItemTarget target) {
        super(inverted);
        this.itemStackTemplate = itemStackTemplate;
        this.checkComponents = checkComponents;
        this.checkCount = checkCount;
        this.count = count;
        this.comparisonType = comparisonType;
        this.target = target;
        this.cachedItemStack = null;
    }

    @Override
    public Component getDescription() {
        return getDescription(count.getDescription(), getItemStack().getHoverName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = target.getItemStack(actionData, actionData.getPlayer().arc$getPlayer());

        if (itemStack == null || itemStack.isEmpty()) {
            return false;
        }

        if (!ItemStack.isSameItem(getItemStack(), itemStack)) {
            return false;
        }

        boolean hasComponents = !checkComponents || !getItemStack().getComponents().isEmpty();
        boolean sameComponents = !checkComponents || ItemStack.isSameItemSameComponents(getItemStack(), itemStack);
        if (checkComponents && hasComponents && !sameComponents) {
            return false;
        }

        return !checkCount || comparisonType.compare(itemStack.getCount(), count.resolve(actionData));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM;
    }

    public ItemStack getItemStack() {
        if (cachedItemStack != null) return cachedItemStack;
        this.cachedItemStack = itemStackTemplate.create();
        return cachedItemStack;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getCount() {
        return count;
    }

    public ItemStackTemplate getItemStackTemplate() {
        return itemStackTemplate;
    }

    public boolean shouldCheckComponents() {
        return checkComponents;
    }

    public boolean shouldCheckCount() {
        return checkCount;
    }

    public ArcItemTarget getTarget() {
        return target;
    }

    public static class Serializer implements IConditionSerializer<ItemCondition> {
        @Override
        public ItemCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            ItemStackTemplate template = getItemStackTemplate(jsonObject, "item");
            int templateCount = template.count();

            return new ItemCondition(
                    inverted,
                    template,
                    GsonHelper.getAsBoolean(jsonObject, "check_components", true),
                    GsonHelper.getAsBoolean(jsonObject, "check_count", templateCount > 1),
                    getNumberProvider(jsonObject, "count", new ConstantNumberProvider(templateCount)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    getItemTarget(jsonObject, "target", ArcItemTarget.ACTION)
            );
        }

        @Override
        public ItemCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ItemCondition(
                    inverted,
                    ItemStackTemplate.STREAM_CODEC.decode(buf),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class),
                    buf.readEnum(ArcItemTarget.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            ItemStackTemplate.STREAM_CODEC.encode(buf, type.itemStackTemplate);
            buf.writeBoolean(type.checkComponents);
            buf.writeBoolean(type.checkCount);
            INumberProviderSerializer.toNetwork(type.count, buf);
            buf.writeEnum(type.comparisonType);
            buf.writeEnum(type.target);
        }
    }
}