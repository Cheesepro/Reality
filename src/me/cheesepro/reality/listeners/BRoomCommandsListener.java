package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BRoomIdle;
import me.cheesepro.reality.bossrooms.rooms.BRoom;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Mark on 2015-07-28.
 */
public class BRoomCommandsListener implements Listener{

    private Reality plugin = Reality.getPlugin();
    private DataManager dataManager;
    private Messenger msg;
    //Format: Map<Invited Player, the inviter>;
    private Map<UUID, UUID> invitedPlayer = new HashMap<UUID, UUID>();
    private Map<UUID, Integer> timeoutCount = new HashMap<UUID, Integer>();
    private boolean isRepeatingTaskRunning = false;
    private BRoomManager bRoomManager;
    private BRoomIdle bRoomIdle;

    public BRoomCommandsListener(Reality plugin){
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        msg = new Messenger(plugin);
        bRoomManager = new BRoomManager(plugin);
        bRoomIdle = new BRoomIdle(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
            String[] args = e.getMessage().split(" ");
            String command = args[0];
            UUID id = p.getUniqueId();
            BRoom bRoom = bRoomManager.getBRoom(p);
            if(command.equalsIgnoreCase("/start")) {
                bRoomIdle.setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
                if (dataManager.getBRoomPlayersRole(id)) {
                    if(bRoomManager.getBRoom(e.getPlayer()).getState()==BRoom.BRoomState.LOBBY){
                        if (bRoom.getCurrentPlayers() >= bRoom.getMinPlayer()) {
                            bRoom.start();
                        } else {
                            msg.send(p, "d", "Sorry, the game cannot start because there are not enough players. Players required for this room: " + bRoom.getCurrentPlayers());
                        }
                    }else{
                        msg.send(p, "c", "You can only start the game when the room is in LOBBY state!");
                    }
                } else {
                    msg.send(p, "e", "Sorry, only the host of the game can access the command /start");
                }
            }else if(command.equalsIgnoreCase("/quit")){
                bRoomIdle.setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
                bRoom.removePlayer(p);
            }else if(command.equalsIgnoreCase("/invite")){
                bRoomIdle.setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
                if(args.length!=2){
                    msg.send(p, "e", "Player provide a valid player's name /invite <player>");
                    return;
                }
                if(dataManager.getBRoomPlayersRole(id)){
                    if(bRoom.getState()==BRoom.BRoomState.LOBBY) {
                        if (bRoom.getCurrentPlayers() <= bRoom.getMaxPlayer()) {
                            if (Bukkit.getPlayer(args[1]).isOnline()) {
                                if (!invitedPlayer.containsKey(Bukkit.getPlayer(args[1]).getUniqueId())) {
                                    if(!dataManager.getInGamePlayersList().contains(Bukkit.getPlayer(args[1]).getUniqueId())){
                                        invitedPlayer.put(Bukkit.getPlayer(args[1]).getUniqueId(), p.getUniqueId());
                                        msg.send(p, "a", "Invitation was sent successfully to player " + args[1] + "!");
                                        msg.send(Bukkit.getPlayer(args[1]), "e", "You are invited to be in the boss room with " + p.getName() + ", do you accept? This invitation will expire in 1 minute! Type /accept to accept the invitation, type /deny to deny.");
                                        addCoolDownCountDown(Bukkit.getPlayer(args[1]).getUniqueId());
                                    }else{
                                        msg.send(p, "d", "Sorry, player " + args[1] + " is already in a boss room!");
                                    }
                                } else {
                                    msg.send(p, "d", args[1] + " is already invited by someone.");
                                }
                            } else {
                                msg.send(p, "d", "Sorry, player " + args[1] + " is not online!");
                            }
                        } else {
                            msg.send(p, "d", "Sorry, the room has exceeded its maximum player(s) limit! Max players allowed: " + bRoom.getMaxPlayer());
                        }
                    }else{
                        msg.send(p, "d", "Invitation can only be sent when the room is in Lobby state!");
                    }
                }else{
                    msg.send(p, "e", "Sorry, only the host of the game can access the command /invite");
                }
            }else{
                msg.send(p, "c", "You are not allowed to use any non boss room related commands!");
            }
        } else{
            if(invitedPlayer.keySet().contains(p.getUniqueId())) {
                String command = e.getMessage();
                if(command.equalsIgnoreCase("/accept")){
                    e.setCancelled(true);
                    BRoom bRoom = bRoomManager.getBRoom(dataManager.getBRoomPlayersRoom(invitedPlayer.get(p.getUniqueId())));
                    if(bRoom.getState() == BRoom.BRoomState.LOBBY){
                        bRoom.addPlayer(p);
                        msg.send(p, "d", "You can only access to the command /quit right now.");
                    }else{
                        msg.send(p, "c", "Sorry but the boss room that you were invited to is currently not accepting new players to join.");
                    }
                    invitedPlayer.remove(p.getUniqueId());
                }else if(command.equalsIgnoreCase("/deny")){
                    e.setCancelled(true);
                    msg.send(p, "e", "Successfully denied the invitation");
                    msg.send(Bukkit.getPlayer(invitedPlayer.get(p.getUniqueId())), "c", p.getName() + " have denied your invitation.");
                    invitedPlayer.remove(p.getUniqueId());
                }
            }
        }
    }

    private void addCoolDownCountDown(final UUID id){
        timeoutCount.put(id, 60);
        if(!isRepeatingTaskRunning){
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    isRepeatingTaskRunning=true;
                    if(invitedPlayer.containsKey(id) && timeoutCount.get(id)==0){
                        invitedPlayer.remove(id);
                    }
                    if(invitedPlayer.isEmpty() || invitedPlayer.toString().equalsIgnoreCase("{}"))
                    {
                        isRepeatingTaskRunning=false;
                        cancel();
                    }
                    for(UUID uuid : timeoutCount.keySet()){
                        timeoutCount.put(uuid, timeoutCount.get(uuid)-1);
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
            //20 ticks = 1 sec;
        }
    }
}
