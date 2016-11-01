package de.reelos.recipecreator;

import java.util.Set;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableSet;

import de.reelos.recipecreator.util.CannotReadJsonException;
import de.reelos.recipecreator.util.RecipeReader;

public class RecipeCreator extends JavaPlugin {

    private final Set<String> jsons = ImmutableSet.of( "dirt", "lava" );

    @SuppressWarnings( "unused" )
    @Override
    public void onEnable() {
        getLogger().log( Level.INFO, "RecipeCreator loaded." );
        for ( String string : this.jsons ) {
            try {
                new RecipeReader( "./misc/" + string + ".json" );
            } catch ( CannotReadJsonException ex ) {
                getLogger().log( Level.WARNING, "Could not load " + string, ex );
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().log( Level.INFO, "RecipeCreator unloaded." );
    }
}
