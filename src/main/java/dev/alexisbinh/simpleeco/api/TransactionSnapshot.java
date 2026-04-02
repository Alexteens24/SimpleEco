package dev.alexisbinh.simpleeco.api;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionSnapshot(
        TransactionKind kind,
        UUID counterpartId,
        UUID targetId,
        BigDecimal amount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
                long timestamp,
                @Nullable String source,
                @Nullable String note
) {

        public boolean hasSource() {
                return source != null;
        }

        public boolean hasNote() {
                return note != null;
        }

        public boolean hasMetadata() {
                return source != null || note != null;
        }
}