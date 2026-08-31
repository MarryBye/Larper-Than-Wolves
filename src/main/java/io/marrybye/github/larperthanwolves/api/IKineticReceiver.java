package io.marrybye.github.larperthanwolves.api;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

/**
 * Common unified interface for all machines and workstations that can receive
 * mechanical / kinetic rotational force from manual Mill Cranks or Create kinetic networks.
 */
public interface IKineticReceiver {
    /**
     * Whether this block entity accepts kinetic rotation from the specified face/direction.
     *
     * @param face The face of THIS block where the rotational force is being applied from.
     * @return true if rotation from this face is accepted.
     */
    boolean acceptsKineticRotationFrom(Direction face);

    /**
     * Called when a player right-clicks a Mill Crank attached to this block entity.
     *
     * @param fromFace The face where the crank is mounted.
     * @param player   The player performing the crank rotation.
     * @return true if the manual crank interaction succeeded (advanced work/progress).
     */
    boolean onManualCrank(Direction fromFace, Player player);

    /**
     * Called on server tick when an active Create kinetic network is supplying rotational RPM.
     *
     * @param speed    The absolute RPM speed (e.g. 16.0, 64.0, 256.0).
     * @param fromFace The face supplying the rotation.
     */
    void tickKineticRotation(float speed, Direction fromFace);

    /**
     * Whether this block entity currently has valid ingredients / work to process.
     *
     * @return true if work is available.
     */
    boolean hasWorkAvailable();
}
