---
name: git-release-manager
description: >-
  Standardizes semantic versioning (MAJOR.MINOR.PATCH), mandatory iterative version bumping on EVERY user follow-up/fix,
  Conventional Commits (feat, fix, docs, chore), automated git tagging, build verification, and user push permission checks.
---

# Git Release & Versioning Manager

This skill governs version management, commit formatting, local tag creation, structured development reporting, the iterative version refinement lifecycle, and user authorization for pushing changes to GitHub.

---

## 1. Semantic Versioning Rules (MAJOR.MINOR.PATCH)

The mod version is maintained in:
- `gradle.properties` (`mod_version = X.Y.Z`)
- `AGENTS.md` (`- **Current Version**: X.Y.Z`)
- Git tags (`vX.Y.Z`)

### Increment Rules:
- **PATCH (X.Y.Z+1)**: Bug fixes, texture tweaks, model adjustments, recipe rebalancing, small improvements.
- **MINOR (X.Y+1.0)**: New gameplay features, new machines, new materials/tools/armor sets, non-breaking overhauls.
- **MAJOR (X+1.0.0)**: Large game-breaking overhauls, save/world migration breaks, platform/MC version updates.

---

## 2. Release & Iterative Refinement Lifecycle Protocol

> [!IMPORTANT]
> The release and versioning workflow follows a strict multi-step interactive lifecycle:

### Step 1: Task Completion & Structured Reporting
1. Create or set the target version in `gradle.properties`, `AGENTS.md`, `README.md`, and `docs/`.
2. Implement all requested changes and compile/verify with `./gradlew build`.
3. Create a local conventional commit and update/create the local git tag `vX.Y.Z`.
4. Report to the user with a structured summary:
   - **Accomplishments**: Overview of features/changes implemented.
   - **Problems & Solutions**: Any compilation errors, crashes, edge cases, or bugs encountered during development and how they were fixed.
   - **Summary of Results**: Clear, concise status of the build and files.
5. Ask the user the mandatory question:
   **"Мы продолжаем работу над этой версией мода или заканчиваем?"**

### Step 2: Iterative Refinement on Current Version (No Version Bump)
- If the user indicates that work continues (e.g. "продолжаем", or provides follow-up tweaks, bug reports, balance tweaks, or model adjustments for this version):
  - **DO NOT** increment the version number. Stay on the current version `X.Y.Z`.
  - Implement the requested refinements on this version.
  - Compile, verify (`./gradlew build`), update documentation, and update the local commit / tag `vX.Y.Z`.
  - Report any issues and solutions.
  - Re-ask: **"Мы продолжаем работу над этой версией мода или заканчиваем?"**
  - Continue this loop until the user explicitly states that work is finished.

### Step 3: Work Completion & Push Authorization
- When the user explicitly states that work is finished (e.g. "заканчиваем", "работа закончена", "всё готово"):
  - Ask the user for confirmation to push:
    **"Хотите сделать пуш изменений на GitHub?"**

### Step 4: Remote Push Execution
- If the user responds affirmatively (e.g. "Да", "Пушь"):
  - Execute `git push` and `git push origin --tags`.
  - Confirm successful upload to remote.

---

## 3. Standardized Conventional Commits

All commit messages MUST follow the format:
`<type>(<optional scope>): <short description in Russian or English>`

### Allowed Types:
- `feat`: A new feature, item, block, mechanic, or recipe.
- `fix`: A bug fix, drop calculation fix, texture correction, model fix, or recipe rebalance.
- `refactor`: Code restructuring without changing external behavior.
- `docs`: Documentation updates (`AGENTS.md`, `README.md`, `docs/`, skills).
- `chore`: Gradle tasks, asset reorganization, CI/CD, build script maintenance.

### Examples:
- `feat(mechanisms): add Kinetic Piston, Filter Grate, and Entity Observer`
- `fix(sieve): fix mesh shaking cooldown and Create rotation speed scaling`
- `docs(skills): update documentation and release management skills`
