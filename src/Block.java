public abstract class Block {
    protected Position position;

    public Block(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean interact(GameBoard board);

    public boolean isEmpty() {
        return this instanceof EmptyBlock;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}