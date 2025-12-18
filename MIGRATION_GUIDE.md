# JEST Card Game - Migration & Integration Guide

## What Changed

This document explains what was modified and how to use the new features.

---

## 1. Configuration System (NEW)

### Before
```java
// Old code - direct game creation
Game game = new Game(3, 2, 1);  // 3 players, 2 human, difficulty 1
game.startGame();
```

### After
```java
// New code - with configuration
GameConfig config = new GameConfig(true, GameVariant.NO_MERCY);
Game game = new Game(3, 2, 1, config);
game.startGame();
```

### Benefits
- ✓ Clear separation of concerns
- ✓ Easy to test different configurations
- ✓ Can be persisted/loaded later
- ✓ Extensible for future settings

---

## 2. Expansion Card System

### Before
```java
// Deck always had Ace, 2, 3, 4
private void initializeDeck() {
    for (Suit suit : Suit.values()) {
        for (Numbers number : Numbers.values()) {
            cards.add(new SuitCard(suit, number));
        }
    }
    cards.add(new JokerCard());
}
// Result: 17 cards (4 suits × 4 values + 1 Joker)
```

### After
```java
// Deck constructor accepts expansion flag
public Deck(boolean hasExpansion) {
    this.cards = new ArrayList<>();
    this.hasExpansion = hasExpansion;
    initializeDeck();
}

// Conditionally includes 6, 7, 8, 9
private void initializeDeck() {
    for (Suit suit : Suit.values()) {
        for (Numbers number : Numbers.values()) {
            // Skip expansion cards if disabled
            if (!hasExpansion && isExpansionCard(number)) {
                continue;
            }
            cards.add(new SuitCard(suit, number));
        }
    }
    cards.add(new JokerCard());
}
// Result: 17 cards (base) or 33 cards (expansion)
```

### Integration in Game
```java
private void initializeDeck() {
    // Uses config setting
    deck = new Deck(gameConfig.isExpansionEnabled());
    deck.shuffle();
}
```

---

## 3. Game Variant System (NEW)

### GameVariant Enum
```java
public enum GameVariant {
    NORMAL("Normal Mode", "..."),
    NO_MERCY("No Mercy", "..."),
    GO_ALL_OUT("Go All Out", "...")
}
```

### GameConfig Manages Variant
```java
GameConfig config = new GameConfig(false, GameVariant.GO_ALL_OUT);

// Later: access variant settings
if (config.getVariant() == GameVariant.NO_MERCY) {
    int threshold = config.getNoMercyThreshold();  // 7-10
}
```

---

## 4. Scoring System (UPDATED)

### Before
```java
// Game.java - always used FinalScoreVisitor
for (Player player : players) {
    FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
    int score = player.calculateFinalScore(visitor);
}
```

### After
```java
// Game.java - uses factory to select visitor
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

// In endGame()
for (Player player : players) {
    ScoreVisitor visitor = createScoreVisitor(player.getJest());
    int score = player.calculateFinalScore(visitor);
}
```

### Why Factory Pattern?
- ✓ Single responsibility
- ✓ Easy to add new variants
- ✓ Visitor pattern properly encapsulated
- ✓ No if-else chains in scoring logic

---

## 5. Trophy System (CONDITIONAL)

### Before
```java
public void startGame() {
    // ...
    setupTrophies();  // Always awarded
    // ...
}
```

### After
```java
public void startGame() {
    // ...
    // Skip trophies in GO_ALL_OUT variant
    if (gameConfig.getVariant() != GameVariant.GO_ALL_OUT) {
        setupTrophies();
    }
    // ...
}

public void endGame() {
    // Award trophies only in NORMAL and NO_MERCY variants
    if (gameConfig.getVariant() != GameVariant.GO_ALL_OUT) {
        awardTrophies();
    } else {
        System.out.println("(No trophies awarded in GO ALL OUT variant)");
    }
    // ...
}
```

---

## 6. Main Class Flow (UPDATED)

### Before
```java
public static void main(String[] args) {
    // Input: player count → human count → AI difficulty
    int totalPlayers = 4;
    int humanPlayers = 2;
    int aiDifficulty = 1;
    
    // Directly create game
    Game game = new Game(totalPlayers, humanPlayers, aiDifficulty);
    game.startGame();
}
```

### After
```java
public static void main(String[] args) {
    // Input 1-3: Same as before
    int totalPlayers = 4;
    int humanPlayers = 2;
    int aiDifficulty = 1;
    
    // Input 4: EXPANSION CHOICE (NEW)
    System.out.println("Use expansion cards? (1 = No, 2 = Yes): ");
    boolean useExpansion = (InputHandler.getInt() == 2);
    
    // Input 5: VARIANT SELECTION (NEW)
    System.out.println("1 = NORMAL MODE");
    System.out.println("2 = NO MERCY");
    System.out.println("3 = GO ALL OUT");
    int variantChoice = InputHandler.getInt();
    GameVariant variant = mapChoiceToVariant(variantChoice);
    
    // Create config with new settings
    GameConfig gameConfig = new GameConfig(useExpansion, variant);
    
    // Create and start game
    Game game = new Game(totalPlayers, humanPlayers, aiDifficulty, gameConfig);
    game.startGame();
}
```

---

## 7. Save/Load System (FOUNDATION READY)

### Available Now
```java
// Save a game state
Map<String, Object> gameState = new HashMap<>();
gameState.put("round", currentRound);
gameState.put("players", players);
GameSaveManager.saveGame(gameState, GameVariant.NO_MERCY);
// File: saves/JEST_NO_MERCY_2024-12-18_14-30-45.jest

// List saved games
List<String> saves = GameSaveManager.listSavedGames();

// Load a game
Map<String, Object> loaded = GameSaveManager.loadGame(filename);

// Delete a save
GameSaveManager.deleteSave(filename);
```

### To Integrate into Game
1. Add menu option in `Main.java`:
   ```java
   System.out.println("1. New Game");
   System.out.println("2. Load Game");
   int choice = InputHandler.getInt();
   ```

2. Check for saved games:
   ```java
   if (choice == 2) {
       List<String> saves = GameSaveManager.listSavedGames();
       // Display and let user select
   }
   ```

3. Load game state:
   ```java
   Map<String, Object> state = GameSaveManager.loadGame(selected);
   // Restore game from state
   ```

---

## 8. Numbers Enum (EXTENDED)

### Before
```java
public enum Numbers {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4);
}
```

### After
```java
public enum Numbers {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    SIX(6),      // NEW - expansion
    SEVEN(7),    // NEW - expansion
    EIGHT(8),    // NEW - expansion
    NINE(9);     // NEW - expansion
}
```

### Impact
- Deck initialization now skips 6,7,8,9 if expansion disabled
- SuitCard works with all values automatically
- Scoring visitors handle expansion cards (same logic as base cards)

---

## 9. Key Class Changes Summary

| Class | Changes | Reason |
|-------|---------|--------|
| `Main.java` | Added variant & expansion selection | User choice for gameplay |
| `GameConfig` | NEW CLASS | Encapsulate settings |
| `GameVariant` | NEW ENUM | Type-safe variant selection |
| `Game.java` | Added gameConfig field, createScoreVisitor() | Variant support |
| `Deck.java` | Added hasExpansion parameter | Optional expansion cards |
| `Numbers.java` | Added SIX, SEVEN, EIGHT, NINE | Expansion cards |
| `FinalScoreVisitor` | No change (NORMAL mode) | Unchanged behavior |
| `NoMercyScoreVisitor` | NEW CLASS | NO MERCY variant |
| `GoAllOutScoreVisitor` | NEW CLASS | GO ALL OUT variant |
| `GameSaveManager` | NEW CLASS | Save/load foundation |

---

## 10. Backward Compatibility

### Old Code Still Works
```java
// Old way - still valid, uses default config
Game game = new Game(3, 2, 1);
game.startGame();
// Equivalent to:
GameConfig default = new GameConfig();  // NORMAL, no expansion
Game game = new Game(3, 2, 1, default);
```

### Upgrade Path
1. Existing code: No changes required
2. To use variants: Create `GameConfig` with desired variant
3. To use expansion: Set `useExpansion = true` in `GameConfig`
4. To save games: Call `GameSaveManager` methods

---

## 11. Testing the Implementation

### Test Variant Functionality
```bash
# NORMAL mode
Run: Main.java → Select 3 or 4 players → 
     No expansion → NORMAL variant → Play game

# NO MERCY mode  
Run: Main.java → Select 3 or 4 players → 
     No expansion → NO MERCY variant → Play game
     (Note: Random threshold shown, check if it applies)

# GO ALL OUT mode
Run: Main.java → Select 3 or 4 players → 
     No expansion → GO ALL OUT variant → Play game
     (Note: No trophies should appear)
```

### Test Expansion
```bash
# With expansion cards
Run: Main.java → Select players → Use expansion → 
     (Any variant) → Play game
     (Note: Cards 6, 7, 8, 9 should appear)
```

### Verify Scoring
```bash
# NORMAL mode scoring
Player Jest: 3♠ 2♠ 4♦ 3♣ Joker, 0 Hearts
Expected: 3 + 2 + (-4) + 3 + 4 = 8

# NO MERCY with threshold 8
Player Jest total: 12 points
Exceeds 8 → Final score: 0

# GO ALL OUT scoring
Player Jest: 3♠ 2♠ 4♦ 3♣ Joker
Expected: (3 + 2 + 4 + 3) × 1.5 = 12 × 1.5 = 18
```

---

## 12. Future Integration Points

### Immediately Available
- [ ] Save game during gameplay
- [ ] Load game from main menu
- [ ] Display variant statistics
- [ ] Difficulty settings per AI player

### Near-term
- [ ] Custom variant builder
- [ ] Expansion card special effects
- [ ] Achievement system
- [ ] Elo rating system

### Long-term
- [ ] GUI/Swing implementation
- [ ] Network multiplayer
- [ ] Mobile app version
- [ ] Tournament mode

---

## 13. Common Integration Tasks

### Add Save Button to Game
```java
// In Game.playRound() or after each round
if (userPressedSaveButton()) {
    Map<String, Object> state = serializeGameState();
    GameSaveManager.saveGame(state, gameConfig.getVariant());
    System.out.println("Game saved!");
}
```

### Add Load Game Menu
```java
// In Main.main()
List<String> saves = GameSaveManager.listSavedGames();
if (!saves.isEmpty()) {
    System.out.println("Available saves:");
    for (int i = 0; i < saves.size(); i++) {
        System.out.println(i + ": " + GameSaveManager.formatSaveInfo(saves.get(i)));
    }
}
```

### Add Statistics Tracking
```java
// Create new VariantStats class
public class VariantStats {
    private GameVariant variant;
    private int gamesPlayed;
    private int gamesWon;
    private double avgScore;
}
```

---

## Summary

**Key Points:**
1. ✅ All existing code remains compatible
2. ✅ New features are opt-in via `GameConfig`
3. ✅ Variants are type-safe (`GameVariant` enum)
4. ✅ Scoring logic properly separated (Visitor pattern)
5. ✅ Save system ready for integration
6. ✅ Expansion cards fully functional
7. ✅ Architecture is modular and extensible

**Next Steps:**
1. Test all three variants
2. Verify expansion cards appear in deck
3. Check scoring calculations per variant
4. Integrate save/load into main menu
5. Add variant statistics tracking
