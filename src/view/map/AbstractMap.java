package view.map;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import model.characters.Character;
import model.point.Point;

/**
 * Abstract class representing a map in the game.
 * It includes a grid of buttons and a control panel with game management options.
 */
public abstract class AbstractMap 
{
    
    public static final int GRID_SIZE   = 20;  // Dimensione della griglia
    public static final int BUTTON_SIZE = 200; // Dimensione dei bottoni 120

    protected JFrame frame;
    protected GridPanel gridPanel;       // Rappresenta il pannello della griglia di gioco
    private JLayeredPane layeredPanel;
    //protected JPanel controlPanel;       // Pannello per i pulsanti di controllo
    //protected JButton hiddenMenuButton;  // Pulsante per aprire/chiudere il menu

    private List<Character> enemiesList; // Lista dei nemici
    private List<Character> alliesList;  // Lista degli alleati
    private final int numLevel;          // Numero del livello
    private Map<Character, Point> characterMap;

    //private GameStateManager gameStateManager;

    /**
     * Costruttore
     * @param enemiesList Lista dei nemici nel livello corrente.
     * @param alliesList Lista degli alleati nel livello corrente.
     * @param numLevel Numero del livello corrente.
     */
    public AbstractMap(List<Character> enemiesList, List<Character> alliesList, int numLevel) 
    {
        this.enemiesList      = enemiesList;
        this.alliesList       = alliesList;
        this.numLevel         = numLevel;
        this.characterMap = new HashMap<>();

        // Popola la mappa con tutti i personaggi
        for (Character enemy : enemiesList) {
            characterMap.put(enemy, enemy.getPosition());
        }

        for (Character ally : alliesList) {
            characterMap.put(ally, ally.getPosition());
        }
        
       // this.gameStateManager = new GameStateManager();
    }
    
    
    public void start() 
    {
    	System.out.print("Open Level "+this.numLevel+" Frame ->");
    	
    	Timer timer = new Timer(16, e -> {
    	    Map<JButton, Point> imageButtonList = this.gridPanel.getImageButtonList();

    	    // CASO 1: Un bottone ha immagine ma NESSUN personaggio si trova in quella posizione
    	    for (Map.Entry<JButton, Point> entry : imageButtonList.entrySet()) 
    	    {
    	        JButton button = entry.getKey();
    	        Point buttonPos = entry.getValue();
    	        boolean personFound = false;

    	        // Stampa di debug per capire se un bottone ha un'immagine
    	        if (button.getIcon() != null) {
    	            System.out.println("Bottone con immagine trovato nella posizione " + buttonPos);
    	        }

    	        // Controlla se un personaggio è presente nella stessa posizione
    	        for (Point characterPos : characterMap.values()) {
    	            if (characterPos.equals(buttonPos)) {
    	                personFound = true;
    	                break;
    	            }
    	        }

    	        // Se non ci sono personaggi nella posizione del bottone, rimuovi l'immagine
    	        if (!personFound && button.getIcon() != null) {
    	            System.out.println("Nessun personaggio trovato in " + buttonPos + ", rimuovo immagine");
    	            button.setIcon(null); // Rimuovi immagine
    	        }
    	    }

    	    // CASO 2: Un personaggio si trova su una posizione dove il bottone NON ha immagine
    	    for (Map.Entry<Character, Point> entry : characterMap.entrySet()) {
    	        Character character = entry.getKey();
    	        Point characterPos = entry.getValue();
    	        JButton button = this.gridPanel.getGridButtons()[characterPos.getY()][characterPos.getX()];

    	        // Stampa di debug per capire se il personaggio ha un'immagine
    	        System.out.println("Controllando personaggio " + character + " nella posizione " + characterPos);

    	        if (button.getIcon() == null) {
    	            System.out.println("Nessuna immagine nel bottone alla posizione " + characterPos + ", aggiungo immagine del personaggio");
    	            ImageIcon originalIcon = new ImageIcon(character.getImage());
    	            Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH); // adatta la misura
    	            button.setIcon(new ImageIcon(scaledImage));
    	        }
    	    }
    	});

    	
    	initializeFrame();
        
        //initializeControlPanel(); 

        frame.setVisible(true);
        timer.start();
    }
    
    
    
    /**
     * Initializes the main frame and layout.
     */
    private void initializeFrame()
    {
        this.frame = new JFrame("Five Realms");                      //Creo la finestra
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //Quando la finestra viene chiusa, il programma termina

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Ottego le dimensioni dello schermo
	    int width  = (int) (screenSize.getWidth() * 0.6);                   // 60% della larghezza dello schermo
	    int height = (int) (width * 3.0 / 4.0);                             // Rapporto 4:3
	    this.frame.setSize(width, height);                                  // Imposta le dimensioni della finestra
	         
        this.frame.setLocationRelativeTo(null);   //Quando inizia il gioco posiziona la finestra al centro dello schermo 

        this.frame.setLayout(new BorderLayout());

        // Crea il JLayeredPanel che gestisce i vari layer
        this.layeredPanel = new JLayeredPane();
        this.frame.setContentPane(this.layeredPanel);
   
		// 0. Background 
        this.initializeBackgroundMap();
        
        // 1. Griglia di bottoni trasparente
        this.initializeGridMap();
        
        // 3. Personaggi     
        
        
        

        /*
        this.characterPanel = new JPanel();
        characterPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        layeredPane.add(characterPanel, Integer.valueOf(2));  // Livello 2 (Personaggi sopra la griglia)*/


        /*controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(Color.LIGHT_GRAY); // Sfondo visibile
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50)); // Margine interno
        mainPanel.add(controlPanel, BorderLayout.EAST); // Pannello sulla destra*/
    }

    
    



    private void initializeBackgroundMap() 
    {
        // Ottieni l'immagine associata al livello corrente
        String backgroundFile = "images/background/background" + numLevel + ".jpg";

        // Carica l'immagine dal file
        ImageIcon backgroundImage = new ImageIcon(backgroundFile);

        // Controlla che l'immagine sia valida
        if (backgroundImage.getIconWidth() > 0 && backgroundImage.getIconHeight() > 0) 
        {
        	// Ridimensiona l'immagine per adattarla al frame
            Image image = backgroundImage.getImage();
            Image resizedImage = image.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);
            backgroundImage = new ImageIcon(resizedImage);
        	
            // Crea un JLabel per contenere l'immagine di sfondo
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // Configura le dimensioni

            // Usa il JLayeredPane esistente del frame
            JLayeredPane layeredPane = frame.getLayeredPane();

            // Aggiungi il background al livello inferiore 0
            layeredPane.add(backgroundLabel, Integer.valueOf(0)); 
        } 
        else {
            System.err.println("Errore: immagine di background non trovata per il livello " + numLevel);
        }
    }
    
    
    private void initializeGridMap() 
    {
        // Crea e inizializza il GridPanel se non esiste già
        if (this.gridPanel == null) 
        {
            this.gridPanel = new GridPanel(this.layeredPanel, enemiesList, alliesList);
        }      
        
        // Imposta le dimensioni del gridPanel uguali a quelle del frame
        this. gridPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // Stesse dimensioni del frame

        // Rendi la griglia trasparente per non coprire lo sfondo
        this.gridPanel.setOpaque(false);

        // Aggiungi gridPanel al JLayeredPane nel livello superiore (livello 1)
        this.layeredPanel.add(this.gridPanel, Integer.valueOf(1));

        // Rendi visibile il layout dopo aver aggiunto il componente
        this.layeredPanel.revalidate();
        this.layeredPanel.repaint();
    }
    
    /**
     * Initializes the control panel with buttons for game actions.
     */
    /*private void initializeControlPanel() {
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        JButton exitButton = new JButton("Exit");

        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        saveButton.addActionListener(e -> {
            try {
                saveGame();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        loadButton.addActionListener(e -> {
            try {
                loadGame();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        exitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(Box.createVerticalStrut(10)); // Space before buttons
        controlPanel.add(saveButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(loadButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(exitButton);
    }*/
    
    
    /**
     * Saves the current game state.
     * @throws IOException if an error occurs during saving.
     */
    /*private void saveGame() throws IOException {
        List<Character> allies = new ArrayList<>();
        List<Character> enemies = new ArrayList<>();
        int level = 1;

        gameStateManager.saveStatus(allies, enemies, level);
        JOptionPane.showMessageDialog(frame, "Gioco salvato con successo!");
    }*/

    /**
     * Loads a previously saved game state.
     * @throws IOException if an error occurs during loading.
     */
    /*private void loadGame() throws IOException {
        GameState gameState = gameStateManager.loadStatus();
        if (gameState != null) {
            JOptionPane.showMessageDialog(frame, "Game loaded successfully! Level: " + gameState.getLevel());
        } else {
            JOptionPane.showMessageDialog(frame, "No saves found.");
        }
    }*/
    
    

    /**
     * Gets the list of enemies in the current level.
     * @return List of enemy characters.
     */
    public List<Character> getEnemiesList() {
        return enemiesList;
    }

    /**
     * Gets the list of allies characters.
     * @return List of allies.
     */
    public List<Character> getAlliesList() {
        return alliesList;
    }
    
    //metodo per chiudere il frame manualmente
    public void closeWindow() 
    {
        if (this.frame != null) 
            this.frame.dispose();
        
    }
    
    /**
     * Spawns a character on the given button based on its position on the grid.
     * If a character is present at the specified grid position, the button will display the character's image.
     * If no character is present, the button will become transparent.
     *
     * @param button The button on the grid where the character will be spawned.
     * @param row The row index of the button on the grid.
     * @param col The column index of the button on the grid.
     * @param allies The list of allied characters.
     * @param enemies The list of enemy characters.
     */
	public void spawnCharacter(List<Character> spawnList) 
	{
		// Fare controllo se ci sono nella lista alleati o nemici
		if(spawnList.get(0).isAllied()) {
			// Lista di alleati e spawnare in basso
			spawnList.get(0).moveTo(new Point(17, 3));			
			/*ImageIcon originalIcon = new ImageIcon(spawnList.get(0).getImage());
            JButton button = this.gridPanel.getGridButtons()[17][3];
            Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH); // adatta la misura
            button.setIcon(new ImageIcon(scaledImage));*/
			
			
			spawnList.get(1).moveTo(new Point(19, 10));
			spawnList.get(2).moveTo(new Point(17, 16));
			
		}else {
			// Lista di nemici, spawnare in alto
			spawnList.get(0).moveTo(new Point(2, 3));
			spawnList.get(1).moveTo(new Point(8, 5));
			spawnList.get(2).moveTo(new Point(5, 14));
			
		}
	}
			
	
	/* Il metodo rimuove l'immagine dove si trova il personaggio e aggiunge l'immagine del personaggio nel  bottone targhet
	 * moveCharacter usa moveTo e aggiornamento grafico
	*/
	public void moveCharacter(Character character, Point target) {
		
	}
	
	//rimuova dalla mappe il personaggio 
	public void removeCharacter(Character character, Point target) {
		
	}
	
    // Dato  x e y restituisce il bottone in quella posizione
	public JButton getButtonAt(int x, int y) {
        if (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE) {
            return gridPanel.getGridButtons()[x][y];
        }
        return null;
    }
}
