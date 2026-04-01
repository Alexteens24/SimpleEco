package dev.alexisbinh.simpleeco.service;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EconomyConfigSnapshotTest {

    @Test
    void nonPositiveHistoryRetentionIsNormalizedToKeepForever() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("history.retention-days", 0);

        EconomyConfigSnapshot snapshot = EconomyConfigSnapshot.from(config);

        assertEquals(-1, snapshot.historyRetentionDays());
    }

    @Test
    void positiveHistoryRetentionIsPreserved() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("history.retention-days", 7);

        EconomyConfigSnapshot snapshot = EconomyConfigSnapshot.from(config);

        assertEquals(7, snapshot.historyRetentionDays());
        assertTrue(snapshot.historyRetentionDays() > 0);
    }
}