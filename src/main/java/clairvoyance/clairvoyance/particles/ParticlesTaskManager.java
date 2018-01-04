package clairvoyance.clairvoyance.particles;

import clairvoyance.clairvoyance.Clairvoyance;
import com.flowpowered.math.vector.Vector3d;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.util.Direction;

import java.util.concurrent.TimeUnit;

public class ParticlesTaskManager {
    private Vector3d positionTo;
    private Vector3d positionFrom;
    private Player player;
    private boolean arrived;
    private Clairvoyance plugin;
    private Logger logger;
    private Vector3d vectorBetween;

    public ParticlesTaskManager(Vector3d positionTo, Vector3d positionFrom, Player player, Clairvoyance plugin) {
        this.player = player;
        this.positionTo = positionTo;
        this.positionFrom = positionFrom;
        this.plugin = plugin;
        logger = plugin.getLogger();
        vectorBetween =  positionTo.sub(positionFrom);
    }

    public void generateParticles() {
        Task task = Task.builder().execute(
                new ParticleGeneratorTask(positionTo, positionFrom, player, plugin))
                .interval(1, TimeUnit.SECONDS)
                .name("Particles Generator")
                .submit(plugin);
    }

}
