package me.cheesepro.reality.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Packet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;


/**
 * Created by Mark on 2015-06-07.
 */
public class GraphicalAPI {

    public static void sendTitleToPlayer(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
        con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn*20, stay*20, fadeOut*20));

        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}")));
        }

        if (title != null) {
            title = ChatColor.translateAlternateColorCodes('&', title);
            con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}")));
        }
    }

    public static void sendHotBarText(String text, Player[] player){
        String json = "{text:\""+text+"\"}";
        sendRaw(json, player);
    }

    public static void sendHotBarText(String text, Player player){
        sendRaw(text, player);
    }

    public static void sendRaw(String json, Player[] player){
        PacketPlayOutChat chat = new PacketPlayOutChat(new ChatComponentText(json), (byte)2);
        for(Player p : player)sendPacket(chat, p);
    }
    public static void sendRaw(String text, Player player){
        PacketPlayOutChat chat = new PacketPlayOutChat(new ChatComponentText(text), (byte)2);
        sendPacket(chat, player);
    }


    private static void sendPacket(Packet p, Player p1){
        ((CraftPlayer)p1).getHandle().playerConnection.sendPacket(p);
    }

    public static void createScoreboard(Player p, String title, String[] text){
        //Create new Scoreboard

        ScoreboardManager mgr = Bukkit.getScoreboardManager();
        Scoreboard s = mgr.getNewScoreboard();

        //Create objective


        Objective obj = s.registerNewObjective("sb", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(title);


        //Add lines

        int value = text.length;
        for(String x : text){
            String pre = "";
            for(char c : (value+"").toCharArray()){
                pre+="¡ì"+c;
            }
            pre+="¡ìf";
            x=pre+x;
            if(x.length()<=16){
                obj.getScore(x).setScore(value);
            }else{
                Team team = s.registerNewTeam("line"+value);
                String prefix = x.substring(0, 16);
                String name = x.substring(16, x.length());
                String suffix = "";
                if(name.length()>16){
                    name = name.substring(0, 16);
                    suffix = x.substring(32, x.length());
                    if(suffix.length()>16)suffix = suffix.substring(0, 16);
                }
                team.setPrefix(prefix);
                team.setSuffix(suffix);
                OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                team.addPlayer(op);
                obj.getScore(op).setScore(value);
            }


            value--;
        }

        //Set the Scoreboard for the Player

        p.setScoreboard(s);
    }

    public static void setCustomName(Player p, String prefix, String suffix){
        //TODO add custom names
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Team team = board.registerNewTeam("LOL");
        team.addEntry("MarkCP");
        team.setPrefix(ChatColor.GREEN + "[Lv.99] " + ChatColor.RESET);

        Bukkit.getPlayer("JinCongIsFai").setScoreboard(board);
    }

}
