package com.daqem.arc.event.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

/**
 * The RegistryEvent interface defines a set of events related to registering different types within a registry.
 */
public interface RegistryEvent {

    /**
     * @see RegisterActionType#registerActionType()
     */
    Event<RegisterActionType> REGISTER_ACTION_TYPE = EventFactory.createLoop();
    /**
     * @see RegisterRewardType#registerRewardType()
     */
    Event<RegisterRewardType> REGISTER_REWARD_TYPE = EventFactory.createLoop();
    /**
     * @see RegisterConditionType#registerConditionType()
     */
    Event<RegisterConditionType> REGISTER_CONDITION_TYPE = EventFactory.createLoop();
    /**
     * @see RegisterActionHolderType#registerActionHolderType()
     */
    Event<RegisterActionHolderType> REGISTER_ACTION_HOLDER_TYPE = EventFactory.createLoop();

    /**
     * @see RegisterActionSerializer#registerActionSerializer()
     */
    Event<RegisterActionSerializer> REGISTER_ACTION_SERIALIZER = EventFactory.createLoop();
    /**
     * @see RegisterRewardSerializer#registerRewardSerializer()
     */
    Event<RegisterRewardSerializer> REGISTER_REWARD_SERIALIZER = EventFactory.createLoop();
    /**
     * @see RegisterConditionSerializer#registerConditionSerializer()
     */
    Event<RegisterConditionSerializer> REGISTER_CONDITION_SERIALIZER = EventFactory.createLoop();

    interface RegisterActionType {
        /**
         * Invoked when registering an action type.
         */
        void registerActionType();
    }

    interface RegisterRewardType {
        /**
         * Invoked when registering a reward type.
         */
        void registerRewardType();
    }

    interface RegisterConditionType {
        /**
         * Invoked when registering a condition type.
         */
        void registerConditionType();
    }

    interface RegisterActionHolderType {
        /**
         * Invoked when registering an action holder type.
         */
        void registerActionHolderType();
    }

    interface RegisterActionSerializer {
        /**
         * Invoked when registering an action serializer.
         */
        void registerActionSerializer();
    }

    interface RegisterRewardSerializer {
        /**
         * Invoked when registering a reward serializer.
         */
        void registerRewardSerializer();
    }

    interface RegisterConditionSerializer {
        /**
         * Invoked when registering a condition serializer.
         */
        void registerConditionSerializer();
    }
}
