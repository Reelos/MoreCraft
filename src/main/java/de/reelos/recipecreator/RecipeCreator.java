package de.reelos.recipecreator;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.recipecreator.command.ReloadCommand;
import de.reelos.recipecreator.event.MoreCraftListener;
import de.reelos.recipecreator.recipe.RecipeManager;

public final class RecipeCreator extends JavaPlugin {

	private final NamespacedKey namespace = new NamespacedKey(this, "morecraft");
	private RecipeManager rManager;

	@Override
	public void onEnable() {
		registerCommands();
		registerEvents();
		registerMoreRecipes();
	}
	
	private void registerCommands() {
		getCommand("reloadrecipes").setExecutor(new ReloadCommand(this));
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(MoreCraftListener.getInstance(this), this);
	}

	private void registerMoreRecipes() {
		 rManager = new RecipeManager(this, namespace);
	}

	@Override
	public void onDisable() {
	}
	
	public NamespacedKey getNamespace() {
		return namespace;
	}
	
	public RecipeManager getRecipeManager() {
		return rManager;
	}
}
