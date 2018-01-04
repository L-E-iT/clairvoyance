package clairvoyance.clairvoyance;

import clairvoyance.clairvoyance.commands.CommandRegister;
import clairvoyance.clairvoyance.config.ConfigManager;
import clairvoyance.clairvoyance.config.PluginConfig;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
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

    @Listener
    public void onGameInit(GameInitializationEvent event){
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.init(this);

        this.pluginConfig = configManager.loadPluginConfig();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Run logger when server is loaded.
        this.logger.info("Clairvoyance has loaded!");
        this.game = game;
        // Register our commands
        CommandRegister.registerCommands(this);
    }


}
