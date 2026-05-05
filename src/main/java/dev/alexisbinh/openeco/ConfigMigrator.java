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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

final class ConfigMigrator {

    private static final String LEGACY_CURRENCY_ROOT = "currency";
    private static final String NEW_CURRENCIES_ROOT = "currencies";

    private ConfigMigrator() {
    }

    static boolean migrate(FileConfiguration currentConfig, FileConfiguration defaultConfig) {
        Objects.requireNonNull(currentConfig, "currentConfig");
        Objects.requireNonNull(defaultConfig, "defaultConfig");

        boolean changed = migrateLegacyCurrencySection(currentConfig);
        changed |= copyMissingDefaults(defaultConfig, currentConfig);
        return changed;
    }

    private static boolean migrateLegacyCurrencySection(FileConfiguration currentConfig) {
        if (!currentConfig.isConfigurationSection(LEGACY_CURRENCY_ROOT)) {
            return false;
        }

        if (!currentConfig.isConfigurationSection(NEW_CURRENCIES_ROOT)) {
            String legacyCurrencyId = sanitized(currentConfig.getString("currency.id"), "openeco");
            String singular = sanitized(currentConfig.getString("currency.name-singular"), "Dollar");
            String plural = sanitized(currentConfig.getString("currency.name-plural"), "Dollars");
            int fractionalDigits = clampFractionalDigits(currentConfig.getInt("currency.decimal-digits", 2));
            double startingBalance = currentConfig.getDouble("currency.starting-balance", 0.0);
            double maxBalance = currentConfig.getDouble("currency.max-balance", -1.0);

            currentConfig.set("currencies.default", legacyCurrencyId);
            currentConfig.set("currencies.definitions." + legacyCurrencyId + ".name-singular", singular);
            currentConfig.set("currencies.definitions." + legacyCurrencyId + ".name-plural", plural);
            currentConfig.set("currencies.definitions." + legacyCurrencyId + ".decimal-digits", fractionalDigits);
            currentConfig.set("currencies.definitions." + legacyCurrencyId + ".starting-balance", startingBalance);
            currentConfig.set("currencies.definitions." + legacyCurrencyId + ".max-balance", maxBalance);
        }

        currentConfig.set(LEGACY_CURRENCY_ROOT, null);
        return true;
    }

    private static boolean copyMissingDefaults(ConfigurationSection source, ConfigurationSection target) {
        boolean changed = false;
        for (String key : source.getKeys(false)) {
            ConfigurationSection sourceSection = source.getConfigurationSection(key);
            if (sourceSection != null) {
                ConfigurationSection targetSection = target.getConfigurationSection(key);
                if (targetSection == null) {
                    if (target.contains(key, true)) {
                        target.set(key, null);
                    }
                    targetSection = target.createSection(key);
                    changed = true;
                }
                changed |= copyMissingDefaults(sourceSection, targetSection);
            } else if (!target.contains(key, true)) {
                target.set(key, source.get(key));
                changed = true;
            }
        }
        return changed;
    }

    private static String sanitized(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? fallback : trimmed;
    }

    private static int clampFractionalDigits(int fractionalDigits) {
        return Math.max(0, Math.min(8, fractionalDigits));
    }
}