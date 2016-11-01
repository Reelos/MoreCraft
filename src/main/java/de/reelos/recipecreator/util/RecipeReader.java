package de.reelos.recipecreator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeReader {

    private ItemStack craftedItem = new ItemStack( Material.AIR );
    private RecipeType recipeType = RecipeType.NONE;
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private String[] recipe = null;
    private Recipe ret;

    public RecipeReader( final String target ) throws CannotReadJsonException {
        JsonObject json;
        try {
            try ( JsonReader reader = Json.createReader( new FileInputStream( target ) ) ) {
                json = reader.readObject();
            }
        } catch ( IOException e ) {
            throw new CannotReadJsonException( "Could not read " + target, e );
        }
        this.recipeType = RecipeType.valueOf( json.getString( "type", "NONE" ).toUpperCase() );
        if ( this.recipeType.equals( RecipeType.NONE ) ) {
            throw new IllegalArgumentException( "Wrong or no RecipeType" );
        }
        JsonObject tar = json.getJsonObject( "for" );
        {
            Material mat = Material.getMaterial( tar.getString( "name", "AIR" ).toUpperCase() );
            if ( mat.equals( Material.AIR ) ) {
                throw new RuntimeException( "Wrong or no Material" );
            }
            int amount = tar.getInt( "amount", 1 );
            // short meta = ( short ) tar.getInt( "meta", 0 );
            String dName = tar.getString( "displayName", "" );
            this.craftedItem = new ItemStack( mat, amount );
            if ( !dName.matches( "" ) ) {
                ItemMeta iMeta = this.craftedItem.getItemMeta();
                iMeta.setDisplayName( dName );
                this.craftedItem.setItemMeta( iMeta );
            }
        }
        if ( this.recipeType.equals( RecipeType.SHAPED ) ) {
            JsonArray array = json.getJsonArray( "recipe" );
            List<String> swap = array.getValuesAs( JsonString.class ).stream().map( ( c ) -> c.getString() )
                .collect( Collectors.toList() );
            this.recipe = swap.toArray( new String[swap.size()] );
            if ( this.recipe == null ) {
                throw new RuntimeException( "Wrong or no Recipe" );
            }
            array = json.getJsonArray( "ingredients" );
            array.getValuesAs( JsonObject.class ).forEach( c -> {
                Material mat = Material.getMaterial( c.getString( "name" ).toUpperCase() );
                char tag = c.getString( "tag", " " ).charAt( 0 );
                this.ingredients.add( new RecipeIngredient( tag, mat ) );
            } );
        } else {
            JsonArray array = json.getJsonArray( "ingredients" );
            array.getValuesAs( JsonObject.class ).forEach( c -> {
                Material mat = Material.getMaterial( c.getString( "name" ).toUpperCase() );
                int amount = c.getInt( "amount", 1 );
                this.ingredients.add( new RecipeIngredient( amount, mat ) );
            } );
        }

        switch ( this.recipeType ) {
            case SHAPED:
                this.ret = new ShapedRecipe( this.craftedItem );
                ( ( ShapedRecipe ) this.ret ).shape( this.recipe );
                for ( RecipeIngredient i : this.ingredients ) {
                    ( ( ShapedRecipe ) this.ret ).setIngredient( i.getTag(), i.getMat() );
                }
                break;
            case SHAPELESS:
                this.ret = new ShapelessRecipe( this.craftedItem );
                for ( RecipeIngredient i : this.ingredients ) {
                    ( ( ShapelessRecipe ) this.ret ).addIngredient( i.getAmount(), i.getMat() );
                }
                break;
            default:
                break;
        }
    }

}
