package dev.alexisbinh.openeco.crossserver;

import dev.alexisbinh.openeco.service.AccountService;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrossServerMessengerTest {

    @Mock
    private JavaPlugin plugin;

    @Mock
    private AccountService service;

    @Mock
    private Server server;

    @Mock
    private AsyncScheduler asyncScheduler;

    @Mock
    private ScheduledTask scheduledTask;

    @Mock
    private Player player;

    private CrossServerMessenger messenger;

    @BeforeEach
    void setUp() {
        messenger = new CrossServerMessenger(plugin, service, Logger.getLogger("cross-server-messenger-test"));

        lenient().when(plugin.getServer()).thenReturn(server);
        lenient().when(server.getAsyncScheduler()).thenReturn(asyncScheduler);
        lenient().doAnswer(invocation -> {
            invocation.<Consumer<ScheduledTask>>getArgument(1).accept(scheduledTask);
            return scheduledTask;
        }).when(asyncScheduler).runNow(eq(plugin), any());
    }

    @Test
    void flushForMatchingPlayerUuidTriggersFlushAndAck() {
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);
        when(server.getPlayer(playerId)).thenReturn(player);
        when(player.isOnline()).thenReturn(true);

        messenger.onPluginMessageReceived(
                CrossServerMessenger.CHANNEL,
                player,
                ("flush " + playerId).getBytes(StandardCharsets.UTF_8));

        verify(service).flushAccount(playerId);

        ArgumentCaptor<byte[]> payload = ArgumentCaptor.forClass(byte[].class);
        verify(player).sendPluginMessage(eq(plugin), eq(CrossServerMessenger.CHANNEL), payload.capture());
        assertEquals("flushed " + playerId, new String(payload.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void refreshForMatchingPlayerUuidTriggersRefresh() {
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);

        messenger.onPluginMessageReceived(
                CrossServerMessenger.CHANNEL,
                player,
                ("refresh " + playerId).getBytes(StandardCharsets.UTF_8));

        verify(service).refreshAccount(playerId);
        verify(player, never()).sendPluginMessage(eq(plugin), eq(CrossServerMessenger.CHANNEL), any());
    }

    @Test
    void mismatchedPayloadUuidIsIgnored() {
        UUID playerId = UUID.randomUUID();
        UUID payloadId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);

        messenger.onPluginMessageReceived(
                CrossServerMessenger.CHANNEL,
                player,
                ("flush " + payloadId).getBytes(StandardCharsets.UTF_8));

        verifyNoInteractions(service);
        verify(asyncScheduler, never()).runNow(eq(plugin), any());
        verify(player, never()).sendPluginMessage(eq(plugin), eq(CrossServerMessenger.CHANNEL), any());
    }
}
