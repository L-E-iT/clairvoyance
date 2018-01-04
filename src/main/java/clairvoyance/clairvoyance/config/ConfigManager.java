package clairvoyance.clairvoyance.config;

import clairvoyance.clairvoyance.Clairvoyance;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.asset.Asset;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private static ConfigManager instance = new ConfigManager();
    private Clairvoyance plugin;
    private Logger logger;

    public void init(Clairvoyance plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public PluginConfig loadPluginConfig() {
        File configFile;
        configFile = plugin.getDefaultConfig().toFile();
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(configFile).build();
        if (!configFile.exists()) {
            Asset defaultConfig = plugin.getInstance().getAsset("default_config.conf").get();
            try {
                defaultConfig.copyToFile(configFile.toPath());
                loader.save(loader.load());
            } catch (IOException e) {
                logger.warn("Error in loading the default config!" +  e.getMessage());
            }
        }

        try {
            CommentedConfigurationNode defaultConfig = loader.load();
            //Load config node
            CommentedConfigurationNode configNode = defaultConfig.getNode("");
            //Load config sub-node
//            String rewardCommand = rewardActions.getNode("").getString();


            // return values
            return new PluginConfig();

        } catch (IOException e) {
            logger.warn("Error loading config!" + e.getMessage());
        }

        return new PluginConfig();

    }
}
