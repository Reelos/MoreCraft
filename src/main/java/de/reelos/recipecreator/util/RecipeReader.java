package de.reelos.recipecreator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
	private final NamespacedKey namespace;

	public RecipeReader(NamespacedKey namespace, final String target) throws IOException {
		this(namespace, new FileInputStream(target));
	}

	public RecipeReader(NamespacedKey namespace, final InputStream inputStream) throws IOException {
		this.namespace = namespace;
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

	private ItemStack getItem() throws IOException {
		MoreRecipeFor recipeFor = this.moreRecipe.getFor();
		Material mat = Material.getMaterial(recipeFor.getName().toUpperCase());
		if (mat == null) {
			throw new CannotParseJsonException("Non readable or no result provided: \"" + recipeFor.getName() + "\"");
		}
		
		int amount = recipeFor.getAmount();
		byte meta = recipeFor.getMeta();
		String displayName = recipeFor.getDisplayName();
		ItemStack craftedItem = new ItemStack(mat, amount, meta);
		
		ItemMeta iMeta = craftedItem.getItemMeta();
		if (displayName != null && !displayName.isEmpty()) {
			iMeta.setDisplayName(displayName);
		}
		craftedItem.setItemMeta(iMeta);
		
		return craftedItem;
	}

	private Recipe createShaped() throws IOException {
		ItemStack craftedItem = getItem();
		ShapedRecipe rec = new ShapedRecipe(namespace, craftedItem);
		rec.shape(this.moreRecipe.getRecipe().toArray(new String[0]));
		for (MoreRecipeIngredients i : this.moreRecipe.getIngredients()) {
			RecipeIngredient ind = new RecipeIngredient(i.getTag(), Material.getMaterial(i.getName().toUpperCase()));
			rec.setIngredient(ind.getTag(), ind.getMat());
		}
		return rec;
	}

	private Recipe createShapeless() throws IOException {
		ItemStack craftedItem = getItem();
		ShapelessRecipe rec = new ShapelessRecipe(namespace, craftedItem);
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
		Material material = Material.getMaterial(ind.getName().toUpperCase());
		if (material == null) {
			throw new CannotParseJsonException("Could not identify " + ind.getName());
		}
		return new FurnaceRecipe(craftedItem, material);
	}

}
