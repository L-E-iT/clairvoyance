package clairvoyance.clairvoyance.commands.commandexecuters;

import clairvoyance.clairvoyance.Clairvoyance;
import clairvoyance.clairvoyance.particles.ParticlesTracker;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ClairvoyanceEndCommand implements CommandExecutor {
    private Clairvoyance plugin;
    private ParticlesTracker particlesTracker;
    private HashMap<UUID, UUID> UUIDPair;
    private Logger logger;

    public ClairvoyanceEndCommand(Clairvoyance plugin) {
        this.plugin = plugin;
        particlesTracker = plugin.getParticlesTracker();
        UUIDPair = particlesTracker.getActivePlayerParticleTaskUUIDs();
        logger = plugin.getLogger();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = ((Player) src).getPlayer().get();

        if (UUIDPair.containsKey(player.getUniqueId()))
        {
            UUID taskUUID = UUIDPair.get(player.getUniqueId());
            Task taskToEnd = Sponge.getScheduler().getTaskById(taskUUID).get();
            taskToEnd.cancel();
            player.sendMessage(Text.of(TextColors.AQUA, "The magic around you fades away"));
        } else {
            player.sendMessage(Text.of(TextColors.AQUA, "You don't seem to be using clairvoyance right now"));
        }

        return CommandResult.success();
    }
}
