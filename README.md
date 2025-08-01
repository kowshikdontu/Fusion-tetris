
---

### Preview

# Fusion-Tetris

Fusion-Tetris is a twist on the classic Tetris formula—here, **four elemental power blocks** spawn and fall into your desired column, each unlocking a unique effect when you match three in a row.

---

## Table of Contents

1. [Game Overview](#game-overview)  
2. [Block Types & Powers](#block-types--powers)  
3. [Gameplay Mechanics](#gameplay-mechanics)  
4. [Prerequisites](#prerequisites)  
5. [Installation & Running](#installation--running)  
6. [Column Selection](#column-selection)  
8. [Contributing](#contributing)  

---

## Game Overview

In Fusion-Tetris, you still clear lines and score points—but with an elemental twist!  
Arrange **three identical power blocks** (Earth, Water, Fire, or Wind) in a row, column, or L-shape to trigger spectacular effects that reshape the board.

## Block Types & Powers

| Block Type         | Power Effect                                                                          |
| ------------------ | ------------------------------------------------------------------------------------- |
| **Earth Block**    | Triggers an **Earthquake**: bottom-layer “bedrock” blocks shuffle randomly.           |
| **Water Block**    | Summons a **Tsunami**: top-layer blocks shuffle like raging waves.                    |
| **Fire Block**     | Unleashes **Flames**: destroys itself and all adjacent blocks within a 1-unit radius. |
| **Wind Block**     | Conjures a **Storm**: clears its entire row and column in a gust of wind.             |
| **No Power Block** | Occupies space but has no special effect.                                             |
| **Empty Block**    | Unfillable slot—blocks cannot occupy this space.                                      |

## Gameplay Mechanics

- The board is represented as a 2D grid.  
- Blocks spawn at the top and fall into a chosen column.  
- Matching **three** of the same **power block** in a straight line or L-shape triggers that block’s special effect.  
- Standard line clears still apply for horizontal full-row fills.  
- Use Earthquakes, Tsunamis, Flames, and Storms strategically to maximize combos and score multipliers.

## Design & Architecture

Fusion-Tetris is built with maintainability and extensibility in mind, following SOLID principles and the Factory Pattern.

### SOLID Principles

- Single Responsibility Principle

Each class has one responsibility: e.g., Board manages grid state, Renderer handles drawing, individual Block subclasses encapsulate behavior.

- Open/Closed Principle

The code is open for extension (adding new block types) but closed for modification. New power blocks can be introduced by extending the Block base class without altering existing code.

- Liskov Substitution Principle

Subclasses of Block (e.g., EarthBlock, FireBlock) can replace the base type without breaking game logic, ensuring consistent behavior.

- Interface Segregation Principle

Fine-grained interfaces separate concerns: e.g., Droppable for block drop behavior, Effect for power activation, so classes only implement what they need.

- Dependency Inversion Principle

High-level modules (GameController, Board) depend on abstractions (Block, Effect) rather than concrete implementations, enabling easy swapping and testing.

### Factory Pattern

A BlockFactory class encapsulates the creation of block instances. Given a block type identifier or enum, the factory returns the appropriate Block subclass.

This centralizes instantiation logic and decouples client code from concrete classes, making it easy to add or modify block types.

## Prerequisites

- **Java JDK 11+**  
- (Optional) **IntelliJ IDEA**, **Eclipse**, or another Java IDE

## Installation & Running

```bash
# 1. Clone this repository
git clone https://github.com/kowshikdontu/Fusion-tetris.git
cd Fusion-tetris

# 2. Compile (CLI)
javac -d out $(find src -name "*.java")

# 3. Run
touch out && java -cp out com.fusiontetris.Main
```
## Column Selection
- Before each block spawn, choose the column number (leftmost column is 1) where you want the block to fall. Once selected, the block drops straight down into that column—no manual movement, rotation, or drop controls.

            
## Contributing
- Fork the repository.

- Create a feature branch: git checkout -b feature/MyFeature

- Commit your changes: git commit -m "Add MyFeature"

- Push to your branch: git push origin feature/MyFeature

- Open a Pull Request and describe your changes.


