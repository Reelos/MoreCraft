package de.reelos.recipecreator.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MoreCraftListener implements Listener {
	private static final MoreCraftListener INSTANCE = new MoreCraftListener();
	
	private MoreCraftListener() {
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage("You feel like you are able to create more things.");
	}
	
	public static MoreCraftListener getInstance() {
		return INSTANCE;
	}
}
