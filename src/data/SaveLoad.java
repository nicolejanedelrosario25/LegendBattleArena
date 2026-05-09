/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import game.GamePanel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author nicol
 */
public class SaveLoad {

    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public void saveGame() {
        try {
            FileWriter writer = new FileWriter("save.txt");

            writer.write(gp.currentWave + "\n");
            writer.write(gp.gold + "\n");
            writer.write(gp.enemiesDefeated + "\n");
            writer.write(gp.turnsTaken + "\n");

            writer.close();

            gp.message = "Game Saved!";
        } catch(IOException e) {
            gp.message = "Save Failed!";
        }
    }

    public void loadGame() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("save.txt"));

            gp.currentWave = Integer.parseInt(reader.readLine());
            gp.gold = Integer.parseInt(reader.readLine());
            gp.enemiesDefeated = Integer.parseInt(reader.readLine());
            gp.turnsTaken = Integer.parseInt(reader.readLine());

            reader.close();

            gp.startNextWave();
            gp.message = "Game Loaded!";
        } catch(IOException e) {
            gp.message = "No Save File Found!";
        }
    }
}