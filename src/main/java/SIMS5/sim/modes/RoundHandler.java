package SIMS5.sim.modes;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.networkFiles.Networks;
import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.entitiys.MyEntity;
import SIMS5.sim.enviroment.Field;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.network.handling.NetworkCreator;

public abstract class RoundHandler {

    protected LightData light;
    protected Field field; 
    protected NetworkCreator networkCreator;
    protected Networks network;
    protected List<MyEntity> bodys = new ArrayList<>();


    public RoundHandler(Profile profile, LightData lightdata) {
        network = new Networks(profile);
        field = new Field(profile);
        networkCreator = new NetworkCreator(profile);
        light = lightdata;
    }
}
