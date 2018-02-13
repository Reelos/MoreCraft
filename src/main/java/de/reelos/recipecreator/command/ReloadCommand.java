package de.reelos.recipecreator.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.reelos.recipecreator.RecipeCreator;

public class ReloadCommand implements CommandExecutor {

	private final RecipeCreator plugin;
	
	public ReloadCommand(RecipeCreator plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean result = false;
		
		String cmd = command.getName().toLowerCase();
		
		if(cmd.equals("reloadrecipes")) {
			plugin.getRecipeManager().reloadRecipes();
			result = true;
		}
		
		return result;
	}

}
