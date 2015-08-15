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

    private enum PlayEffect {EXPLODE, LOVE, MUSIC, SMOKE, };

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

    public void effect(Location loc, Effect effect){
        loc.getWorld().playEffect(loc, effect, effect.getData());
    }

    private Color getRandomColor(){
        int randInt = tools.randInt(0, randomColors.size()-1);
        return randomColors.get(randInt);
    }

}
