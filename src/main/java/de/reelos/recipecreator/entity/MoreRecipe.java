package de.reelos.recipecreator.entity;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

/**
 * @author HerrLock
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

		public String getName() {
			return this.name;
		}

		public String getDisplayName() {
			return this.displayName;
		}

		public int getAmount() {
			return this.amount <= 0 ? 1 : this.amount;
		}

		public byte getMeta() {
			return this.meta;
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
