package nl.gewoonhdgaming.anticurse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		console.sendMessage(ChatColor.GREEN + "Het AntiCurse systeem is uitgeschakeld!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("anticurse"))
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Alleen spelers kunnen dit commando uitvoeren!");
			return false;
		}
		Player p = (Player) sender;
		if(p.hasPermission("GHGA.Staff")) {
		if (args.length == 0) {
		      p.sendMessage(PlayerChatEvent.prefix + "Fout gebruik van commando. Moet zijn: /anticurse <command> [optional arguments]");
		      p.sendMessage(PlayerChatEvent.prefix + "Commands: ");
		      p.sendMessage(PlayerChatEvent.prefix + "Clear: Verwijder target van warn lvl's");
		  } else if(args.length >= 1) {
			  Player target = getServer().getPlayer(args[1]);
			  if(args[0].equalsIgnoreCase("clear")) {
					  if(target == null) {
						  p.sendMessage(PlayerChatEvent.prefix + "Target is niet online of bestaat niet");
					  } else {
						  if(PlayerChatEvent.lvl1.contains(target)) PlayerChatEvent.lvl1.remove(target);
						  if(PlayerChatEvent.lvl2.contains(target)) PlayerChatEvent.lvl2.remove(target);						  
					  }
			  }
		  }
	}
		return false;
}
}
