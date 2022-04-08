package no.ntnu.tdt4240.g25.td.model.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import no.ntnu.tdt4240.g25.td.model.entity.components.TransformComponent;
import no.ntnu.tdt4240.g25.td.model.entity.components.SpriteComponent;

public class RenderSystem extends SortedIteratingSystem {

    private final ComponentMapper<SpriteComponent> spriteMapper;
    private final ComponentMapper<TransformComponent> positionMapper;


    private final SpriteBatch batch;
    private final Array<Entity> renderQueue;
    private final OrthographicCamera cam;
    private final Comparator<Entity> comparator;

    public RenderSystem(SpriteBatch batch) {
        super(Family.all(SpriteComponent.class, TransformComponent.class).get(), new ZComparator());
        this.batch = batch;
        comparator = new ZComparator();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderQueue = new Array<>();
        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
        positionMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.update(deltaTime);
        renderQueue.sort(comparator);
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();
        for (Entity e : new Array.ArrayIterator<>(renderQueue)) {
            SpriteComponent sprite = spriteMapper.get(e);
            TransformComponent position = positionMapper.get(e);
            if (sprite.region == null) {
                continue;
            }

            float width = sprite.region.getRegionWidth();
            float height = sprite.region.getRegionHeight();
            float originX = width/2f;
            float originY = height/2f;

            batch.draw(sprite.region,
                    position.position.x - originX, position.position.y - originY,
                    originX, originY,
                    width, height,
                    1, 1,
                    position.rotation - 90);

        }
        batch.end();
        renderQueue.clear();
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }
}