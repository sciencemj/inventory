package inventoryPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private Inventory inv;
    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(this,this);
        this.getCommand("changename").setExecutor(this);
        this.getCommand("inv").setExecutor(this);
        PluginDescriptionFile file = this.getDescription();
        System.out.println(file.getName() + "version:" + file.getVersion() + " loaded");
    }

    public ItemStack createItem(Material material, String name){
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
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
            inv = Bukkit.createInventory(null, 9, "inv");
            inv.addItem(createItem(Material.EMERALD, ChatColor.GREEN + "다이아 => 에메랄드"));
            inv.addItem(createItem(Material.DIAMOND, ChatColor.GREEN + "다이아 <= 에메랄드"));
            Player p = Bukkit.getPlayer(strings[0]);
            p.openInventory(inv);
        }
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        //System.out.println("아이템 움직임");
        if (e.getInventory() == inv){
            for (HumanEntity h : e.getInventory().getViewers()){
                h.getWorld().playSound(h.getLocation(),Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10,10);
                if (h.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND, 1), 1) && e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "다이아 => 에메랄드")) {
                    h.getInventory().removeItem(new ItemStack(Material.DIAMOND, 1));
                    h.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
                }else if (h.getInventory().containsAtLeast(new ItemStack(Material.EMERALD, 1), 1) && e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "다이아 <= 에메랄드")){
                    h.getInventory().removeItem(new ItemStack(Material.EMERALD, 1));
                    h.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                }
            }
            e.setCancelled(true);
        }
    }

    @Override
    public void onDisable(){
        PluginDescriptionFile file = this.getDescription();
        System.out.println(file.getName() + "version:" + file.getVersion() + " unloaded");
    }
}
