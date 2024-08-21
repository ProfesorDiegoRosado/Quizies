package ies.portadaalta.gameengine.model.observer;

import ies.portadaalta.gameengine.model.Player;
import ies.portadaalta.quizzengine.model.Question;

import java.util.List;

public interface GameEngineObserver {

    public void notifyGameStarts();

    public void notifyGameEnds(List<Player> winnerPlayers);

    public void notifyRoundStarts(int roundCount);

    public void notifyRoundEnds(int roundCount);

    public void notifyTurnStarts(Player player);

    public void notifyPlayerChoice(Player player, Question question, int choice);

    public void notifyQuestion(Question question);

    public void notifyTurnEnds(Player player);

}
