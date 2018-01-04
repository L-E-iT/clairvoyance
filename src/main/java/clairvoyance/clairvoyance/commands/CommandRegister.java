package clairvoyance.clairvoyance.commands;

import clairvoyance.clairvoyance.Clairvoyance;
import clairvoyance.clairvoyance.commands.commandexecuters.ClairvoyanceMainCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

public class CommandRegister {
    public static void registerCommands(Clairvoyance plugin) {
// Example Command
//    CommandSpec Referral = CommandSpec.builder()
//            .description(Text.of("Basic command for Referrals plugin"))
//            .permission("referrals.command.referral")
//            .executor(new ReferralsMainCommand())
//            .build();

        // /clairvoyance, /cv
        CommandSpec Clairvoyance  = CommandSpec.builder()
                .description(Text.of("Basic Clairvoyance command"))
                .arguments(GenericArguments.location(Text.of("location")))
                .executor(new ClairvoyanceMainCommand(plugin))
                .build();

        Sponge.getCommandManager().register(plugin, Clairvoyance, "clairvoyance", "cv");

    }
}
