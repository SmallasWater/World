package com.smallaswater.world.utils;

import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import com.smallaswater.world.World;

import java.util.LinkedList;
import java.util.List;

/**
 * @author SmallasWater
 * Create on 2020/12/5 22:34
 * Package com.smallaswater.world.utils
 */
public class WorldConfig {

    private String levelName;

    private boolean showMessage = true;

    private String name;


    private WorldMessageType type;

    private boolean isPrivate = false;


    private String message;

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public WorldConfig(String levelName){
        this.levelName = levelName;
        this.type = WorldMessageType.TITLE;
    }

    private WorldConfig(String levelName, WorldMessageType type){
        this.levelName = levelName;
        this.type = type;
    }

    public static WorldConfig getInstance(String worldNames){
        WorldConfig worldConfig = new WorldConfig(worldNames,WorldConfig.WorldMessageType.TITLE);
        if(World.getInstance().getConfig().exists("worlds-name."+worldNames+".message-type")){
            worldConfig.setType(WorldConfig.getWorldMessageTypeByName(
                    World.getInstance().getConfig().getString("worlds-name."+worldNames+".message-type")));
        }else if(World.getInstance().getConfig().exists("worlds-name.default.message-type")){
            worldConfig.setType(WorldConfig.getWorldMessageTypeByName(
                    World.getInstance().getConfig().getString("worlds-name.default.message-type")));
        }
        if(World.getInstance().getConfig().exists("worlds-name."+worldNames+".showMessage")){
            worldConfig.setShowMessage(World.getInstance().getConfig().getBoolean("worlds-name."+worldNames+".showMessage",true));
        }else if(World.getInstance().getConfig().exists("worlds-name.default.showMessage")){
            worldConfig.setShowMessage(World.getInstance().getConfig().getBoolean("worlds-name.default.showMessage",true));
        }
        if(World.getInstance().getConfig().exists("worlds-name."+worldNames+".message")){
            worldConfig.setMessage(World.getInstance().getConfig().getString("worlds-name."+worldNames+".message"));
        }else if(World.getInstance().getConfig().exists("worlds-name.default.message")){
            worldConfig.setMessage(World.getInstance().getConfig().getString("worlds-name.default.message"));
        }else{
            worldConfig.setMessage(World.getInstance().getLanguage().getString("transfer-message"));
        }
        if(World.getInstance().getConfig().exists("worlds-name."+worldNames+".name")){
            worldConfig.setName(World.getInstance().getConfig().getString("worlds-name."+worldNames+".name"));
        }else{
            worldConfig.setName(worldNames);
        }
//        if(World.getInstance().getConfig().exists("worlds-name."+worldNames+".framepos")){
//            worldConfig.setFramepos(getPosByStringList(World.getInstance().getConfig().getStringList("worlds-name."+worldNames+".framepos")));
//        }
        if(World.getInstance().getConfig().getStringList("private-worlds").contains(worldNames)){
            worldConfig.setPrivate();
        }
        return worldConfig;
    }

//    private void setFramepos(LinkedList<Position> framepos) {
//        this.framepos = framepos;
//    }

    private static LinkedList<Position> getPosByStringList(List<String> list){
        LinkedList<Position> positions = new LinkedList<>();
        for(String s:list){
            Position position = DataTools.getPositionByString(s);
            if(position != null){
                positions.add(position);
            }
        }
        return positions;
    }

    public boolean isPrivate() {
        return isPrivate;
    }


//    public boolean addFramePos(Position position){
//        if(!framepos.contains(position)){
//            return framepos.add(position);
//        }
//        return false;
//    }


    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    private void setType(WorldMessageType type) {
        this.type = type;
    }

    public String getLevelName() {
        return levelName;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private void setPrivate() {
        isPrivate = true;
    }

    public String getMessage() {
        return message;
    }

    public WorldMessageType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof WorldConfig){
            return ((WorldConfig) obj).levelName.equalsIgnoreCase(levelName);
        }
        return false;
    }

//    public LinkedList<Position> getFramepos() {
//        return framepos;
//    }

    private static WorldMessageType getWorldMessageTypeByName(String name){
        WorldMessageType type;
        try{
            type = WorldMessageType.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException ignore){
            return WorldMessageType.TITLE;
        }
        return type;

    }

//    public LinkedList<String> posByString(){
//        LinkedList<String> pos = new LinkedList<>();
//        for(Position position:framepos){
//            pos.add(DataTools.positionToString(position));
//        }
//        return pos;
//    }

//    public void savePos(){
//        Config config = World.getInstance().getConfig();
//        config.set("worlds-name."+levelName+".framepos",posByString());
//        config.save();
//    }
    public enum WorldMessageType{
        /**
         * title类型
         * */
        TITLE,
        /**
         * 信息
         * */
        MESSAGE,
        /**
         * 底部tip
         * */
        TIP,
        /**
         * 底部 popup
         * */
        POPUP
    }
}
