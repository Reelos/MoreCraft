package de.reelos.recipecreator.recipe;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import de.reelos.recipecreator.json.CannotParseJsonException;
import de.reelos.recipecreator.json.MoreRecipe;
import de.reelos.recipecreator.json.MoreRecipe.MoreRecipeFor;
import de.reelos.recipecreator.json.MoreRecipe.MoreRecipeFor.MoreRecipeForNBTData;
import de.reelos.recipecreator.json.MoreRecipe.MoreRecipeFor.MoreRecipeForNBTData.MoreRecipeForEnchantment;
import de.reelos.recipecreator.json.MoreRecipe.MoreRecipeIngredients;

public class RecipeReader {

	private final MoreRecipe moreRecipe;
	private final Recipe recipe;
	private final NamespacedKey namespace;

	public RecipeReader(NamespacedKey namespace, final MoreRecipe moreRecipe) throws IOException {
		this.namespace = namespace;
		this.moreRecipe = moreRecipe;
		
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
		if (mat == null) {
			throw new CannotParseJsonException("Non readable or no result provided: \"" + recipeFor.getName() + "\"");
		}
		
		int amount = recipeFor.getAmount();
		byte meta = recipeFor.getMeta();
		ItemStack craftedItem = new ItemStack(mat, amount, meta);
		
		MoreRecipeForNBTData nbtData = recipeFor.getNBTData();
		ItemMeta iMeta = craftedItem.getItemMeta();
		
		if(recipeFor.getDisplayName() != null && !recipeFor.getDisplayName().equals("")) {
			iMeta.setDisplayName(recipeFor.getDisplayName());
		}
		
		iMeta = syncronizeItemMeta(iMeta, nbtData);
		craftedItem.setItemMeta(iMeta);
		
		return craftedItem;
	}
	
	private ItemMeta syncronizeItemMeta(ItemMeta base, MoreRecipeForNBTData nbtData) {
		ItemMeta iMeta = base;
		
		if (nbtData != null) {
			if(nbtData.getDisplayName() != null && !nbtData.getDisplayName().equals("")) {
				iMeta.setDisplayName(nbtData.getDisplayName());
			}
			
			if(nbtData.getLocalizedName() != null && !nbtData.getLocalizedName().equals("")) {
				iMeta.setLocalizedName(nbtData.getLocalizedName());
			}
			
			if(nbtData.getLore() != null) {
				iMeta.setLore(new ArrayList<>(nbtData.getLore()));
			}
			
			iMeta.setUnbreakable(nbtData.getUnbreakable());
			
			if(nbtData.getEnchantments() != null) {
				for(MoreRecipeForEnchantment e: nbtData.getEnchantments()) {
					Enchantment ench = null;
					if(e.getName().toLowerCase().equals("power")) {
						ench = Enchantment.ARROW_DAMAGE;
					} else if(e.getName().toLowerCase().equals("flame")) {
						ench = Enchantment.ARROW_FIRE;
					} else if(e.getName().toLowerCase().equals("infinity")) {
						ench = Enchantment.ARROW_INFINITE;
					} else if(e.getName().toLowerCase().equals("punch")) {
						ench = Enchantment.ARROW_KNOCKBACK;
					} else if(e.getName().toLowerCase().equals("curseofbinding")) {
						ench = Enchantment.BINDING_CURSE;
					} else if(e.getName().toLowerCase().equals("sharpness")) {
						ench = Enchantment.DAMAGE_ALL;
					} else if(e.getName().toLowerCase().equals("baneofarthropods")) {
						ench = Enchantment.DAMAGE_ARTHROPODS;
					} else if(e.getName().toLowerCase().equals("smite")) {
						ench = Enchantment.DAMAGE_UNDEAD;
					} else if(e.getName().toLowerCase().equals("depthstrider")) {
						ench = Enchantment.DEPTH_STRIDER;
					} else if(e.getName().toLowerCase().equals("efficency")) {
						ench = Enchantment.DIG_SPEED;
					} else if(e.getName().toLowerCase().equals("unbreaking")) {
						ench = Enchantment.DURABILITY;
					} else if(e.getName().toLowerCase().equals("fireaspect")) {
						ench = Enchantment.FIRE_ASPECT;
					} else if(e.getName().toLowerCase().equals("frostwalker")) {
						ench = Enchantment.FROST_WALKER;
					} else if(e.getName().toLowerCase().equals("knockback")) {
						ench = Enchantment.KNOCKBACK;
					} else if(e.getName().toLowerCase().equals("fortune")) {
						ench = Enchantment.LOOT_BONUS_BLOCKS;
					} else if(e.getName().toLowerCase().equals("looting")) {
						ench = Enchantment.LOOT_BONUS_MOBS;
					} else if(e.getName().toLowerCase().equals("luckofthesea")) {
						ench = Enchantment.LUCK;
					} else if(e.getName().toLowerCase().equals("lure")) {
						ench = Enchantment.LURE;
					} else if(e.getName().toLowerCase().equals("mending")) {
						ench = Enchantment.MENDING;
					} else if(e.getName().toLowerCase().equals("respiration")) {
						ench = Enchantment.OXYGEN;
					} else if(e.getName().toLowerCase().equals("protection")) {
						ench = Enchantment.PROTECTION_ENVIRONMENTAL;
					} else if(e.getName().toLowerCase().equals("blastprotection")) {
						ench = Enchantment.PROTECTION_EXPLOSIONS;
					} else if(e.getName().toLowerCase().equals("featherfalling")) {
						ench = Enchantment.PROTECTION_FALL;
					} else if(e.getName().toLowerCase().equals("fireprotection")) {
						ench = Enchantment.PROTECTION_FIRE;
					} else if(e.getName().toLowerCase().equals("projectileprotection")) {
						ench = Enchantment.PROTECTION_PROJECTILE;
					} else if(e.getName().toLowerCase().equals("silktouch")) {
						ench = Enchantment.SILK_TOUCH;
					} else if(e.getName().toLowerCase().equals("sweepingedge")) {
						ench = Enchantment.SWEEPING_EDGE;
					} else if(e.getName().toLowerCase().equals("thorns")) {
						ench = Enchantment.THORNS;
					} else if(e.getName().toLowerCase().equals("curseofvanishing")) {
						ench = Enchantment.VANISHING_CURSE;
					} else if(e.getName().toLowerCase().equals("aquaaffinity")) {
						ench = Enchantment.WATER_WORKER;
					}
					if(ench != null) {
						iMeta.addEnchant(ench, e.getLevel(), e.doIgnoreRestrictions());
					}
				}
			}
		}
		
		return iMeta;
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
