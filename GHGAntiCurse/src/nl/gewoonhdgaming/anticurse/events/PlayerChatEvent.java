package nl.gewoonhdgaming.anticurse.events;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;
import nl.gewoonhdgaming.anticurse.Main;

public class PlayerChatEvent implements Listener {
	
	public static String prefix = ChatColor.AQUA + "GHG" + ChatColor.RED + "Anticurse " + ChatColor.WHITE; 
	
	public static ArrayList<Player> lvl1 = new ArrayList<Player>();
	public static ArrayList<Player> lvl2= new ArrayList<Player>();
	
	private Main plugin;
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		for(String word : e.getMessage().split(" ")) {
			if(plugin.getConfig().getStringList("Scheldwoorden").contains(word)) {
				e.setCancelled(true);				
				if(!(lvl1.contains(e.getPlayer()) && !(lvl2.contains(e.getPlayer())))) {
					lvl1.add(e.getPlayer());
                    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"Schelden is niet toegestaan!\"}"), 20, 40, 20);
                    PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"Je hebt nu 1 waarschuwing!\"}"), 20, 40, 20);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(title);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(subtitle);
                    e.getPlayer().sendMessage(prefix + "Schelden is niet toegestaan! Je hebt nu 1 waarschuwing!");
                    return;
				}
				if(lvl1.contains(e.getPlayer())) {
					lvl1.remove(e.getPlayer());
					lvl2.add(e.getPlayer());
					PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"Schelden is niet toegestaan!\"}"), 20, 40, 20);
                    PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"Je hebt nu 2 waarschuwingen!\"}"), 20, 40, 20);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(title);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(subtitle);
                    e.getPlayer().sendMessage(prefix + "Schelden is niet toegestaan! Je hebt nu 2 waarschuwingen! Scheld je nog 1 keer wordt je gekickt!");
					return;
				} if(lvl2.contains(e.getPlayer())) {
					lvl2.remove(e.getPlayer());
					e.getPlayer().kickPlayer(ChatColor.RED + "Schelden is niet toegestaan!");
				}
			}
		}
	}

}
