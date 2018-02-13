package de.reelos.recipecreator.recipe;

import org.bukkit.Material;

public class RecipeIngredient {
    private final Material mat;
    private final int amount;
    private final char tag;

    public RecipeIngredient( char tag, Material mat ) {
        this.mat = mat;
        this.tag = tag;
        this.amount = 1;
    }

    public RecipeIngredient( int amount, Material mat ) {
        this.mat = mat;
        this.tag = ' ';
        this.amount = amount;
    }

    public Material getMat() {
        return this.mat;
    }

    public int getAmount() {
        return this.amount;
    }

    public char getTag() {
        return this.tag;
    }
}
