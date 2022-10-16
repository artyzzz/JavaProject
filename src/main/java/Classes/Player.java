package Classes;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId;
    private String nickname;
    private List<Progress> progresses = new ArrayList<>();
    private List<Currency> currencies = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    public Player() {
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProgresses(List<Progress> progresses) {
        this.progresses = progresses;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Progress> getProgresses() {
        return progresses;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addProgress(Progress prog) {
        progresses.add(prog);
    }

    public void addCurrency(Currency cur) {
        currencies.add(cur);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", progresses=" + progresses + '\'' +
                ", currencies=" + currencies + '\'' +
                ", items=" + items +
                '}' + '\'';
    }
}
