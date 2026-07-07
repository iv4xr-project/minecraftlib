# minecraftlib


This is a library containing functions, tactics, and  goals for driving an [aplib](https://github.com/iv4xr-project/aplib) agent for testing the **Minecraft** game.

The library is written in Java and organized as a Maven project. It follows the same pattern as the other iv4xr game-bridges (`uuselib` for Space Engineers, `iv4xrDemo` for 
Lab Recruits). It is bridge between the game-agnostic `aplib`/iv4xr agent framework and one specific game.

However, Minecraft is not controlled through an in-process client. Instead this library talks over HTTP to the **MineflayerTestbench** (a separate Node/TypeScript project) running in *server mode*, which drives a [Mineflayer](https://github.com/PrismarineJS/mineflayer) bot connected to a real Minecraft server.


## Main components

- **`MinecraftEnv`** (`extends eu.iv4xr.framework.mainConcepts.Iv4xrEnvironment`) — HTTP client for the testbench. `observe(agentId)` → `GET /status`; typed action methods (`moveTo`, `mine`, `place`, `select`, `attack`, `checkBlock`, …) → `POST /action`; `buildLevel(...)` → `POST /build-level` (caches the tag→position/UUID map); `resetWorker()` → `POST /reset`.
- **`StatusToWorldModel`** — pure, side-effect-free converter from the `/status` JSON to an iv4xr `WorldModel` (unit-tested without a server).
- **`MinecraftState`** (`extends Iv4xrAgentState<Void>`) — holds the current `WorldModel`; *replaces* it each tick (the `/status` scan is egocentric/local). Navigation is delegated to the testbench, so no nav-graph is kept.
- **`MinecraftTacticLib` / `MinecraftGoalLib`** — one primitive tactic per testbench action; achievement goals plus `check_*`-backed **oracle goals** that emit `VerdictEvent`s (`Iv4xrEDSL.assertTrue_`).

All Java code lives in the package `eu.iv4xr.minecraft`.

## Building

Requires **Java 11+** (the bridge uses `java.net.http.HttpClient`). `aplib` is pulled from jitpack.

```
mvn compile          # build
mvn test             # run unit tests (mock HTTP server — no Minecraft needed)
mvn package          # build a jar
```

## Running the demo (needs a live Minecraft server + the testbench)

1. Start a vanilla Minecraft server (`online-mode=false`), and OP the bot.
2. In the `MineflayerTestbench/` project, start it in **server mode** (no `test=` arg):
   ```
   npm run start
   ```
   This connects the bot and exposes the HTTP API on port 3000.
3. From this project, run the tests:
   ```
   mvn test
   ```

## Related projects

- [aplib](https://github.com/iv4xr-project/aplib) The iv4xr agent-programming framework (game-agnostic).
- [MineflayerTestbench](https://github.com/se-fbk/MineflayerTestbench) The Node/TS project this library drives over HTTP.
