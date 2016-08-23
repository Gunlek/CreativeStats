package com.simpleduino.CreativeStats;

import com.simpleduino.CreativeStats.Listeners.PlayerListener;
import net.minecraft.server.v1_8_R3.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Simple-Duino on 07/04/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class CreativeStats extends JavaPlugin {

    File dataFile = new File("plugins/CreativeStats/data.yml");
    YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

    public void onEnable()
    {
        if(!dataFile.exists())
        {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PlayerListener.BrokenBlocks.clear();
        PlayerListener.PlacedBlocks.clear();
        PlayerListener.JoinDate.clear();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard pScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            pScoreboard.registerNewObjective(ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() + "Statistiques", "dummy");
            pScoreboard.getObjective(ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() + "Statistiques").setDisplaySlot(DisplaySlot.SIDEBAR);
            pScoreboard.getObjective(DisplaySlot.SIDEBAR).getScore("Blocs cassés").setScore(Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".brokenBlocks").toString()));
            pScoreboard.getObjective(DisplaySlot.SIDEBAR).getScore("Blocs placés").setScore(Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".placedBlocks").toString()));
            p.setScoreboard(pScoreboard);

            PlayerListener.BrokenBlocks.put(p, Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".brokenBlocks").toString()));
            PlayerListener.PlacedBlocks.put(p, Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".placedBlocks").toString()));
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void onDisable()
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(!PlayerListener.BrokenBlocks.containsKey(p) || !PlayerListener.PlacedBlocks.containsKey(p))
            {
                dataConfig.set(p.getUniqueId().toString()+".brokenBlocks",0);
                dataConfig.set(p.getUniqueId().toString()+".placedBlocks",0);
            }
            else
            {
                dataConfig.set(p.getUniqueId().toString()+".brokenBlocks",PlayerListener.BrokenBlocks.get(p));
                dataConfig.set(p.getUniqueId().toString()+".placedBlocks",PlayerListener.PlacedBlocks.get(p));
            }
            PlayerListener.BrokenBlocks.remove(p);
            PlayerListener.PlacedBlocks.remove(p);

            try {
                dataConfig.save(dataFile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        PlayerListener.BrokenBlocks.clear();
        PlayerListener.PlacedBlocks.clear();
        PlayerListener.JoinDate.clear();
    }

}
