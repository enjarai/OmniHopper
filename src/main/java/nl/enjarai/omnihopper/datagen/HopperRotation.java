package nl.enjarai.omnihopper.datagen;

import static net.minecraft.data.client.VariantSettings.Rotation.*;

import java.util.Map;

import net.minecraft.data.client.VariantSettings;
import net.minecraft.util.math.Direction;

public record HopperRotation(Direction modelDirection, VariantSettings.Rotation rotX, VariantSettings.Rotation rotY) {
    public static final Map<Direction, Map<Direction, HopperRotation>> ALL = Map.of(
            Direction.UP, Map.of(
                    Direction.UP, new HopperRotation(Direction.UP),
                    Direction.DOWN, new HopperRotation(Direction.DOWN),
                    Direction.NORTH, new HopperRotation(Direction.NORTH),
                    Direction.EAST, new HopperRotation(Direction.EAST),
                    Direction.SOUTH, new HopperRotation(Direction.SOUTH),
                    Direction.WEST, new HopperRotation(Direction.WEST)
            ),
            Direction.DOWN, Map.of(
                    Direction.UP, new HopperRotation(Direction.DOWN, R180),
                    Direction.DOWN, new HopperRotation(Direction.UP, R180),
                    Direction.NORTH, new HopperRotation(Direction.SOUTH, R180),
                    Direction.EAST, new HopperRotation(Direction.EAST, R180),
                    Direction.SOUTH, new HopperRotation(Direction.NORTH, R180),
                    Direction.WEST, new HopperRotation(Direction.WEST, R180)
            ),
            Direction.NORTH, Map.of(
                    Direction.UP, new HopperRotation(Direction.SOUTH, R90),
                    Direction.DOWN, new HopperRotation(Direction.NORTH, R90),
                    Direction.NORTH, new HopperRotation(Direction.UP, R90),
                    Direction.EAST, new HopperRotation(Direction.EAST, R90),
                    Direction.SOUTH, new HopperRotation(Direction.DOWN, R90),
                    Direction.WEST, new HopperRotation(Direction.WEST, R90)
            ),
            Direction.EAST, Map.of(
                    Direction.UP, new HopperRotation(Direction.SOUTH, R90, R90),
                    Direction.DOWN, new HopperRotation(Direction.NORTH, R90, R90),
                    Direction.NORTH, new HopperRotation(Direction.WEST, R90, R90),
                    Direction.EAST, new HopperRotation(Direction.UP, R90, R90),
                    Direction.SOUTH, new HopperRotation(Direction.EAST, R90, R90),
                    Direction.WEST, new HopperRotation(Direction.DOWN, R90, R90)
            ),
            Direction.SOUTH, Map.of(
                    Direction.UP, new HopperRotation(Direction.NORTH, R270),
                    Direction.DOWN, new HopperRotation(Direction.SOUTH, R270),
                    Direction.NORTH, new HopperRotation(Direction.DOWN, R270),
                    Direction.EAST, new HopperRotation(Direction.EAST, R270),
                    Direction.SOUTH, new HopperRotation(Direction.UP, R270),
                    Direction.WEST, new HopperRotation(Direction.WEST, R270)
            ),
            Direction.WEST, Map.of(
                    Direction.UP, new HopperRotation(Direction.SOUTH, R90, R270),
                    Direction.DOWN, new HopperRotation(Direction.NORTH, R90, R270),
                    Direction.NORTH, new HopperRotation(Direction.EAST, R90, R270),
                    Direction.EAST, new HopperRotation(Direction.DOWN, R90, R270),
                    Direction.SOUTH, new HopperRotation(Direction.WEST, R90, R270),
                    Direction.WEST, new HopperRotation(Direction.UP, R90, R270)
            )
    );

    public static HopperRotation getFor(Direction suckyDirection, Direction pointyDirection) {
        return ALL.get(suckyDirection).get(pointyDirection);
    }

    public HopperRotation(Direction modelDirection) {
        this(modelDirection, R0, R0);
    }

    public HopperRotation(Direction modelDirection, VariantSettings.Rotation rotX) {
        this(modelDirection, rotX, R0);
    }
}
