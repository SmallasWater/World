package com.smallaswater.world.utils;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;



/**
 * @author SmallasWater
 * Create on 2020/12/6 12:58
 * Package com.smallaswater.world.utils
 */
public class DataTools {


    public static Position getPositionByString(String pos){
        String[] posList = pos.split(":");
        if(posList.length > 3){
            String level = posList[3];
            Level level1 = Server.getInstance().getLevelByName(level);
            if(level1 != null){
                return new Position(Double.parseDouble(posList[0])
                        ,Double.parseDouble(posList[1])
                        ,Double.parseDouble(posList[2]),level1);

            }

        }
        return null;
    }

}
