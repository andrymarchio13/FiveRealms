package view;

import javax.swing.*;
import model.characters.Character;
import model.gameStatus.Game;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionMenu {

    private JFrame frame;
    private JButton nextButton;
    private final List<JPanel> selectedPanels = new ArrayList<>();
    private final List<String> selectedCharacters = new ArrayList<>();

    public void start(List<Character> allAllies) {
        System.out.println("Open Characters Selection Menu Frame ->");

        frame = new JFrame("Characters Selection Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);

        // Sfondo casuale
        String bgPath = "images/background/background" + (int)(Math.random() * 6) + ".jpg";
        ImageIcon bgIcon = new ImageIcon(new ImageIcon(bgPath).getImage().getScaledInstance(800, 800, Image.SCALE_SMOOTH));
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Titolo con stile fantasy migliorato
        JLabel info = new JLabel("SELECT 3 CHARACTERS", SwingConstants.CENTER);
        info.setFont(new Font("Serif", Font.BOLD, 36));
        info.setForeground(new Color(245, 222, 179)); // Beige/oro chiaro
        gbc.gridx = 1;
        gbc.gridy = 0;
        bgLabel.add(info, gbc);

        // Pulsante "Next" con stile fantasy
        nextButton = createFantasyButton("NEXT");
        nextButton.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        bgLabel.add(nextButton, gbc);

        // Personaggi disponibili
        addCharacter(bgLabel, "Archer", "A skilled archer, master of the bow.", "images/characters/archer/archerHero.png", 0, 1, gbc);
        addCharacter(bgLabel, "Barbarian", "A fierce warrior with immense strength.", "images/characters/barbarian/barbarianHero.png", 2, 1, gbc);
        addCharacter(bgLabel, "Juggernaut", "A massive tank, unstoppable force.", "images/characters/juggernaut/juggernautHero.png", 1, 2, gbc);
        addCharacter(bgLabel, "Knight", "A noble knight, brave and strong.", "images/characters/knight/knightHero.png", 0, 3, gbc);
        addCharacter(bgLabel, "Wizard", "A master of the arcane arts.", "images/characters/wizard/wizardHero.png", 2, 3, gbc);

        frame.add(bgLabel);
        frame.setVisible(true);
    }

    private JButton createFantasyButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setForeground(new Color(245, 222, 179)); // Beige/oro chiaro
        button.setBackground(new Color(101, 67, 33)); // Marrone scuro
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 60));

        button.addMouseListener(new MouseAdapter() {
            private Color originalBg = new Color(101, 67, 33);
            private Color hoverBg = new Color(139, 117, 82);
            
            public void mouseEntered(MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(hoverBg);
                    button.setForeground(new Color(255, 248, 220));
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(originalBg);
                    button.setForeground(new Color(245, 222, 179));
                } else {
                    button.setBackground(new Color(60, 40, 20));
                    button.setForeground(new Color(120, 120, 120));
                }
            }
        });

        // Gestione stato disabled
        button.addPropertyChangeListener("enabled", evt -> {
            if (!button.isEnabled()) {
                button.setBackground(new Color(60, 40, 20));
                button.setForeground(new Color(120, 120, 120));
            } else {
                button.setBackground(new Color(101, 67, 33));
                button.setForeground(new Color(245, 222, 179));
            }
        });

        return button;
    }

    private void addCharacter(JLabel bgLabel, String name, String desc, String imgPath, int x, int y, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(20, 20, 20, 220));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JLabel imgLabel = new JLabel(new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)), SwingConstants.CENTER);
        
        // Testo del personaggio con stile fantasy
        JLabel lbl = new JLabel("<html><center>" + name + "<br>" + desc + "</center></html>", SwingConstants.CENTER);
        lbl.setFont(new Font("Serif", Font.BOLD, 14));
        lbl.setForeground(new Color(245, 222, 179)); // Beige/oro chiaro

        panel.add(imgLabel, BorderLayout.NORTH);
        panel.add(lbl, BorderLayout.SOUTH);

        // Animazione selezione
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (selectedPanels.contains(panel)) {
                    selectedPanels.remove(panel);
                    selectedCharacters.remove(name);
                    panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                } else if (selectedPanels.size() < Game.MAX_ALLIES_PER_ROUND) {
                    selectedPanels.add(panel);
                    selectedCharacters.add(name);
                    panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3)); // glow selezione
                }
                nextButton.setEnabled(selectedPanels.size() == Game.MAX_ALLIES_PER_ROUND);
            }

            public void mouseEntered(MouseEvent e) {
                if (!selectedPanels.contains(panel)) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!selectedPanels.contains(panel)) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                }
            }
        });

        gbc.gridx = x;
        gbc.gridy = y;
        bgLabel.add(panel, gbc);
    }

    public void addNextButtonListener(ActionListener listener) {
        nextButton.addActionListener(listener);
    }

    public List<String> getSelectedCharacterNames() {
        return new ArrayList<>(selectedCharacters);
    }

    public void close() {
        frame.dispose();
    }
}