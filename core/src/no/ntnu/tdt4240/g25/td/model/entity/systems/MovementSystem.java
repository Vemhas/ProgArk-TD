package no.ntnu.tdt4240.g25.td.model.entity.systems;


import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;

import no.ntnu.tdt4240.g25.td.model.entity.components.PositionComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.RotationComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.VelocityComponent;

@All({VelocityComponent.class, PositionComponent.class})
public class MovementSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> mPosition;
    private ComponentMapper<VelocityComponent> mVelocity;
    private ComponentMapper<RotationComponent> mRotation;

    @Override
    protected void process(int entityId) {
        final PositionComponent position = mPosition.get(entityId);
        final VelocityComponent velocity = mVelocity.get(entityId);
        final RotationComponent rotation = mRotation.has(entityId) ? mRotation.get(entityId) : null;

        float delta = this.world.getDelta();
        position.get().x += velocity.get().x * delta;
        position.get().y += velocity.get().y * delta;

        if (rotation != null) {
            // set rotation to the direction of movement in degrees, where 0 along the x-axis, pointing right.
            float newRotation = velocity.get().angleDeg();
            rotation.set(newRotation);
        }

    }
}
