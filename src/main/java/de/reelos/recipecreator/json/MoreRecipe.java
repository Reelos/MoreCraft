package de.reelos.recipecreator.json;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

/**
 * @author HerrLock, Reelos
 */
public class MoreRecipe {

	private String type;
	@SerializedName("for")
	private MoreRecipeFor _for;
	private Collection<String> recipe;
	private Collection<MoreRecipeIngredients> ingredients;

	public String getType() {
		return this.type;
	}

	public MoreRecipeFor getFor() {
		return this._for;
	}

	public Collection<String> getRecipe() {
		return this.recipe;
	}

	public Collection<MoreRecipeIngredients> getIngredients() {
		return this.ingredients;
	}

	public static class MoreRecipeFor {
		private String name, displayName;
		private int amount;
		private byte meta;
		private MoreRecipeForNBTData nbtData;

		public String getName() {
			return this.name;
		}
		
		@Deprecated
		public String getDisplayName() {
			return this.displayName;
		}

		public int getAmount() {
			return this.amount <= 0 ? 1 : this.amount;
		}

		public byte getMeta() {
			return this.meta;
		}
		
		public MoreRecipeForNBTData getNBTData() {
			return nbtData;
		}

		public static class MoreRecipeForNBTData {
			private String displayName, localisedName;
			private boolean unbreakable;
			private Collection<String> lore;
			private Collection<MoreRecipeForEnchantment> enchantments;
			
			public String getDisplayName() {
				return displayName;
			}
			
			public String getLocalizedName() {
				return localisedName;
			}
			
			public boolean getUnbreakable() {
				return unbreakable;
			}
			
			public Collection<String> getLore() {
				return lore;
			}
			
			public Collection<MoreRecipeForEnchantment> getEnchantments() {
				return enchantments;
			}
			
			public static class MoreRecipeForEnchantment {
				private String name;
				private int level;
				private boolean ignoreRestrictions;
				
				public String getName() {
					return name;
				}
				
				public int getLevel() {
					return level;
				}
				
				public boolean doIgnoreRestrictions() {
					return ignoreRestrictions;
				}
			}
		}
	}

	public static class MoreRecipeIngredients {
		private String name;
		private char tag;
		private int amount;

		public String getName() {
			return this.name;
		}

		public char getTag() {
			return this.tag;
		}

		public int getAmount() {
			return this.amount;
		}

	}

}
