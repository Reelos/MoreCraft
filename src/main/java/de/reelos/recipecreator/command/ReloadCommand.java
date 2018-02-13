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
			//TODO Create a Manager to keep Track on registered Recipes and
			//	   write a Method to Add Recipes or remove them, based on the recipe folder
			result = true;
		}
		
		return result;
	}

}
