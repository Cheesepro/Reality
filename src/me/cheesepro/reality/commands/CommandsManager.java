package me.cheesepro.reality.commands;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-07-14.
 */
public class CommandsManager implements CommandExecutor{

    private Reality plugin;
    private Messenger msg;
    private Tools tools;
    private List<String> cratesItems;
    private Map<String, Map<String, Integer>> cratesLocations;
    private Config cratesConfig;
    private Map<UUID, Map<String, String>> playersINFO;
    private PlayerManager pManager;
    private RankManager rankManager;

    private CratesCommands cratesCommands;
    private BossesCommands bossesCommands;

    public CommandsManager(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
        pManager = new PlayerManager(plugin);
        cratesItems = plugin.getCratesItems();
        cratesLocations = plugin.getCratesLocations();
        cratesConfig = plugin.getCratesConfig();

        cratesCommands = new CratesCommands(plugin);
        bossesCommands = new BossesCommands(plugin);

        playersINFO = plugin.getPlayersINFO();
        rankManager = new RankManager(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(cmd.getLabel().equalsIgnoreCase("bossroom") || cmd.getLabel().equalsIgnoreCase("room")){
                if(args.length==0){
                    bossesCommands.commandBuy(p, "menu");
                }else if(args.length==1){
                    bossesCommands.commandBuy(p, args[0]);
                }
            }
            if(cmd.getLabel().equalsIgnoreCase("reality")){
                if(args.length==0){
                    if(pManager.hasAdminPermission(p)){
                        msg.help(p, new ArrayList<String>(){{
                            add("/reality" + ChatColor.WHITE + " -Shows this page.");
                            add("/reality info" + ChatColor.WHITE + " -Shows the information about the plugin.");
                            add("/reality crateslist" + ChatColor.WHITE + " -Shows a list of crates information.");
                            add("/reality setcrate" + ChatColor.WHITE + " -Set a crate to your current location.");
                            add("/reality removecrate [name]" + ChatColor.WHITE + " -Remove a crate.");
                            add("/reality bossroom [create/remove/enable] [name]" + ChatColor.WHITE + " -Boss room administration command");
                            add("/reality bossroom [name] set [lobby/end/spawn] [bossName/amount/time]" + ChatColor.WHITE + " -Boss room moderation command");
                            add("/bossroom [room]" + ChatColor.WHITE + " -Boss room purchase command.");
                        }});
                    }else{
                        msg.help(p, new ArrayList<String>(){{
                            add("/reality" + ChatColor.WHITE + " -Shows this page.");
                            add("/reality info" + ChatColor.WHITE + " -Shows the information about the plugin.");
                            add("/bossroom [menu/room]" + ChatColor.WHITE + " -Boss room purchase command.");
                        }});
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("info")){
                    msg.empty(p, 6);
                    msg.board(p, "INFO", new ArrayList<String>() {{
                        add("");
                        add("Creator: " + ChatColor.GOLD + "MarkCP");
                        add("Site: " + ChatColor.AQUA.toString() + "markcp.me/reality");
                        add("Version: " + ChatColor.GREEN + plugin.getDescription().getVersion());
                        add("");
                    }});
                    return true;
                }else if(args[0].equalsIgnoreCase("assignRandomRank")){
                    Map<String, String> cache = playersINFO.get(p.getUniqueId());
                    if(cache.get("rank")!=null){
                        msg.send(p, "c", "You already have a rank, therefore you can not be assigned to another rank.");
                    }else{
                        rankManager.giveRank(p);
                    }
                    return true;
                }
                if(pManager.hasAdminPermission(p)){
                    if(args[0].equalsIgnoreCase("crateslist")){
                        if(args.length!=1){return false;}
                        cratesCommands.commandCratesList(p);
                        return true;
                    }else if(args[0].equalsIgnoreCase("setcrate")){
                        if (args.length!=2){return false;}
                        cratesCommands.commandSetCrate(p, args[1]);
                        return true;
                    }else if(args[0].equalsIgnoreCase("removecrate")){
                        if(args.length!=2){return false;}
                        cratesCommands.commandRemoveCrate(p, args[1]);
                        return true;
                    }else if(args[0].equalsIgnoreCase("bossroom")){
                        if(args.length==3){
                            if(args[1].equalsIgnoreCase("create")){
                                bossesCommands.commandCreate(p, args[2]);
                            }else if(args[1].equalsIgnoreCase("enable")){
                                bossesCommands.commandEnable(p, args[2]);
                            }else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete")){
                                bossesCommands.commandRemove(p, args[2]);
                            }
                        }else if(args.length==4 || args.length==5){
                            if(args[2].equalsIgnoreCase("set")){
                                // Format: /reality bossroom bRoomName set [lobby/end/spawn] <bossName/amount/time>
                                if(args.length==4){
                                    bossesCommands.commandSet(p, args[1], args[3]);
                                }else{
                                    bossesCommands.commandSet(p, args[1], args[3], args[4]);
                                }
                            }
                        }
                    }else{
                        msg.send(p, "a", "Type " + ChatColor.YELLOW + "/reality" + ChatColor.GREEN + " for help");
                    }
                }
            }
        }
        return false;
    }


}
