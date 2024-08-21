package ies.portadaalta.gameengine.model.observer;

import ies.portadaalta.gameengine.model.Player;
import ies.portadaalta.quizzengine.model.Question;

import java.util.ArrayList;
import java.util.List;

public class GameEnginePublisher {

    private List<GameEngineObserver> observers = new ArrayList();

    // Publisher methods
    synchronized public void register(GameEngineObserver observer) {
        observers.add(observer);
    }

    synchronized public void unregister(GameEngineObserver observer) {
        observers.remove(observer);
    }


    // Game events
    synchronized public void notifyGameStarts() {
        observers.stream().forEach( observer -> observer.notifyGameStarts() );
    }

    synchronized public void notifyGameEnds(List<Player> winnerPlayers) {
        observers.stream().forEach( observer -> observer.notifyGameEnds(winnerPlayers) );
    }

    synchronized public void notifyRoundStarts(int roundCount) {
        observers.stream().forEach( observer -> observer.notifyRoundStarts(roundCount) );
    }

    synchronized public void notifyRoundEnds(int roundCount) {
        observers.stream().forEach( observer -> observer.notifyRoundEnds(roundCount) );
    }

    synchronized public void notifyTurnStarts(Player player) {
        observers.stream().forEach( observer -> observer.notifyTurnStarts(player) );
    }

    synchronized public void notifyQuestion(Question question) {
        observers.stream().forEach( observer -> observer.notifyQuestion(question) );
    }

    synchronized public void notifyPlayerChoice(Player player, Question question, int choice) {
        observers.stream().forEach( observer -> observer.notifyPlayerChoice(player, question, choice));
    }

    synchronized public void notifyTurnEnds(Player player) {
        observers.stream().forEach( observer -> observer.notifyTurnEnds(player) );
    }

}
