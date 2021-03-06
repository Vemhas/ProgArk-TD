package no.ntnu.tdt4240.g25.td.model.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import no.ntnu.tdt4240.g25.td.model.entity.components.BoundsComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.DamageComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.ExpireComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.MobComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.PositionComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.ProjectileComponent;

@All({ProjectileComponent.class, PositionComponent.class})
public class CollisionSystem extends IteratingSystem {

    public static int POSITION_CHECK_THRESHOLD = 3;

    private ComponentMapper<ProjectileComponent> mProjectile;
    private ComponentMapper<PositionComponent> mPosition;
    private ComponentMapper<BoundsComponent> mBounds;
    private ComponentMapper<DamageComponent> mDamage;
    private EntitySubscription mobSubscription;

    @Override
    protected void initialize() {
        super.initialize();
        mobSubscription =
                world.getAspectSubscriptionManager().get(Aspect.all(
                        MobComponent.class, PositionComponent.class, BoundsComponent.class // Using texture compo for now
                ).exclude(ExpireComponent.class));
    }

    @Override
    protected void process(int entityId) {
        ProjectileComponent projectile = mProjectile.get(entityId);
        PositionComponent position = mPosition.get(entityId);
        IntBag mobIds = mobSubscription.getEntities();

        for (int i = 0; i < mobIds.size(); i++) {
            int mob = mobIds.get(i);
            PositionComponent mobPosition = mPosition.get(mob);
            if (position.get().dst(mobPosition.get()) > POSITION_CHECK_THRESHOLD) continue;
            BoundsComponent mobBounds = mBounds.get(mob);
            if (mobBounds != null && overlaps(position, mobBounds)) {
                DamageComponent damage = mDamage.create(mob);
                damage.damage += projectile.damage;

                // If the projectile has splash damage, apply it to all mobs in the area
                if (projectile.radius > 0) {
                    for (int j = 0; j < mobIds.size(); j++) {
                        int nearbyMob = mobIds.get(j);
                        if (nearbyMob == mob) continue;
                        PositionComponent nearbyMobPosition = mPosition.get(nearbyMob);

                        if (nearbyMobPosition.get().dst(mobPosition.get()) > POSITION_CHECK_THRESHOLD) continue;
                        if (nearbyMobPosition.get().dst(position.get()) > projectile.radius) continue;

                        BoundsComponent nearbyMobBounds = mBounds.get(nearbyMob);
                        Circle splashArea = new Circle(position.get(), projectile.radius);
                        if (nearbyMobBounds != null && overlaps(splashArea, nearbyMobBounds)) {
                            applyDamage(nearbyMob, projectile.damage);
                        }
                    }
                }
                world.getEntity(entityId).deleteFromWorld();
            }
        }
    }

    private void applyDamage(int mob, float damage) {
        DamageComponent mobDamage = mDamage.create(mob);
        mobDamage.damage += damage;
    }

    private boolean overlaps(PositionComponent projectile, BoundsComponent mobBounds) {
        return mobBounds.get().contains(projectile.get());
    }
    private boolean overlaps(Circle projectile, BoundsComponent mobBounds) {
        return Intersector.overlaps(projectile, mobBounds.get());
    }
}
