package view.map;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.characters.Character;


public class GridPanel extends JPanel{
	
    private List<Character> enemiesList; // List of enemies
    private List<Character> alliesList;  // List of allies
    private JButton[][] gridButtons;
    private JFrame frame;
    
    
	public GridPanel(JFrame frame, List<Character> enemiesList, List<Character> alliesList) {
        super(new GridLayout(AbstractMap.GRID_SIZE, AbstractMap.GRID_SIZE)); // Imposta il layout corretto
		this.frame = frame;
		this.enemiesList = enemiesList;
		this.alliesList = alliesList;
        this.gridButtons = new JButton[AbstractMap.GRID_SIZE][AbstractMap.GRID_SIZE];
        
        initializeGrid();
        
	}

	// Da mettere dentro initializeGrid() nel for
    private void spawnCharacters(JButton button) { //DA FINIRE
    	boolean isPosition = alliesList.stream().filter(character -> character.getPosition().equals(new Position(row,col)).count()) > 0);
    	//Suggerimento?: 	boolean isPosition = alliesList.stream().anyMatch(character -> character.getPosition().equals(new Position(row, col)));
    	if(isPosition) {
    		button.setIcon(character -> character.getImage()); //GetImage
		} else {
 			//bottone invisible
			button.setVisible(false);
		}
    }
       
    /**
     * Initializes the grid panel with buttons.
     */
    private void initializeGrid() {
        this.removeAll();
        for (int i = 0; i < AbstractMap.GRID_SIZE; i++) {
        	for (int j = 0; j < AbstractMap.GRID_SIZE; j++) {
                final int row = i;
                final int col = j;
                gridButtons[i][j] = new JButton();
                gridButtons[i][j].setPreferredSize(new Dimension(AbstractMap.BUTTON_SIZE, AbstractMap.BUTTON_SIZE));
                gridButtons[i][j].addActionListener(e -> showButtonCoordinates(frame, row, col));
                spawnCharacters(gridButtons[i][j]);
                this.add(gridButtons[i][j]);
            }
        }
        this.repaint();
        this.revalidate();
    }
    
    /**
     * Displays the coordinates of the clicked grid button.
     * @param row The row index of the button.
     * @param col The column index of the button.
     */
    private void showButtonCoordinates(JFrame frame, int row, int col) {
        JOptionPane.showMessageDialog(frame, "Position: [" + row + ", " + col + "]");
    }
    
	public JButton[][] getGridButtons() {
		return gridButtons;
	}


}
