package com.simpleduino.CreativeStats.API;

import com.simpleduino.CreativeStats.Listeners.PlayerListener;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Created by Simple-Duino on 08/04/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class CreaStats {

    File dataFile = new File("plugins/CreativeStats/data.yml");
    YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    Player p;

    public CreaStats(Player p)
    {
        this.p = p;
    }

    public int getPlacedBlocks()
    {
        return PlayerListener.PlacedBlocks.get(this.p);
    }

    public int getBrokenBlocks()
    {
        return PlayerListener.BrokenBlocks.get(this.p);
    }

    public int getTimePlayed()
    {
        return p.getStatistic(Statistic.PLAY_ONE_TICK)/20;
    }

}
