package clairvoyance.clairvoyance;

import clairvoyance.clairvoyance.commands.CommandRegister;
import clairvoyance.clairvoyance.config.ConfigManager;
import clairvoyance.clairvoyance.config.PluginConfig;
import clairvoyance.clairvoyance.particles.ParticlesTracker;
import clairvoyance.clairvoyance.pathfinding.PathfindingTask;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

@Plugin(
        id = "clairvoyance",
        name = "Clairvoyance",
        description = "Clairvoyance shows you a path to your destination",
        authors = {
                "BranFlakes"
        }
)
public class Clairvoyance {

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path defaultConfigDir;

    @Inject
    private PluginContainer instance;

    private PluginConfig pluginConfig;
    private ParticlesTracker particlesTracker;

    public Logger getLogger() {
        return logger;
    }
    public PluginContainer getInstance() {
        return instance;
    }
    public Path getDefaultConfigDir() {
        return defaultConfigDir;
    }
    public Path getDefaultConfig() { return defaultConfig; }
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
    public Game getGame() {
        return game;
    }
    public ParticlesTracker getParticlesTracker() {
        return particlesTracker;
    }

    @Listener
    public void onGameInit(GameInitializationEvent event){
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.init(this);

        this.pluginConfig = configManager.loadPluginConfig();
    }

    @Listener
    public void onInitialize(final GameInitializationEvent event){
        PathfindingTask.register(this, game.getRegistry());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Run logger when server is loaded.
        this.logger.info("Clairvoyance has loaded!");
        this.game = game;
        // Start our particlesTracker
        this.particlesTracker = ParticlesTracker.getInstance();

        // Register our commands
        CommandRegister.registerCommands(this);
    }


}
