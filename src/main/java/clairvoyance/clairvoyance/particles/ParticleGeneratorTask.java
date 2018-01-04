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
        this.arrived = false;
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.positionTo = positionTo;
        this.positionFrom = positionFrom;
        this.distanceBetweenPoints = positionFrom.distance(positionTo);
        this.addToX = abs(positionTo.getX() - positionFrom.getX())/distanceBetweenPoints;
        this.addToY = abs(positionTo.getY() - positionFrom.getY())/distanceBetweenPoints;
        this.addToZ = abs(positionTo.getZ() - positionFrom.getZ())/distanceBetweenPoints;
        this.particleEffect = ParticleEffect.builder().type(ParticleTypes.REDSTONE_DUST)
                .quantity(10)
                .option(ParticleOptions.COLOR, Color.YELLOW)
                .build();
    }


    @Override
    public void accept(Task task) {
        if (this.arrived || !player.isOnline()) {
            task.cancel();
            logger.info(task.getName()+ " for clairvoyance as finished!");
        } else {
            Vector3d playerPosition = Sponge.getServer().getPlayer(player.getUniqueId()).get().getLocation().getPosition();
            this.distanceBetweenPoints = playerPosition.distance(positionTo);
            this.addToX = (positionTo.getX() - playerPosition.getX())/distanceBetweenPoints;
            this.addToY = (positionTo.getY() - playerPosition.getY())/distanceBetweenPoints;
            this.addToZ = (positionTo.getZ() - playerPosition.getZ())/distanceBetweenPoints;

            spawnParticlesOnPlayer(playerPosition);
            Vector3d spawnPosition = playerPosition.clone();
            for (int x = 0; x <20; x++) {
                spawnPosition = spawnPosition.add(addToX, addToY, addToZ);
                if (spawnPosition.distance(positionTo) < 1) {
                    break;
                }
                if (playerPosition.distance(positionTo) < 5)
                    this.arrived = true;
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
