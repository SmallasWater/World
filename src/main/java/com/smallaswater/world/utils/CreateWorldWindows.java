package com.smallaswater.world.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import com.smallaswater.world.World;

import java.util.LinkedList;

/**
 * @author SmallasWater
 * Create on 2020/12/5 22:20
 * Package com.smallaswater.world.utils
 */
public class CreateWorldWindows {

    public static final int WORLDS = 0x551AA01;


    public static void sendWorlds(Player player){
        FormWindowSimple simple = new FormWindowSimple(World.getInstance().getLanguage().getString("title"),"");
        for(WorldConfig config:World.getInstance().getWorldConfigByWindows()){
            Level level = Server.getInstance().getLevelByName(config.getLevelName());
            if(level == null){
                continue;
            }
            String message = World.getInstance().replaceMessage(config,World.getInstance().
                    getLanguage().getString("button-message"));
            simple.addButton(
                    new ElementButton(message,new ElementButtonImageData("path","textures/ui/icon_recipe_nature")));
        }
        player.showFormWindow(simple,WORLDS);
    }

}
