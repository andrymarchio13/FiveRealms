package view.map;

import java.util.List;

import javax.swing.JButton;

import model.characters.Character;
import model.point.Point;

/**
 * Interface representing a generic map in the game.
 * Defines the essential methods that any map implementation must provide.
 */
public interface Map {

    /**
     * Retrieves the button located at the specified coordinates.
     *
     * @param x The x-coordinate of the button.
     * @param y The y-coordinate of the button.
     * @return The JButton at the specified position.
     */
    public JButton getButtonAt(int x, int y);

    /**
     * Closes the current map window.
     */
    public void closeWindow();
    
    public void spawnCharacter(List<Character> spawnList);
    
    public void moveCharacter(Character character, Point target);
    
    public void removeCharacter(Character character, Point target);
}
