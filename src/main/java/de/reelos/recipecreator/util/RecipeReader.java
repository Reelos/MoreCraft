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

	public RecipeReader(final String target) throws IOException {
		this(new FileInputStream(target));
	}

	@SuppressWarnings("deprecation")
	public RecipeReader(final InputStream inputStream) throws IOException {
		final MoreRecipe moreRecipe;
		try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			moreRecipe = new Gson().fromJson(reader, MoreRecipe.class);
		}
		try {
			this.recipeType = RecipeType.valueOf(moreRecipe.getType().toUpperCase());
			switch (this.recipeType) {
			case SHAPED:
			case SHAPELESS:
			case FURNACE:
				break;
			default:
				throw new CannotParseJsonException("Wrong or no RecipeType");
			}
		} catch (IllegalArgumentException ex) {
			throw new CannotParseJsonException("Could not read ", ex);
		}
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
		if (this.recipeType == RecipeType.SHAPED) {
			Collection<String> recipe = moreRecipe.getRecipe();
			this.shape = recipe.toArray(new String[recipe.size()]);
			if (this.shape == null) {
				throw new CannotParseJsonException("Wrong or no Recipe");
			}
			Collection<MoreRecipeIngredients> ingredients = moreRecipe.getIngredients();
			ingredients.forEach(it -> this.ingredients
					.add(new RecipeIngredient(it.getTag(), Material.getMaterial(it.getName().toUpperCase()))));
		} else {
			moreRecipe.getIngredients().forEach(it -> this.ingredients
					.add(new RecipeIngredient(it.getAmount(), Material.getMaterial(it.getName().toUpperCase()))));
		}
	}

	public Recipe createRecipe() {
		Recipe recipe;
		if (this.recipeType == RecipeType.SHAPED) {
			recipe = createShaped();
		} else if (this.recipeType == RecipeType.SHAPELESS) {
			recipe = createShapeless();
		} else {
			recipe = null;
		}
		return recipe;
	}

	private Recipe createShaped() {
		ShapedRecipe rec = new ShapedRecipe(this.craftedItem);
		rec.shape(this.shape);
		for (RecipeIngredient i : this.ingredients) {
			rec.setIngredient(i.getTag(), i.getMat());
		}
		return rec;
	}

	private Recipe createShapeless() {
		ShapelessRecipe rec = new ShapelessRecipe(this.craftedItem);
		for (RecipeIngredient i : this.ingredients) {
			rec.addIngredient(i.getAmount(), i.getMat());
		}
		return rec;
	}

}
