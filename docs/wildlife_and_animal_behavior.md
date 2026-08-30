# Wildlife & Animal Behavior Overhaul

This system revamps animal behavior, creating a dangerous and dynamic predator-prey ecosystem, reactive animal panic, and defensive mechanics.

---

## ⚙️ Mechanics & Working Principles
- **Wild Wolf Predation (`AnimalBehaviorHandler`)**:
  - Wild, untamed wolves actively hunt **ALL peaceful animals** (cows, pigs, sheep, chickens, rabbits, horses, llamas, goats).
  - Wolves trigger dung production after devouring prey.
- **Enhanced Animal Panic & Fleeing**:
  - Damaged peaceful animals receive a high sprint speed buff (**Speed II**) and actively pathfind away from the attacker.
- **Cow Defensive Kick**:
  - If a player or mob attacks a cow in close melee range ($\le 3.0$ blocks), the cow immediately delivers a powerful rear hoof kick.
  - **Effects**: Deals 5.0 damage (2.5 hearts), applies strong backwards knockback, spawns crit particles, and plays an impact sound before the cow sprints away with **Speed III**.
- **Mob Drop Rebalancing**:
  - Peaceful animals drop bones upon slaughter (30% chance for large animals, 15% for small).
  - Zombies and variants drop 1 bone with a 25% chance.
  - Iron Golems drop **0 iron ingots / nuggets** (only poppies).

---

## 🧪 Testing Guide & Edge Cases
1. **Cow Melee Kick**: Approach a cow with an empty hand and punch it at point-blank range. Verify the player takes 2.5 hearts of damage, is knocked backward, and the cow flees rapidly.
2. **Wolf Predation**: Spawn a wild wolf near a sheep. Verify the wolf targets, attacks, and defeats the sheep.

---

## 📂 Key Source Files
- Handler: `src/main/java/io/marrybye/github/larperthanwolves/event/AnimalBehaviorHandler.java`
- Mob Drops: `src/main/java/io/marrybye/github/larperthanwolves/event/MobDropHandler.java`
