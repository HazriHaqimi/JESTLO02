# JEST Card Game - Quick Reference Guide

## Game Startup Flow

```
Main.java
    ↓
Player Count & Names (3-4 players)
    ↓
Human Player Count
    ↓
AI Difficulty (1=Defensive, 2=Offensive, 3=Mixed)
    ↓
EXPANSION CHOICE ← NEW
    (1 = Base deck: 17 cards)
    (2 = Expansion: 33 cards)
    ↓
VARIANT SELECTION ← NEW
    (1 = NORMAL)
    (2 = NO MERCY)
    (3 = GO ALL OUT)
    ↓
GameConfig created with settings
    ↓
Game.startGame()
```

---

## Quick Variant Comparison

| Feature | NORMAL | NO MERCY | GO ALL OUT |
|---------|--------|----------|-----------|
| **Trophies** | ✓ Awarded | ✓ Awarded | ✗ None |
| **Spades/Clubs** | +Value | +Value | +Value |
| **Diamonds** | -Value | -Value | **+Value** |
| **Hearts** | 0 | 0 | **+Value** |
| **Joker Rule** | Complex (Hearts based) | Complex (Hearts based) | **×1.5 multiplier** |
| **Special Rule** | None | Threshold penalty | All cards beneficial |
| **Risk Level** | Low | **High** | Medium |
| **Difficulty** | Balanced | Strategic | Aggressive |

### NO MERCY Threshold Example:
```
Game starts → Random threshold: 7
Round 1: Score 5 points
Round 2: Score 4 points
Total: 9 points > 7 threshold → FINAL SCORE = 0 (NO MERCY!)
```

### GO ALL OUT Example:
```
Jest cards: 4♠ 5♠ 3♦ 2♣ Joker
Calculation:
  4 (Spade) +
  5 (Spade) +
  3 (Diamond - adds instead of subtracts) +
  2 (Club) +
  Joker (×1.5)
= (4 + 5 + 3 + 2) × 1.5 = 14 × 1.5 = 21 points
```

---

## Expansion Cards

### Base Deck (Default)
- **Cards**: Ace, 2, 3, 4 in each suit
- **Total**: 4 suits × 4 values = 16 suit cards + 1 Joker = **17 cards**
- **Trophy count**: 1-2 cards based on player count

### Expansion Deck (Optional)
- **Cards**: Ace, 2, 3, 4, **6, 7, 8, 9** in each suit
- **Total**: 4 suits × 8 values = 32 suit cards + 1 Joker = **33 cards**
- **Impact**: Longer game, more cards in play, higher scores possible
- **Note**: No card 5 (intentional game design)

---

## File Locations

### New Files
```
base/
  ├── GameVariant.java (Enum: NORMAL, NO_MERCY, GO_ALL_OUT)
  ├── GameConfig.java (Configuration container)
  └── GameSaveManager.java (Save/load functionality)

visitor/
  ├── NoMercyScoreVisitor.java (NO MERCY scoring)
  └── GoAllOutScoreVisitor.java (GO ALL OUT scoring)
```

### Modified Files
```
properties/Numbers.java (Added SIX, SEVEN, EIGHT, NINE)
base/Deck.java (Added expansion support)
base/Game.java (Added variant support)
Main.java (Added variant/expansion selection)
```

---

## Game Configuration Class Usage

```java
// Create config with normal mode, no expansion
GameConfig config1 = new GameConfig(false, GameVariant.NORMAL);

// Create config with GO ALL OUT, with expansion
GameConfig config2 = new GameConfig(true, GameVariant.GO_ALL_OUT);

// For NO MERCY, threshold auto-generates (7-10 random)
GameConfig config3 = new GameConfig(false, GameVariant.NO_MERCY);
int threshold = config3.getNoMercyThreshold();  // e.g., 8

// Check if score exceeds NO MERCY threshold
if (config3.exceedsNoMercyThreshold(10)) {
    score = 0;  // Jest value reset to 0
}
```

---

## Score Visitor Factory Pattern

```java
// In Game.java - Factory method creates correct visitor
private ScoreVisitor createScoreVisitor(Jest jest) {
    switch (gameConfig.getVariant()) {
        case NO_MERCY:
            return new NoMercyScoreVisitor(jest, gameConfig);
        case GO_ALL_OUT:
            return new GoAllOutScoreVisitor(jest);
        case NORMAL:
        default:
            return new FinalScoreVisitor(jest);
    }
}
```

---

## Scoring Rules by Variant

### NORMAL Mode Scoring
```
Spade A-4:     +Value (A=5 if alone, else 1)
Club A-4:      +Value (A=5 if alone, else 1)
Diamond A-4:   -Value
Heart A-4:     0
Joker:         Depends on Heart count
Black Pairs:   +2 bonus for Spade+Club same value
```

### NO MERCY Mode Scoring
```
Same as NORMAL, PLUS:
If final_score > threshold (7-10):
    final_score = 0 (PENALTY!)
```

### GO ALL OUT Mode Scoring
```
Spade 1-4,6-9:     +Value
Club 1-4,6-9:      +Value
Diamond 1-4,6-9:   +Value (NOT subtract)
Heart 1-4,6-9:     +Value (NOT 0)
Joker:             (total_score) × 1.5
Black Pairs:       +2 bonus (same as normal)
```

---

## Trophy Awards (Except GO ALL OUT)

### Conditions
- NORMAL: Trophies awarded based on Jest conditions
- NO MERCY: Trophies awarded normally (before threshold penalty)
- GO ALL OUT: **No trophies awarded**

### Example Trophy Conditions
- Highest Spade: Player with highest Spade card
- Lowest Heart: Player with lowest Heart card
- Majority 4s: Player with most cards of value 4
- Joker: Player who has the Joker card
- etc.

---

## Save/Load System (Ready for Integration)

```java
// Save a game
Map<String, Object> gameState = new HashMap<>();
gameState.put("round", 3);
gameState.put("players", players);
GameSaveManager.saveGame(gameState, GameVariant.NO_MERCY);
// Creates: saves/JEST_NO_MERCY_2024-12-18_14-30-45.jest

// List available saves
List<String> saves = GameSaveManager.listSavedGames();
for (String save : saves) {
    System.out.println(GameSaveManager.formatSaveInfo(save));
}

// Load a game
Map<String, Object> loaded = GameSaveManager.loadGame("JEST_NO_MERCY_2024-12-18_14-30-45.jest");

// Delete a save
GameSaveManager.deleteSave("JEST_NO_MERCY_2024-12-18_14-30-45.jest");
```

---

## Architecture Highlights

### Modularity
- Configuration isolated in `GameConfig`
- Variants isolated in `GameVariant` enum
- Scoring logic isolated in `ScoreVisitor` implementations
- Game logic isolated in `Game` class
- UI/Input isolated in `Main` and `InputHandler`

### Extensibility
**Adding new variant:**
1. Add enum to `GameVariant.java`
2. Create new `Visitor` class
3. Update `Game.createScoreVisitor()` switch statement
4. Add menu option in `Main.java`

**Adding expansion rules:**
1. Create new cards in `Numbers` enum
2. Adjust deck initialization logic
3. Update scoring visitors if needed

**Adding persistence features:**
1. GameSaveManager already created
2. Call `GameSaveManager.saveGame()` when needed
3. Add load menu in `Main.java`

---

## Testing Checklist

- [ ] Base deck: 17 cards total
- [ ] Expansion deck: 33 cards total
- [ ] NORMAL variant: Standard scoring
- [ ] NO MERCY variant: Threshold applied (7-10)
- [ ] GO ALL OUT variant: No trophies, all cards add, Joker ×1.5
- [ ] Variant selection menu appears
- [ ] Expansion selection menu appears
- [ ] Game config displays correctly
- [ ] AI players work with all variants
- [ ] Human player input works with all variants
- [ ] Final scores calculated per variant rules
- [ ] Winner determined correctly

---

## Common Issues & Solutions

### Issue: Threshold not applied in NO MERCY
**Solution**: Ensure `NoMercyScoreVisitor` is created instead of `FinalScoreVisitor`

### Issue: Trophies appearing in GO ALL OUT
**Solution**: Check `if (gameConfig.getVariant() != GameVariant.GO_ALL_OUT)` in `startGame()`

### Issue: Expansion cards not in deck
**Solution**: Verify `GameConfig.isExpansionEnabled()` returns true and is passed to `Deck` constructor

### Issue: Joker not multiplying in GO ALL OUT
**Solution**: Ensure `GoAllOutScoreVisitor.visit(JokerCard)` is called and multiplies by 1.5

---

## Design Patterns Used

1. **Strategy Pattern**: `ScoreVisitor` implementations
2. **Factory Pattern**: `Game.createScoreVisitor()`
3. **Visitor Pattern**: Score calculation logic
4. **Configuration Pattern**: `GameConfig` encapsulation
5. **Enum Pattern**: `GameVariant` for type-safe options

---

## Next Steps (Future Development)

1. **Save During Game**: Add save option in Round/Game loop
2. **Resume Game**: Add main menu to load saved games
3. **Statistics**: Track wins, average scores per variant
4. **Custom Variants**: Allow players to create custom rules
5. **GUI Integration**: Migrate console UI to Swing/JavaFX
6. **Multiplayer**: Network support for online games
7. **Card Effects**: Special effects for expansion cards
8. **Achievements**: Badge system for variant completion
