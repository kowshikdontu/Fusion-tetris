import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameUIFX extends Application {
    private static final int CELL_SIZE = 50;
    private static final int BOARD_SIZE = 10;
    private static final int SIDEBAR_WIDTH = 200;

    private Rectangle[][] cells = new Rectangle[BOARD_SIZE][BOARD_SIZE];
    private GameController controller = new GameController();

    // UI Elements
    private Label scoreLabel = new Label("0");
    private Label levelLabel = new Label("1");
    private Label timerLabel = new Label("180");
    private Label nextBlockLabel = new Label();
    private Rectangle nextBlockPreview = new Rectangle(CELL_SIZE, CELL_SIZE);

    // Game state
    private int currentCol = 0;
    private String currentBlockType;
    private int timeLeft = 180;
    private Timeline timer;
    private Timeline dropPreviewAnimation;

    @Override
    public void start(Stage primaryStage) {
        // Main layout
        BorderPane root = new BorderPane();
        setupBackground(root);

        // Game board
        GridPane boardGrid = createBoardGrid();
        StackPane boardContainer = new StackPane(boardGrid);
        boardContainer.setPadding(new javafx.geometry.Insets(20));

        // Sidebar with game info
        VBox sidebar = createSidebar();

        root.setCenter(boardContainer);
        root.setRight(sidebar);

        // Scene setup
        Scene scene = new Scene(root, 800, 600);
        setupKeyControls(scene);

        // Initialize game
        setupGame();
        setupTimer();
        setupDropPreviewAnimation();

        primaryStage.setTitle("Fusion Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupBackground(BorderPane root) {
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.DARKBLUE),
                new Stop(1, Color.BLACK)
        );
        root.setBackground(new Background(new BackgroundFill(gradient, null, null)));
    }

    private GridPane createBoardGrid() {
        GridPane boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.rgb(255, 255, 255, 0.2));
                rect.setStrokeWidth(1);
                rect.setArcWidth(10);
                rect.setArcHeight(10);

                boardGrid.add(rect, j, i);
                cells[i][j] = rect;
            }
        }
        return boardGrid;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new javafx.geometry.Insets(20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: rgba(0, 0, 50, 0.7);");

        // Game title
        Label title = new Label("FUSION TETRIS");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.GOLD);

        // Score display
        VBox scoreBox = createInfoBox("SCORE", scoreLabel);
        VBox levelBox = createInfoBox("LEVEL", levelLabel);
        VBox timerBox = createInfoBox("TIME", timerLabel);

        // Next block preview
        VBox nextBlockBox = new VBox(10);
        Label nextTitle = new Label("NEXT BLOCK");
        nextTitle.setFont(Font.font(14));
        nextTitle.setTextFill(Color.WHITE);
        nextBlockPreview.setFill(Color.TRANSPARENT);
        nextBlockPreview.setStroke(Color.WHITE);
        nextBlockPreview.setStrokeWidth(2);
        nextBlockPreview.setArcWidth(15);
        nextBlockPreview.setArcHeight(15);
        nextBlockLabel.setFont(Font.font(16));
        nextBlockLabel.setTextFill(Color.WHITE);
        nextBlockBox.getChildren().addAll(nextTitle, nextBlockPreview, nextBlockLabel);

        // Instructions
        Label instructions = new Label("CONTROLS:\n← → or A D: Move\nSPACE: Drop\nESC: Pause");
        instructions.setFont(Font.font(12));
        instructions.setTextFill(Color.LIGHTGRAY);

        sidebar.getChildren().addAll(title, scoreBox, levelBox, timerBox, nextBlockBox, instructions);
        return sidebar;
    }

    private VBox createInfoBox(String titleText, Label valueLabel) {
        VBox box = new VBox(5);
        Label title = new Label(titleText);
        title.setFont(Font.font(14));
        title.setTextFill(Color.WHITE);
        valueLabel.setFont(Font.font("Arial", 30));
        valueLabel.setTextFill(Color.WHITE);
        box.getChildren().addAll(title, valueLabel);
        return box;
    }

    private void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                moveLeft();
            } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                moveRight();
            } else if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
                dropBlock();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                pauseGame();
            }
        });
    }

    private void setupGame() {
        currentBlockType = randomBlockType();
        updateNextBlockPreview();
        updateDropPreview();
    }

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText(String.valueOf(timeLeft));
            timerLabel.setTextFill(timeLeft > 30 ? Color.WHITE : Color.RED);

            if (timeLeft <= 0) {
                timer.stop();
                gameOver();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void setupDropPreviewAnimation() {
        dropPreviewAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, e -> updateDropPreview()),
                new KeyFrame(Duration.millis(100)),
                new KeyFrame(Duration.millis(200), e -> updateDropPreview())
        );
        dropPreviewAnimation.setCycleCount(Animation.INDEFINITE);
        dropPreviewAnimation.play();
    }

    private void moveLeft() {
        if (currentCol > 0) {
            currentCol--;
            updateDropPreview();
        }
    }

    private void moveRight() {
        if (currentCol < BOARD_SIZE - 1) {
            currentCol++;
            updateDropPreview();
        }
    }

    private void dropBlock() {
        Block block = BlockFactory.createBlock(currentBlockType, new Position(0, currentCol));
        if (controller.getBoard().dropBlock(block, currentCol)) {
            // Process game logic
            controller.updateBoard();

            // Update UI
            updateBoardUI();

            // Update score from game board
            scoreLabel.setText(String.valueOf(controller.getBoard().getScore()));

            // Get next block
            currentBlockType = randomBlockType();
            updateNextBlockPreview();

            // Check for game over
            if (controller.getBoard().isBoardFull()) {
                gameOver();
            }
        }
    }

    private void pauseGame() {
        if (timer.getStatus() == Animation.Status.RUNNING) {
            timer.pause();
            dropPreviewAnimation.pause();
        } else {
            timer.play();
            dropPreviewAnimation.play();
        }
    }

    private void gameOver() {
        timer.stop();
        dropPreviewAnimation.stop();

        Label gameOverLabel = new Label("GAME OVER\nFINAL SCORE: " + controller.getBoard().getScore());
        gameOverLabel.setFont(Font.font(30));
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setAlignment(Pos.CENTER);

        StackPane overlay = new StackPane(gameOverLabel);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        ((BorderPane) timerLabel.getParent().getParent().getParent()).setCenter(overlay);
    }

    private void updateBoardUI() {
        GameBoard board = controller.getBoard();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Block b = board.getBlockAt(i, j);
                if (b != null && !b.isEmpty()) {
                    cells[i][j].setFill(getColorForBlock(b));
                    animateBlockAppearance(cells[i][j]);
                } else {
                    cells[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
    }

    private void animateBlockAppearance(Rectangle rect) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), rect);
        st.setFromX(0.5);
        st.setFromY(0.5);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    private void updateNextBlockPreview() {
        nextBlockLabel.setText(currentBlockType.toUpperCase());
        nextBlockPreview.setFill(getColorForBlock(
                BlockFactory.createBlock(currentBlockType, new Position(0, 0))));

        // Add animation to preview
        RotateTransition rt = new RotateTransition(Duration.millis(1000), nextBlockPreview);
        rt.setByAngle(360);
        rt.setCycleCount(1);
        rt.play();
    }

    private void updateDropPreview() {
        // Clear previous preview
        for (int j = 0; j < BOARD_SIZE; j++) {
            cells[0][j].setStroke(Color.rgb(255, 255, 255, 0.2));
        }

        // Show current preview
        cells[0][currentCol].setStroke(Color.GOLD);
        cells[0][currentCol].setStrokeWidth(3);

        // Add animation
        FadeTransition ft = new FadeTransition(Duration.millis(500), cells[0][currentCol]);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
    }

    private Paint getColorForBlock(Block b) {
        if (b instanceof FireBlock) return new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.ORANGE), new Stop(1, Color.RED));
        if (b instanceof WaterBlock) return new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.BLUE), new Stop(1, Color.LIGHTBLUE));
        if (b instanceof EarthBlock) return new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.SANDYBROWN), new Stop(1, Color.BROWN));
        if (b instanceof AirBlock) return new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE), new Stop(1, Color.LIGHTBLUE));
        if (b instanceof NoPowerBlock) return Color.DARKGRAY;
        return Color.TRANSPARENT;
    }

    private String randomBlockType() {
        String[] types = {"fire", "water", "earth", "air", "nopower"};
        return types[(int) (Math.random() * types.length)];
    }

    public static void main(String[] args) {
        launch(args);
    }
}