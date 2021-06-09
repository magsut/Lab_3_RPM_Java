import java.util.List;
import java.util.Objects;

public class Entity {
    private static int idCounter = 1;
    protected final long id;
    protected transient World world;
    protected String title;
    protected double posX;
    protected double posZ;
    protected boolean aggressive;
    protected int maxHealth;
    protected int health;
    protected int attackDamage;
    protected transient Entity target;
    protected boolean death = false;

    public Entity(World world, String title, double posX, double posZ, boolean agressive, int maxHealth, int health, int attackDamage) {
        this.world = world;
        this.title = title;
        this.posX = posX;
        this.posZ = posZ;
        this.aggressive = agressive;
        this.maxHealth = maxHealth;
        this.health = health;
        this.attackDamage = attackDamage;
        this.id = idCounter++;
    }

    public Entity(String title, double posX, double posZ, boolean aggressive, int maxHealth, int health, int attackDamage, boolean death) {
        this.title = title;
        this.posX = posX;
        this.posZ = posZ;
        this.aggressive = aggressive;
        this.maxHealth = maxHealth;
        this.health = health;
        this.attackDamage = attackDamage;
        this.id = idCounter++;
        this.world = GameServer.getInstance().getServerWorld();
    }

    public void update() {
        if(aggressive)
        {
            if(target == null)
                searchTarget();
            if(target != null)
            {
                double distance = (Math.pow((target.posX-this.posX),2)+Math.pow((target.posZ-this.posZ),2));
                if(distance >= 4)
                {
                    if(this.posX != target.posX)
                    {
                        this.posX += (target.posX-this.posX)/(Math.abs(target.posX-this.posX));
                    }
                    if(this.posZ != target.posZ)
                    {
                        this.posZ += (target.posZ-this.posZ)/(Math.abs(target.posZ-this.posZ));
                    }
                }
                else
                    attack(target);
            }
        }
    }

    public void searchTarget() {
        List<Entity> targetEntity = world.getEntitiesNearEntity(this,20);
        for(int i = targetEntity.size() - 1; i >= 0; i--)
        {
            if(!targetEntity.get(i).aggressive)
            {
                target = targetEntity.get(i);
                break;
            }
        }
    }

    public void attack(Entity entity){
        if(entity instanceof EntityPlayer)
        {
            if(!entity.death)
            {
                entity.health -= this.attackDamage + 0.5 * GameServer.getInstance().getServerConfig().getDifficulty();
                if(entity.health > 0)
                {
                    this.health -= entity.attackDamage + 0.5 * GameServer.getInstance().getServerConfig().getDifficulty();
                }
                else
                {
                    System.out.println("Player " + ((EntityPlayer) entity).getNickname() + " was killed by " + this.title + " on server update " + GameServer.getInstance().getServerTicks());
                    entity.death = true;
                    this.target = null;
                }
                if(this.health <= 0)
                {
                    this.death = true;
                    System.out.println("Player " + ((EntityPlayer) entity).getNickname() + " killed entity " + this.title + " on server update " + GameServer.getInstance().getServerTicks());
                }
            }

        }
        else
        {
            if(!entity.death)
            {
                entity.health -= this.attackDamage + 0.5 * GameServer.getInstance().getServerConfig().getDifficulty();
                if(entity.health <= 0)
                {
                    entity.death = true;
                    this.target = null;
                    System.out.println("Entity " + this.title + " killed entity " + entity.title + " on server update " + GameServer.getInstance().getServerTicks());
                }
            }

        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id && Double.compare(entity.posX, posX) == 0 && Double.compare(entity.posZ, posZ) == 0 && aggressive == entity.aggressive && maxHealth == entity.maxHealth && health == entity.health && attackDamage == entity.attackDamage && Objects.equals(world, entity.world) && Objects.equals(title, entity.title) && Objects.equals(target, entity.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, world, title, posX, posZ, aggressive, maxHealth, health, attackDamage, target);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posX=" + posX +
                ", posZ=" + posZ +
                ", agressive=" + aggressive +
                ", maxHealth=" + maxHealth +
                ", health=" + health +
                ", attackDamage=" + attackDamage +
                ", target=" + target +
                '}';
    }


}

