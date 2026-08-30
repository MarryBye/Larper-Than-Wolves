# Entity Observer (`entity_observer` / `EntityObserverBlock`)

The **Entity Observer** is a specialized redstone detector block with a carved Skeleton face sensor on the front and a redstone output port on the back. It emits a redstone pulse whenever any entity enters the 1-block volume in front of its sensor face.

---

## ⚙️ Mechanics & Working Principles
- **Block States & Properties**:
  - `FACING` (`Direction.NORTH, SOUTH, EAST, WEST, UP, DOWN`): Supports full 6-directional orientation.
  - `POWERED` (`Boolean`): `true` while actively outputting a redstone pulse.
- **Detection Algorithm & Volume**:
  - Checks the $1.0 \times 1.0 \times 1.0$ bounding box immediately adjacent to the sensor face (`pos.relative(facing)`).
  - Detects **all entity types**: players, monsters, peaceful animals, dropped item entities, arrows, and minecarts.
- **Redstone Pulse Timing**:
  - When an entity is detected, sets `POWERED = true` and schedules a tick for **4 ticks (0.2s)**.
  - After 4 ticks, if no entities remain in the sensor zone, resets `POWERED = false`.
  - Outputs strong redstone power (level 15) strictly from the opposite back face.

---

## 📦 Crafting & Progression
- **Recipe**: 6 Cobblestone (top/bottom rows) + 2 Redstone Dust (left/right middle) + 1 Bone (center).
- **Station**: 3x3 Crafting Table.
- **Tier Requirement**: Requires Redstone Dust and Bone (obtained early via mob drops or peaceful animal hunting).

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Connects to Create redstone links, gearshift triggers, clutch controllers, and sequenced gearshifts.
- **Kinetic Piston Traps**: Placing an Entity Observer adjacent to a Kinetic Piston creates an automatic entity catapult or mob trap.

---

## 🧪 Testing Guide & Edge Cases
1. **Directional Sensor Check**: Place Entity Observers facing all 6 directions (including UP and DOWN). Connect redstone lamps to their rear ports.
2. **Entity Movement Detection**: Walk in front of the sensor face. Verify the redstone lamp lights up for 4 ticks and then turns off.
3. **Item Entity Detection**: Throw an item in front of the sensor face. Verify a 4-tick redstone pulse is emitted.
4. **Continuous Occupation**: Stand still in the sensor zone. Verify the redstone pulse remains active until stepping out.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/EntityObserverBlock.java`
- Blockstates: `src/main/resources/assets/larperthanwolves/blockstates/entity_observer.json`
- Models: `src/main/resources/assets/larperthanwolves/models/block/entity_observer.json`
