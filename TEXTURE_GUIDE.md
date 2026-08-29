# 🎨 Руководство по текстурам и ассетам (Larper Than Wolves)

Все текстуры, модели и ассеты находятся в пространстве имён `larperthanwolves` (`assets/larperthanwolves/`).

---

## 📁 Структура директорий

```
src/main/resources/assets/larperthanwolves/
├── blockstates/          # Описания состояний блоков (печь, смешиватель, сито, руды)
├── lang/
│   ├── en_us.json        # Английская локализация
│   └── ru_ru.json        # Русская локализация
├── models/
│   ├── block/            # JSON-модели блоков
│   └── item/             # JSON-модели предметов
└── textures/
    ├── block/            # Текстуры блоков (16x16 PNG)
    ├── gui/container/    # Текстуры интерфейсов (печь, смешиватель, сито)
    ├── item/             # Текстуры предметов (16x16 PNG)
    └── models/armor/     # Текстуры слоев брони (64x32 PNG)
```

---

## 🗡️ Список текстур предметов (`textures/item/`)

### Базовые ресурсы и материалы
* `silicon_shard.png` — кремниевый осколок
* `dry_grass.png` — сухая трава
* `rope.png` — верёвка
* `lighter.png` — зажигалка
* `chisel.png` — стамеска
* `mesh.png` — сетка для сита
* `diamond_ingot.png` — алмазный слиток

### Камешки (Nuggets)
* `stone_nugget.png`
* `diorite_nugget.png`
* `granite_nugget.png`
* `andesite_nugget.png`
* `tuff_nugget.png`
* `calcite_nugget.png`
* `copper_nugget.png`
* `tin_nugget.png`
* `bronze_nugget.png`

### Рудные пыли (Dusts)
* `copper_dust.png`
* `tin_dust.png`
* `iron_dust.png`
* `gold_dust.png`
* `bronze_dust.png`

### Олово и Бронза
* `raw_tin.png`
* `tin_ingot.png`
* `bronze_ingot.png`

### Инструменты и Оружие
* **Кремний**: `silicon_pickaxe.png`, `silicon_axe.png`, `silicon_shovel.png`, `silicon_shears.png`, `silicon_spear.png`
* **Медь**: `copper_sword.png`, `copper_pickaxe.png`, `copper_axe.png`, `copper_shovel.png`, `copper_hoe.png`
* **Бронза**: `bronze_sword.png`, `bronze_pickaxe.png`, `bronze_axe.png`, `bronze_shovel.png`, `bronze_hoe.png`
* **Укреплённое железо**: `reinforced_iron_sword.png`, `reinforced_iron_pickaxe.png`, `reinforced_iron_axe.png`, `reinforced_iron_shovel.png`, `reinforced_iron_hoe.png`

### Иконки брони
* **Медь**: `copper_helmet.png`, `copper_chestplate.png`, `copper_leggings.png`, `copper_boots.png`
* **Бронза**: `bronze_helmet.png`, `bronze_chestplate.png`, `bronze_leggings.png`, `bronze_boots.png`
* **Укреплённое железо**: `reinforced_iron_helmet.png`, `reinforced_iron_chestplate.png`, `reinforced_iron_leggings.png`, `reinforced_iron_boots.png`

---

## 🧱 Список текстур блоков (`textures/block/`)

* `brick_furnace_front.png`, `brick_furnace_front_lit.png`, `brick_furnace_front_embers.png`, `brick_furnace_side.png`, `brick_furnace_top.png`, `brick_furnace_bottom.png`
* `alloy_mixer_front.png`, `alloy_mixer_front_lit.png`, `alloy_mixer_front_embers.png`, `alloy_mixer_side.png`, `alloy_mixer_top.png`, `alloy_mixer_bottom.png`
* `sieve_side.png`, `sieve_top.png`, `sieve_bottom.png`, `sieve_mesh.png`
* `unfired_brick_stage0.png`, `unfired_brick_stage1.png`, `unfired_brick_stage2.png`, `unfired_brick_stage3.png`
* `tin_ore.png`, `deepslate_tin_ore.png`, `raw_tin_block.png`, `tin_block.png`, `bronze_block.png`

---

## 🛡️ Текстуры моделей брони (`textures/models/armor/`)

* `copper_layer_1.png`, `copper_layer_2.png`
* `bronze_layer_1.png`, `bronze_layer_2.png`
* `reinforced_iron_layer_1.png`, `reinforced_iron_layer_2.png`
