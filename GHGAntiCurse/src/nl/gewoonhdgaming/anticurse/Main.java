package nl.gewoonhdgaming.anticurse;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;

public class Main extends JavaPlugin implements Listener {
	
	public ArrayList<Player> lvl1 = new ArrayList<Player>();
	public ArrayList<Player> lvl2 = new ArrayList<Player>();
	public String prefix = ChatColor.AQUA + "GHG" + ChatColor.RED + "AntiCurse > > " + ChatColor.WHITE;

	public void onEnable() {
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		
		console.sendMessage(ChatColor.GREEN + "Het AntiCurse systeem wordt geladen!");
		getConfig().options().copyDefaults(true);
		saveConfig();
		getServer().getPluginManager().registerEvents(this, this);
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
		      p.sendMessage(prefix + "Fout gebruik van commando. Moet zijn: /anticurse <command> [optional arguments]");
		      p.sendMessage(prefix + "Commands: ");
		      p.sendMessage(prefix + "Clear: Verwijder target van warn lvl's");
		  } else if(args.length >= 1) {
			  Player target = getServer().getPlayer(args[1]);
			  if(args[0].equalsIgnoreCase("clear")) {
				  
				  if(args[1] == null) {
					  p.sendMessage(prefix + "FOUT GEBRUIK! Moet zijn: /anticurse clear <speler>");
					  return false;
				  }
					  if(target == null) {
						  p.sendMessage(prefix + "Target is niet online of bestaat niet");
						  return false;
					  } else {
						  if(lvl1.contains(target)) lvl1.remove(target);
						  if(lvl2.contains(target)) lvl2.remove(target);						  
					  }
			  }
		  }
	}
		return false;

}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		for (String word : e.getMessage().split(" ")) {
			if (getConfig().getStringList("badwords").contains(word)) {
				e.setCancelled(true);				
				if((!lvl1.contains(e.getPlayer()) && (!lvl2.contains(e.getPlayer())))) {
					getLogger().info("Zet naar lvl 1");
					lvl1.add(e.getPlayer());
                    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"Schelden is niet toegestaan!\"}"), 20, 40, 20);
                    PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"Je hebt nu 1 waarschuwing!\"}"), 20, 40, 20);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(title);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(subtitle);
                    e.getPlayer().sendMessage(prefix + "Schelden is niet toegestaan! Je hebt nu 1 waarschuwing!");
				} else
				if(lvl1.contains(e.getPlayer())) {
					getLogger().info("Zet naar lvl 2");
					lvl1.remove(e.getPlayer());
					lvl2.add(e.getPlayer());
					PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"Schelden is niet toegestaan!\"}"), 20, 40, 20);
                    PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"Je hebt nu 2 waarschuwingen!\"}"), 20, 40, 20);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(title);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(subtitle);
                    e.getPlayer().sendMessage(prefix + "Schelden is niet toegestaan! Je hebt nu 2 waarschuwingen! Scheld je nog 1 keer wordt je gekickt!");
				} else
				if(lvl2.contains(e.getPlayer())) {
					getLogger().info("Zet naar lvl 3");
					e.getPlayer().kickPlayer(ChatColor.RED + "Schelden is niet toegestaan!");
					lvl2.remove(e.getPlayer());
				}
			}
		}
	}
}
