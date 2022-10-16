package org.example;

import Classes.Currency;
import Classes.Item;
import Classes.Player;
import Classes.Progress;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String DB_URL = "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:init.sql'";
    public static final String USER = "sa";
    public static final String PASS = "";

    public static void main(String[] args) throws IOException {

        List<Player> players = getPlayersFromFile("players.json");//чтение json-файла


        List<Player> playersDB = entryToDataBase(players);

        System.out.println(players.get(222));
        System.out.println(players.size());
        System.out.println(playersDB.size());


    }

    public static List<Player> getPlayersFromFile(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonData = Files.readAllBytes(Paths.get(filename));
        return mapper.readValue(jsonData, new TypeReference<List<Player>>(){});
    }

    public static List<Player> entryToDataBase(List<Player> players) {
        List<Player> players1 = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = connection.createStatement()) {

            //загрузка данных в базу
            //через sql запросы
            for (Player pl : players) {
                statement.executeUpdate(String.format("INSERT INTO players (Id, Nickname)" +
                        " VALUES (%d, '%s');", pl.getPlayerId(), pl.getNickname()));

                for (Progress prog : pl.getProgresses()) {
                    statement.executeUpdate(String.format("INSERT INTO progresses (Id, PlayerId, ResourceId, Score, MaxScore)" +
                            " VALUES (%d, %d, %d, %d, %d);", prog.getId(), prog.getPlayerId(), prog.getResourceId(), prog.getScore(), prog.getMaxScore()));
                }
                for (Currency cur : pl.getCurrencies()) {
                    statement.executeUpdate(String.format("INSERT INTO currencies (Id, PlayerId, ResourceId, Name, Count)" +
                            " VALUES (%d, %d, %d, '%s', %d);", cur.getId(), cur.getPlayerId(), cur.getResourceId(), cur.getName(), cur.getCount()));
                }
                for (Item item : pl.getItems()) {
                    statement.executeUpdate(String.format("INSERT INTO items (Id, PlayerId, ResourceId, Count, Level)" +
                            " VALUES (%d, %d, %d, %d, %d);", item.getId(), item.getPlayerId(), item.getResourceId(), item.getCount(), item.getLevel()));
                }
            }

            //выгрузка из базы
            //через sql запросы
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players");
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                Player player = new Player();
                int playerId = resultSet.getInt("Id");
                player.setPlayerId(playerId);
                player.setNickname(resultSet.getString("Nickname"));

                ResultSet progressResult = statement.executeQuery(
                        String.format("SELECT * FROM progresses WHERE PlayerId=%d", playerId));
                while (progressResult.next()) {
                    player.addProgress(new Progress(progressResult.getInt("Id"),
                            progressResult.getInt("PlayerId"),
                            progressResult.getInt("ResourceId"),
                            progressResult.getInt("Score"),
                            progressResult.getInt("MaxScore")));
                }

                ResultSet currencyResult = statement.executeQuery(
                        String.format("SELECT * FROM currencies WHERE PlayerId=%d", playerId));
                while (currencyResult.next()) {
                    player.addCurrency(new Currency(currencyResult.getInt("id"),
                            currencyResult.getInt("PlayerId"),
                            currencyResult.getInt("ResourceId"),
                            currencyResult.getString("Name"),
                            currencyResult.getInt("Count")));
                }

                ResultSet itemResult = statement.executeQuery(
                        String.format("SELECT * FROM items WHERE PlayerId=%d", playerId));
                while (itemResult.next()) {
                    player.addItem(new Item(itemResult.getInt("Id"),
                            itemResult.getInt("PlayerId"),
                            itemResult.getInt("ResourceId"),
                            itemResult.getInt("Count"),
                            itemResult.getInt("Level")));
                }

                players1.add(player);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return players1;
    }

    public static List<Player> extractionFromDataBase() {
        List<Player> players = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM players")) {
            ResultSet resultSet = statement.executeQuery();


            while(resultSet.next()) {
                Player player = new Player();
                int playerId = resultSet.getInt("Id");
                player.setPlayerId(playerId);
                System.out.println("pl" + playerId);
                player.setNickname(resultSet.getString("Nickname"));

                ResultSet progressResult = statement.executeQuery(
                        String.format("SELECT * FROM progresses WHERE PlayerId=%d", playerId));
                while(progressResult.next()) {
                    player.addProgress(new Progress(progressResult.getInt("Id"),
                            progressResult.getInt("PlayerId"),
                            progressResult.getInt("ResourceId"),
                            progressResult.getInt("Score"),
                            progressResult.getInt("MaxScore")));
                }

                ResultSet currencyResult = statement.executeQuery(
                        String.format("SELECT * FROM currencies WHERE PlayerId=%d", playerId));
                while(currencyResult.next()) {
                    player.addCurrency(new Currency(currencyResult.getInt("id"),
                            currencyResult.getInt("PlayerId"),
                            currencyResult.getInt("ResourceId"),
                            currencyResult.getString("Name"),
                            currencyResult.getInt("Count")));
                }

                ResultSet itemResult = statement.executeQuery(
                        String.format("SELECT * FROM items WHERE PlayerId=%d", playerId));
                while(itemResult.next()) {
                    player.addItem(new Item(itemResult.getInt("Id"),
                            itemResult.getInt("PlayerId"),
                            itemResult.getInt("ResourceId"),
                            itemResult.getInt("Count"),
                            itemResult.getInt("Level")));
                }

                players.add(player);
                resultSet = statement.executeQuery("SELECT * FROM players");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return players;
    }

    public static boolean comparisonOfPlayers(List<Player> pl1, List<Player> pl2) {
        return pl1.equals(pl2);
    }
}