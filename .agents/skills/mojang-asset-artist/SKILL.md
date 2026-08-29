---
name: mojang-asset-artist
description: >-
  Mandatory protocol for creating and editing pixel art textures by adapting official Mojang vanilla assets
  in strict accordance with JAPPA modern art standards and color ramps.
---

# Mojang Asset Adaptation & JAPPA Artistry Protocol

This skill mandates that whenever textures (items, blocks, entity layers, UI icons) are created or modified for this mod, they MUST be built upon official **Mojang vanilla assets** as their foundation, while strictly following modern **JAPPA** lighting, shading, and pixel art standards.

---

## 🏛️ Core Principles

### 1. Always Ground Textures in Official Mojang Assets
- **Never draw textures entirely from scratch** when an official Mojang asset exists that can serve as a base, template, or constituent element.
- Identify and extract candidate vanilla textures directly from the client JAR (`~/.gradle/caches/**/minecraft_1.21.1_client.jar`) or vanilla resources (e.g., `stick.png`, `string.png`, `leather.png`, `wheat.png`, `short_grass.png`, `fern.png`, `oak_planks.png`, `stripped_oak_log.png`, `scaffolding_side.png`, `ladder.png`, `lead_knot.png`).
- Seamlessly blend, compose, re-tint, or modify vanilla pixel clusters to ensure 100% aesthetic harmony with modern Minecraft.

### 2. Justified & Purposeful Modifications
- Vanilla assets should be used:
  - **Directly / Composed**: Combining multiple vanilla elements (e.g. constructing a wooden frame by combining vanilla `stick.png` segments and binding them with `string.png` pixels).
  - **Palette-Shifted (Hue/Value Ramps)**: Re-coloring a vanilla template using authentic JAPPA material ramps (e.g., shifting vanilla leather to dark tanned leather, or wheat to wilted sun-dried hay).
  - **Form-Modified**: Subtly adjusting posture, orientation, or bounds when justified by the gameplay concept (e.g. loosening and drooping grain stalks so they look wilted and unbound).

### 3. Strict Modern JAPPA Style Rules
1. **Top-Left Lighting**:
   - Specular highlights and glints belong strictly on **top-left** borders and upper faces.
   - Base midtones occupy the central body mass.
   - Deep shadows and dark grounding outlines belong strictly on the **bottom-right** edges.
2. **Contrast & Color Shading Ramps**:
   - 5–8 distinct, hue-shifted shading steps per material.
   - Warm-shifted highlights and cool-shifted shadows (avoiding flat monochromatic ramps).
3. **Dimensions**:
   - All standard item and block textures MUST remain crisp **16x16 RGBA PNG**.

---

## 🐍 Automated Extraction & Composition Workflow

When generating textures:
1. Locate and extract relevant vanilla source textures from `minecraft_1.21.1_client.jar` using Python (`zipfile` & `PIL.Image`).
2. Compose / transform the layers (cropping, masking, merging, re-mapping palettes).
3. Verify top-left lighting and edge contrast.
4. Save directly into `src/main/resources/assets/larperthanwolves/textures/item/` or `textures/block/`.
5. Remove any temporary scratch files.
