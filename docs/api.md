# SimpleEco Addon API

Server owners can ignore this file unless another plugin integrates with SimpleEco directly.

The API is for same-server integrations. It is not a cross-server sync layer.

## Setup

Add SimpleEco as a dependency in `plugin.yml`:

```yaml
depend: [SimpleEco]
```

Resolve the service through Bukkit:

```java
RegisteredServiceProvider<SimpleEcoApi> registration = Bukkit.getServicesManager()
        .getRegistration(SimpleEcoApi.class);
if (registration == null) {
    throw new IllegalStateException("SimpleEco API is not available");
}

SimpleEcoApi api = registration.getProvider();
```

Call mutating methods from a safe server thread. The API does not move async addon calls onto the right thread for you.

## Main Methods

### Accounts

| Method | Result |
|---|---|
| `hasAccount(UUID)` | `boolean` |
| `getAccount(UUID)` | `Optional<AccountSnapshot>` |
| `findByName(String)` | `Optional<AccountSnapshot>` |
| `createAccount(UUID, String)` | `AccountOperationResult` |
| `renameAccount(UUID, String)` | `AccountOperationResult` |
| `deleteAccount(UUID)` | `AccountOperationResult` |

Account names are trimmed, non-blank, max 16 characters, and unique ignoring case.

### Balances

| Method | Result |
|---|---|
| `getBalance(UUID)` | `BigDecimal` |
| `has(UUID, BigDecimal)` | `boolean` |
| `canDeposit(UUID, BigDecimal)` | `BalanceCheckResult` |
| `canWithdraw(UUID, BigDecimal)` | `BalanceCheckResult` |
| `deposit(UUID, BigDecimal)` | `BalanceChangeResult` |
| `withdraw(UUID, BigDecimal)` | `BalanceChangeResult` |
| `setBalance(UUID, BigDecimal)` | `BalanceChangeResult` |
| `reset(UUID)` | `BalanceChangeResult` |

`has(UUID, BigDecimal)` requires a non-negative amount.

### Transfers

`transfer(UUID, UUID, BigDecimal)` returns `TransferResult`.

Possible statuses:

- `SUCCESS`
- `COOLDOWN`
- `INSUFFICIENT_FUNDS`
- `ACCOUNT_NOT_FOUND`
- `BALANCE_LIMIT`
- `CANCELLED`
- `TOO_LOW`
- `INVALID_AMOUNT`
- `SELF_TRANSFER`

### History And Leaderboard

| Method | Result |
|---|---|
| `getHistory(UUID, int, int)` | `HistoryPage` |
| `getHistory(UUID, int, int, HistoryFilter)` | `HistoryPage` |
| `getTopAccounts(int)` | `List<AccountSnapshot>` |
| `getRankOf(UUID)` | `int` |
| `logCustomTransaction(UUID, BigDecimal, TransactionKind)` | `void` |

`logCustomTransaction(...)` records a history entry without changing a balance. Its amount must be positive.

### Currency

`getCurrencyInfo()` returns ID, display names, fractional digits, starting balance, and max balance.

`format(BigDecimal)` returns the plugin's formatted currency string.

## Error Model

- Normal business failures are returned as status objects.
- `SimpleEcoApiException` is for API-level failures such as history lookup errors.

## Events

SimpleEco exposes Bukkit events for account create, rename, delete, balance changes, and payments.

## Example

```java
BalanceChangeResult result = api.withdraw(player.getUniqueId(), price);
if (!result.isSuccess()) {
    return false;
}
return true;
```
