package de.reelos.recipecreator.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeReader {

	private ItemStack craftedItem = new ItemStack(Material.AIR);
	private RecipeType recipeType = RecipeType.NONE;
	private List<RecipeIngredient> ingredients = new ArrayList<>();
	private String[] recipe = null;
	private Recipe ret;

	public RecipeReader(String target) {
		try{
			JsonReader reader = Json.createReader(new FileInputStream(target));
			JsonObject json = reader.readObject();
			reader.close();
			recipeType = RecipeType.getType(json.getString("type","NONE").toUpperCase());
			if(recipeType.equals(RecipeType.NONE)) {
				throw new RuntimeException("Wrong or no RecipeType");
			}
			JsonObject tar = json.getJsonObject("for");
			{
				Material mat = Material.getMaterial(tar.getString("name","AIR").toUpperCase());
				if(mat.equals(Material.AIR)) {
					throw new RuntimeException("Wrong or no Material");
				}
				int amount = tar.getInt("amount",1);
				short meta = (short)tar.getInt("meta",0);
				String dName = tar.getString("displayName","");
				craftedItem = new ItemStack(mat,amount);
				if(!dName.matches("")) {
					ItemMeta iMeta = craftedItem.getItemMeta();
					iMeta.setDisplayName(dName);
					craftedItem.setItemMeta(iMeta);
				}
			}
			if(recipeType.equals(RecipeType.SHAPED)) {
				JsonArray array = json.getJsonArray("recipe");
				List<String> swap = array.getValuesAs(JsonString.class).stream().map((c) -> c.getString()).collect(Collectors.toList());
				recipe = swap.toArray(new String[swap.size()]);
				if(recipe == null) {
					throw new RuntimeException("Wrong or no Recipe");
				}
				array = json.getJsonArray("ingredients");
				array.getValuesAs(JsonObject.class).forEach(c -> {
					Material mat = Material.getMaterial(c.getString("name").toUpperCase());
					char tag = c.getString("tag"," ").charAt(0);
					ingredients.add(new RecipeIngredient(tag, mat));
				});
			} else {
				JsonArray array = json.getJsonArray("ingredients");
				array.getValuesAs(JsonObject.class).forEach(c -> {
					Material mat = Material.getMaterial(c.getString("name").toUpperCase());
					int amount = c.getInt("amount",1);
					ingredients.add(new RecipeIngredient(amount, mat));
				});
			}
			
			switch(recipeType){
			case SHAPED:
				ret = new ShapedRecipe(craftedItem);
				((ShapedRecipe)ret).shape(recipe);
				for(RecipeIngredient i: ingredients) {
					((ShapedRecipe)ret).setIngredient(i.getTag(), i.getMat());
				}
				break;
			case SHAPELESS:
				ret = new ShapelessRecipe(craftedItem);
				for(RecipeIngredient i: ingredients) {
					((ShapelessRecipe)ret).addIngredient(i.getAmount(),i.getMat());
				}
				break;
			default:
				break;
			};
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void readData(String target) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(target)));
		String line = "";
		int linecount = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			} else {
				linecount++;
				if (line.startsWith("for")) {
					int datastart = line.indexOf(':') + 1;
					int dataend = line.length() - 1;
					short metaType = 0;
					int stacksize = 1;
					String name = "";
					if (line.contains("{")) {
						dataend = line.indexOf('{');
						String data = line.substring(line.indexOf('{') + 1, line.length() - 2);

						String[] more = data.split(",");
						switch (more.length) {
						case 3:
							if (!more[2].matches(""))
								name = more[2];
						case 2:
							if (!more[1].matches(""))
								metaType = Short.valueOf(more[1]);
						case 1:
							if (!more[0].matches("")) {
								stacksize = Integer.valueOf(more[0]);
							}
							break;
						}
					}
					String choose = line.substring(datastart, dataend).trim().toUpperCase();
					Material mat = Material.getMaterial(choose);
					if (mat == null) {
						break;
					}
					craftedItem = new ItemStack(mat, stacksize, metaType);
					if(!name.matches("")) {
						ItemMeta meta = craftedItem.getItemMeta();
						meta.setDisplayName(name);
						craftedItem.setItemMeta(meta);
					}
				}
				if (line.startsWith("type")) {
					line = line.substring(line.indexOf(':') + 1, line.length() - 1).trim().toUpperCase();
					recipeType = RecipeType.getType(line);
				}
				if(recipeType == RecipeType.SHAPED && line.startsWith("shape"))
				if(line.startsWith("ingredients")) {
					
				}
			}
		}
	}

	public static void main(String[] args) {
		new RecipeReader("./misc/dirt.json");
		new RecipeReader("./misc/lava.json");
	}
}
