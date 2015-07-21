package me.cheesepro.reality.commands;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.Messenger;
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

    CratesCommands cratesCommands;

    public CommandsManager(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
        cratesItems = plugin.getCratesItems();
        cratesLocations = plugin.getCratesLocations();
        cratesConfig = plugin.getCratesConfig();
        cratesCommands = new CratesCommands(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(cmd.getLabel().equalsIgnoreCase("reality")){
                if(args.length==2){
                    if(args[0].equalsIgnoreCase("setcrate")){
                        cratesCommands.commandSetCrate(p, args[1]);
                    }else if(args[0].equalsIgnoreCase("removecrate")){
                        cratesCommands.commandRemoveCrate(p, args[1]);
                    }
                }else if(args.length==1 && args[0].equalsIgnoreCase("crateslist")){
                    cratesCommands.commandCratesList(p);
                }else {
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
