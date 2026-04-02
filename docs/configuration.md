# Configuration

Config file: `plugins/SimpleEco/config.yml`  
Reload command: `/eco reload`

Most settings reload safely. Storage backend and storage file changes do not. Treat those as stop-the-server maintenance.

## Currency

```yaml
currency:
  id: "simpleeco"
  name-singular: "Dollar"
  name-plural: "Dollars"
  decimal-digits: 2
  starting-balance: 0.00
  max-balance: -1
```

Notes:

- `decimal-digits` controls rounding and display.
- `starting-balance` affects new accounts and `/eco reset`.
- `max-balance: -1` means unlimited.

## Storage

```yaml
storage:
  type: sqlite
  sqlite:
    file: economy.db
  h2:
    file: economy
```

Notes:

- Data is stored under `plugins/SimpleEco/`.
- `sqlite` is the default.
- Changing backend or file name does not move old data.

## Auto-Save

```yaml
autosave-interval: 30
```

Notes:

- Value is in seconds.
- Values less than 1 are clamped to 1.
- Lower values reduce possible loss after an unclean stop, but write more often.

## Pay

```yaml
pay:
  cooldown-seconds: 0
  tax-percent: 0.0
  min-amount: 0.01
```

Notes:

- `cooldown-seconds: 0` disables cooldown.
- `tax-percent` is deducted from the sent amount before the receiver is credited.
- `min-amount` rejects transfers smaller than the configured value after scaling.

Example: `tax-percent: 5.0` means sending 100 deducts 100 from the sender and credits 95 to the receiver.

## Baltop

```yaml
baltop:
  page-size: 10
  cache-ttl-seconds: 30
```

Notes:

- Higher cache TTL lowers repeated sort cost.
- Higher cache TTL also means slightly older leaderboard views.

## History

```yaml
history:
  retention-days: -1
```

Notes:

- Any value less than or equal to `0` keeps all history.
- A positive value deletes older history rows in the background.
- Deleting rows does not guarantee the SQLite file shrinks immediately.

## Messages

Messages use [MiniMessage](https://docs.advntr.dev/minimessage/format.html).

Keep these warnings present unless you replace them with equivalent text:

- `account-sync-failed`
- `account-name-conflict`

Common placeholders:

| Key | Placeholders |
|---|---|
| `balance-self` | `<balance>` |
| `balance-other` | `<player>`, `<balance>` |
| `pay-sent` | `<player>`, `<amount>` |
| `pay-received` | `<player>`, `<amount>` |
| `pay-tax` | `<tax>` |
| `pay-cooldown` | `<seconds>` |
| `account-not-found` | `<player>` |
| `eco-give` | `<player>`, `<amount>`, `<balance>` |
| `eco-take` | `<player>`, `<amount>`, `<balance>` |
| `eco-set` | `<player>`, `<balance>` |
| `eco-reset` | `<player>`, `<balance>` |
| `eco-delete` | `<player>` |
| `eco-balance-limit` | `<player>`, `<limit>` |
| `baltop-header` | `<page>`, `<total>` |
| `baltop-entry` | `<rank>`, `<player>`, `<balance>` |
| `history-header` | `<player>`, `<page>`, `<total>` |
| `history-pay-sent` | `<date>`, `<amount>`, `<counterpart>` |
| `history-pay-received` | `<date>`, `<amount>`, `<counterpart>` |
| `history-custom` | `<date>`, `<kind>`, `<amount>`, `<details>`, `<source>`, `<note>`, `<balance>` |
