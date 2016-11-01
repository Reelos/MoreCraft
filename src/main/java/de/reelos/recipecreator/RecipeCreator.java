package de.reelos.recipecreator;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.recipecreator.util.RecipeReader;

public class RecipeCreator extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().log( Level.INFO, "RecipeCreator loaded." );
        new RecipeReader( "./misc/dirt.json" );
        new RecipeReader( "./misc/lava.json" );
    }

    @Override
    public void onDisable() {
        getLogger().log( Level.INFO, "RecipeCreator unloaded." );
    }
}
