import java.lang.reflect.Array;
import java.util.ArrayList;

public class PhysicEngine implements Engine {
    private ArrayList<DynamicSprite> movingSpriteList;
    private ArrayList<Sprite> environment;

    public PhysicEngine() {
        movingSpriteList = new ArrayList<>();
        environment = new ArrayList<>();
    }

    public void addToEnvironmentList(Sprite sprite) {
        if (!environment.contains(sprite)) {
            environment.add(sprite);
        }
    }

    public void setEnvironment(ArrayList<Sprite> environment) {
        this.environment = environment;
    }

    public void addToMovingSpriteList(DynamicSprite sprite) {
        if (!movingSpriteList.contains(sprite)) {
            movingSpriteList.add(sprite);
        }
    }

    @Override
    public void update() {
        for (DynamicSprite dynamicSprite : movingSpriteList) {
            // Déplacement du personnage s'il peut bouger
            dynamicSprite.moveIfPossible(environment);

            // Vérification des collisions avec les pièges
            for (Sprite sprite : environment) {
                if (sprite instanceof TrapSprite) {
                    TrapSprite trap = (TrapSprite) sprite;
                    if (dynamicSprite.getHitBox().intersects(trap.getHitBox())) {
                        // Appliquer les dégâts au personnage
                        dynamicSprite.takeDamage(trap.getDamage());
                        System.out.println("Collision avec un piège ! Santé actuelle : " + dynamicSprite.getCurrentHealth());
                    }
                }
            }
        }
    }
}