# AGENTS.md

NeoForge 1.21.1 mod (Gensokyo Legacy, Touhou characters/structures). Java 21, Gradle 8.8, `net.neoforged.moddev` 2.0.80. Java sources in `src/main/java/dev/xkmc/gensokyolegacy/`.

## Commands
- `./gradlew runClient` / `./gradlew runServer` — run the game.
- `./gradlew runData` — datagen. Writes to `src/generated/resources/`, which is committed to git and merged into the jar as main resources (`build.gradle` line 68). Generated output must be committed alongside code changes.
- `./gradlew build` — full build (jar + sources jar).
- `gradle.properties` sets `org.gradle.daemon=false` and `-Xmx3G`: every Gradle invocation is a slow cold JVM. Batch independent Gradle work into one command.
- `src/test/java/organize/*` are `main()` utilities run from the IDE, **not JUnit tests**. `organize.ResourceOrganizer` merges the split zh_cn lang files (below). `./gradlew test` runs no real tests.

## Registration (two systems)
- `GensokyoLegacy.REGISTRATE` (`L2Registrate`, dev.xkmc's fork of Registrate) — blocks, items, entities, creative tabs, with datagen. Register classes in `init/registrate/GL*.java` and call `register()` from the `GensokyoLegacy()` constructor (order matters).
- `GensokyoLegacy.REG` (`dev.xkmc.l2core...Reg`) — vanilla/built-in registries (sensors/memories/activities via `SR`, see `GLBrains`) and custom datapack registries via `dataReg` + `CdcVal` (see `content/rpg/core/CodecRegistry.java`).

## Key conventions
- Networking: single `GensokyoLegacy.HANDLER` (l2serial `PacketHandler`); register every packet there. Packets use l2serial codecs.
- Custom JSON codecs use l2serial `CodecHandler`/`Handlers` (see the `FluidIngredient` registration in the mod constructor).
- Datapack registries: define a `ResourceKey` in `CodecRegistry.Keys`, register with `REG.dataReg("name", CODEC)`. Runtime JSON lives at `data/gensokyolegacy/gensokyolegacy/<registry>/...` (currently `dialog`, `dialog_starter`, `quest`, `trade`). New option/action/condition/requirement/reward subclasses must be registered in `CodecRegistry`.
- Config: `GLModConfig.SERVER` via `REGISTRATE.registerSynced(...)` (server-synced), built with l2core `ConfigInit` (pattern in `init/data/GLModConfig.java`).
- Optional-mod compat lives in `compat/` (touhoulittlemaid, curios, jei) and must be guarded with `ModList.get().isLoaded(...)`. TLM compat is the most elaborate; always guard new TLM references.
- Mixins go in `mixin/` and must be declared in `src/main/resources/gensokyolegacy.mixins.json` (`defaultRequire: 1` — unlisted/unmatched mixins fail hard).
- Access transformer: `src/main/resources/META-INF/accesstransformer.cfg`.

## Code layout
- `content/entity/characters/<character>/` — per-character Entity/Model/Renderer (fairy, maiden, rumia, merchant, boss); shared youkai base classes in `content/entity/youkai/`; AI in `content/entity/behavior/` (brain/sensor/task/move/combat).
- `content/attachment/` — player/entity data (character data, homes/structures, gap mapping).
- `content/rpg/` — quest/dialog/trade system (condition/requirement/action/reward). Actively developed.
- `content/spell/` — danmaku/spell cards. `content/ui/` — menus and screens.

## Lang
- English: generated from the `GLLang` enum (`init/data/GLLang.java`); `GLLang.FOO$BAR` → key `gensokyolegacy.foo.bar`. Output lands in `src/generated/.../lang/{en_us,en_ud}.json`.
- zh_cn is hand-authored, not generated: split per-category JSON under `src/test/resources/gensokyolegacy/lang/zh_cn/<category>.json`, merged into `src/main/resources/assets/gensokyolegacy/lang/zh_cn.json` by running the `organize.ResourceOrganizer` main. Supports `-cartesian` block expansion. After adding zh_cn strings, rerun the organizer.

## Git
- Two remotes: `base` = upstream `Minecraft-LightLand/GensokyoLegacy`, `origin` = fork `Signifier-Dollhouse`. `main` tracks `base/main`.
- Feature branches in use: `quest`, `multiplex_worldgen`, `youkaishomecoming`.
- Commit style: short lowercase one-liners ("quest tab", "dialog init").
- Bump version via `mod_version` in `gradle.properties` (scheme like `0.3.2+1`).

## Dependencies
- Dev.xkmc L2 ecosystem libs (l2core, l2serial, l2library, l2damagetracker, l2menustacker, l2tabs, l2modularblocks, danmaku_api, fast_projectile_api) are jarJar'd. Dev jars can be pulled from `mavenLocal()` or the flatDir `libs/` (sources jars included) — when bumping a library version, confirm the jar exists locally first. Also GeckoLib, Curios, JEI, Touhou Little Maid, Sodium, Jade.
