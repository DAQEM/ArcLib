package com.daqem.arc.api.math;

import com.daqem.arc.data.ActionData;
import net.minecraft.network.chat.Component;

public interface INumberProvider {

    INumberProviderType<?> getType();

    INumberProviderSerializer<?> getSerializer();

    double resolve(ActionData actionData);

    Component getDescription();
}