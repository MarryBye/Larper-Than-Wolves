---
name: agents-md-updater
description: >-
  Mandatory protocol to inspect all changes made during a task and automatically update AGENTS.md and README.md with full accuracy.
---

# Documentation Post-Task Audit & Update Protocol (AGENTS.md & README.md)

This skill enforces a mandatory post-modification review step: whenever changes are made to the codebase (new items, blocks, mechanics, recipes, textures, balance adjustments, or configs), the agent MUST audit the diff and update **BOTH** `AGENTS.md` and `README.md` to ensure they remain 100% accurate, up-to-date sources of truth.

---

## 🔍 Audit Checklist

Before finishing any task:
1. **New / Removed Items, Blocks & Mechanics**:
   - Verify all new registrations in `ModBlocks.java`, `ModItems.java`, `ModCreativeTabs.java`.
   - Update file trees, item listings, and localization notes in `AGENTS.md`.
   - Update user-facing mechanics, progression sections, and tables in `README.md`.
2. **Mining Tier & Drop Rules**:
   - Check if `BlockBreakHandler.java` drop tables, tool restrictions, or hardness were updated.
   - Document any changes in the Tool Tier Matrix & Soils sections of both `AGENTS.md` and `README.md`.
3. **Machine & Processing Logic**:
   - Check if `FuelRegistry`, `AlloyRegistry`, `SmeltingRegistry`, or `FoodCookingRegistry` recipes/mechanics were altered.
   - Document updated slot configurations, burn times, recipes, and automation in `AGENTS.md` and `README.md`.
4. **Semantic Versioning & Tags**:
   - Check `gradle.properties` (`mod_version = X.Y.Z`) and ensure both `AGENTS.md` and `README.md` match the current version.

---

## 📝 Update Procedure
1. Run `git status` or inspect modified files.
2. Update the corresponding sections in `AGENTS.md`.
3. Update `README.md` whenever game mechanics, features, recipes, or balance rules are added or altered.
4. Verify that neither `AGENTS.md` nor `README.md` contain stale references.