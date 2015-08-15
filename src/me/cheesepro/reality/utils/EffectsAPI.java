package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mark on 2015-08-14.
 */
public class EffectsAPI {

    private Reality plugin;
    private Tools tools;
    private List<Color> randomColors = new ArrayList<Color>();

    public enum PlayEffect{EXPLODE, LOVE, MUSIC, SMOKE, CLOUD, ENDER, CRIT, FIRE}

    public EffectsAPI(Reality plugin){
        this.plugin = plugin;
        tools = new Tools(plugin);
        randomColors.add(Color.AQUA);
        randomColors.add(Color.BLACK);
        randomColors.add(Color.BLUE);
        randomColors.add(Color.GRAY);
        randomColors.add(Color.GREEN);
        randomColors.add(Color.LIME);
        randomColors.add(Color.MAROON);
        randomColors.add(Color.OLIVE);
        randomColors.add(Color.ORANGE);
        randomColors.add(Color.PURPLE);
        randomColors.add(Color.RED);
        randomColors.add(Color.SILVER);
        randomColors.add(Color.WHITE);
        randomColors.add(Color.YELLOW);
        randomColors.add(Color.TEAL);
    }

    public void firework(UUID id, FireworkEffect.Type fireworkEffect){
        Location loc = Bukkit.getPlayer(id).getLocation();
        loc.setY(loc.getY() - 1);
        Firework f = Bukkit.getPlayer(id).getWorld().spawn(loc, Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(fireworkEffect)
                .withColor(getRandomColor())
                .withFade(getRandomColor())
                .withFlicker()
                .withTrail()
                .build());
        fm.setPower(0);
        f.setFireworkMeta(fm);
    }

    public void effect(Location loc, PlayEffect playEffect){
        switch (playEffect){
            case CRIT: for(int i = 0; i<10; i++){loc.getWorld().playEffect(loc, Effect.CRIT, null);}
                break;
            case CLOUD: for(int i = 0; i<25; i++){loc.getWorld().playEffect(loc, Effect.CLOUD, null);}
                break;
            case ENDER: for(int i = 0; i<25; i++){loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, null);}
                break;
            case EXPLODE: loc.getWorld().playEffect(loc, Effect.EXPLOSION_HUGE, null);
                break;
            case LOVE: loc.getWorld().playEffect(loc, Effect.HEART, null);
                break;
            case MUSIC: loc.getWorld().playEffect(loc, Effect.NOTE, null);
                break;
            case SMOKE: for(int i = 0; i<25; i++){loc.getWorld().playEffect(loc, Effect.LARGE_SMOKE, null);}
                break;
            case FIRE:  for(int i = 0; i<3; i++){loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, null);}
                break;
        }
    }

    private Color getRandomColor(){
        int randInt = tools.randInt(0, randomColors.size()-1);
        return randomColors.get(randInt);
    }

}
