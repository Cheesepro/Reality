package me.cheesepro.reality;

import me.cheesepro.reality.abilities.*;
import me.cheesepro.reality.commands.CommandsManager;
import me.cheesepro.reality.level.LevelLimiter;
import me.cheesepro.reality.listeners.*;
import me.cheesepro.reality.luckycrates.KeysGiver;
import me.cheesepro.reality.luckycrates.LuckyCrates;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.ConfigManager;
import me.cheesepro.reality.utils.Logger;
import me.cheesepro.reality.level.XPGainListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    public static Map<String, List<String>> settings = new HashMap<String, List<String>>();

    /*
     * This map stores all the information about ranks such as the name, health, starting-kits and abilities of a rank.</b>
     * Format: (Rank name, Map(name/health/starting-kits/abilities, List(value)))
     */
    public static Map<String, Map<String, List<String>>> ranks = new HashMap<String, Map<String, List<String>>>();

    /*
     * This map stores how much XP are each level needed.
     * Format: Map(Level, XP required)
     */
    public static Map<String, Integer> levels = new HashMap<String, Integer>();

    /*
     * This map stores all the items that each level are allowed.
     * Format: Map(Level, NavigableSet(AllowedItems))
     */
    public static Map<String, NavigableSet<String>> levelLimits = new HashMap<String, NavigableSet<String>>();

    /*
     * This set contains all the blocked items.
     */
    public static NavigableSet<String> blockedItems = new TreeSet<String>();

    /*
     * This set contains all the levels
     */
    public static NavigableSet<Integer> levelsSet = new TreeSet<Integer>();

    /*
     * This map stores the information of every abilities
     * Format: Map(AbilityName, Map(item/item_name/cooldown, value))
     */
    public static Map<String, Map<String, String>> abilitiesOptions = new HashMap<String, Map<String, String>>();

    /*
     * This map stores all the configurable messages.
     */
    public static Map<String, String> messages = new HashMap<String, String>();

    /*
     * This map stores every player's information
     * Format: Map(UUID of the player, Map(rank/level/xp, value))
     */
    public static Map<UUID, Map<String, String>> playersINFO = new HashMap<UUID, Map<String, String>>();

    /*
     * This array contains all the Abilities types.
     */
    public static Abilities abilities[] = new Abilities[9];

    /*
     * This is the list that contains all the prizes in the crates
     */
    public static List<String> cratesItems = new ArrayList<String>();

    /*
     * This map contains location information about crates
     * Format: Map(Name, Map(x/y/z, value))
     */
    public static Map<String, Map<String, Integer>> cratesLocations = new HashMap<String, Map<String, Integer>>();

    /*
     * Stores the world where all the crates are allowed to be create.
     */
    public static String cratesWorld = null;

    /*
     * Stores the world where all the crates are allowed to be create.
     */
    public static ItemStack crateKey = null;

    /*
     * This map stores where the bosses located
     * Format: Map<BossName, Map<x/y/z, values>
     */
    public static Map<String, Map<String, Double>> bossesLocations = new HashMap<String, Map<String, Double>>();

    /*
     * Stores the world where all the bosses will be allowed.
     */
    public static String bossesWorld = null;

    Logger logger = new Logger();
    ConfigManager configManager;
    Config ranksConfig;
    Config levelsConfig;
    Config msgConfig;
    Config storageConfig;
    Config abilitiesConfig;
    Config cratesConfig;
    Config bossesConfig;
    World world;
    public static String pName = "[Reality]";
    private final JavaPlugin plugin = this;

    @Override
    public void onEnable(){
        try{
            loadConfig();
            saveDefaultConfig();
            cache();
            registerCommands();
            registerListeners();
            registerAbilities();
            logger.info("Successfully Enabled");
        }catch (Exception e){
            logger.error(e);
        }
    }

    @Override
    public void onDisable(){
        try{
            HandlerList.unregisterAll(plugin);
            Bukkit.getScheduler().cancelTasks(plugin);
            logger.info("Successfully Disabled");
        }catch (Exception e){
            logger.error(e);
        }
    }

    private void registerCommands(){
        CommandsManager commandsManager = new CommandsManager(this);
        getCommand("reality").setExecutor(commandsManager);
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
        bossesConfig = configManager.getNewConfig("bosses.yml");
    }

    private void cache(){
        if(getConfig().getString("RespawnLocation")!=null){
            List<String> cords = new ArrayList<String>();
            cords.add(getConfig().getString("RespawnLocation.world"));
            cords.add(getConfig().getString("RespawnLocation.x"));
            cords.add(getConfig().getString("RespawnLocation.y"));
            cords.add(getConfig().getString("RespawnLocation.z"));
            cords.add(getConfig().getString("RespawnLocation.pitch"));
            cords.add(getConfig().getString("RespawnLocation.yaw"));
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
            for(String cache : ranksConfig.getConfigurationSection("ranks").getKeys(false)){
                Map<String, List<String>> mapCache = new HashMap<String, List<String>>();
                List<String> healthCache = new ArrayList<String>();
                List<String> kitsCache = new ArrayList<String>();
                if(ranksConfig.get("ranks."+cache+".health") != null){
                    healthCache.add(String.valueOf(ranksConfig.get("ranks." + cache + ".health")));
                    mapCache.put("health", healthCache);
                }
                if(ranksConfig.get("ranks."+cache+".starting-kit") != null){
                    for(String kititemcache : ranksConfig.getConfigurationSection("ranks."+cache+".starting-kit").getKeys(false)){
                        if(ranksConfig.get("ranks."+cache+".starting-kit."+kititemcache+".item_name")!=null){
                            kitsCache.add(kititemcache+
                                    "#"+ ranksConfig.get("ranks."+cache+".starting-kit."+kititemcache+".amount")+
                                    "#"+ ranksConfig.get("ranks."+cache+".starting-kit."+kititemcache+".item_name"));
                        }else{
                            kitsCache.add(kititemcache+"#"+ranksConfig.get("ranks."+cache+".starting-kit."+kititemcache+".amount"));
                        }
                    }
                    mapCache.put("starting-kit", kitsCache);
                }
                if(ranksConfig.get("ranks."+cache+".abilities") !=null){
                    List<String> abCache = new ArrayList<String>();
                    for(String ability : ranksConfig.getStringList("ranks."+cache+".abilities")){
                        abCache.add(ability.toUpperCase());
                    }
                    mapCache.put("abilities", abCache);
                }
                ranks.put(cache, mapCache);
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

        if(bossesConfig.get("world")!=null) {
            bossesWorld = String.valueOf(bossesConfig.get("world"));
            if (bossesConfig.get("locations") != null) {
                for (String bossName : bossesConfig.getConfigurationSection("locations").getKeys(false)) {
                    Map<String, Double> location = new HashMap<String, Double>();
                    location.put("x", bossesConfig.getDouble("locations." + bossName + ".x"));
                    location.put("y", bossesConfig.getDouble("locations." + bossName + ".y"));
                    location.put("z", bossesConfig.getDouble("locations." + bossName + ".z"));
                    bossesLocations.put(bossName.toLowerCase(), location);
                }
            }
        }
    }

    public Map<String, Map<String, List<String>>> getRanks() {
        return ranks;
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

    public Config getBossesConfig(){
        return bossesConfig;
    }

    public Map<String, Map<String, Double>> getBossesLocations(){
        return bossesLocations;
    }

    public String getBossesWorld(){
        return bossesWorld;
    }


}
