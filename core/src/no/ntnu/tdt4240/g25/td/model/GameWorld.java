package no.ntnu.tdt4240.g25.td.model;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.mostlyoriginal.api.SingletonPlugin;

import no.ntnu.tdt4240.g25.td.TdGame;
import no.ntnu.tdt4240.g25.td.controller.GameController;
import no.ntnu.tdt4240.g25.td.model.entity.factories.MobFactory;
import no.ntnu.tdt4240.g25.td.model.entity.factories.ProjectileFactory;
import no.ntnu.tdt4240.g25.td.model.entity.factories.TowerFactory;
import no.ntnu.tdt4240.g25.td.model.entity.systems.AimingSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.BuyUpgradeManager;
import no.ntnu.tdt4240.g25.td.model.entity.systems.EventHandler;
import no.ntnu.tdt4240.g25.td.model.entity.systems.WaveSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.MapManager;
import no.ntnu.tdt4240.g25.td.model.entity.systems.render.AnimationSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.BoundsSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.CollisionSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.DamageSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.ExpireSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.FindTargetSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.FiringSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.render.HealthRenderSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.render.MapRenderSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.MovementSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.PathingSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.MyCameraSystem;
import no.ntnu.tdt4240.g25.td.model.entity.systems.render.RenderSystem;
import no.ntnu.tdt4240.g25.td.view.GameView;

public class GameWorld {

    public final static int WORLD_WIDTH = 9;
    public final static int WORLD_HEIGHT = 16;

    World world;

    private final GameController.GameWorldCallback controller;
    private final GameView.GameViewCallback view;

    public GameWorld(
            ShapeRenderer renderer,
            SpriteBatch batch,
            GameController.GameWorldCallback controller,
            GameView.GameViewCallback view
    ) {
        this.controller = controller;
        this.view = view;
        createWorld(batch, renderer);
    }

    protected void createWorld(SpriteBatch batch, ShapeRenderer renderer) {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .dependsOn(
                        EntityLinkManager.class,
                        SingletonPlugin.class)
                .with(
                        // Managers who need to initialize Singleton Components etc.
                        new MapManager(),
                        new EventHandler(),
                        new BuyUpgradeManager(),

                        // Game systems
                        new WaveSystem(),
                        new MovementSystem(),
                        new PathingSystem(),
                        new BoundsSystem(),
                        new CollisionSystem(),
                        new FindTargetSystem(1f / 60),
                        new AimingSystem(),
                        new FiringSystem(),
                        new DamageSystem(),
                        new ExpireSystem(),
                        //new ViewManager(),

                        // Renders
                        new MyCameraSystem(TdGame.UI_WIDTH, TdGame.UI_HEIGHT),
                        new AnimationSystem(),

                        // Renders
                        new MapRenderSystem(),
                        new RenderSystem(),
                        new HealthRenderSystem(),
                        //new WidgetRenderSystem(),
                        //new DebugRenderSystem(),

                        // Factories
                        new TowerFactory(),
                        new MobFactory(),
                        new ProjectileFactory()
                )
                .build()
                .register(controller)
                .register(view)
                .register(batch)
                .register(renderer);

        this.world = new World(config);
        // set world for the factories to be able to create entities
    }

    public void update(float delta) {
        world.setDelta(delta);
        world.process();
    }

    public void dispose() {
        world.dispose();
    }

    public void resize(int width, int height) {
        world.getSystem(MyCameraSystem.class).updateViewports(width, height);
    }

    public void clickOnWorld(int screenX, int screenY) {
        world.getSystem(EventHandler.class).receiveClick(screenX, screenY);
    }
    // There's probably a better way to do this with enums, but I'm not sure how to do it
    public void createTower1() {
        world.getSystem(BuyUpgradeManager.class).buyTower1();
    }

    public void createTower2() {
        world.getSystem(BuyUpgradeManager.class).buyTower2();
    }

    public void upgradeSelectedTower() {
        world.getSystem(BuyUpgradeManager.class).upgradeTower();
    }

    public int getScore() {
        return world.getSystem(EventHandler.class).getScore();
    }
}
