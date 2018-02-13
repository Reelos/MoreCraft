package de.reelos.recipecreator.recipe;

import de.reelos.recipecreator.json.MoreRecipe;

public class RecipeFile {

	private String filename;
	private MoreRecipe recipe;
	
	public RecipeFile(String filename, MoreRecipe recipe) {
		this.filename = filename;
		this.recipe = recipe;
	}
	
	public String getFileName() {
		return filename;
	}
	
	public MoreRecipe getRecipe() {
		return recipe;
	}
}
