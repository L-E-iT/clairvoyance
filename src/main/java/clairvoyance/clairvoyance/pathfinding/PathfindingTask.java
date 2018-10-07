package clairvoyance.clairvoyance.pathfinding;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.entity.ai.task.AITask;
import org.spongepowered.api.entity.ai.task.AITaskType;
import org.spongepowered.api.entity.ai.task.AbstractAITask;
import org.spongepowered.api.entity.living.Agent;


public class PathfindingTask extends AbstractAITask<Agent> {

    private static final double MOVEMENT_SPEED = 1;
    private static final double APPROACH_DISTANCE_SQUARED = 2 * 2;
    private static final double MAX_DISTANCE_SQUARED = 20 * 20;
    private static final float EXECUTION_CHANCE = 0.2F;
    private static final int MUTEX_FLAG_MOVE = 1;

    private static Vector3d locationTo;
    private static Vector3d locationOf;
    private static AITaskType TYPE;
    private static boolean atLocation;

    public PathfindingTask(Vector3d locationTo, Vector3d locationOf) {
        super(TYPE);
        ((EntityAIBase) (Object) this).setMutexBits(MUTEX_FLAG_MOVE);
        PathfindingTask.locationTo = locationTo;
        PathfindingTask.locationOf = locationOf;
    }

    public static void register(final Object plugin, final GameRegistry gameRegistry){
        TYPE = gameRegistry.registerAITaskType(plugin, "pathfinder_entity", "PathfinderEntity", PathfindingTask.class);
    }

    @Override
    public void start() {
        getNavigator().tryMoveToXYZ(locationTo.getX(), locationTo.getY(), locationTo.getZ(), MOVEMENT_SPEED);
    }

    @Override
    public boolean shouldUpdate() {
        final Agent owner = getOwner().get();
        if (owner.getRandom().nextFloat() > EXECUTION_CHANCE) {
            return false;
        }

        atLocation = locationOf.distanceSquared(locationTo) < MAX_DISTANCE_SQUARED;
        return atLocation;
    }

    private PathNavigate getNavigator() {
        return ((EntityLiving) (getOwner().get())).getNavigator();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean continueUpdating() {
        if (getNavigator().noPath()) {
            return false;
        }
        if (atLocation) {
            return false;
        }
        return locationOf.distanceSquared(locationTo) > APPROACH_DISTANCE_SQUARED;
    }

    @Override
    public void reset() {
        getNavigator().clearPath();
    }

    @Override
    public boolean canRunConcurrentWith(AITask<Agent> other) {
        return (((EntityAIBase) (Object) this).getMutexBits() & ((EntityAIBase) other).getMutexBits()) == 0;
    }

    @Override
    public boolean canBeInterrupted() {
        return true;
    }
}
