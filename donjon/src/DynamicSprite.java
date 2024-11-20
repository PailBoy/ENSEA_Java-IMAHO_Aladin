import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite {
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private double timeBetweenFrame = 250;
    private boolean isWalking = true;
    private final int spriteSheetNumberOfColumn = 10;

    // Attributs de santé
    private int maxHealth = 100;
    private int currentHealth = 100;
    private Runnable onDeathCallback; // Callback pour la mort du personnage

    public void setOnDeathCallback(Runnable onDeathCallback) {
        this.onDeathCallback = onDeathCallback;
    }


    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
    }

    // Getter et setter pour la santé
    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = Math.max(0, Math.min(currentHealth, maxHealth)); // Limite entre 0 et maxHealth
        if (this.currentHealth == 0 && onDeathCallback != null) {
            onDeathCallback.run(); // Appeler le callback quand la santé tombe à 0
        }
    }

    public void checkCollisionWithTraps(ArrayList<Sprite> environment) {
        for (Sprite sprite : environment) {
            if (sprite instanceof TrapSprite) {
                TrapSprite trap = (TrapSprite) sprite;
                if (this.getHitBox().intersects(trap.getHitBox())) {
                    this.takeDamage(trap.getDamage());
                    System.out.println("Collision avec un piège ! Santé : " + this.getCurrentHealth());
                }
            }
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }


    private boolean isMovingPossible(ArrayList<Sprite> environment){
        Rectangle2D.Double moved = new Rectangle2D.Double();
        switch(direction){
            case EAST: moved.setRect(super.getHitBox().getX()+speed,super.getHitBox().getY(),
                                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case WEST:  moved.setRect(super.getHitBox().getX()-speed,super.getHitBox().getY(),
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case NORTH:  moved.setRect(super.getHitBox().getX(),super.getHitBox().getY()-speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case SOUTH:  moved.setRect(super.getHitBox().getX(),super.getHitBox().getY()+speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
        }

        for (Sprite s : environment){
            if ((s instanceof SolidSprite) && (s!=this)){
                if (((SolidSprite) s).intersect(moved)){
                    return false;
                }
            }
        }
        return true;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void move(){
        switch (direction){
            case NORTH -> {
                this.y-=speed;
            }
            case SOUTH -> {
                this.y+=speed;
            }
            case EAST -> {
                this.x+=speed;
            }
            case WEST -> {
                this.x-=speed;
            }
        }
    }

    public void moveIfPossible(ArrayList<Sprite> environment){
        if (isMovingPossible(environment)){
            move();
        }
    }

    @Override
    public void draw(Graphics g) {
        // Dessin du sprite
        int index = (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn);
        g.drawImage(image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (index * this.width), (int) (direction.getFrameLineNumber() * height),
                (int) ((index + 1) * this.width), (int) ((direction.getFrameLineNumber() + 1) * this.height), null);

        // Dessin de la barre de vie
        int barWidth = (int) width; // Largeur de la barre de vie
        int barHeight = 5; // Hauteur de la barre de vie
        int barX = (int) x; // Position X de la barre
        int barY = (int) y - barHeight - 5; // Position Y (au-dessus du sprite)

        // Dessiner le contour de la barre
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);

        // Dessiner la santé actuelle (en vert)
        int healthWidth = (int) ((currentHealth / (double) maxHealth) * barWidth); // Largeur proportionnelle à la santé
        g.setColor(Color.GREEN);
        g.fillRect(barX + 1, barY + 1, healthWidth, barHeight - 1);
    }


    public void takeDamage(int damage) {
        setCurrentHealth(currentHealth - damage);
    }

    public void heal(int amount) {
        setCurrentHealth(currentHealth + amount);
    }


}
