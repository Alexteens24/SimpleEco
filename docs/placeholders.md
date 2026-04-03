# PlaceholderAPI

Install PlaceholderAPI if you want placeholders. SimpleEco registers its own expansion automatically.

## Player Placeholders

| Placeholder | Result |
|---|---|
| `%simpleeco_balance%` | Raw balance |
| `%simpleeco_balance_formatted%` | Formatted balance |
| `%simpleeco_rank%` | Leaderboard rank (empty if not ranked) |
| `%simpleeco_frozen%` | `true` if account is frozen, `false` otherwise |
| `%simpleeco_currency_singular%` | Singular currency name |
| `%simpleeco_currency_plural%` | Plural currency name |

## Leaderboard Placeholders

| Placeholder | Result |
|---|---|
| `%simpleeco_top_1_name%` | Name at rank 1 |
| `%simpleeco_top_1_balance%` | Raw balance at rank 1 |
| `%simpleeco_top_1_balance_formatted%` | Formatted balance at rank 1 |
| `%simpleeco_top_N_name%` | Name at rank N |
| `%simpleeco_top_N_balance%` | Raw balance at rank N |
| `%simpleeco_top_N_balance_formatted%` | Formatted balance at rank N |

If a rank does not exist:

- `_name` returns `---`
- other rank fields return `0`

Leaderboard placeholders use the same cache controlled by `baltop.cache-ttl-seconds`.
