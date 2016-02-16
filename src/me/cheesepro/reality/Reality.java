package me.cheesepro.reality;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.cheesepro.reality.abilities.*;
import me.cheesepro.reality.bossrooms.*;
import me.cheesepro.reality.bossrooms.bosses.*;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.commands.CommandsManager;
import me.cheesepro.reality.level.LevelLimiter;
import me.cheesepro.reality.level.XPGainListener;
import me.cheesepro.reality.listeners.*;
import me.cheesepro.reality.luckycrates.KeysGiver;
import me.cheesepro.reality.luckycrates.LuckyCrates;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.ConfigManager;
import me.cheesepro.reality.utils.Logger;
import me.cheesepro.reality.utils.MVdWPlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;


/**
 * Created by Mark on 2015-04-02.
 */
public class Reality extends JavaPlugin implements Listener{

    /*
     * The map that stores all the settings of the plugin.
     * Format: Map(Setting, List(value))
     */
    private Map<String, List<String>> settings = new HashMap<String, List<String>>();

    /*
     * This map stores name information about ranks
     */
    private Map<String, String> ranksName = new HashMap<String, String>();

    /*
     * This map stores health information about ranks
     */
    private Map<String, Double> ranksHealth = new HashMap<String, Double>();

    /*
     * This map stores starting-kits information about ranks
     */
    private Map<String, List<String>> ranksKit = new HashMap<String, List<String>>();

    /*
     * This map stores what ability(ies) ranks have
     */
    private Map<String, List<String>> ranksAbilities = new HashMap<String, List<String>>();

    /*
     * This map stores how much XP are each level needed.
     * Format: Map(Level, XP required)
     */
    private Map<String, Integer> levels = new HashMap<String, Integer>();

    /*
     * This map stores all the items that each level are allowed.
     * Format: Map(Level, NavigableSet(AllowedItems))
     */
    private Map<String, NavigableSet<String>> levelLimits = new HashMap<String, NavigableSet<String>>();

    /*
     * This set contains all the blocked items.
     */
    private NavigableSet<String> blockedItems = new TreeSet<String>();

    /*
     * This set contains all the levels
     */
    private NavigableSet<Integer> levelsSet = new TreeSet<Integer>();

    /*
     * This map stores the information of every abilities
     * Format: Map(AbilityName, Map(item/item_name/cooldown, value))
     */
    private Map<String, Map<String, String>> abilitiesOptions = new HashMap<String, Map<String, String>>();

    /*
     * This map stores all the configurable messages.
     */
    private Map<String, String> messages = new HashMap<String, String>();

    /*
     * This map stores every player's information
     * Format: Map(UUID of the player, Map(rank/level/xp, value))
     */
    private Map<UUID, Map<String, String>> playersINFO = new HashMap<UUID, Map<String, String>>();

    /*
     * This array contains all the Abilities.
     */
    private Abilities abilities[] = new Abilities[10];

    /*
     * This is the list that contains all the prizes in the crates
     */
    private List<String> cratesItems = new ArrayList<String>();

    /*
     * This map contains location information about crates
     * Format: Map(Name, Map(x/y/z, value))
     */
    private Map<String, Map<String, Integer>> cratesLocations = new HashMap<String, Map<String, Integer>>();

    /*
     * Stores the world where all the crates are allowed to be create.
     */
    private String cratesWorld = null;

    /*
     * Stores the world where all the crates are allowed to be create.
     */
    private ItemStack crateKey = null;

    /*
     * This map stores what boss is in what room
     * Format: Map<RoomName, BossName>
     */
    private Map<String, String> bRoomsBosses = new HashMap<String,String>();

    /*
     * This map stores where the bossrooms located
     * Format: Map<RoomName, Map<x/y/z, value>>
     */
    private Map<String, Map<String, Double>> bRoomsBossesLocations = new HashMap<String, Map<String, Double>>();

    /*
     * Stores lobby,spawn,end and spectate locations of boss rooms
     * Format: Map<BossRoomName, Map<lobby/spawn/end/spectate, Map<x/y/z, value>>>
     */
    private Map<String, Map<String, Map<String, Double>>> bRoomsLocations = new HashMap<String, Map<String, Map<String, Double>>>();

    /*
     * Stores boss rooms maxplayer/minplayer/no action time out time
     * Format: Map<BossRoomName, Map<maxplayer/minplayer/no action time out time, value>>
     */
    private Map<String, Map<String, String>> bRoomsSettings = new HashMap<String, Map<String, String>>();

    /*
     * Stores the world where all the bossrooms will be allowed.
     */
    private String bossesWorld = null;

    /*
     * Stores all the bossTypes
     */
    private Set<String> bossesTypes = new HashSet<String>();

    /*
     * Stores either the boss room is enabled or not.
     */
    private Map<String, Boolean> bRoomsEnabled = new HashMap<String, Boolean>();

    /*
     * Stores all the players who are currently playing with their role.
     * true == the host
     * false == who are invited
     */
    private Map<UUID, Boolean> bRoomPlayersRole = new HashMap<UUID, Boolean>();

    /*
     * Stores which player is in what room
     */
    private Map<UUID, String> bRoomPlayersRoom = new HashMap<UUID, String>();

    /*
     * Stores how many times the bosses were killed per game round;
     */
    private Map<String, Integer> bRoomWinCount = new HashMap<String, Integer>();

    /*
     * Contains bosses array
     */
    private Bosses[] bosses = new Bosses[9];

    /*
     * Boss room idle manager register
     */
    private BRoomIdle bRoomIdle = new BRoomIdle(this);

    /*
     * Stores the login location for players who exit minecraft while in a boss room.
     * Format <Player, the room that the player quit>
     */
    private Map<UUID, String> quitBRoom = new HashMap<UUID, String>();

    private Logger logger = new Logger();

    private ConfigManager configManager;
    private Config ranksConfig;
    private Config levelsConfig;
    private Config msgConfig;
    private Config storageConfig;
    private Config abilitiesConfig;
    private Config cratesConfig;
    private Config bossRoomsConfig;
    private World world;
    private static Economy economy = null;
    public static String pName = "[Reality]";
    private static Reality plugin;

    private BRoomManager bRoomManager;

    @Override
    public void onEnable(){
        plugin = this;
        try{
            bRoomManager = new BRoomManager(this);
            loadConfig();
            saveDefaultConfig();
            registerBosses();
            cache();
            registerAbilities();
            setupEconomy();
            registerCommands();
            registerListeners();
            if(getWorldEdit()==null){
                logger.warn("WorldEdit dependency not found!");
            }
            if(getWorldGuard()==null){
                logger.warn("WorldGuard dependency not found!");
            }
            logger.info("Successfully Enabled");
            MVdWPlaceholderAPI.updateAllPlaceHolders();
        }catch (Exception e){
            logger.error(e);
        }
    }

    @Override
    public void onDisable(){
        try{
            HandlerList.unregisterAll((Plugin) plugin);
            Bukkit.getScheduler().cancelTasks(plugin);
            logger.info("Successfully Disabled");
        }catch (Exception e){
            logger.error(e);
        }
    }

    public boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    private void registerCommands(){
        CommandsManager commandsManager = new CommandsManager(this);
        getCommand("reality").setExecutor(commandsManager);
        getCommand("bossroom").setExecutor(commandsManager);
        getCommand("assignRandomRank").setExecutor(commandsManager);
        getCommand("room").setExecutor(commandsManager);
    }

    private void registerListeners(){
        new JoinListener(this);
        new RespawnListener(this);
        new XPGainListener(this);
        new LevelLimiter(this);
        new ExplosionCanceler(this);
        new PlacingPreventer(this);
        new KeysGiver(this);
        new LuckyCrates(this);
        new CratesBreakingPreventer(this);
        new BRoomBossesDieListener(this);
        new BRoomCommandsListener(this);
        new PlayerDeathListener(this);
        new PlayerQuitListener(this);
        new BRoomUpdateListener();
        new BRoomShopInvListener(this);
        new BRoomIdleListeners(this);
        new EntityDamageListener(this);
        new PlayerInteractListener(this);
        new CraftPreventer(this);
    }

    private void registerAbilities(){
        abilities[0] = new AbilityExplode(this);
        abilities[1] = new AbilityDoubleJump(this);
        abilities[2] = new AbilityTeleport(this);
        abilities[3] = new AbilityClimb(this);
        abilities[4] = new AbilityFireball(this);
        abilities[5] = new AbilityWitherskull(this);
        abilities[6] = new AbilityCobweb(this);
        abilities[7] = new AbilityLightning(this);
        abilities[8] = new AbilityFeed(this);
        abilities[9] = new AbilityHyperJump(this);
    }

    private void registerBosses(){
        bosses[0] = new BossBlaze(this);
        bosses[1] = new BossChicken(this);
        bosses[2] = new BossCow(this);
        bosses[3] = new BossCreeper(this);
        bosses[4] = new BossEnderman(this);
        bosses[5] = new BossPig(this);
        bosses[6] = new BossSkeleton(this);
        bosses[7] = new BossSpider(this);
        bosses[8] = new BossZombie(this);
    }

    private void loadConfig() throws IOException{
        configManager = new ConfigManager(this);
        ranksConfig = configManager.getNewConfig("ranks.yml", new String[]{"\"Reality\" Ranks Configurations"});
        msgConfig = configManager.getNewConfig("messages.yml", new String[]{"I\"Reality\" Messages Configurations"});
        storageConfig = configManager.getNewConfig("storage.yml", new String[]{"\"Reality\" Data Storage"});
        configManager.copyDefaultConfig("abilities.yml");
        abilitiesConfig = configManager.getNewConfig("abilities.yml", new String[]{"\"Reality\" Abilities Configurations"});
        configManager.copyDefaultConfig("levels.yml");
        levelsConfig = configManager.getNewConfig("levels.yml");
        configManager.copyDefaultConfig("luckycrates.yml");
        cratesConfig = configManager.getNewConfig("luckycrates.yml");
        configManager.copyDefaultConfig("bossrooms.yml");
        bossRoomsConfig = configManager.getNewConfig("bossrooms.yml");
    }

    private void cache(){
        if(getConfig().getString("RespawnLocation")!=null){
            List<String> cords = new ArrayList<String>();
            cords.add(getConfig().getString("RespawnLocation.world"));
            cords.add(getConfig().getString("RespawnLocation.x"));
            cords.add(getConfig().getString("RespawnLocation.y"));
            cords.add(getConfig().getString("RespawnLocation.z"));
            cords.add(getConfig().getString("RespawnLocation.yaw"));
            cords.add(getConfig().getString("RespawnLocation.pitch"));
            settings.put("respawnlocation", cords);
        }else{
            world = getServer().getWorld("world");
            getConfig().set("RespawnLocation.world", world.getName());
            getConfig().set("RespawnLocation.x", world.getSpawnLocation().getX());
            getConfig().set("RespawnLocation.y", world.getSpawnLocation().getY());
            getConfig().set("RespawnLocation.z", world.getSpawnLocation().getZ());
            getConfig().set("RespawnLocation.pitch", world.getSpawnLocation().getPitch());
            getConfig().set("RespawnLocation.yaw", world.getSpawnLocation().getYaw());
            saveConfig();
            List<String> cords = new ArrayList<String>();
            cords.add(getConfig().getString("RespawnLocation.world"));
            cords.add(getConfig().getString("RespawnLocation.x"));
            cords.add(getConfig().getString("RespawnLocation.y"));
            cords.add(getConfig().getString("RespawnLocation.z"));
            cords.add(getConfig().getString("RespawnLocation.pitch"));
            cords.add(getConfig().getString("RespawnLocati on.yaw"));
            settings.put("respawnlocation", cords);
        }
        if(getConfig().getString("Default_allowed_ranks")!=null){
            settings.put("allowed_ranks", getConfig().getStringList("Default_allowed_ranks"));
        }
        if(msgConfig.get("messages")!=null){
            for(String cache : msgConfig.getConfigurationSection("messages").getKeys(false)){
                messages.put(cache, ChatColor.translateAlternateColorCodes('&', msgConfig.getString("messages." + cache)));
            }
        }
        if(ranksConfig.get("ranks")!=null){
            for(String rank : ranksConfig.getConfigurationSection("ranks").getKeys(false)){
                if(ranksConfig.get("ranks."+rank+".name") != null){
                    ranksName.put(rank, String.valueOf(ranksConfig.get("ranks." + rank + ".name")));
                }
                if(ranksConfig.get("ranks."+rank+".health") != null){
                    ranksHealth.put(rank, Double.parseDouble(String.valueOf(ranksConfig.get("ranks." + rank + ".health"))));
                }
                if(ranksConfig.get("ranks."+rank+".starting-kit") != null){
                    List<String> kitsCache = new ArrayList<String>();
                    for(String kititemcache : ranksConfig.getConfigurationSection("ranks."+rank+".starting-kit").getKeys(false)){
                        if(ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".item_name")!=null){
                            if(ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".metadata")!=null){
                                kitsCache.add(kititemcache+
                                        "#"+ ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".amount")+
                                        "#"+ ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".item_name")+
                                        "#"+ ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".metadata"));
                            }else{
                                kitsCache.add(kititemcache+
                                        "#"+ ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".amount")+
                                        "#"+ ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".item_name"));
                            }
                        }else{
                            kitsCache.add(kititemcache+"#"+ranksConfig.get("ranks."+rank+".starting-kit."+kititemcache+".amount"));
                        }
                    }
                    ranksKit.put(rank, kitsCache);
                }
                if(ranksConfig.get("ranks."+rank+".abilities") !=null){
                    List<String> abCache = new ArrayList<String>();
                    for(String ability : ranksConfig.getStringList("ranks."+rank+".abilities")){
                        abCache.add(ability.toUpperCase());
                    }
                    ranksAbilities.put(rank, abCache);
                }
            }
        }

        for(String ability : abilitiesConfig.getConfigurationSection("abilities").getKeys(false)){
            Map<String, String> abilitiesCache = new HashMap<String, String>();
            if(abilitiesConfig.get("abilities."+ability+".cooldown")!=null && abilitiesConfig.get("abilities."+ability+".item")!=null && abilitiesConfig.get("abilities."+ability+".item_name")!=null){
                abilitiesCache.put("cooldown", String.valueOf(abilitiesConfig.get("abilities."+ability+".cooldown")));
                abilitiesCache.put("item", String.valueOf(abilitiesConfig.get("abilities."+ability+".item")));
                abilitiesCache.put("item_name", String.valueOf(abilitiesConfig.get("abilities."+ability+".item_name")));
            }else{
                if(abilitiesConfig.get("abilities."+ability+".cooldown")!=null){
                    if(abilitiesConfig.get("abilities."+ability+".item")!=null) {
                        abilitiesCache.put("cooldown", String.valueOf(abilitiesConfig.get("abilities."+ability+".cooldown")));
                        abilitiesCache.put("item", String.valueOf(abilitiesConfig.get("abilities."+ability+".item")));
                    }else{
                        abilitiesCache.put("cooldown", String.valueOf(abilitiesConfig.get("abilities."+ability+".cooldown")));
                    }
                }else{
                    if(abilitiesConfig.get("abilities."+ability+".item")!=null){
                        if(abilitiesConfig.get("abilities."+ability+".item_name")!=null){
                            abilitiesCache.put("item", String.valueOf(abilitiesConfig.get("abilities."+ability+".item")));
                            abilitiesCache.put("item_name", String.valueOf(abilitiesConfig.get("abilities."+ability+".item_name")));
                        }else{
                            abilitiesCache.put("item", String.valueOf(abilitiesConfig.get("abilities."+ability+".item")));
                        }
                    }
                }
            }
            abilitiesOptions.put(ability.toUpperCase(), abilitiesCache);
        }

        if(Boolean.parseBoolean(getConfig().getString("use-database"))){
            List<String> value = new ArrayList<String>();
            value.add("true");
            settings.put("use-database", value);
            //TODO add databse feature
        }else{
            List<String> value = new ArrayList<String>();
            value.add("false");
            settings.put("use-database", value);
            if(storageConfig.get("players")!=null){
                for(String uuid : storageConfig.getConfigurationSection("players").getKeys(false)){
                    Map<String, String> cache = new HashMap<String, String>();
                    Map<String, List<String>> listMap = new HashMap<String, List<String>>();
                    if(storageConfig.getString("players."+uuid+".rank")!=null){
                        cache.put("rank", storageConfig.getString("players."+uuid+".rank"));
                    }
                    if(storageConfig.getString("players."+uuid+".xp")!=null){
                        cache.put("xp", storageConfig.getString("players."+uuid+".xp"));
                    }
                    if(storageConfig.getString("players."+uuid+".level")!=null){
                        cache.put("level", storageConfig.getString("players."+uuid+".level"));
                    }
                    if(storageConfig.getString("players."+uuid+".quitRoom")!=null){
                        quitBRoom.put(UUID.fromString(uuid), storageConfig.getString("players."+uuid+".quitRoom"));
                    }
                    playersINFO.put(UUID.fromString(uuid), cache);
                }
            }
        }

        if(levelsConfig.get("levels")!=null){
            if(levelsConfig.get("blockeditems")!=null){
                blockedItems = new TreeSet<String>(levelsConfig.getStringList("blockeditems"));
            }

            for(String sNum : levelsConfig.getConfigurationSection("levels").getKeys(false)){
                try {
                    levelsSet.add(Integer.parseInt(sNum));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            for(String level : levelsConfig.getConfigurationSection("levels").getKeys(false)){
                if(levelsConfig.get("levels."+level+".alloweditems")!=null && blockedItems!=null){
                    NavigableSet<String> cache = new TreeSet<String>(levelsConfig.getStringList("levels."+level+".alloweditems"));
                    if(!levelLimits.isEmpty()){
                        try{
                            cache.addAll(levelLimits.get(String.valueOf(levelsSet.lower(Integer.parseInt(level)))));
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                    }
                    levelLimits.put(level, cache);
                }
                levels.put(level, levelsConfig.getInt("levels."+level+".xp"));
            }
        }

        if(cratesConfig.getStringList("items")!=null){
            cratesItems = cratesConfig.getStringList("items");
            if(cratesConfig.getString("crates_world")!=null){
                cratesWorld = cratesConfig.getString("crates_world");
                if(cratesConfig.get("locations")!=null){
                    for(String name : cratesConfig.getConfigurationSection("locations").getKeys(false)){
                        Map<String, Integer> locationsInfo = new HashMap<String, Integer>();
                        locationsInfo.put("x", cratesConfig.getInt("locations." + name + ".x"));
                        locationsInfo.put("y", cratesConfig.getInt("locations." + name + ".y"));
                        locationsInfo.put("z", cratesConfig.getInt("locations." + name + ".z"));
                        cratesLocations.put(name, locationsInfo);
                    }
                }
            }
        }

        crateKey = new ItemStack(Material.TRIPWIRE_HOOK, 1);
        ItemMeta im = crateKey.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add("Go to spawn and");
        lore.add("see what you can");
        lore.add("unlock from this key!");
        im.setLore(lore);
        im.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Lucky" + " " + ChatColor.GOLD.toString() + ChatColor.BOLD + "Crate Key");
        crateKey.setItemMeta(im);

        if(bossRoomsConfig.get("world")!=null) {
            bossesWorld = String.valueOf(bossRoomsConfig.get("world"));
            if(bossRoomsConfig.get("rooms")!=null){
                for(String roomName : bossRoomsConfig.getConfigurationSection("rooms").getKeys(false)){
                    int errorCheck = 0;
                    if(bossRoomsConfig.get("rooms." + roomName + ".locations")!=null){
                        Map<String, Map<String, Double>> locs = new HashMap<String, Map<String, Double>>();
                        if(bossRoomsConfig.get("rooms." + roomName + ".locations.lobby")!=null){
                            Map<String, Double> location = new HashMap<String, Double>();
                            location.put("x", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.lobby.x"));
                            location.put("y", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.lobby.y"));
                            location.put("z", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.lobby.z"));
                            location.put("pitch", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.lobby.pitch"));
                            location.put("yaw", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.lobby.yaw"));
                            locs.put("lobby", location);
                            bRoomsLocations.put(roomName, locs);
                            errorCheck++;
                        }
                        if(bossRoomsConfig.get("rooms." + roomName + ".locations.spectate")!=null){
                            Map<String, Double> location = new HashMap<String, Double>();
                            location.put("x", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spectate.x"));
                            location.put("y", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spectate.y"));
                            location.put("z", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spectate.z"));
                            location.put("pitch", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spectate.pitch"));
                            location.put("yaw", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spectate.yaw"));
                            locs.put("spectate", location);
                            bRoomsLocations.put(roomName, locs);
                            errorCheck++;
                        }
                        if(bossRoomsConfig.get("rooms." + roomName + ".locations.end")!=null){
                            Map<String, Double> location = new HashMap<String, Double>();
                            location.put("x", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.end.x"));
                            location.put("y", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.end.y"));
                            location.put("z", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.end.z"));
                            location.put("pitch", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.end.pitch"));
                            location.put("yaw", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.end.yaw"));
                            locs.put("end", location);
                            bRoomsLocations.put(roomName, locs);
                            errorCheck++;
                        }
                        if(bossRoomsConfig.get("rooms." + roomName + ".locations.spawn")!=null){
                            Map<String, Double> location = new HashMap<String, Double>();
                            location.put("x", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spawn.x"));
                            location.put("y", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spawn.y"));
                            location.put("z", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spawn.z"));
                            location.put("pitch", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spawn.pitch"));
                            location.put("yaw", bossRoomsConfig.getDouble("rooms." + roomName + ".locations.spawn.yaw"));
                            locs.put("spawn", location);
                            bRoomsLocations.put(roomName, locs);
                            errorCheck++;
                        }
                    }
                    if(bossRoomsConfig.get("rooms." + roomName + ".boss")!=null){
                        if(bossRoomsConfig.get("rooms." + roomName + ".boss.type")!=null){
                            bRoomsBosses.put(roomName, String.valueOf(bossRoomsConfig.get("rooms." + roomName + ".boss.type")));
                            errorCheck++;
                            if(bossRoomsConfig.get("rooms." + roomName + ".boss.spawnlocation")!=null){
                                Map<String, Double> loc = new HashMap<String, Double>();
                                loc.put("x", bossRoomsConfig.getDouble("rooms." + roomName + ".boss.spawnlocation.x"));
                                loc.put("y", bossRoomsConfig.getDouble("rooms." + roomName + ".boss.spawnlocation.y"));
                                loc.put("z", bossRoomsConfig.getDouble("rooms." + roomName + ".boss.spawnlocation.z"));
                                loc.put("pitch", bossRoomsConfig.getDouble("rooms." + roomName + ".boss.spawnlocation.pitch"));
                                loc.put("yaw", bossRoomsConfig.getDouble("rooms." + roomName + ".boss.spawnlocation.yaw"));
                                bRoomsBossesLocations.put(roomName, loc);
                                errorCheck++;
                            }
                        }
                    }

                    if(bossRoomsConfig.get("rooms."+ roomName + ".settings")!=null){
                        Map<String, String> settings = new HashMap<String, String>();
                        if(bossRoomsConfig.get("rooms."+ roomName + ".settings.maxplayers")!=null){
                            settings.put("maxplayers", String.valueOf(bossRoomsConfig.get("rooms."+ roomName + ".settings.maxplayers")));
                            errorCheck++;
                        }
                        if(bossRoomsConfig.get("rooms."+ roomName + ".settings.minplayers")!=null){
                            settings.put("minplayers", String.valueOf(bossRoomsConfig.get("rooms."+ roomName + ".settings.minplayers")));
                            errorCheck++;
                        }
                        if(bossRoomsConfig.get("rooms."+ roomName + ".settings.idletimeout")!=null){
                            settings.put("idletimeout", String.valueOf(bossRoomsConfig.get("rooms."+ roomName + ".settings.idletimeout")));
                            errorCheck++;
                        }
                        bRoomsSettings.put(roomName, settings);
                    }
                    if(errorCheck==9){
                        bossRoomsConfig.set("rooms." + roomName + ".enabled", true);
                        bossRoomsConfig.saveConfig();
                        bRoomsEnabled.put(roomName, true);
                        bRoomManager.setupBRooms();
                    }
                }
                if(bRoomsBosses!=null){
                    for(String room : bRoomsBosses.keySet()){
                        bossesTypes.add(bRoomsBosses.get(room));
                    }
                }
            }
        }
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldEditPlugin) plugin;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public static Economy getEconomy(){
        return economy;
    }

    public static Reality getPlugin(){
        return plugin;
    }

//    public Map<String, Map<String, List<String>>> getRanks() {
//        return ranks;
//    }


    public Map<String, String> getRanksName() {
        return ranksName;
    }

    public Map<String, List<String>> getRanksKit() {
        return ranksKit;
    }

    public Map<String, Double> getRanksHealth() {
        return ranksHealth;
    }

    public Map<String, List<String>> getRanksAbilities() {
        return ranksAbilities;
    }

    public Map<String, Map<String, String>> getAbilitiesOptions() {
        return abilitiesOptions;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public Map<String, List<String>> getSettings() {
        return settings;
    }

    public Map<UUID, Map<String, String>> getPlayersINFO() {
        return playersINFO;
    }

    public Map<String, Integer> getLevels() {
        return levels;
    }

    public Map<String, NavigableSet<String>> getLevelLimits() {
        return levelLimits;
    }

    public NavigableSet<String> getBlockedItems() {
        return blockedItems;
    }

    public NavigableSet<Integer> getLevelsSet() {
        return levelsSet;
    }

    public List<String> getCratesItems() {
        return cratesItems;
    }

    public String getCratesWorld() {
        return cratesWorld;
    }

    public Map<String, Map<String, Integer>> getCratesLocations() {
        return cratesLocations;
    }

    public Config getCratesConfig(){
        return cratesConfig;
    }

    public ItemStack getCrateKey() {
        return crateKey;
    }

    public Config getStorageConfig() {
        return storageConfig;
    }

    public Config getAbilitiesConfig() {
        return abilitiesConfig;
    }

    public Abilities[] getAbilities() {
        return abilities;
    }

    public Config getBossRoomsConfig(){
        return bossRoomsConfig;
    }

    public Map<String, String> getbRoomsBosses() {
        return bRoomsBosses;
    }

    public Map<String, Map<String, Double>> getbRoomsBossesLocations() {
        return bRoomsBossesLocations;
    }

    public Map<String, Map<String, Map<String, Double>>> getbRoomsLocations() {
        return bRoomsLocations;
    }

    public Map<String, Map<String, String>> getbRoomsSettings() {
        return bRoomsSettings;
    }

    public String getBossesWorld(){
        return bossesWorld;
    }

    public Set<String> getBossesTypes(){
        return bossesTypes;
    }

    public Map<String, Boolean> getbRoomsEnabled(){
        return bRoomsEnabled;
    }

    public Map<UUID, Boolean> getbRoomPlayersRole() {
        return bRoomPlayersRole;
    }

    public Map<UUID, String> getbRoomPlayersRoom() {
        return bRoomPlayersRoom;
    }

    public Map<String, Integer> getbRoomWinCount(){
        return bRoomWinCount;
    }

    public Bosses[] getBosses(){
        return bosses;
    }

    public BRoomIdle getbRoomIdle(){
        return bRoomIdle;
    }

    public Map<UUID, String> getQuitBRoom(){
        return quitBRoom;
    }
}
