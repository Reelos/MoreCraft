package de.reelos.recipecreator.recipe;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;

import de.reelos.recipecreator.json.MoreRecipe;

public class RecipeManager {
	private final Path recipeFolder = Paths.get(".plugins/MoreCraft/recipes/");

	private final List<RecipeFile> recipes = new ArrayList<>();
	private final Plugin plugin;
	private final NamespacedKey namespace;

	public RecipeManager(Plugin plugin, NamespacedKey namespace) {
		this.plugin = plugin;
		this.namespace = namespace;
		
		loadRecipes();
		registerRecipes();
	}

	private void loadRecipes() {
		if (!Files.exists(this.recipeFolder)) {
			try {
				Files.createDirectories(this.recipeFolder);
			} catch (IOException e) {
				plugin.getLogger().log(Level.WARNING, "Could not create 'recipeFolder'.", e);
			}
		} else {
			try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.recipeFolder, "*.json")) {
				for (Path path : directoryStream) {

					try (Reader reader = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
						MoreRecipe moreRecipe = new Gson().fromJson(reader, MoreRecipe.class);
						recipes.add(new RecipeFile(path.getFileName().toString(), moreRecipe));
					}
				}
			} catch (IOException ex) {
				plugin.getLogger().log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}
	
	public void reloadRecipes() {
		
	}

	private void registerRecipes() {
		for (RecipeFile recipeFile : recipes) {
			try {
				Recipe recipe = new RecipeReader(namespace, recipeFile.getRecipe()).createRecipe();
				if (recipe != null) {
					Bukkit.getServer().addRecipe(recipe);
					plugin.getLogger().info("Registered recipe: " + recipeFile.getFileName());
				}
			} catch (Exception ex) {
				plugin.getLogger().log(Level.WARNING, "Could not load " + recipeFile.getFileName(), ex);
			}
		}
	}
}
