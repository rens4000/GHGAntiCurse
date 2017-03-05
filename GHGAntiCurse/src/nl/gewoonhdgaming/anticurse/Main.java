package nl.gewoonhdgaming.anticurse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import nl.gewoonhdgaming.anticurse.events.PlayerChatEvent;

public class Main extends JavaPlugin {

	public void onEnable() {
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		
		console.sendMessage(ChatColor.GREEN + "Het AntiCurse systeem wordt geladen!");
		getConfig().options().copyDefaults(true);
		saveConfig();
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
		console.sendMessage(ChatColor.AQUA + "Het AntiCurse systeem is succesvol geladen!");
	}
	
	public void onDisable() {
	
	}
}
