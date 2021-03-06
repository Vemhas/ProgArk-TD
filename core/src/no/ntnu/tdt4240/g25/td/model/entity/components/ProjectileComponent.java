package no.ntnu.tdt4240.g25.td.model.entity.components;

import com.artemis.PooledComponent;

public class ProjectileComponent extends PooledComponent {
    public float damage;
    public float radius; // radius of the explosion, if any

    public void set(float damage, float radius) {
        this.damage = damage;
        this.radius = radius;
    }

    @Override
    protected void reset() {
        damage = 0;
        radius = 0;
    }
}
