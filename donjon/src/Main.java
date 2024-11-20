import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    JFrame displayZoneFrame;

    public Main() {
        // Initialisation de la fenêtre principale
        displayZoneFrame = new JFrame("Zelda");
        displayZoneFrame.setSize(600, 600);
        displayZoneFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Afficher l'écran titre au démarrage
        showTitleScreen();

        // Rendre la fenêtre visible
        displayZoneFrame.setVisible(true);
    }

    private void showGameOverScreen() {
        SwingUtilities.invokeLater(() -> {
            GameOverScreen gameOverScreen = new GameOverScreen(displayZoneFrame);
            displayZoneFrame.getContentPane().removeAll();
            displayZoneFrame.add(gameOverScreen);
            displayZoneFrame.revalidate();
            displayZoneFrame.repaint();
        });
    }

    private void showTitleScreen() {
        // Affichage de l'écran titre
        TitleScreen titleScreen = new TitleScreen(displayZoneFrame);
        displayZoneFrame.getContentPane().removeAll(); // Nettoyer l'ancien contenu
        displayZoneFrame.add(titleScreen); // Ajouter l'écran titre
        displayZoneFrame.revalidate(); // Revalider les composants
        displayZoneFrame.repaint(); // Redessiner l'interface
    }

    public void startGame() throws Exception {
        // Initialisation du jeu
        DynamicSprite hero = new DynamicSprite(200, 300,
                ImageIO.read(new File("./img/heroTileSheetLowRes.png")), 48, 50);

        // Configurer le callback pour la mort
        hero.setOnDeathCallback(() -> {
            System.out.println("Le personnage est mort !");
            showGameOverScreen(); // Afficher l'écran Game Over
        });

        RenderEngine renderEngine = new RenderEngine(displayZoneFrame);
        PhysicEngine physicEngine = new PhysicEngine();
        GameEngine gameEngine = new GameEngine(hero);

        // Timers pour les moteurs
        Timer renderTimer = new Timer(50, (e) -> renderEngine.update());
        Timer gameTimer = new Timer(50, (e) -> gameEngine.update());
        Timer physicTimer = new Timer(50, (e) -> physicEngine.update());

        renderTimer.start();
        gameTimer.start();
        physicTimer.start();

        // Nettoyer l'écran titre et ajouter le jeu
        displayZoneFrame.getContentPane().removeAll();
        displayZoneFrame.add(renderEngine);
        displayZoneFrame.revalidate();
        displayZoneFrame.repaint();

        // Chargement des éléments du niveau
        Playground level = new Playground("./data/level1.txt");
        renderEngine.addToRenderList(level.getSpriteList());
        renderEngine.addToRenderList(hero);

        // Configuration des moteurs
        physicEngine.addToMovingSpriteList(hero);
        physicEngine.setEnvironment(level.getSolidSpriteList());

        // Ajouter le KeyListener pour le contrôle du personnage
        displayZoneFrame.addKeyListener(gameEngine);

        // Focus sur la fenêtre pour capturer les entrées clavier
        displayZoneFrame.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Main();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}