package inventoryPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Inventory inv;
    @Override
    public void onEnable(){
        this.getCommand("changename").setExecutor(this);
        this.getCommand("inv").setExecutor(this);
        PluginDescriptionFile file = this.getDescription();
        System.out.println(file.getName() + "version:" + file.getVersion() + " loaded");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings){
        if (cmd.getName().equals("changename")){
            if (sender instanceof Player){
                ((Player) sender).setDisplayName(ChatColor.GOLD + strings[0] + ChatColor.WHITE);
                ((Player) sender).setPlayerListName(ChatColor.GOLD + strings[0]);
                ((Player) sender).setCustomName(ChatColor.GOLD + strings[0]);
                ((Player) sender).setCustomNameVisible(true);
            }
        }else if (cmd.getName().equals("inv")){

        }
        return false;
    }

    @Override
    public void onDisable(){
        PluginDescriptionFile file = this.getDescription();
        System.out.println(file.getName() + "version:" + file.getVersion() + " unloaded");
    }
}
