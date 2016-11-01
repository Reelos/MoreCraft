package de.reelos.recipecreator;

import java.util.Set;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableSet;

import de.reelos.recipecreator.util.CannotParseJsonException;
import de.reelos.recipecreator.util.RecipeReader;

public class RecipeCreator extends JavaPlugin {

    private final Set<String> jsons = ImmutableSet.of( "dirt", "lava" );

    @Override
    public void onEnable() {
        getLogger().log( Level.INFO, "RecipeCreator loaded." );
        for ( String string : this.jsons ) {
            try {
                new RecipeReader( "./misc/" + string + ".json" ).registerRecipe();
            } catch ( CannotParseJsonException ex ) {
                getLogger().log( Level.WARNING, "Could not load " + string, ex );
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().log( Level.INFO, "RecipeCreator unloaded." );
    }
}
