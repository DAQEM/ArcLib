package com.daqem.arc.api.math;

import com.daqem.arc.data.ActionData;

public interface INumberProvider {

    INumberProviderType<?> getType();

    INumberProviderSerializer<?> getSerializer();

    double resolve(ActionData actionData);
}