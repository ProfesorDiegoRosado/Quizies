package ies.portadaalta.gameengine.model.stats;

import ies.portadaalta.gameengine.model.Player;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Question;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameStats {
    /*

    private Map<Player, PlayerStats> playerStatsMap;

    public GameStats() {
        playerStatsMap = new HashMap<>();
    }

    public void update(Player player, Collection<Category> categories, Question question, int choice) {
        PlayerStats playerStats = playerStatsMap.getOrDefault(player, new PlayerStats(categories));
        playerStats.update(question, choice);
        playerStatsMap.put(player, playerStats);
    }

    public boolean checkWinner() {
        playerStatsMap.entrySet().stream().filter( entry ->
                entry.getValue().isWinner()
        ).toList();
    }

    @Override
    public String toString() {
        return "GameStats{" +
                "playerStatsMap=" + playerStatsMap +
                '}';
    }

     */
}
