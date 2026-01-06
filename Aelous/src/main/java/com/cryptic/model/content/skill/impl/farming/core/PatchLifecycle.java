package com.cryptic.model.content.skill.impl.farming.core;

/**
 * Finite State Machine for OSRS Farming Patch Lifecycle.
 */
public enum PatchLifecycle {
    WEEDS, // Patch has weeds (3 stages)
    EMPTY, // Patch is raked and ready for planting
    GROWING, // Crop is currently in growth phase
    DISEASED, // Crop is diseased and needs curing
    FULLY_GROWN, // Crop is mature and ready for harvest/checking
    STUMP, // Tree has been chopped down
    DEAD; // Crop has died from disease

    /**
     * Predicate to check if a state transition is valid.
     * Prevents logic bugs like DEAD -> GROWING.
     */
    public boolean canTransitionTo(PatchLifecycle next) {
        return switch (this) {
            case WEEDS -> next == WEEDS || next == EMPTY;
            case EMPTY -> next == GROWING;
            case GROWING -> next == GROWING || next == FULLY_GROWN || next == DEAD || next == DISEASED;
            case DISEASED -> next == GROWING || next == DEAD || next == EMPTY;
            case FULLY_GROWN -> next == FULLY_GROWN || next == STUMP || next == EMPTY;
            case STUMP -> next == EMPTY;
            case DEAD -> next == EMPTY;
        };
    }
}
