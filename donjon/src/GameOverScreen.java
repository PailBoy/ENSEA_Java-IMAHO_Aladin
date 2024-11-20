import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameOverScreen extends JPanel {
    private final JFrame parentFrame;

    public GameOverScreen(JFrame parentFrame) {
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

        // Titre "Game Over"
        JLabel gameOverLabel = new JLabel("Game Over", JLabel.CENTER);
        gameOverLabel.setFont(pixelFont);
        this.add(gameOverLabel, BorderLayout.CENTER);

        // Bouton pour retourner à l'écran titre
        JButton restartButton = new JButton("Restart");
        restartButton.setFont(pixelFont.deriveFont(20f));
        restartButton.addActionListener(e -> showTitleScreen());

        // Ajouter le bouton
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showTitleScreen() {
        parentFrame.getContentPane().removeAll();
        TitleScreen titleScreen = new TitleScreen(parentFrame);
        parentFrame.add(titleScreen);
        parentFrame.revalidate();
        parentFrame.repaint();
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