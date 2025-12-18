# JEST Card Game - Implementation Summary
## Expansion Cards, Game Variants, and Save/Load System

---

## 1. EXPANSION CARDS (Cards 6, 7, 8, 9)

### Implementation:
- **Modified**: `properties/Numbers.java`
  - Added enums: `SIX(6)`, `SEVEN(7)`, `EIGHT(8)`, `NINE(9)`
  
- **Modified**: `base/Deck.java`
  - Constructor now accepts `boolean hasExpansion` parameter
  - `initializeDeck()` now conditionally includes expansion cards
  - Added `hasExpansion()` getter

### Usage:
- At game start, player is asked: "Use expansion cards? (1 = No, 2 = Yes)"
- Base deck: 16 suit cards (Ace, 2, 3, 4 × 4 suits) + 1 Joker = **17 cards**
- Expansion deck: 32 suit cards (Ace, 2, 3, 4, 6, 7, 8, 9 × 4 suits) + 1 Joker = **33 cards**

---

## 2. GAME VARIANTS

### New File: `base/GameVariant.java`
Enum with 3 variants:

#### **NORMAL MODE** (Default)
- **Rules**: Standard JEST game
  - Spades and Clubs: Add their face value
  - Diamonds: Subtract their face value
  - Hearts: Worth 0 (unless Joker rules apply)
  - Trophies: Awarded normally
  - Joker: Value depends on Hearts count

#### **NO MERCY**
- **Rules**: Threshold penalty system
  - Random threshold generated at start (between 7-10)
  - If player's Jest value > threshold → Jest value = **0**
  - All other scoring rules remain the same
  - Trophies: Still awarded normally
  - **Risk**: Player must balance scoring vs. exceeding threshold
  - **Example**: Threshold is 8. If player gets 10 points, it becomes 0!

#### **GO ALL OUT**
- **Rules**: Aggressive, high-reward variant
  - **No trophies awarded** (cannot gain trophy cards)
  - **All cards add value** (no negative cards):
    - Spades: Add value ✓
    - Clubs: Add value ✓
    - Diamonds: **Add value** (not subtract) ✓
    - Hearts: **Add value** (not 0) ✓
  - Black pairs: Still add 2 bonus
  - **Joker**: Multiplies total Jest by **1.5** (simple rule)
  - **Strategy**: Aggressive gameplay, focus on high-value cards

---

## 3. GAME CONFIGURATION SYSTEM

### New File: `base/GameConfig.java`
- **Purpose**: Encapsulate all game settings
- **Modularity**: Separates configuration from game logic
- **Extensibility**: Easy to add new variants or settings

#### Key Methods:
```java
GameConfig(boolean useExpansion, GameVariant variant)
isExpansionEnabled()
getVariant()
getNoMercyThreshold()  // Random 7-10 for NO_MERCY
exceedsNoMercyThreshold(int score)
```

---

## 4. VARIANT-SPECIFIC SCORE VISITORS

### Modified: `visitor/FinalScoreVisitor.java`
- Base visitor for NORMAL mode
- Extended by variant-specific visitors

### New File: `visitor/NoMercyScoreVisitor.java`
- Extends `FinalScoreVisitor`
- Applies threshold penalty: if score > threshold, return 0
- Inherits all normal scoring rules

### New File: `visitor/GoAllOutScoreVisitor.java`
- Implements `ScoreVisitor`
- All cards add value (no subtraction)
- Joker multiplies total by 1.5
- Black pairs still bonus +2

---

## 5. SAVE/LOAD SYSTEM

### New File: `base/GameSaveManager.java`
- **Purpose**: Manage game save/load functionality
- **Storage**: `saves/` directory (auto-created)
- **Format**: `JEST_VARIANT_TIMESTAMP.jest` files

#### Key Methods:
```java
saveGame(Map<String, Object> gameState, GameVariant variant)
loadGame(String filename)
listSavedGames()
deleteSave(String filename)
formatSaveInfo(String filename)
```

#### Features:
- Serialization-based approach
- Timestamped filenames (auto-sorts by most recent)
- Error handling with informative messages
- Ready for future integration into game flow

---

## 6. UPDATED GAME CLASS

### Modified: `base/Game.java`
- **New field**: `GameConfig gameConfig`
- **New constructor**: `Game(numberOfPlayers, numberOfHumans, aiDifficulty, gameConfig)`
- **Updated methods**:
  - `startGame()`: Displays game configuration
  - `initializeDeck()`: Uses expansion setting from config
  - `setupTrophies()`: Skipped for GO_ALL_OUT
  - `computeFinalScores()`: Uses variant-specific visitor
  - `endGame()`: Skips trophy awards for GO_ALL_OUT
  - **New method**: `createScoreVisitor()` - Factory pattern for visitors

---

## 7. UPDATED MAIN CLASS

### Modified: `Main.java`
- **New imports**: `GameConfig`, `GameVariant`
- **New flow**:
  1. Player count
  2. Human player count
  3. AI difficulty (if applicable)
  4. **Expansion card choice** (new)
  5. **Variant selection** (new)
  6. Game starts

#### Variant Selection Menu:
```
========================================
     GAME VARIANTS
========================================
1 = NORMAL MODE
    Standard rules: Spades/Clubs add, Diamonds subtract

2 = NO MERCY
    Jest value reset to 0 if exceeds random threshold (7-10)

3 = GO ALL OUT
    No trophies! All cards add value. Joker multiplies by 1.5
```

---

## 8. ARCHITECTURE & OOP PRINCIPLES

### ✓ Modularity
- Independent components linked by clear relations:
  - `GameConfig` → manages configuration
  - `GameVariant` → defines rule sets
  - `ScoreVisitor` family → implements variant rules
  - `Deck` → supports expansion toggle
  - `GameSaveManager` → handles persistence

### ✓ Extensibility
- **Adding new cards**: Extend `Numbers` enum
- **Adding new variants**: Add to `GameVariant`, create new `Visitor` class
- **Changing rules**: Create new visitor without modifying existing code
- **Save features**: `GameSaveManager` API ready for integration

### ✓ Design Patterns Used
- **Strategy Pattern**: `ScoreVisitor` implementations vary by variant
- **Factory Pattern**: `Game.createScoreVisitor()` creates appropriate visitor
- **Visitor Pattern**: Score calculation logic separated from data
- **Configuration Pattern**: `GameConfig` encapsulates settings

---

## 9. GAME FLOW EXAMPLE

### Example: NO MERCY Variant with Expansion
```
1. Start game → Select NO MERCY
2. Threshold randomly set to 8
3. Game plays normally
4. End game scoring:
   - Player A: 12 points → Exceeds threshold of 8 → Score = 0 (NO MERCY!)
   - Player B: 7 points → Below threshold → Score = 7 ✓
   - Winner: Player B
```

### Example: GO ALL OUT Variant
```
1. Start game → Select GO ALL OUT
2. No trophy cards awarded during game
3. All cards add value (Diamonds add, not subtract)
4. If player has Joker: Jest × 1.5
5. End scoring:
   - Player A: 8 Spades + 1 Joker → 8 × 1.5 = 12 points
   - Player B: 6 Diamonds + 2 Clubs → 6 + 2 = 8 points
   - Winner: Player A
```

---

## 10. FILES CREATED/MODIFIED

### Created:
- `base/GameVariant.java` - Variant enum
- `base/GameConfig.java` - Configuration class
- `base/GameSaveManager.java` - Save/load functionality
- `visitor/NoMercyScoreVisitor.java` - NO MERCY scoring
- `visitor/GoAllOutScoreVisitor.java` - GO ALL OUT scoring

### Modified:
- `properties/Numbers.java` - Added SIX, SEVEN, EIGHT, NINE
- `base/Deck.java` - Added expansion support
- `base/Game.java` - Integrated variants and config
- `Main.java` - Added variant and expansion selection

---

## 11. FUTURE ENHANCEMENTS

### Ready to implement:
- Save game during gameplay
- Resume saved game from menu
- Variant statistics tracking
- Custom variant creation UI
- Additional card effects for expansion cards
- Multiplayer online support
- UI/GUI integration

---

## Summary

The implementation successfully adds:
✅ **Expansion cards** (6, 7, 8, 9)  
✅ **3 game variants** with distinct rules  
✅ **Modular, extensible architecture** following OOP principles  
✅ **Save/load system** foundation for persistence  
✅ **Clean separation of concerns** - easy to modify or extend

The code maintains backward compatibility while being fully extensible for future enhancements.
