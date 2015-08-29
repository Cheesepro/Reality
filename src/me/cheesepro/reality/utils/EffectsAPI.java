package me.cheesepro.reality.utils;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Mark on 2015-08-14.
 */
public class EffectsAPI {

    private static List<Color> randomColors = new ArrayList<Color>(){{
            add(Color.AQUA);
            add(Color.BLACK);
            add(Color.BLUE);
            add(Color.GRAY);
            add(Color.GREEN);
            add(Color.LIME);
            add(Color.MAROON);
            add(Color.OLIVE);
            add(Color.ORANGE);
            add(Color.PURPLE);
            add(Color.RED);
            add(Color.SILVER);
            add(Color.WHITE);
            add(Color.YELLOW);
            add(Color.TEAL);
        }
    };

    public enum PlayEffect{EXPLODE, LOVE, MUSIC, SMOKE, CLOUD, ENDER, CRIT, FIRE, MAD}

    public static void firework(UUID id, FireworkEffect.Type fireworkEffect){
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

    public static void effect(Location loc, PlayEffect playEffect){
        loc.setY(loc.getY()+1);
        switch (playEffect){
            case CRIT: for(int i = 0; i<45; i++){loc.getWorld().playEffect(loc, Effect.MAGIC_CRIT, null);}
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
            case MAD:
                Location loc1 = new Location(loc.getWorld(), loc.getX() + 0.3, loc.getY()+1, loc.getZ());
                Location loc2 = new Location(loc.getWorld(), loc.getX() - 0.3, loc.getY()+1, loc.getZ());
                loc.getWorld().playEffect(loc, Effect.VILLAGER_THUNDERCLOUD, null);
                loc.getWorld().playEffect(loc1, Effect.VILLAGER_THUNDERCLOUD, null);
                loc.getWorld().playEffect(loc2, Effect.VILLAGER_THUNDERCLOUD, null);
        }
    }

    private static Color getRandomColor(){
        int randInt = randInt(0, randomColors.size()-1);
        return randomColors.get(randInt);
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
