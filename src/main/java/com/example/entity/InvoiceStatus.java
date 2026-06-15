package com.example.entity;

/**
 * Lifecycle status of an {@link Invoice}.
 *
 * <p>Allowed transitions:
 * <ul>
 *   <li>DRAFT &rarr; CONFIRMED, VOID</li>
 *   <li>CONFIRMED &rarr; PAID, VOID</li>
 *   <li>PAID, VOID are terminal</li>
 * </ul>
 */
public enum InvoiceStatus {
    DRAFT,
    CONFIRMED,
    PAID,
    VOID;

    /**
     * Whether this status may legally transition to {@code target}.
     * Self-transitions and transitions out of terminal states are not allowed.
     */
    public boolean canTransitionTo(InvoiceStatus target) {
        if (target == null) {
            return false;
        }
        switch (this) {
            case DRAFT:
                return target == CONFIRMED || target == VOID;
            case CONFIRMED:
                return target == PAID || target == VOID;
            case PAID:
            case VOID:
            default:
                return false;
        }
    }
}
