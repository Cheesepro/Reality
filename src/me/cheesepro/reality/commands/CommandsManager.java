package me.cheesepro.reality.commands;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.PlayerManager;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2015-07-14.
 */
public class CommandsManager implements CommandExecutor{

    Reality plugin;
    Messenger msg;
    Tools tools;
    List<String> cratesItems;
    Map<String, Map<String, Integer>> cratesLocations;
    Config cratesConfig;
    PlayerManager pManager;

    CratesCommands cratesCommands;
    BossesCommands bossesCommands;

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
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(cmd.getLabel().equalsIgnoreCase("reality")){
                if(args.length==0){
                    if(pManager.hasDefaultPermission(p)){

                    }else if(pManager.hasAdminPermission(p)){

                    }
                }else if(args.length==1){
                    if(pManager.hasDefaultPermission(p)){
                        if(args[0].equalsIgnoreCase("crateslist")){
                            cratesCommands.commandCratesList(p);
                        }
                    }else{
                        msg.noPermission(p);
                    }
                }else if(args.length==2){
                    if(pManager.hasAdminPermission(p)){
                        if(args[0].equalsIgnoreCase("setcrate")){
                            cratesCommands.commandSetCrate(p, args[1]);
                        }else if(args[0].equalsIgnoreCase("removecrate")){
                            cratesCommands.commandRemoveCrate(p, args[1]);
                        }
                    }else{
                        msg.noPermission(p);
                    }
                }else if(args.length>=3){
                    if(args[0].equalsIgnoreCase("bossroom")){
                        if(pManager.hasDefaultPermission(p)){
                            if(args[1].equalsIgnoreCase("buy")){
                                bossesCommands.commandBuy(p, args[2]);
                            }
                        }
                        if(pManager.hasAdminPermission(p)){
                            if(args[1].equalsIgnoreCase("create")){
                                bossesCommands.commandCreate(p, args[2]);
                            }else if(args[2].equalsIgnoreCase("set")){
                                // Format: /reality bossroom bRoomName set [lobby/end/spawn] <bossName/amount/time>
                                if(args.length==4){
                                    bossesCommands.commandSet(p, args[1], args[3]);
                                }else if(args.length==5){
                                    bossesCommands.commandSet(p, args[1], args[3], args[4]);
                                }
                            }else if(args[1].equalsIgnoreCase("enable")){
                                bossesCommands.commandEnable(p, args[2]);
                            }else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete")){
                                bossesCommands.commandRemove(p, args[2]);
                            }
                        }else{
                            msg.noPermission(p);
                        }
                    }
                }else{
                    msg.send(p, "a", "Type /reality for help");
                }
            }
        }else{
            if(cmd.getLabel().equalsIgnoreCase("reality")){

            }
        }
        return false;
    }


}
