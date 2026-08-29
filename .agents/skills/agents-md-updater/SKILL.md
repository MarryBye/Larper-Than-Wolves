---
name: agents-md-updater
description: >-
  Mandatory protocol to inspect all changes made during a task and automatically update AGENTS.md with full accuracy.
---

# AGENTS.md Post-Task Audit & Update Protocol

This skill enforces a mandatory post-modification review step: whenever changes are made to the codebase (new items, blocks, mechanics, recipes, textures, or configs), the agent MUST audit the diff and update AGENTS.md to ensure it remains the single, 100% accurate source of truth for future agent invocations.

---

## 🔍 Audit Checklist

Before finishing any task:
1. **New / Removed Items or Blocks**:
   - Verify all new registrations in ModBlocks.java, ModItems.java, ModCreativeTabs.java.
   - Update file trees, item listings, and localization notes in AGENTS.md.
2. **Mining Tier & Drop Rules**:
   - Check if BlockBreakHandler.java drop tables or tier restrictions were updated.
   - Document any changes in the Progression section of AGENTS.md.
3. **Machine & Processing Logic**:
   - Check if FuelRegistry, AlloyRegistry, or SmeltingRegistry recipes or mechanics were altered.
   - Document updated slot configurations, burn times, and machine behaviors.
4. **Semantic Versioning & Tags**:
   - Check gradle.properties (mod_version = X.Y.Z) and ensure AGENTS.md matches the current version.

---

## 📝 Update Procedure
1. Run git status or inspect modified files.
2. Update the corresponding sections in AGENTS.md.
3. Verify that AGENTS.md contains zero stale references.