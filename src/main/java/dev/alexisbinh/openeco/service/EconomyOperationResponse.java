package dev.alexisbinh.openeco.service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Internal response object for core economy mutations.
 * This keeps BigDecimal precision in core logic and adapters can convert at the boundary.
 */
public record EconomyOperationResponse(BigDecimal amount,
                                       BigDecimal balance,
                                       ResponseType type,
                                       String errorMessage) {

    public enum ResponseType {
        SUCCESS,
        FAILURE,
        NOT_IMPLEMENTED
    }

    public EconomyOperationResponse {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(balance, "balance");
        Objects.requireNonNull(type, "type");
        errorMessage = errorMessage == null ? "" : errorMessage;
    }

    public boolean transactionSuccess() {
        return type == ResponseType.SUCCESS;
    }
}
