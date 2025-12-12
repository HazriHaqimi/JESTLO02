# JEST Card Game - Documentation

## Project Overview

JEST is a strategic card game for 3-4 players designed by Brett J. Gilbert. This Java implementation follows object-oriented design principles including Strategy and Visitor design patterns.

## Game Rules Summary

### Components
- **17 Cards**: 16 suit cards (4 suits × 4 values) + 1 Joker
- **Suits**: Spade ♠, Club ♣, Diamond ♦, Heart ♥
- **Values**: Ace (1), 2, 3, 4
- **Trophies**: 2 for 3 players, 1 for 4 players

### Gameplay
1. **Deal**: Each player receives 2 cards per round
2. **Make Offers**: Each player places 1 card face-up, 1 card face-down
3. **Take Cards**: Players take turns (highest face-up card goes first) taking 1 card from another player's offer
4. **Collect Leftovers**: Remaining cards go to offer owners' Jests
5. **Repeat**: Continue until deck is empty
6. **Score**: Calculate Jest scores and award trophies

### Scoring Rules
- **Spades ♠ & Clubs ♣**: Add face value to score
- **Diamonds ♦**: Subtract face value from score
- **Hearts ♥**: Worth nothing (unless Joker rules apply)
- **Aces**: Worth 5 if only card of their suit, otherwise 1
- **Black Pairs**: Spade + Club of same value = +2 bonus
- **Joker**: 
  - 0 Hearts → +4 points
  - 1-3 Hearts → Joker worth 0
  - 4 Hearts → Each Heart adds its value

### Trophy Conditions (from card orange bands)
- Highest/Lowest card of a specific suit
- Player with the Joker
- Majority (most cards of a face value)

---

## Class Diagram Changes

### Initial UML Diagram (from design)
The initial design included:
- `Game`, `Round`, `Player`, `Card`, `Deck`, `Jest`, `Offer` base classes
- `HumanPlayer`, `VirtualPlayer` player implementations
- `SuitCard`, `JokerCard` card types
- `PlayStrategy` interface with `HumanStrategy`, `AIStrategy`
- `DefensiveStrategy`, `OffensiveStrategy` concrete strategies
- `ScoreVisitor` interface with multiple visitor implementations
- `Trophy` for trophy management

### Final Implementation Changes

1. **TrophyType Enum**: Added to `Game.java` with 12 trophy types:
   - `HIGHEST_SPADE`, `LOWEST_SPADE`, `HIGHEST_CLUB`, `LOWEST_CLUB`
   - `HIGHEST_DIAMOND`, `LOWEST_DIAMOND`, `HIGHEST_HEART`, `LOWEST_HEART`
   - `JOKER`, `MAJORITY_2`, `MAJORITY_3`, `MAJORITY_4`

2. **Trophy Card Mapping**: Each card's orange band defines its trophy when drawn as a trophy card

3. **Numbers Enum**: Fixed values (ACE=1, TWO=2, THREE=3, FOUR=4)

4. **Card Abstract Class**: Made properly abstract with package declaration

5. **PlayStrategy Interface**: Added `chooseCard(Offer)` method

6. **Round Turn Order**: Implemented based on highest face-up card with suit tie-breaking

---

## Application Status

### ✅ Implemented Features

| Feature | Status | Notes |
|---------|--------|-------|
| Card types (Suit, Joker) | ✅ Complete | SuitCard, JokerCard classes |
| Deck management | ✅ Complete | 17 cards, shuffle, draw |
| Player types | ✅ Complete | Human (console) and Virtual (AI) |
| Strategy pattern | ✅ Complete | Offensive, Defensive, Human strategies |
| Offer system | ✅ Complete | Face-up/face-down card selection |
| Round management | ✅ Complete | Deal, make offers, take cards |
| Turn order | ✅ Complete | Highest face-up card goes first |
| Suit hierarchy | ✅ Complete | Spade > Club > Diamond > Heart |
| Trophy system | ✅ Complete | 1-2 trophies based on player count |
| Trophy conditions | ✅ Complete | 12 different trophy types |
| Scoring (Visitor pattern) | ✅ Complete | All scoring rules implemented |
| Ace rule | ✅ Complete | 5 if alone in suit, else 1 |
| Joker/Heart rules | ✅ Complete | Value based on Heart count |
| Black pairs bonus | ✅ Complete | +2 for same-value Spade+Club |
| Game flow | ✅ Complete | Multiple rounds until deck empty |
| Javadoc documentation | ✅ Complete | All classes documented |

### ⚠️ Known Limitations

| Issue | Description |
|-------|-------------|
| AI Strategy | AI players have simple strategies (always take face-up) |
| No GUI | Console-only interface |
| No save/load | Game state not persisted |
| Single human | Only supports 1 human player |

### 🐛 Fixed Bugs

1. **Trophy count**: Fixed to 2 for 3 players, 1 for 4 players (was showing all trophies)
2. **Game rounds**: Fixed to run multiple rounds until deck empty (was ending after 1 round)
3. **DefensiveStrategy**: Fixed to properly extend AIStrategy
4. **Numbers enum**: Fixed ACE value from 14 to 1

---

## Package Structure

```
src/
├── Main.java                 # Entry point
├── base/                     # Core game classes
│   ├── Card.java            # Abstract card
│   ├── Deck.java            # Card deck
│   ├── Game.java            # Game controller
│   ├── Jest.java            # Won cards collection
│   ├── Offer.java           # Two-card offer
│   ├── Player.java          # Abstract player
│   └── Round.java           # Round logic
├── card/                     # Card implementations
│   ├── JokerCard.java       # Joker card
│   └── SuitCard.java        # Suit card
├── player/                   # Player implementations
│   ├── HumanPlayer.java     # Human player
│   └── VirtualPlayer.java   # AI player
├── properties/               # Enumerations
│   ├── Numbers.java         # Card values
│   └── Suit.java            # Card suits
├── strategy/                 # Strategy pattern
│   ├── AIStrategy.java      # Abstract AI strategy
│   ├── HumanStrategy.java   # Human input strategy
│   ├── PlayStrategy.java    # Strategy interface
│   └── typestrategy/        # Concrete strategies
│       ├── DefensiveStrategy.java
│       └── OffensiveStrategy.java
├── trophy/                   # Trophy management
│   └── Trophy.java          # Trophy class
└── visitor/                  # Visitor pattern
    ├── AceRuleVisitor.java  # Ace scoring
    ├── FinalScoreVisitor.java # Complete scoring
    ├── JokerRuleVisitor.java # Joker scoring
    ├── ScoreVisitor.java    # Visitor interface
    └── TrophyScoreVisitor.java # Trophy tracking
```

---

## Design Patterns Used

### 1. Strategy Pattern
- **Purpose**: Encapsulate player decision-making behavior
- **Interface**: `PlayStrategy`
- **Implementations**: `HumanStrategy`, `OffensiveStrategy`, `DefensiveStrategy`

### 2. Visitor Pattern
- **Purpose**: Separate scoring algorithms from card structure
- **Interface**: `ScoreVisitor`
- **Implementations**: `FinalScoreVisitor`, `AceRuleVisitor`, `JokerRuleVisitor`, `TrophyScoreVisitor`

---

## How to Run

```bash
# From project root
cd src
javac -encoding UTF-8 Main.java base/*.java card/*.java player/*.java properties/*.java strategy/*.java strategy/typestrategy/*.java trophy/*.java visitor/*.java -d ../classes
cd ../classes
java Main
```

---

## Authors
JEST Team - LO02 Project

## Version
1.0
