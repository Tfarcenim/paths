package com.tfar.paths.config;

public class PathValue {

    private final double speedMultiplier;
    private final int maxDistance;
    private final boolean requiresLineOfSight;

    public PathValue(double speedMultiplier, int maxDistance, boolean requiresLineOfSight) {
        this.speedMultiplier = speedMultiplier;
        this.maxDistance = maxDistance;
        this.requiresLineOfSight = requiresLineOfSight;
    }

    public double speedMultiplier() {
        return speedMultiplier;
    }

    public int maxDistance() {
        return maxDistance;
    }

    public boolean requiresLineOfSight() {
        return requiresLineOfSight;
    }

}
