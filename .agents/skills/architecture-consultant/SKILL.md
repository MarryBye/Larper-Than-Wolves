---
name: architecture-consultant
description: >-
  Protocol for evaluating and proposing global architectural changes or breaking refactors.
  Mandates presenting structured options, trade-offs, and expected outcomes to the user for explicit approval before execution.
---

# Global Architectural Consultation Protocol

This skill enforces strict rules when proposing or encountering changes that have a wide impact on the mod's architecture, balance, save compatibility, or progression systems.

---

## 🛑 When to Trigger Consultation

Always pause, consult, and ask the user for a decision when a task involves:
1. **Adding or removing whole systems** (e.g., introducing a new power system, removing vanilla mechanics, overhauling mining progression).
2. **Breaking save compatibility** (e.g., renaming BlockEntity IDs, altering registry names of existing blocks/items).
3. **Major gameplay balance shifts** (e.g., changes to tool durability tiers, fuel consumption formulas, ore generation distribution).
4. **Altering core registries** (e.g., FuelRegistry, AlloyRegistry, SmeltingRegistry, ModToolMaterials).

---

## 📋 Consultation Format

When presenting a choice to the user:
1. **Summary of the Problem / Opportunity**: Clearly explain why the change is being considered.
2. **Options with Trade-offs**:
   - **Option A (Recommended)**: The cleanest / most modular path + expected result.
   - **Option B (Alternative)**: A lighter or different approach + trade-offs.
   - **Option C**: Status quo / minimal patch.
3. **Estimated Impact**: List affected classes, recipes, client screens, or save data.
4. **Call to Action**: Wait for the user's explicit response before implementing the breaking part.