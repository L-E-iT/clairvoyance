package clairvoyance.clairvoyance.particles;

import clairvoyance.clairvoyance.Clairvoyance;
import clairvoyance.clairvoyance.pathfinding.PathfindingTask;
import com.flowpowered.math.vector.Vector3d;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.ai.Goal;
import org.spongepowered.api.entity.ai.GoalType;
import org.spongepowered.api.entity.ai.GoalTypes;
import org.spongepowered.api.entity.ai.task.AITask;
import org.spongepowered.api.entity.ai.task.AITaskType;
import org.spongepowered.api.entity.ai.task.AITaskTypes;
import org.spongepowered.api.entity.ai.task.builtin.WatchClosestAITask;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.entity.living.animal.Chicken;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.ai.AITaskEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;


import java.util.Optional;
import java.util.function.Consumer;

import static java.lang.Math.*;

class ParticleGeneratorTask implements Consumer<Task> {
    private final Clairvoyance plugin;
    private ParticleEffect particleEffect;
    private Player player;
    private boolean arrived;
    private Logger logger;
    private Vector3d positionTo;
    private Vector3d positionFrom;
    private double distanceBetweenPoints;
    private double addToX;
    private double addToY;
    private double addToZ;
    private Entity pathfindingEntity;

    public ParticleGeneratorTask(Vector3d positionTo, Vector3d positionFrom,
                                 Player player, Clairvoyance plugin) {
        this.player = player;
        this.plugin = plugin;
        this.positionTo = positionTo;
        this.positionFrom = positionFrom;
        arrived = false;
        logger = plugin.getLogger();
        distanceBetweenPoints = positionFrom.distance(positionTo);
        addToX = (abs(positionTo.getX() - positionFrom.getX())/distanceBetweenPoints);
        addToY = (abs(positionTo.getY() - positionFrom.getY())/distanceBetweenPoints);
        addToZ = (abs(positionTo.getZ() - positionFrom.getZ())/distanceBetweenPoints);
        Vector3d offsetVector = Vector3d.from(Math.random(),Math.random(),Math.random());
        particleEffect = ParticleEffect.builder().type(ParticleTypes.SMOKE)
                .quantity(20)
                .offset(offsetVector)
                .option(ParticleOptions.COLOR, Color.CYAN)
                .build();
        spawnPathfindingEntity(player.getLocation());
        movePathfindingEntity(pathfindingEntity);
    }

    @Override
    public void accept(Task task) {
        if (arrived || !player.isOnline()) {
            task.cancel();
            player.sendMessage(Text.of(TextColors.AQUA, "You have reached your destination"));
        } else {
            Vector3d playerPosition = Sponge.getServer().getPlayer(player.getUniqueId()).get().getLocation().getPosition();
            distanceBetweenPoints = playerPosition.distance(positionTo);
            addToX = ((positionTo.getX() - playerPosition.getX())/distanceBetweenPoints);
            addToY = ((positionTo.getY() - playerPosition.getY())/distanceBetweenPoints);
            addToZ = ((positionTo.getZ() - playerPosition.getZ())/distanceBetweenPoints);

            spawnParticlesOnPlayer(playerPosition);
            Vector3d spawnPosition = playerPosition.clone();
            for (int x = 0; x <20; x++) {
                spawnPosition = spawnPosition.add(addToX, addToY, addToZ);
                if (spawnPosition.distance(positionTo) < 1) {
                    break;
                }
                if (playerPosition.distance(positionTo) < 5) {
                    arrived = true;
                    spawnFinishParticles(positionTo);
                }
                spawnParticlesOnPath(spawnPosition);
            }
        }
    }

    private void spawnFinishParticles(Vector3d positionTo) {
        Vector3d offsetVector = Vector3d.from(1.5,1.5,1.5);
        player.spawnParticles(ParticleEffect.builder()
                        .type(ParticleTypes.REDSTONE_DUST)
                        .option(ParticleOptions.OFFSET, offsetVector)
                        .option(ParticleOptions.COLOR, Color.CYAN)
                        .quantity(40).build(), positionTo,
                40);
    }

    private void spawnParticlesOnPlayer(Vector3d playerPosition){
        player.spawnParticles(particleEffect, playerPosition,
                40);
    }

    private void spawnParticlesOnPath(Vector3d spawnPosition) {
        player.spawnParticles(particleEffect, spawnPosition,
                40);
    }

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    private void spawnPathfindingEntity(Location<World> spawnLocation){
        World world = spawnLocation.getExtent();

        this.pathfindingEntity = world.createEntity(EntityTypes.CHICKEN, spawnLocation.getPosition());

        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
            world.spawnEntity(pathfindingEntity);
        }
    }

    private void movePathfindingEntity(Entity pathfindingEntity) {
        PathfindingTask pathfindingTask = new PathfindingTask(positionTo, positionFrom);
        Agent pathfindingAgent = (Agent) pathfindingEntity;

        Optional<Goal<Agent>> normalGoal = pathfindingAgent.getGoal(GoalTypes.NORMAL);
        normalGoal.ifPresent(Goal::clear);

        Goal<Agent> goal = pathfindingAgent.getGoal(GoalTypes.NORMAL).get();
        goal.addTask(0, pathfindingTask);

    }
}
