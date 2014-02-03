package info.tregmine.portalgun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import info.tregmine.portalgun.PortalGun;

public class PGCommand implements CommandExecutor {
	PortalGun plugin;

	public PGCommand(PortalGun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("pg")) {

		}
		return true;
	}
}