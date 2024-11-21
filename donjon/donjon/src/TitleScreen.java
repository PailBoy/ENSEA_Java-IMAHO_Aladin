import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class TitleScreen extends JPanel {

    private final JFrame parentFrame;

    public TitleScreen(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setupUI();
    }

    private void setupUI() {
        this.setLayout(new BorderLayout());

        // Charger la police Pixelated
        Font pixelFont = loadCustomFont("./typo/PixelatedPusab.ttf", 36);
        if (pixelFont == null) {
            System.out.println("Police introuvable, utilisation de Arial par défaut.");
            pixelFont = new Font("Arial", Font.BOLD, 36); // Police de secours
        }

        // Titre du jeu
        JLabel title = new JLabel("Zelda", JLabel.CENTER);
        title.setFont(pixelFont);
        this.add(title, BorderLayout.NORTH);

        // Boutons
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        // Appliquer la police aux boutons
        startButton.setFont(pixelFont.deriveFont(20f));
        exitButton.setFont(pixelFont.deriveFont(20f));

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Action pour démarrer le jeu
        startButton.addActionListener(e -> {
            try {
                Main mainApp = new Main();
                mainApp.startGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Action pour quitter
        exitButton.addActionListener(e -> System.exit(0));
    }

    private Font loadCustomFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return font.deriveFont(size); // Taille personnalisée
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de la police : " + e.getMessage());
            return null; // Retourne null si la police ne peut pas être chargée
        }
    }
}

