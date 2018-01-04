package clairvoyance.clairvoyance.commands.commandexecuters;

import clairvoyance.clairvoyance.Clairvoyance;
import clairvoyance.clairvoyance.particles.ParticlesTaskManager;
import com.flowpowered.math.vector.Vector3d;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

public class ClairvoyanceMainCommand implements CommandExecutor{

    private final Clairvoyance plugin;
    private final Logger logger;
    private final Game game;
    private GameRegistry gameRegistry;
    private boolean arrived;

    public ClairvoyanceMainCommand(Clairvoyance plugin){
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.game = plugin.getGame();
        this.gameRegistry = game.getRegistry();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player){
            Player commandSender = ((Player) src).getPlayer().get();
            Location locCommand = (Location) args.getOne(Text.of("location")).get();
            Location locPlayer = commandSender.getLocation();

            Vector3d positionTo = new Vector3d(locCommand.getX(), locCommand.getY(), locCommand.getZ());
            Vector3d positionFrom = new Vector3d(locPlayer.getX(), locPlayer.getY(), locPlayer.getZ());

            ParticlesTaskManager particlesTaskManager = new ParticlesTaskManager(positionTo, positionFrom, commandSender, plugin);
            particlesTaskManager.generateParticles();
        }
        return CommandResult.success();
    }
}
