package com.simpleduino.CreativeStats.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Simple-Duino on 07/04/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class PlayerListener implements Listener {

    public static HashMap<Player, Integer> BrokenBlocks = new HashMap<>();
    public static HashMap<Player, Integer> PlacedBlocks = new HashMap<>();
    public static HashMap<Player, Long> JoinDate = new HashMap<>();

    File dataFile = new File("plugins/CreativeStats/data.yml");
    YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        int tempBrokenBlocks = BrokenBlocks.get(e.getPlayer())+1;
        BrokenBlocks.remove(e.getPlayer());
        BrokenBlocks.put(e.getPlayer(), tempBrokenBlocks);

        e.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("Blocs cassés").setScore(tempBrokenBlocks);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        int tempPlacedBlocks = PlacedBlocks.get(e.getPlayer())+1;
        PlacedBlocks.remove(e.getPlayer());
        PlacedBlocks.put(e.getPlayer(), tempPlacedBlocks);

        e.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("Blocs placés").setScore(tempPlacedBlocks);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(!dataConfig.isSet(p.getUniqueId().toString() + ".brokenBlocks") || !dataConfig.isSet(p.getUniqueId().toString() + ".placedBlocks")) {
            dataConfig.set(p.getUniqueId().toString() + ".brokenBlocks", 0);
            dataConfig.set(p.getUniqueId().toString() + ".placedBlocks", 0);
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Scoreboard pScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        pScoreboard.registerNewObjective(ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() + "Statistiques", "dummy");
        pScoreboard.getObjective(ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() + "Statistiques").setDisplaySlot(DisplaySlot.SIDEBAR);
        pScoreboard.getObjective(DisplaySlot.SIDEBAR).getScore("Blocs cassés").setScore(Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".brokenBlocks").toString()));
        pScoreboard.getObjective(DisplaySlot.SIDEBAR).getScore("Blocs placés").setScore(Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".placedBlocks").toString()));
        p.setScoreboard(pScoreboard);
        PlayerListener.BrokenBlocks.put(p, Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".brokenBlocks").toString()));
        PlayerListener.PlacedBlocks.put(p, Integer.parseInt(dataConfig.get(p.getUniqueId().toString() + ".placedBlocks").toString()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        int playingTime = e.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK)/20;
        dataConfig.set(e.getPlayer().getUniqueId().toString()+".playingTime", playingTime);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
