package gameLogic;

import map.MapObjects;

import java.util.ArrayList;

public class Player {

    String name;
    String surname;
    String nickName;
    int initialArmies;
    int armies;
    String armyColor;
    boolean survived;
    ArrayList<MapObjects.Territory> territoryCards;
    ArrayList<MapObjects.Territory> conqueredTerritories;

    public Player(String name, String surname, String nickName,
                  int initialArmies, int armies,
                  String armyColor, boolean survived) {
        this.name = name;
        this.surname = surname;
        this.nickName = nickName;
        this.initialArmies = initialArmies;
        this.armies = armies;
        this.armyColor = armyColor;
        this.survived = survived;
        this.territoryCards = new ArrayList<>();
        this.conqueredTerritories = new ArrayList<>();
    }

    public void addConqueredTerritory(MapObjects.Territory element) {
        conqueredTerritories.add(element);
        element.setOwner(this.nickName);
        element.setColor(this.armyColor);
    }
    public void removeConqueredTerritory(MapObjects.Territory element) {
        conqueredTerritories.remove(element);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getNickName() {
        return nickName;
    }

    public int getInitialArmies() {
        return initialArmies;
    }

    public int getArmies() {
        return armies;
    }

    public String getArmyColor() {
        return armyColor;
    }

    public int getNumberOfConqueredTerritories() {
        return conqueredTerritories.size();
    }

    public boolean isSurvived() {
        return survived;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setInitialArmies(int initialArmies) {
        this.initialArmies = initialArmies;
    }

    public void setArmies(int armies) {
        this.armies = armies;
    }

    public void setArmyColor(String armyColor) {
        this.armyColor = armyColor;
    }

    public void setSurvived(boolean survived) {
        this.survived = survived;
    }
}
