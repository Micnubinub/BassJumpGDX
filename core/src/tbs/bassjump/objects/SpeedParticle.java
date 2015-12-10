package tbs.bassjump.objects;


import tbs.bassjump.GameValues;
import tbs.bassjump.ScreenDimen;
import tbs.bassjump.Utility;
import tbs.bassjump.utility.GameObject;

public class SpeedParticle extends GameObject {

    public void update() {
        yPos += GameValues.SPEED_FACTOR * 2.25f;
        if (yPos > ScreenDimen.height)
            yPos = Utility.randInt(-(GameValues.SPEED_PARTICLE_HEIGHT * 2),
                    -GameValues.SPEED_PARTICLE_HEIGHT);
    }
}
