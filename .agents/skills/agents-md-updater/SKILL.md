---
name: agents-md-updater
description: >-
  Mandatory protocol to inspect all changes made during a task and automatically update AGENTS.md and README.md with full accuracy.
---

# Documentation Post-Task Audit & Update Protocol (AGENTS.md, README.md & Platform Descriptions)

This skill enforces a mandatory post-modification review step: whenever changes are made to the codebase (new items, blocks, mechanics, recipes, textures, balance adjustments, or configs), the agent MUST audit the diff and update:
1. `AGENTS.md` (detailed developer source of truth)
2. `README.md` (user-facing GitHub repository documentation)
3. `docs/curseforge_modrinth_description.md` (curated platform description for CurseForge & Modrinth)

---

## 🔍 Audit Checklist

Before finishing any task:
1. **New / Removed Items, Blocks & Mechanics**:
   - Verify all new registrations in `ModBlocks.java`, `ModItems.java`, `ModCreativeTabs.java`.
   - Update file trees, item listings, and localization notes in `AGENTS.md`.
   - Update user-facing mechanics, progression sections, and tables in `README.md`.
2. **CurseForge & Modrinth Description Synchronization (`docs/curseforge_modrinth_description.md`)**:
   - Mirror all new features, balance changes, recipes, and mechanics from `README.md` to `docs/curseforge_modrinth_description.md`.
   - **Formatting & Sanitation Rule**: CurseForge and Modrinth Markdown/HTML parsers fail or display artifacts on LaTeX math expressions and non-standard symbols:
     - Replace all LaTeX arrows (`$\rightarrow$`, `$\leftarrow$`) with standard text arrows (`->`, `<-`).
     - Replace LaTeX inequalities and math symbols (`$\le$`, `$\ge$`, `$\pm$`) with clean characters (`<=`, `>=`, `+/-`).
     - Ensure clean, web-safe markdown and headers that render flawlessly on both CurseForge and Modrinth.
3. **Mining Tier & Drop Rules**:
   - Check if `BlockBreakHandler.java` drop tables, tool restrictions, or hardness were updated.
   - Document any changes in the Tool Tier Matrix & Soils sections of `AGENTS.md`, `README.md`, and platform descriptions.
4. **Machine & Processing Logic**:
   - Check if `FuelRegistry`, `AlloyRegistry`, `SmeltingRegistry`, or `FoodCookingRegistry` recipes/mechanics were altered.
   - Document updated slot configurations, burn times, recipes, and automation across all documents.
5. **Semantic Versioning & Tags**:
   - Check `gradle.properties` (`mod_version = X.Y.Z`) and ensure `AGENTS.md`, `README.md`, and platform descriptions match the current version.

---

## 📝 Update Procedure
1. Run `git status` or inspect modified files.
2. Update the corresponding sections in `AGENTS.md`.
3. Update `README.md` whenever game mechanics, features, recipes, or balance rules are added or altered.
4. Synchronize and sanitize changes into `docs/curseforge_modrinth_description.md`.
5. Verify that none of the documents contain stale references or broken symbols.