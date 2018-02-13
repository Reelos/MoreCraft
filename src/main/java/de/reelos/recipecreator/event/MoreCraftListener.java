package de.reelos.recipecreator.event;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.reelos.recipecreator.RecipeCreator;

public class MoreCraftListener implements Listener {
	private static final MoreCraftListener INSTANCE = new MoreCraftListener();
	
	private NamespacedKey namespace = null;
	
	private MoreCraftListener() {
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
	}
	
	private void setNamespace(RecipeCreator parent) {
		if(this.namespace == null) {
			this.namespace = parent.getNamespace();
		}
	}
	
	public static MoreCraftListener getInstance(RecipeCreator parent) {
		INSTANCE.setNamespace(parent);
		return INSTANCE;
	}
}
