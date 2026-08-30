---
name: git-release-manager
description: >-
  Standardizes semantic versioning (MAJOR.MINOR.PATCH), mandatory iterative version bumping on EVERY user follow-up/fix,
  Conventional Commits (feat, fix, docs, chore), automated git tagging, build verification, and user push permission checks.
---

# Git Release & Versioning Manager

This skill governs version management, commit formatting, local tag creation, mandatory iterative version bumps on follow-up tasks, and user authorization for pushing changes to GitHub.

---

## 1. Semantic Versioning Rules (MAJOR.MINOR.PATCH)

The mod version is maintained in:
- `gradle.properties` (`mod_version = X.Y.Z`)
- `AGENTS.md` (`- **Current Version**: X.Y.Z`)
- Git tags (`vX.Y.Z`)

### Increment Rules:
- **PATCH (X.Y.Z+1)**: Bug fixes, texture tweaks, model adjustments, recipe rebalancing, polish, and follow-up iterative refinements.
- **MINOR (X.Y+1.0)**: New gameplay features, new machines, new materials/tools/armor sets, non-breaking overhauls.
- **MAJOR (X+1.0.0)**: Large game-breaking overhauls, save/world migration breaks, platform/MC version updates.

---

## 2. Mandatory Iterative Version Bumping (Continuous Release Rule)

> [!IMPORTANT]
> **EVERY follow-up iteration, refinement, or fix request from the user MUST increment the version!**
> 
> - Even if the user asks to "доработать обновление перед пушем", "поправить текстуру", "исправить баг", "изменить крафт", or "доделать модель" immediately after a previous task:
>   - **DO NOT** reuse, force-update, or overwrite the same version number or git tag!
>   - **DO NOT** stay on the same `X.Y.Z` across multiple iterative turns!
>   - **ALWAYS** bump the version immediately in `gradle.properties` and `AGENTS.md` (e.g. `1.27.0` $\rightarrow$ `1.27.1` $\rightarrow$ `1.27.2` $\rightarrow$ `...`).
>   - Create a fresh new tag `vX.Y.Z` for every new completed request.

---

## 3. Standardized Conventional Commits

All commit messages MUST follow the format:
`<type>(<optional scope>): <short description in Russian or English>`

### Allowed Types:
- `feat`: A new feature, item, block, mechanic, or recipe.
- `fix`: A bug fix, drop calculation fix, texture correction, model fix, or recipe rebalance.
- `refactor`: Code restructuring without changing external behavior.
- `docs`: Documentation updates (`AGENTS.md`, `README.md`, skills).
- `chore`: Gradle tasks, asset reorganization, CI/CD, build script maintenance.

### Examples:
- `feat(mechanisms): add Kinetic Piston, Filter Grate, and Entity Observer`
- `fix(mechanisms): fix observer orientation, filter grate Z-fighting, and piston extension`
- `fix(recipes): rebalance recipes for kinetic piston, filter grate, and entity observer`

---

## 4. Push Confirmation Requirement

MANDATORY RULE:
At the end of every action or release, NEVER push to remote (`git push`, `git push origin --tags`) automatically without user permission.
Always ask the user for confirmation (Yes / No).
