package clairvoyance.clairvoyance.particles;

import clairvoyance.clairvoyance.Clairvoyance;
import com.flowpowered.math.vector.Vector3d;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.effect.particle.*;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Color;


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

    public ParticleGeneratorTask(Vector3d positionTo, Vector3d positionFrom,
                                 Player player, Clairvoyance plugin) {
        this.player = player;
        this.plugin = plugin;
        this.positionTo = positionTo;
        this.positionFrom = positionFrom;
        arrived = false;
        logger = plugin.getLogger();
        distanceBetweenPoints = positionFrom.distance(positionTo);
        addToX = abs(positionTo.getX() - positionFrom.getX())/distanceBetweenPoints;
        addToY = abs(positionTo.getY() - positionFrom.getY())/distanceBetweenPoints;
        addToZ = abs(positionTo.getZ() - positionFrom.getZ())/distanceBetweenPoints;
        Vector3d offsetVector = Vector3d.from(.25,.25,.25);
        particleEffect = ParticleEffect.builder().type(ParticleTypes.REDSTONE_DUST)
                .quantity(20)
                .offset(offsetVector)
                .option(ParticleOptions.COLOR, Color.YELLOW)
                .build();
    }


    @Override
    public void accept(Task task) {
        if (arrived || !player.isOnline()) {
            task.cancel();
            logger.info(task.getName()+ " for clairvoyance as finished!");
        } else {
            Vector3d playerPosition = Sponge.getServer().getPlayer(player.getUniqueId()).get().getLocation().getPosition();
            distanceBetweenPoints = playerPosition.distance(positionTo);
            addToX = (positionTo.getX() - playerPosition.getX())/distanceBetweenPoints;
            addToY = (positionTo.getY() - playerPosition.getY())/distanceBetweenPoints;
            addToZ = (positionTo.getZ() - playerPosition.getZ())/distanceBetweenPoints;

            spawnParticlesOnPlayer(playerPosition);
            Vector3d spawnPosition = playerPosition.clone();
            for (int x = 0; x <20; x++) {
                spawnPosition = spawnPosition.add(addToX, addToY, addToZ);
                if (spawnPosition.distance(positionTo) < 1) {
                    break;
                }
                if (playerPosition.distance(positionTo) < 5)
                    arrived = true;
                    spawnFinishParticles(positionTo);
                spawnParticlesOnPath(spawnPosition);
            }
        }
    }

    private void spawnFinishParticles(Vector3d positionTo) {
        Vector3d offsetVector = Vector3d.from(1.5,1.5,1.5);
        player.spawnParticles(ParticleEffect.builder()
                        .type(ParticleTypes.REDSTONE_DUST)
                        .option(ParticleOptions.COLOR, Color.YELLOW)
                        .option(ParticleOptions.OFFSET, offsetVector)
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
}
