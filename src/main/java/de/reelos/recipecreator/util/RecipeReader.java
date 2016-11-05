package de.reelos.recipecreator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import de.reelos.recipecreator.entity.MoreRecipe;
import de.reelos.recipecreator.entity.MoreRecipe.MoreRecipeFor;
import de.reelos.recipecreator.entity.MoreRecipe.MoreRecipeIngredients;

public class RecipeReader {

	private ItemStack craftedItem = new ItemStack(Material.AIR);
	private RecipeType recipeType = RecipeType.NONE;
	private List<RecipeIngredient> ingredients = new ArrayList<>();
	private String[] shape = null;
	private final MoreRecipe moreRecipe;
	private Recipe recipe;

	public RecipeReader(final String target) throws IOException {
		this(new FileInputStream(target));
	}

	public RecipeReader(final InputStream inputStream) throws IOException {
		try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			moreRecipe = new Gson().fromJson(reader, MoreRecipe.class);
		}
		try {
			this.recipeType = RecipeType.valueOf(moreRecipe.getType().toUpperCase());
			switch (this.recipeType) {
			case SHAPED:
				recipe = createShaped();
				break;
			case SHAPELESS:
				recipe = createShapeless();
				break;
			case FURNACE:
				break;
			default:
				throw new CannotParseJsonException("Wrong or no RecipeType");
			}
		} catch (IllegalArgumentException ex) {
			throw new CannotParseJsonException("Could not read ", ex);
		}
	}

	public Recipe createRecipe() {
		return recipe;
	}
	
	@SuppressWarnings("deprecation")
	private void getItem() throws IOException {
		MoreRecipeFor recipeFor = moreRecipe.getFor();
		{
			Material mat = Material.getMaterial(recipeFor.getName().toUpperCase());
			if (mat.equals(Material.AIR)) {
				throw new CannotParseJsonException("Wrong or no Material");
			}
			int amount = recipeFor.getAmount();
			String dName = recipeFor.getDisplayName();
			this.craftedItem = new ItemStack(mat, amount);
			byte meta = recipeFor.getMeta();
			this.craftedItem.getData().setData(meta);
			if (!dName.matches("")) {
				ItemMeta iMeta = this.craftedItem.getItemMeta();
				iMeta.setDisplayName(dName);
				this.craftedItem.setItemMeta(iMeta);
			}
		}
	}

	private Recipe createShaped() throws IOException {
		getItem();
		Collection<String> recipe = moreRecipe.getRecipe();
		this.shape = recipe.toArray(new String[recipe.size()]);
		if (this.shape == null) {
			throw new CannotParseJsonException("Wrong or no Recipe");
		}
		Collection<MoreRecipeIngredients> ingredients = moreRecipe.getIngredients();
		ingredients.forEach(it -> this.ingredients
				.add(new RecipeIngredient(it.getTag(), Material.getMaterial(it.getName().toUpperCase()))));
		ShapedRecipe rec = new ShapedRecipe(this.craftedItem);
		rec.shape(this.shape);
		for (RecipeIngredient i : this.ingredients) {
			rec.setIngredient(i.getTag(), i.getMat());
		}
		return rec;
	}

	private Recipe createShapeless() throws IOException {
		getItem();
		moreRecipe.getIngredients().forEach(it -> this.ingredients
				.add(new RecipeIngredient(it.getAmount(), Material.getMaterial(it.getName().toUpperCase()))));
		
		ShapelessRecipe rec = new ShapelessRecipe(this.craftedItem);
		for (RecipeIngredient i : this.ingredients) {
			rec.addIngredient(i.getAmount(), i.getMat());
		}
		return rec;
	}

}
