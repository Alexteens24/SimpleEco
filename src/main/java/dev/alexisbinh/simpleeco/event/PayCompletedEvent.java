package dev.alexisbinh.simpleeco.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Fired after a pay transfer has been successfully applied.
 */
public class PayCompletedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID fromId;
    private final UUID toId;
    private final BigDecimal sent;
    private final BigDecimal received;
    private final BigDecimal tax;
    private final BigDecimal fromBalanceBefore;
    private final BigDecimal fromBalanceAfter;
    private final BigDecimal toBalanceBefore;
    private final BigDecimal toBalanceAfter;

    public PayCompletedEvent(UUID fromId, UUID toId, BigDecimal sent, BigDecimal received, BigDecimal tax,
                             BigDecimal fromBalanceBefore, BigDecimal fromBalanceAfter,
                             BigDecimal toBalanceBefore, BigDecimal toBalanceAfter) {
        this.fromId = fromId;
        this.toId = toId;
        this.sent = sent;
        this.received = received;
        this.tax = tax;
        this.fromBalanceBefore = fromBalanceBefore;
        this.fromBalanceAfter = fromBalanceAfter;
        this.toBalanceBefore = toBalanceBefore;
        this.toBalanceAfter = toBalanceAfter;
    }

    public UUID getFromId() { return fromId; }

    public UUID getToId() { return toId; }

    public BigDecimal getSent() { return sent; }

    public BigDecimal getReceived() { return received; }

    public BigDecimal getTax() { return tax; }

    public BigDecimal getFromBalanceBefore() { return fromBalanceBefore; }

    public BigDecimal getFromBalanceAfter() { return fromBalanceAfter; }

    public BigDecimal getToBalanceBefore() { return toBalanceBefore; }

    public BigDecimal getToBalanceAfter() { return toBalanceAfter; }

    @Override
    public @NotNull HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }
}