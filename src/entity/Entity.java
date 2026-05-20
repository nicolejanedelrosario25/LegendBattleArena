/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.awt.Rectangle;

/**
 *
 * @author nicol
 */
public class Entity {
    public int worldX;
    public int worldY;
    public int speed = 4;
    public String direction = "down";
    
//    public int screenX;
//    public int screenY;
    
//
//    public String direction;
    
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public boolean collisionOn = false;
}
