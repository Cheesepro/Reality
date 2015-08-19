package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-04-11.
 */
public class AbilityClimb implements Abilities, Listener{

    @Override
    public String getName() {
        return "CLIMB";
    }

    @Override
    public String getDesc() {
        return "Climb on any walls";
    }

    private Reality plugin;
    private Map<UUID, Map<String, String>> playersINFO;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Map<String, String> messages;
    private ArrayList<Material> noVineBlocks = new ArrayList<Material>();
    private Map<String, ArrayList<Block>> vineMap = new HashMap<String, ArrayList<Block>>();
    private Tools tools;

    public AbilityClimb(Reality plugin){
        this.plugin = plugin;
        abilitiesOptions = plugin.getAbilitiesOptions();
        messages = plugin.getMessages();
        playersINFO = plugin.getPlayersINFO();
        tools = new Tools(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        noVineBlocks.add(Material.THIN_GLASS);
        noVineBlocks.add(Material.WOOD_STAIRS);
        noVineBlocks.add(Material.JUNGLE_WOOD_STAIRS);
        noVineBlocks.add(Material.BIRCH_WOOD_STAIRS);
        noVineBlocks.add(Material.SPRUCE_WOOD_STAIRS);
        noVineBlocks.add(Material.COBBLESTONE_STAIRS);
        noVineBlocks.add(Material.BRICK_STAIRS);
        noVineBlocks.add(Material.WOOD_STAIRS);
        noVineBlocks.add(Material.SMOOTH_STAIRS);
        noVineBlocks.add(Material.NETHER_BRICK_STAIRS);
        noVineBlocks.add(Material.SANDSTONE_STAIRS);
        noVineBlocks.add(Material.FENCE);
        noVineBlocks.add(Material.FENCE_GATE);
        noVineBlocks.add(Material.NETHER_FENCE);
        noVineBlocks.add(Material.LADDER);
        noVineBlocks.add(Material.VINE);
        noVineBlocks.add(Material.BED);
        noVineBlocks.add(Material.BED_BLOCK);
        noVineBlocks.add(Material.IRON_FENCE);
        noVineBlocks.add(Material.SNOW);
        noVineBlocks.add(Material.SIGN);
        noVineBlocks.add(Material.LEVER);
        noVineBlocks.add(Material.TRAP_DOOR);
        noVineBlocks.add(Material.PISTON_EXTENSION);
        noVineBlocks.add(Material.PISTON_MOVING_PIECE);
        noVineBlocks.add(Material.TRIPWIRE_HOOK);
        noVineBlocks.add(Material.BOAT);
        noVineBlocks.add(Material.MINECART);
        noVineBlocks.add(Material.CAKE);
        noVineBlocks.add(Material.CAKE_BLOCK);
        noVineBlocks.add(Material.WATER);
        noVineBlocks.add(Material.STATIONARY_WATER);
        noVineBlocks.add(Material.LAVA);
        noVineBlocks.add(Material.STATIONARY_LAVA);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player p = event.getPlayer();
        if(tools.canUseAbility(playersINFO.get(p.getUniqueId()).get("rank"), getName())){
            if (p.getItemInHand().getType() == Material.getMaterial(abilitiesOptions.get(getName()).get("item")) && tools.isHoldingCorrectItem(p, abilitiesOptions.get(getName()).get("item"))) {
                BlockFace bf = yawToFace(p.getLocation().getYaw());
                Block block = p.getLocation().getBlock().getRelative(bf);
                if (block.getType() != Material.AIR) {
                    for (int i = 0; i < 300; i++) {
                        Block temp = block.getLocation().add(0.0D, i, 0.0D).getBlock();
                        Block opp = p.getLocation().add(0.0D, i, 0.0D).getBlock();
                        Block aboveOpp = opp.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();
                        int counter = 0;
                        for (int k = 0; k < noVineBlocks.size(); k++) {
                            if ((temp.getType() != Material.AIR) && (temp.getType() != noVineBlocks.get(k))) {
                                counter++;
                            }
                        }
                        if ((counter != noVineBlocks.size()) || ((opp.getType() != Material.AIR) && (opp.getType() != Material.LONG_GRASS) && (opp.getType() != Material.YELLOW_FLOWER) && (opp.getType() != Material.RED_ROSE))) {
                            break;
                        }
                        if (aboveOpp.getType() == Material.AIR) {
                            p.sendBlockChange(opp.getLocation(), Material.VINE, (byte) 0);
                            addVines(p, opp);
                        }
                        p.setFallDistance(0.0F);
                    }
                } else {
                    for (int i = 0; i < getVines(p).size(); i++) {
                        p.sendBlockChange((getVines(p).get(i)).getLocation(), Material.AIR, (byte) 0);
                    }
                    getVines(p).clear();
                }
            } else {
                for (int i = 0; i < getVines(p).size(); i++) {
                    p.sendBlockChange((getVines(p).get(i)).getLocation(), Material.AIR, (byte) 0);
                }
                getVines(p).clear();
            }
        }
    }


    public ArrayList<Block> getVines(Player player)
    {
        if (this.vineMap.containsKey(player.getName())) {
            return this.vineMap.get(player.getName());
        }
        ArrayList<Block> temp = new ArrayList<Block>();
        return temp;
    }

    public void setVines(Player player, ArrayList<Block> vines)
    {
        this.vineMap.put(player.getName(), vines);
    }

    public void addVines(Player player, Block vine)
    {
        ArrayList<Block> updated;
        updated = getVines(player);
        updated.add(vine);
        setVines(player, updated);
    }

    public BlockFace yawToFace(float yaw)
    {
        BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
        return axis[(java.lang.Math.round(yaw / 90.0F) & 0x3)];
    }

}
