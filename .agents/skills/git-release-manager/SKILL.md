---
name: git-release-manager
description: >-
  Standardizes semantic versioning (MAJOR.MINOR.PATCH), Conventional Commits (feat, fix, docs, chore),
  automated git tagging, build verification, and mandatory user push permission checks.
---

# Git Release & Versioning Manager

This skill governs version management, commit formatting, local tag creation, and user authorization for pushing changes to GitHub.

---

## 1. Semantic Versioning Rules (MAJOR.MINOR.PATCH)

The mod version is maintained in:
- gradle.properties (mod_version = X.Y.Z)
- Git tags (vX.Y.Z)

### Increment Rules:
- PATCH (X.Y.Z+1): Small bug fixes, texture tweaks, typos, minor adjustments, performance polish.
- MINOR (X.Y+1.0): New gameplay features, new machines, new materials/tools/armor sets, non-breaking refactors.
- MAJOR (X+1.0.0): Large overhauls, breaking save/API compatibility, major Minecraft/NeoForge platform migrations.

---

## 2. Standardized Conventional Commits

All commit messages MUST follow the format:
<type>(<optional scope>): <short description in Russian or English>

### Allowed Types:
- feat: A new feature, item, block, mechanic, or recipe.
- fix: A bug fix, drop calculation fix, texture correction.
- refactor: Code restructuring without changing external behavior.
- docs: Documentation updates (AGENTS.md, README.md, skills).
- chore: Gradle tasks, asset reorganization, build script maintenance.

### Example:
feat(materials): add diamond dust and JAPPA textures for dusts and ingots

---

## 3. Push Confirmation Requirement

MANDATORY RULE:
At the end of every action or release, NEVER push to remote (git push, git push origin --tags) automatically without user permission.
Always ask the user for confirmation (Yes / No).
