package de.reelos.recipecreator;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.recipecreator.event.MoreCraftListener;
import de.reelos.recipecreator.recipe.RecipeReader;

public final class RecipeCreator extends JavaPlugin {

	private final Path recipeFolder = Paths.get(".plugins/MoreCraft/recipes/");
	private final NamespacedKey namespace = new NamespacedKey(this, "morecraft");

	@Override
	public void onEnable() {
		registerEvents();
		registerMoreRecipes();
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(MoreCraftListener.getInstance(this), this);
	}

	private void registerMoreRecipes() {
		if (!Files.exists(this.recipeFolder)) {
			try {
				Files.createDirectories(this.recipeFolder);
			} catch (IOException e) {
				getLogger().log(Level.WARNING, "Could not create 'recipeFolder'.", e);
			}
		} else {
			try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.recipeFolder, "*.json")) {
				for (Path path : directoryStream) {
					try {
						Recipe recipe = new RecipeReader(namespace, Files.newInputStream(path)).createRecipe();
						if (recipe != null) {
							Bukkit.getServer().addRecipe(recipe);
							getLogger().info("Registered recipe: " + path.getFileName());
						}
					} catch (Exception ex) {
						getLogger().log(Level.WARNING, "Could not load " + path, ex);
					}
				}
			} catch (IOException ex) {
				getLogger().log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	@Override
	public void onDisable() {
	}
	
	
	public NamespacedKey getNamespace() {
		return namespace;
	}
}
