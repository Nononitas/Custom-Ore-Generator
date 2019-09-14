package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.custom.ore.generator.command.set.SetCommand;
import de.derfrzocker.spigot.utils.CommandSeparator;

public class OreGenCommand extends CommandSeparator {

    public OreGenCommand(CustomOreGenerator customOreGenerator) {
        super(customOreGenerator);
        registerExecutor(new SetCommand(customOreGenerator), "set", Permissions.SET_PERMISSION);
        registerExecutor(new ReloadCommand(customOreGenerator), "reload", Permissions.RELOAD_PERMISSION);
        registerExecutor(new CreateCommand(customOreGenerator), "create", Permissions.CREATE_PERMISSION);

    }
}
