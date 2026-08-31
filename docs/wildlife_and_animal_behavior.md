# Wildlife & Animal Behavior Overhaul

This system revamps animal behavior, creating a dangerous and dynamic predator-prey ecosystem, reactive animal panic, persistent fleeing, and defensive mechanics.

---

## ⚙️ Mechanics & Working Principles
- **Wild Wolf Predation (`AnimalBehaviorHandler`)**:
  - Wild, untamed wolves actively hunt **ALL peaceful animals** (cows, pigs, sheep, chickens, rabbits, horses, llamas, goats).
  - Wolves trigger dung production after devouring prey.
- **Persistent Animal Fleeing (`PersistentFleeGoal`)**:
  - When damaged by a player or predator, peaceful animals do NOT just run for a couple of seconds.
  - They enter persistent flight mode, sprinting with **Speed II (1.8x speed)** and continuously recalculating pathfinding away from the attacker until they are at least **30 blocks away** (`FLEE_DISTANCE_THRESHOLD = 30.0D`).
  - If the attacker pursues them, they keep running without stopping until the 30-block safety distance is established.
- **Cow Defensive Kick**:
  - If a player or mob attacks a cow in close melee range ($\le 3.0$ blocks), the cow immediately delivers a powerful rear hoof kick.
  - **Effects**: Deals 5.0 damage (2.5 hearts), applies strong backwards knockback, spawns crit particles, and plays an impact sound before the cow sprints away with **Speed III**.
- **Mob Drop Rebalancing**:
  - Peaceful animals drop bones upon slaughter (30% chance for large animals, 15% for small).
  - Zombies and variants drop 1 bone with a 25% chance.
  - Iron Golems drop **0 iron ingots / nuggets** (only poppies).

---

## 🧪 Testing Guide & Edge Cases
1. **Persistent Fleeing**: Attack a sheep or cow. Chase after the animal and verify it continues fleeing dynamically at high speed without stopping after 2 seconds, only calming down once it is 30+ blocks away.
2. **Cow Melee Kick**: Approach a cow with an empty hand and punch it at point-blank range. Verify the player takes 2.5 hearts of damage, is knocked backward, and the cow flees rapidly.
3. **Wolf Predation**: Spawn a wild wolf near a sheep. Verify the wolf targets, attacks, and defeats the sheep.

---

## 📂 Key Source Files
- Handler & AI Goals: `src/main/java/io/marrybye/github/larperthanwolves/event/AnimalBehaviorHandler.java`
- Disabled Items & Mob Drops: `src/main/java/io/marrybye/github/larperthanwolves/event/DisabledItemsHandler.java`
