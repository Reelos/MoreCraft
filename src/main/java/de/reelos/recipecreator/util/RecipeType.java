package de.reelos.recipecreator.util;

public enum RecipeType {
	NONE,SHAPED,SHAPELESS,FURNACE;
	
	public static RecipeType getType(String name) {
		RecipeType out = null;
		for(RecipeType type : RecipeType.values()) {
			if(type.name().matches(name)) {
				out = type;
				break;
			}
		}
		return out;
	}
}
