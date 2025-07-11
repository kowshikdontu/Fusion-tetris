public class BlockFactory {
    public static Block createBlock(String type, Position position) {
        switch (type.toLowerCase()) {
            case "fire": return new FireBlock(position);
            case "water": return new WaterBlock(position);
            case "earth": return new EarthBlock(position);
            case "air": return new AirBlock(position);
            case "nopower": return new NoPowerBlock(position);
            default: return new EmptyBlock(position);
        }
    }
}