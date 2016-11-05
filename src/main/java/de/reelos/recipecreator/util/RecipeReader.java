package de.reelos.recipecreator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
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

	private final MoreRecipe moreRecipe;
	private final Recipe recipe;

	public RecipeReader(final String target) throws IOException {
		this(new FileInputStream(target));
	}

	public RecipeReader(final InputStream inputStream) throws IOException {
		try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			this.moreRecipe = new Gson().fromJson(reader, MoreRecipe.class);
		}
		try {
			RecipeType recipeType = RecipeType.valueOf(this.moreRecipe.getType().toUpperCase());
			switch (recipeType) {
			case SHAPED:
				this.recipe = createShaped();
				break;
			case SHAPELESS:
				this.recipe = createShapeless();
				break;
			case FURNACE:
				this.recipe = createFurnace();
				break;
			default:
				throw new CannotParseJsonException("Wrong or no RecipeType");
			}
		} catch (IllegalArgumentException ex) {
			throw new CannotParseJsonException("Could not read file", ex);
		}
	}

	public Recipe createRecipe() {
		return this.recipe;
	}

	@SuppressWarnings("deprecation")
	private ItemStack getItem() throws IOException {
		MoreRecipeFor recipeFor = this.moreRecipe.getFor();
		Material mat = Material.getMaterial(recipeFor.getName().toUpperCase());
		if (mat.equals(Material.AIR)) {
			throw new CannotParseJsonException("Wrong or no Material");
		}
		int amount = recipeFor.getAmount();
		String dName = recipeFor.getDisplayName();
		ItemStack craftedItem = new ItemStack(mat, amount);
		byte meta = recipeFor.getMeta();
		craftedItem.getData().setData(meta);
		if (!dName.matches("")) {
			ItemMeta iMeta = craftedItem.getItemMeta();
			iMeta.setDisplayName(dName);
			craftedItem.setItemMeta(iMeta);
		}
		return craftedItem;
	}

	private Recipe createShaped() throws IOException {
		ItemStack craftedItem = getItem();
		ShapedRecipe rec = new ShapedRecipe(craftedItem);
		rec.shape(this.moreRecipe.getRecipe().toArray(new String[0]));
		for (MoreRecipeIngredients i : this.moreRecipe.getIngredients()) {
			RecipeIngredient ind = new RecipeIngredient(i.getTag(), Material.getMaterial(i.getName().toUpperCase()));
			rec.setIngredient(ind.getTag(), ind.getMat());
		}
		return rec;
	}

	private Recipe createShapeless() throws IOException {
		ItemStack craftedItem = getItem();
		ShapelessRecipe rec = new ShapelessRecipe(craftedItem);
		for (MoreRecipeIngredients it : this.moreRecipe.getIngredients()) {
			RecipeIngredient ind = new RecipeIngredient(it.getAmount(),
					Material.getMaterial(it.getName().toUpperCase()));
			rec.addIngredient(ind.getAmount(), ind.getMat());
		}
		return rec;
	}

	private Recipe createFurnace() throws IOException {
		ItemStack craftedItem = getItem();
		MoreRecipeIngredients ind = this.moreRecipe.getIngredients().iterator().next();
		return new FurnaceRecipe(craftedItem, Material.getMaterial(ind.getName().toUpperCase()));
	}

}
