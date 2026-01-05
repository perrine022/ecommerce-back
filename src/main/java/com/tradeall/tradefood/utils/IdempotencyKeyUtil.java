package com.tradeall.tradefood.utils;

import java.util.UUID;

public class IdempotencyKeyUtil {

    /**
     * Generates a unique idempotency key based on action, ID and optional extra parameters.
     *
     * @param action The action being performed (e.g., "create_payment").
     * @param id     A unique ID (e.g., userId or bookingId).
     * @param extras Optional extra parameters to ensure uniqueness (e.g., amount).
     * @return A unique idempotency key string.
     */
    public static String generate(String action, Object id, Object... extras) {
        StringBuilder sb = new StringBuilder(action).append("_").append(id);
        for (Object extra : extras) {
            sb.append("_").append(extra);
        }
        // Use hash to keep length reasonable if needed, but for Stripe, long strings are fine.
        return UUID.nameUUIDFromBytes(sb.toString().getBytes()).toString();
    }
}
