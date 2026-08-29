---
name: jappa-art-generator
description: >-
  Guidelines, color palettes, and scripts for generating Minecraft JAPPA-style 16x16 pixel art
  for ingots, nuggets, dusts, raw ores, and blocks. Automatically creates textures for missing objects.
---

# JAPPA Art & Auto-Texture Generator

This skill defines the rules, color ramps, lighting principles, and automated scripts for creating textures and models in the authentic Minecraft **JAPPA** modern pixel art style.

---

## 💡 Fundamental JAPPA Style Rules

1. **Top-Left Lighting Rule**:
   - Primary light source always originates from the **top-left**.
   - Top-left borders, edges, and upper surfaces receive the brightest **specular highlights** and **light rims**.
   - The central mass receives saturated **base / midtone** colors.
   - Bottom-right edges and crevices receive dark **shadows** and **grounding outlines**.
   - *Never invert the lighting ramp (never place bright highlights on the bottom/right).*

2. **Contrast & Color Shading Ramps**:
   - Avoid flat monochromatic ramps. Use subtle hue shifts: highlights shift slightly warmer or towards yellow/cyan, shadows shift slightly cooler or towards deeper amber/slate.
   - Use 5–8 distinct shading steps per material to achieve depth without pixel noise.

3. **Standard Template Dimensions**:
   - All standard item textures are **16x16 RGBA PNG**.
   - All standard block textures are **16x16 RGBA PNG**.

---

## 🎨 Standard Base Templates (StandartTextures/)

When generating textures for new metals, minerals, or resources, ALWAYS use the master templates in StandartTextures/:

- StandartTextures/ingot.png: 16x16 standard ingot base with beveled face and specular highlight.
- StandartTextures/nugget.png: 16x16 standard nugget base.
- StandartTextures/dust.png: 16x16 standard dust/powder pile (top-left lit).
- StandartTextures/raw_ore.png: 16x16 standard raw ore chunk.
- StandartTextures/raw_ore_block.png: 16x16 raw ore cluster block.
- StandartTextures/stone_ore.png: Stone ore base with white vein mask pixels.
- StandartTextures/deepslate_ore.png: Deepslate ore base with white vein mask pixels.

---

## 🖌️ Material Color Palette Reference

| Material | Highlights / Glint | Bright Body | Midtone Body | Shadows | Outline / Ground |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Bronze** | #ffce86 / #ffcb82 | #ec9444 | #ca6c2a / #b86024 | #924214 / #5f260a | #3a1606 |
| **Copper** | #ffc4ac / #f39b7d | #ee8c6c | #d66e50 / #b8543c | #98402c / #64261a | #3e160f |
| **Gold** | #fffc96 / #ffffb5 | #ffde38 | #ebb814 / #c38e08 | #a27004 / #764c00 | #462c00 |
| **Diamond** | #dcffff / #c5ffff | #6ef0ee | #44d0d2 / #28a0a6 | #1c8286 / #125c62 | #0a3438 |
| **Tin** | #fafeff / #f8fcff | #d7e4ec | #b4c4cc / #8e9ea8 | #788892 / #4e5a62 | #2c343a |
| **Iron** | #fcfcfc / #f0f0f0 | #dcdcdc | #b9b9b9 / #949494 | #7d7d7d / #505050 | #2e2e2e |
| **Reinforced** | #d2ffff / #216,255,255 | #5cc6ce | #448492 / #345e6a | #284650 / #1c3038 | #0c1418 |

---

## 🐍 Automated Generation Workflow

When creating a new metal or material:
1. Define the 7–8 color ramp dictionary mapping grayscale keys to RGBA tuples.
2. Run a Python script with Pillow (PIL) to map the template pixels to target output files in:
   - src/main/resources/assets/larperthanwolves/textures/item/
   - src/main/resources/assets/larperthanwolves/textures/block/
3. Automatically generate corresponding item model JSON (parent: minecraft:item/generated), block model JSON, blockstate JSON, and loot tables.