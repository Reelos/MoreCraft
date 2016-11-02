package de.reelos.recipecreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.recipecreator.util.CannotParseJsonException;
import de.reelos.recipecreator.util.RecipeReader;

public class RecipeCreator extends JavaPlugin {

	private final Path recipeFolder = Paths.get("./recipes/");

	@Override
	public void onEnable() {
		getLogger().log(Level.INFO, "RecipeCreator loaded.");

		if (!Files.exists(recipeFolder)) {
			try {
				Files.createDirectories(recipeFolder);
			} catch (IOException e) {
				getLogger().log(Level.WARNING, "Could not create 'recipeFolder'.", e);
			}
		}
		for (String string : recipeFolder.toFile().list((File f, String s) -> f.getName().endsWith(".json"))) {
			try {
				new RecipeReader("./recipes/" + string).registerRecipe();
			} catch (FileNotFoundException | CannotParseJsonException ex) {
				getLogger().log(Level.WARNING, "Could not load " + string, ex);
			}
		}
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "RecipeCreator unloaded.");
	}
}
