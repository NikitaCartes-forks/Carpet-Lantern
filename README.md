# Sculk Carpet

## Info
Carpet addon that was specifically made for Sculk SMP.

## Rules

| Rule                                      | Default   | Options                     | Description                                                                                                                                      |
|-------------------------------------------|-----------|-----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| `maxPlayerBotCap`                         | `-1`      | `-1`, `2`, `4`              | Controls how many bots can player spawn without `carpet.unlimitedBots` permission.                                                               |
| `maxPlayerBotGlobalCap`                   | `-1`      | `-1`, `2`, `4`, `8`         | Controls how many bots can be spawned without `carpet.ignoreGlobalBotCap` permission.                                                            |
| `fakePlayerLuckPermsGroup`                | `default` | `default`, `bot`, `player`  | The LuckPerms group to assign to spawned fake players.                                                                                           |
| `fakePlayerRemoveDefaultGroup`            | `false`   | `true`, `false`             | Whether to remove the default group from the fake player if another group is assigned.                                                           |
| `fakePlayerDefaultPrivate`                | `false`   | `true`, `false`             | Whether fake players are spawned as private by default.                                                                                          |
| `playerCommandBlockBotVerification`       | `false`   | `true`, `false`             | If BlockBot is installed, prevents non-ops from spawning fake players with names of linked players.                                              |
| `fakePlayerBlockInteractionDisabledList`  | `""`      | `namespace:block,...`       | Comma, space, or semicolon separated block ids that fake players must not interact with; blocked interactions are silently skipped.              |
| `fakePlayerEntityInteractionDisabledList` | `""`      | `namespace:entity_type,...` | Comma, space, or semicolon separated entity type ids that fake players must not interact with; blocked interactions are silently skipped.        |
| `trialChamberIgnoresFakePlayers`          | `false`   | `true`, `false`             | Fake players are excluded from Trial Spawner player detection and cannot unlock Vaults, preventing bots from inflating rewards or claiming keys. |
