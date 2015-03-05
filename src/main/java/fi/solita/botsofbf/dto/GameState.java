package fi.solita.botsofbf.dto;

import java.util.*;
import java.util.stream.Collectors;

public class GameState {

    public final Map map;
    public final Set<Player> players;
    public final int round;

    public GameState(final Map map, final int round, final Set<Player> players) {
        this.map = map;
        this.round = round;
        this.players = players;
    }

    public GameState() {
        this.map = Map.createDefault();
        this.round = 1;
        this.players = new HashSet<>();
    }

    public GameState addPlayer(final Player player) {
        boolean nameReserved = players.stream().anyMatch(p -> p.name.equals(player.name));
        if ( nameReserved ) {
            throw new IllegalArgumentException("Player already exists.");
        }

        Set<Player> newPlayers = new HashSet<>(players);
        newPlayers.add(player);
        return new GameState(map, round, newPlayers);
    }

    public GameState newRound() {
        return new GameState(map, round + 1, players);
    }

    public Player getPlayer(UUID playerId) {
        return players.stream().filter(p -> p.id.equals(playerId)).findFirst().get();
    }

    public GameState movePlayer(UUID playerId, Move move) {
        final Player player = getPlayer(playerId);
        final Player newPlayer = player.move(player.position.move(move, map));
        final Set<Player> otherPlayers = players.stream().filter(p -> !p.id.equals(playerId)).collect(Collectors.toSet());
        otherPlayers.add(newPlayer);
        return new GameState(map, round, otherPlayers);
    }
}
