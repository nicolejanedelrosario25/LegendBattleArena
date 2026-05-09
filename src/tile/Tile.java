/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;

import java.awt.Color;

/**
 *
 * @author nicol
 */
public class Tile {
    public Color color;
    public boolean collision = false;

    public Tile(Color color, boolean collision) {
        this.color = color;
        this.collision = collision;
    }
}
