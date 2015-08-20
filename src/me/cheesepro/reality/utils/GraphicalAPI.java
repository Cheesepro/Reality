package me.cheesepro.reality.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;


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

    public static boolean unrankedSidebarDisplay(Player p, String[] elements) {
        return unrankedSidebarDisplay((Collection) Arrays.asList(new Player[]{p}), elements);
    }

    public static boolean unrankedSidebarDisplay(Collection<Player> players, String[] elements) {
        if(elements.length > 16) {
            return false;
        } else {
            if(elements[0] == null) {
                elements[0] = "Unamed board";
            }

            if(elements[0].length() > 32) {
                elements[0] = elements[0].substring(0, 40);
            }

            for(int e = 1; e < elements.length; ++e) {
                if(elements[e] != null && elements[e].length() > 40) {
                    elements[e] = elements[e].substring(0, 40);
                }
            }

            try {
                Iterator var3 = players.iterator();

                while(var3.hasNext()) {
                    Player var12 = (Player)var3.next();
                    if(var12.getScoreboard() == null || var12.getScoreboard().getObjective(var12.getUniqueId().toString().substring(0, 16)) == null) {
                        var12.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                        var12.getScoreboard().registerNewObjective(var12.getUniqueId().toString().substring(0, 16), "dummy");
                        var12.getScoreboard().getObjective(var12.getUniqueId().toString().substring(0, 16)).setDisplaySlot(DisplaySlot.SIDEBAR);
                    }

                    var12.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(elements[0]);

                    for(int entry = 1; entry < elements.length; ++entry) {
                        if(elements[entry] != null && var12.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[entry]).getScore() != 16 - entry) {
                            var12.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[entry]).setScore(16 - entry);
                            Iterator toErase = var12.getScoreboard().getEntries().iterator();

                            while(toErase.hasNext()) {
                                String string = (String)toErase.next();
                                if(var12.getScoreboard().getObjective(var12.getUniqueId().toString().substring(0, 16)).getScore(string).getScore() == 16 - entry && string != elements[entry]) {
                                    var12.getScoreboard().resetScores(string);
                                }
                            }
                        }
                    }

                    Iterator var14 = var12.getScoreboard().getEntries().iterator();

                    while(var14.hasNext()) {
                        String var13 = (String)var14.next();
                        boolean var15 = true;
                        String[] var10 = elements;
                        int var9 = elements.length;

                        for(int var8 = 0; var8 < var9; ++var8) {
                            String element = var10[var8];
                            if(element != null && element.equals(var13) && var12.getScoreboard().getObjective(var12.getUniqueId().toString().substring(0, 16)).getScore(var13).getScore() == 16 - Arrays.asList(elements).indexOf(element)) {
                                var15 = false;
                                break;
                            }
                        }

                        if(var15) {
                            var12.getScoreboard().resetScores(var13);
                        }
                    }
                }

                return true;
            } catch (Exception var11) {
                return false;
            }
        }
    }

    public static boolean rankedSidebarDisplay(Player p, String title, HashMap<String, Integer> elements) {
        return rankedSidebarDisplay((Collection)Arrays.asList(new Player[]{p}), title, elements);
    }

    public static boolean rankedSidebarDisplay(Collection<Player> players, String title, HashMap<String, Integer> elements) {
        if(title == null) {
            title = "Unamed board";
        }

        if(title.length() > 32) {
            title = title.substring(0, 32);
        }

        String e;
        String string;
        Iterator var6;
        label88:
        for(; elements.size() > 15; elements.remove(e)) {
            e = (String)elements.keySet().toArray()[0];
            int minimum = elements.get(e);
            var6 = elements.keySet().iterator();

            while(true) {
                do {
                    if(!var6.hasNext()) {
                        continue label88;
                    }

                    string = (String)var6.next();
                } while(elements.get(string) >= minimum && (elements.get(string) != minimum || string.compareTo(e) >= 0));

                e = string;
                minimum = elements.get(string);
            }
        }

        for (Object o : (new ArrayList(elements.keySet()))) {
            e = (String) o;
            if (e != null && e.length() > 40) {
                int string1 = elements.get(e);
                elements.remove(e);
                elements.put(e.substring(0, 40), string1);
            }
        }

        try {

            for (Player e1 : players) {
                if (e1.getScoreboard() == null || e1.getScoreboard().getObjective(e1.getUniqueId().toString().substring(0, 16)) == null) {
                    e1.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    e1.getScoreboard().registerNewObjective(e1.getUniqueId().toString().substring(0, 16), "dummy");
                    e1.getScoreboard().getObjective(e1.getUniqueId().toString().substring(0, 16)).setDisplaySlot(DisplaySlot.SIDEBAR);
                }

                e1.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(title);
                var6 = elements.keySet().iterator();

                while (var6.hasNext()) {
                    string = (String) var6.next();
                    if (e1.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(string).getScore() != elements.get(string)) {
                        e1.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(string).setScore(elements.get(string));
                    }
                }

                var6 = new ArrayList(e1.getScoreboard().getEntries()).iterator();

                while (var6.hasNext()) {
                    string = (String) var6.next();
                    if (!elements.keySet().contains(string)) {
                        e1.getScoreboard().resetScores(string);
                    }
                }
            }

            return true;
        } catch (Exception var7) {
            return false;
        }
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
