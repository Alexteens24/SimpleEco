/*
 * Copyright 2026 alexisbinh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexisbinh.openeco;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigMigratorTest {

    @Test
    void migratesLegacyCurrencyBlockAndRemovesOldKeys() {
        YamlConfiguration current = new YamlConfiguration();
        current.set("currency.id", "coins");
        current.set("currency.name-singular", "Coin");
        current.set("currency.name-plural", "Coins");
        current.set("currency.decimal-digits", 3);
        current.set("currency.starting-balance", 1.234);
        current.set("currency.max-balance", 999.999);

        YamlConfiguration defaults = new YamlConfiguration();
        defaults.set("currencies.default", "openeco");
        defaults.set("currencies.definitions.openeco.name-singular", "Dollar");
        defaults.set("currencies.definitions.openeco.name-plural", "Dollars");
        defaults.set("currencies.definitions.openeco.decimal-digits", 2);
        defaults.set("currencies.definitions.openeco.starting-balance", 0.0);
        defaults.set("currencies.definitions.openeco.max-balance", -1.0);
        defaults.set("accounts.load-strategy", "eager");

        boolean changed = ConfigMigrator.migrate(current, defaults);

        assertTrue(changed);
        assertFalse(current.contains("currency"));
        assertEquals("coins", current.getString("currencies.default"));
        assertEquals("Coin", current.getString("currencies.definitions.coins.name-singular"));
        assertEquals("Coins", current.getString("currencies.definitions.coins.name-plural"));
        assertEquals(3, current.getInt("currencies.definitions.coins.decimal-digits"));
        assertEquals(1.234, current.getDouble("currencies.definitions.coins.starting-balance"));
        assertEquals(999.999, current.getDouble("currencies.definitions.coins.max-balance"));
        assertEquals("eager", current.getString("accounts.load-strategy"));
        assertTrue(current.contains("currencies.definitions.openeco"));
        assertEquals("Dollar", current.getString("currencies.definitions.openeco.name-singular"));
        assertEquals("Dollars", current.getString("currencies.definitions.openeco.name-plural"));
    }

    @Test
    void copiesMissingDefaultsWithoutOverwritingExistingValues() {
        YamlConfiguration current = new YamlConfiguration();
        current.set("currencies.default", "gems");
        current.set("currencies.definitions.gems.name-singular", "Gem");
        current.set("currencies.definitions.gems.name-plural", "Gems");
        current.set("custom.feature.enabled", true);

        YamlConfiguration defaults = new YamlConfiguration();
        defaults.set("currencies.default", "openeco");
        defaults.set("currencies.definitions.openeco.name-singular", "Dollar");
        defaults.set("currencies.definitions.openeco.name-plural", "Dollars");
        defaults.set("accounts.load-strategy", "eager");

        boolean changed = ConfigMigrator.migrate(current, defaults);

        assertTrue(changed);
        assertEquals("gems", current.getString("currencies.default"));
        assertEquals("Gem", current.getString("currencies.definitions.gems.name-singular"));
        assertEquals("Gems", current.getString("currencies.definitions.gems.name-plural"));
        assertEquals("eager", current.getString("accounts.load-strategy"));
        assertTrue(current.getBoolean("custom.feature.enabled"));
    }
}