package com.boomerang.proto.types;

import org.jetbrains.annotations.Range;

public enum Direction {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    EAST(-1, 0, 0),
    WEST(1, 0, 0);

    private final int x, y, z;
    private final Pointer point;

    Direction(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.point = new Pointer(x, y, z);
    }

    public boolean isPositive() {
        return x > 0 || y > 0 || z > 0;
    }

    public boolean isNegative() {
        return x < 0 || y < 0 || z < 0;
    }

    public boolean isVertical() {
        return this == UP || this == DOWN;
    }

    public boolean isHorizontal() {
        return this == NORTH || this == SOUTH || this == EAST || this == WEST;
    }

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    public Direction direction(final double yaw, final double pitch) {
        if (pitch < -45L) return UP;
        if (pitch > 45L) return DOWN;

        final int quadrant = (int) ((yaw + 45) / 90) & 3;

        return switch (quadrant) {
            case 0 -> NORTH;
            case 1 -> EAST;
            case 2 -> SOUTH;
            case 3 -> WEST;
            default -> throw new IllegalStateException("Unexpected value: " + quadrant);
        };
    }

    @Range(from= 1, to= 6)
    public int getOrdinal() {
        return this.ordinal() + 1;
    }

    @Range(from= 1, to= 6)
    public static Direction fromOrdinal(final int ordinal) {
        return values()[ordinal];
    }
}
