# Chess Game

- Game contains Players, and Board, and Logic. 
- Flexible enough to add diffrent types of:
    - Players [like Human, AI]
    - Board [8X8, or something else]
    - Logic/Rule in a way, so that is is easy to add/remove logic/rule

------------------------------------------------------------------------

# Won't able to do by myself, having confustion of where to put what logic
# Hence, Understand from Grok
# This version is a Copy/ Paste version from Grok

# Chess Game Requirements

## Board
- 8x8 grid with pieces (pawn, rook, knight, bishop, queen, king).

## Players
- Two players (White, Black) taking turns.

## Pieces
- Each type has specific move rules (e.g., pawn moves forward, bishop diagonally).

## Moves
- Players select a piece and destination; moves must be validated (e.g., legal moves, no self-check).

## Game State
- Track turns, check, checkmate, stalemate, or draw.

## Rules
- Validate moves based on piece rules and board state.
- Handle special moves (e.g., castling, en passant, pawn promotion—simplified for LLD).
- Detect check, checkmate, or stalemate.

---

## Interview Context
- Explainable in 45 minutes, with clear logic placement (e.g., “move validation goes here because of SRP”).

---

## Logic Placement Framework Application

### Move Validation (e.g., is a rook’s move to a square legal?)
- **Purpose**: Check if a move follows piece rules and board state (e.g., no obstacles, no self-check).
- **Candidates**: Piece class, Board, Game, or a new Validator.
- **Framework**:
    - **SRP**: Validation is a distinct responsibility, not tied to piece movement or board state management.
    - **Cohesion**: A `MoveValidator` centralizes all validation logic (piece rules, check, special moves).
    - **Coupling**: Low, needs only board state and piece rules.
    - **Extensibility**: Interface allows new rules (e.g., variant chess).
- **Decision**: Place in a `MoveValidator` interface and implementation.
    - **Why Not Piece?**: Mixing movement rules with validation bloats `Piece` (SRP violation).
    - **Why Not Board?**: Board manages state (pieces, positions), not rules.
    - **Why Not Game?**: Game coordinates turns and state, not specific move logic.
- **Confusion Resolved**: `MoveValidator` is the clear choice, avoiding overlap with other classes.

### Game State Management (e.g., track turns, check, checkmate)
- **Purpose**: Manage game flow, turns, and outcomes.
- **Candidates**: Game, Board, or a new `StateManager`.
- **Framework**:
    - **SRP**: Game state (turns, outcomes) is separate from board state (piece positions).
    - **Cohesion**: A `Game` class centralizes turn logic, check detection, and game status.
    - **Coupling**: Needs board state and player info, but manageable.
    - **Extensibility**: Can extend for new win conditions (e.g., timed games).
- **Decision**: Place in `Game` class.
    - **Why Not Board?**: Board handles piece positions, not game flow.
    - **Why Not Player?**: Players make moves, not manage game state.
- **Confusion Resolved**: `Game` is the coordinator, clearly responsible for state.

### Move Execution (e.g., update board after a valid move)
- **Purpose**: Move a piece from source to destination, update board.
- **Candidates**: Piece, Board, Game.
- **Framework**:
    - **SRP**: Board manages piece positions, so it should handle updates.
    - **Cohesion**: Board operations (move, capture) are cohesive.
    - **Coupling**: Low, uses internal state.
    - **Extensibility**: Board can support special moves (e.g., castling).
- **Decision**: Place in `Board` class.
    - **Why Not Piece?**: Pieces define rules, not board state.
    - **Why Not Game?**: Game coordinates, doesn’t manage board internals.
- **Confusion Resolved**: `Board` is the obvious place for state changes.

---

## Takeaway
The framework ensures each logic piece has a single, justified home, reducing your “multiple places” confusion. You’ll see this in the design below, with clear explanations for interviews.

---

## Design Overview
Here’s the class structure, keeping it minimal (7 classes/interfaces) and SOLID-compliant:

### Piece (Abstract Class)
- Represents a chess piece (e.g., pawn, king).
- Defines color (White/Black) and position.
- Abstract method `getValidMoves` for piece-specific move rules.
- **Why Here?**: SRP (piece defines its own moves), extensible for new pieces.

### Concrete Pieces (e.g., Pawn, Rook, Knight, Bishop, Queen, King)
- Extend `Piece`, implement `getValidMoves` for specific rules.
- Example: Pawn handles forward moves, captures, en passant (simplified).
- **Why Here?**: Encapsulation, each piece owns its logic.

### Board
- Manages 8x8 grid and piece positions.
- Executes moves (update piece positions, handle captures).
- Provides state queries (e.g., piece at position, king’s position).
- **Why Here?**: SRP (board state management), cohesive (all position logic).

### Player
- Represents a player (White/Black).
- Tracks color and makes moves via `Game`.
- **Why Here?**: SRP (player actions), low coupling.

### MoveValidator (Interface + Implementation)
- Validates moves based on piece rules, board state, and game rules (e.g., no self-check).
- Implementation checks piece moves, obstacles, check, and special cases.
- **Why Here?**: SRP (validation), extensible (new rules), decoupled from `Board` and `Piece`.

### Game
- Manages game state (turns, check, checkmate, stalemate).
- Coordinates moves: validates via `MoveValidator`, executes via `Board`.
- Tracks players and game status.
- **Why Here?**: SRP (game flow), central coordinator.

### Position
- Represents a board position (row, col).
- **Why Here?**: Encapsulates coordinates, improves readability.

---

## Simplifications for LLD
- **Simplified Rules**:
    - Basic piece moves (e.g., pawn moves forward one square, no double move).
    - No castling, en passant, or pawn promotion to keep code concise.
    - Basic check/checkmate detection (no stalemate or draw for simplicity).
- **No Persistence**: In-memory game state.
- **No AI**: Human vs. human, but extensible for AI.