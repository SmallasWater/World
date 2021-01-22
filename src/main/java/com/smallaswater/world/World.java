package com.smallaswater.world;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.smallaswater.world.utils.CreateWorldWindows;
import com.smallaswater.world.utils.WorldConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author SmallasWater
 * Create on 2020/12/5 22:18
 * Package com.smallaswater.world
 */
public class World extends PluginBase implements Listener {

    private Config language;

    private static World instance;

    private LinkedList<WorldConfig> worldConfigs = new LinkedList<>();



    @Override
    public void onEnable() {
        this.getLogger().info("多世界插件加载成功");
        instance = this;
        init();
        this.getServer().getPluginManager().registerEvents(this,this);
    }


    public LinkedList<WorldConfig> getWorldConfigByWindows(){
        LinkedList<WorldConfig> worldConfigs = new LinkedList<>();
        for(WorldConfig config: this.worldConfigs){
            Level level = Server.getInstance().getLevelByName(config.getLevelName());
            if(level == null || config.isPrivate()){
                continue;
            }
            worldConfigs.add(config);
        }
        return worldConfigs;
    }

    private void init(){
        worldConfigs = new LinkedList<>();
        this.saveDefaultConfig();
        this.reloadConfig();
        loadLevel();
        if(!new File(this.getDataFolder()+"/language.yml").exists()){
            this.saveResource("language.yml",false);
        }
        language = new Config(new File(this.getDataFolder()+"/language.yml"),Config.YAML);
        for(String worldNames: getWorlds()){
            worldConfigs.add(WorldConfig.getInstance(worldNames));
        }
    }

    public LinkedList<WorldConfig> getWorldConfigs() {
        return worldConfigs;
    }

    public static World getInstance() {
        return instance;
    }

    public Config getLanguage() {
        return language;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isPlayer()) {
            if (args.length > 0) {
                if (sender.hasPermission("World.reload")) {
                    if ("reload".equalsIgnoreCase(args[0])) {
                        init();
                        sender.sendMessage(TextFormat.colorize('&', "&e重新读取配置文件成功"));
                        return true;
                    }
                }
            }
            CreateWorldWindows.sendWorlds((Player) sender);
        } else {
            sender.sendMessage(TextFormat.colorize('&', "&c请不要用控制台执行"));
        }
        return true;

    }

    public WorldConfig getWorldConfig(String worldName){
        if(worldConfigs.contains(new WorldConfig(worldName))){
            return worldConfigs.get(worldConfigs.indexOf(new WorldConfig(worldName)));
        }
        return null;
    }

    private ArrayList<String> getWorlds(){
        ArrayList<String> worlds = new ArrayList<>();
        File file = new File(this.getServer().getFilePath()+"/worlds");
        File[] s = file.listFiles();
        if(s != null) {
            for (File file1 : s) {
                if (file1.isDirectory()) {
                    worlds.add(file1.getName());

                }
            }
        }
        return worlds;
    }




    private void loadLevel(){
        for(String worldName: getWorlds()){
            if(!this.getServer().isLevelLoaded(worldName)){
                if(this.getServer().isLevelGenerated(worldName)){
                    this.getLogger().info("已加载地图:"+worldName);
                    this.getServer().loadLevel(worldName);
                }
            }
        }
    }


    @EventHandler
    public void onWindowsListener(PlayerFormRespondedEvent event){
        Player player = event.getPlayer();
        if(event.getFormID() == CreateWorldWindows.WORLDS){
            if(event.getWindow() instanceof FormWindowSimple){
                if(event.getResponse() == null){
                    return;
                }
                FormWindowSimple window = (FormWindowSimple) event.getWindow();
                WorldConfig config = getWorldConfigs().get(window.getResponse().getClickedButtonId());
                if(config == null){
                    return;
                }
                Level level = Server.getInstance().getLevelByName(config.getLevelName());
                if(level == null){
                    return;
                }
                player.teleport(level.getSpawnLocation());
                if(!config.isShowMessage()){
                    return;
                }
                String message = replaceMessage(config,config.getMessage());
                switch (config.getType()){
                    case TITLE:
                        String title;
                        String sub = "";
                        if(message.contains("{n}")){
                            title = message.substring(0,message.indexOf("{n}"));
                            sub = message.substring(message.indexOf("{n}") + 3);
                        }else{
                            title = message;
                        }
                        player.sendTitle(title,sub);
                        break;
                    case TIP:
                        player.sendTip(message);
                        break;
                    case POPUP:
                        player.sendPopup(message);
                        break;
                    case MESSAGE:
                        player.sendMessage(message);
                        break;default:break;
                }

            }
        }

    }
    public String replaceMessage(WorldConfig config,String message){
        Level level = Server.getInstance().getLevelByName(config.getLevelName());
        if(level == null){
            return config.getName()+"未加载";
        }
        message = TextFormat.colorize('&',message);
        return message
                .replace("{w-name}",config.getName())
                .replace("{w-players}",level.getPlayers().size()+"");

    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event){
        Player player = event.getPlayer();
        Level level = Server.getInstance().getLevelByName(getConfig().getString("spawn-world"));
        if(level != null){
            player.teleport(level.getSafeSpawn());
        }
    }
}
