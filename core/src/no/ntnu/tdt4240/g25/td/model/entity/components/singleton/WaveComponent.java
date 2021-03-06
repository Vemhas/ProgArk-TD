package no.ntnu.tdt4240.g25.td.model.entity.components.singleton;

import com.artemis.Component;

import net.mostlyoriginal.api.Singleton;

@Singleton
public class WaveComponent extends Component {

    public final static int baseNormalEnemies = 3;
    public final static int baseTankEnemies = 1;
    public final static float waveMultiplier = 1.5f; // multiplier for number of enemies in next wave

    public final static float PAUSE_DURATION = 10f;

    public final static float NORMAL_COOLDOWN = 1f;
    public final static float TANK_COOLDOWN = 2f;

    public final static int NUM_NORMAL_PER_CLUSTER = 4;
    public final static int NUM_TANK_PER_CLUSTER = 2;
    public final static float CLUSTER_INTERVAL = 2f;

    public float time = 0;
    public int numberOfWaves = 0;
    public boolean active = false;
    public boolean enemiesAlive = false;

    public float normalEnemyCooldown = 0;
    public float tankEnemyCooldown = 0;

    public float numNormal = 0;
    public float numTank = 0;
    public int remainingNormalEnemies = 0;
    public int remainingTankEnemies = 0;
}
