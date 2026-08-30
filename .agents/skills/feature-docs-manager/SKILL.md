---
name: feature-docs-manager
description: >-
  Mandatory protocol for creating, updating, and referencing granular feature documentation in docs/. Requires a dedicated doc for every mechanic, pre-modification reading of existing feature docs, and post-modification synchronization alongside AGENTS.md and README.md.
---

# Feature Documentation Protocol (`docs/`)

All individual game mechanics, machines, blocks, items, and overhaul systems must have their own granular, dedicated markdown documentation file in the `docs/` directory.

This protocol ensures that deep technical and architectural context, invariants, edge cases, testing guidelines, and cross-mod interactions are preserved across development iterations, making the codebase clear, discoverable, and maintainable.

---

## 🎯 Core Rules & Workflow

### 1. Dedicated File per Feature / Mechanic
* Every distinct system in the mod must have its own file in `docs/` (e.g. `docs/kinetic_piston.md`, `docs/hand_mill_and_crank.md`, `docs/filter_grate.md`).
* Files must be named in lowercase with underscores (`feature_name.md`).

### 2. Pre-Modification Consultation (Mandatory Reading)
* **BEFORE** modifying, rebalancing, refactoring, or fixing any existing feature, the agent **MUST** read and inspect the corresponding feature documentation file in `docs/`.
* Understand existing design intent, collision bounds, tick intervals, state transitions, client-server sync requirements, and edge cases before making code changes.

### 3. Post-Modification Documentation Sync (The 3-Pillar Rule)
Whenever any feature or mechanic is added, modified, rebalanced, or refactored, the agent MUST update all 3 documentation tiers:
1. **`docs/<feature>.md`**: Granular deep-dive: exact formulas, block states, container slots, testing instructions, cross-mod details.
2. **`AGENTS.md`**: Architectural & technical summary, tool matrices, registry listings, file maps, agent protocols.
3. **`README.md`**: User-facing gameplay guides, tables, release notes, and player-oriented mechanics descriptions.

---

## 📐 Standard Feature Document Structure

Every file in `docs/` MUST follow this standardized logical section hierarchy:

```markdown
# [Feature Name] ([In-Game Name / ID])

Brief 2-3 sentence overview of what the feature is, its survival/thematic purpose, and player progression stage.

---

## ⚙️ Mechanics & Working Principles
- **Block States & Properties**: Specific block state properties (e.g. `FACING`, `POWERED`, `EXTENDED`, `STAGE`).
- **Core Algorithms & Ticking**: Exact tick rates, timers, formulas, energy thresholds, and state machines.
- **Inventory & Containers**: Slot layout, input/output faces, insertion/extraction rules (`WorldlyContainer`).
- **Player Interactions**: Left-click / right-click behaviors, required tools, audio/visual cues, particles.

---

## 📦 Crafting & Progression
- **Recipe & Ingredients**: Crafting grid requirements, work station needed (2x2, 3x3, Hand Mill, Chisel).
- **Tool Tier / Harvesting**: Minimum tool tier, harvest penalties, drop chances.
- **JEI Integration**: Custom recipe categories, information tabs (`addIngredientInfo`), ghost ingredient support.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Kinetic rotational force support, RPM scaling, mechanical mixer/saw/millstone/fan bulk recipes.
- **Redstone & Automation**: Hopper compatibility, comparator outputs, redstone pulse durations, power inversion.
- **Common Tags**: Conventional tags (`c:ingots/*`, `c:dusts/*`, etc.) utilized.

---

## 🧪 Testing Guide & Edge Cases
- **Step-by-Step Test Procedure**: Concrete instructions for verifying the mechanic in-game or via unit/game tests.
- **Edge Cases & Invariants**:
  - Block collision boxes & bounding volume limits.
  - Chunk unloading / reload safety.
  - Multiplayer / Server-to-Client synchronization (`BlockEntity` packets / `setChanged`).
  - Item entity drops & prevention of item deletion/duplication.

---

## 📂 Key Source Files
- Blocks: `src/main/java/.../block/ExampleBlock.java`
- Block Entities: `src/main/java/.../block/entity/ExampleBlockEntity.java`
- Menus & Screens: `src/main/java/.../menu/ExampleMenu.java`, `src/main/java/.../client/ExampleScreen.java`
- Models & Blockstates: `src/main/resources/assets/.../blockstates/example.json`
- Textures: `src/main/resources/assets/.../textures/...`
```

---

## 📋 Feature Creation & Modification Checklist

Before completing any task involving game mechanics:
- [ ] Has the corresponding `docs/<feature>.md` been read before starting changes?
- [ ] For a new feature: has a new `docs/<feature>.md` been created following the standard structure?
- [ ] For an updated feature: has `docs/<feature>.md` been updated with new values, behaviors, and testing steps?
- [ ] Have `AGENTS.md` and `README.md` been synchronized?
