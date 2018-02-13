package de.reelos.recipecreator;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.recipecreator.event.MoreCraftListener;
import de.reelos.recipecreator.recipe.RecipeManager;

public final class RecipeCreator extends JavaPlugin {

	private final NamespacedKey namespace = new NamespacedKey(this, "morecraft");
	private RecipeManager rManager;

	@Override
	public void onEnable() {
		registerEvents();
		registerMoreRecipes();
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
}
