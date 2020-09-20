package inventoryPlugin;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.PacketPlayOutAnimation;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {
    private String[] abillitylist = new String[]{"이도류", "슬로우", "폭파범", "흡혈귀", "방패병", "무능력", "무능력", "무능력", "무능력", "무능력"};
    private Inventory inv;
    private ItemStack menu;
    private ItemStack coin;
    private Location spawn;
    public static HashMap<Player, Integer> abillity = new HashMap<Player, Integer>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("changename").setExecutor(this);
        this.getCommand("inv").setExecutor(this);
        this.getCommand("spawnset").setExecutor(this);
        PluginDescriptionFile file = this.getDescription();
        System.out.println(file.getName() + "version:" + file.getVersion() + " loaded");


        inv = Bukkit.createInventory(null, 27, ChatColor.MAGIC + "MENU");
        itemFill(inv, createItem(Material.BLUE_STAINED_GLASS_PANE, " "), 9, 1);
        inv.addItem(createItem(Material.DIAMOND, ChatColor.GREEN + "다이아 => 코인5"));
        inv.addItem(createItem(Material.EMERALD, ChatColor.GREEN + "에메랄드 => 코인4"));
        inv.addItem(createItem(Material.GOLD_INGOT, ChatColor.GREEN + "금2 => 코인1"));
        inv.addItem(createItem(Material.IRON_INGOT, ChatColor.GREEN + "철4 => 코인1"));
        inv.addItem(createItem(Material.IRON_PICKAXE, ChatColor.GREEN + "철곡"));
        inv.addItem(createItem(Material.COMPASS, ChatColor.GREEN + "스폰"));
        inv.addItem(createItem(Material.PAPER, ChatColor.GREEN + "능력뽑기 고급(코인 15)"));
        inv.addItem(createItem(Material.PAPER, ChatColor.GREEN + "능력뽑기 중급(코인 10)"));
        inv.addItem(createItem(Material.PAPER, ChatColor.GREEN + "능력뽑기 하급(코인 5)"));
        itemFill(inv, createItem(Material.BLUE_STAINED_GLASS_PANE, " "), 27, 19);

        menu = createItem(Material.ENCHANTED_BOOK, "메뉴");
        coin = createItem(Material.GOLD_NUGGET, ChatColor.GOLD + "코인");
    }

    public ItemStack createItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        if (material.equals(Material.IRON_PICKAXE)) {
            meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void itemFill(Inventory inventory, ItemStack item, int count1, int count2) {
        if (count1 == count2 - 1) {
            return;
        }
        inventory.setItem(count1 - 1, item);
        itemFill(inventory, item, count1 - 1, count2);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
        if (cmd.getName().equals("changename")) {
            if (sender instanceof Player) {
                ((Player) sender).setDisplayName(ChatColor.GOLD + strings[0] + ChatColor.WHITE);
                ((Player) sender).setPlayerListName(ChatColor.GOLD + strings[0]);
                ((Player) sender).setCustomName(ChatColor.GOLD + strings[0]);
                ((Player) sender).setCustomNameVisible(true);
            }
        } else if (cmd.getName().equals("inv")) {
            if (sender instanceof Player) {
                Player p = ((Player) sender).getPlayer();
                p.getInventory().addItem(menu);
                if (strings.length == 1){
                    abillity.put(p, Integer.parseInt(strings[0]));
                }
            }
        } else if (cmd.getName().equals("spawnset")) {
            spawn = ((Player) sender).getLocation();
        }
        return false;
    }

    public void menuclick(HumanEntity h, ItemStack item) {
        h.getWorld().playSound(h.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1, 10);
        if (h.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND, 1), 1) && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "다이아 => 코인5")) {
            h.getInventory().removeItem(new ItemStack(Material.DIAMOND, 1));
            for (int i = 0; i < 5; i++)
                h.getInventory().addItem(coin);
        } else if (h.getInventory().containsAtLeast(new ItemStack(Material.EMERALD, 1), 1) && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "에메랄드 => 코인4")) {
            h.getInventory().removeItem(new ItemStack(Material.EMERALD, 1));
            for (int i = 0; i < 4; i++)
                h.getInventory().addItem(coin);
        } else if (h.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT, 2), 1) && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "금2 => 코인1")) {
            h.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 2));
            h.getInventory().addItem(coin);
        } else if (h.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT, 4), 1) && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "철4 => 코인1")) {
            h.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 4));
            h.getInventory().addItem(coin);
        } else if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "철곡")) {
            h.getInventory().addItem(createItem(Material.IRON_PICKAXE, ChatColor.GREEN + "철곡"));
        } else if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "스폰")) {
            h.teleport(spawn);
        } else if (h.getInventory().containsAtLeast(coin, 15) && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "능력뽑기 고급(코인 15)")) {
            for (int i = 0; i < 15; i++)
                h.getInventory().removeItem(coin);
            setAbillity(((Player) h));
        } else if (h.getInventory().containsAtLeast(coin, 10) && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "능력뽑기 중급(코인 10)")) {
            for (int i = 0; i < 10; i++)
                h.getInventory().removeItem(coin);
            setAbillity(((Player) h));
        } else if (h.getInventory().containsAtLeast(coin, 5) && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "능력뽑기 하급(코인 5)")) {
            for (int i = 0; i < 5; i++)
                h.getInventory().removeItem(coin);
            setAbillity(((Player) h));
        }
    }

    public void setAbillity(Player p) {
        Random r = new Random();
        abillity.put(p, r.nextInt(10));
        p.sendTitle("능력:" + abillitylist[abillity.get(p)], "", 20, 20, 20);
        p.closeInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        //System.out.println("아이템 움직임");
        if (e.getInventory() == inv) {
            menuclick(e.getWhoClicked(), e.getCurrentItem());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void scrollEvent(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        if (abillity.get(p) == 4) {
            int slot = e.getNewSlot();
            PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 99999, 300);
            PotionEffect effect1 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 2);
            PotionEffect effect2 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 2);
            if (p.getInventory().getItem(slot) == null){
                p.removePotionEffect(PotionEffectType.SLOW);
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                p.removePotionEffect(PotionEffectType.JUMP);
                return;
            }
            if (p.getInventory().getItem(slot).getType().equals(Material.SHIELD)) {
                //p.sendRawMessage("change");
                p.addPotionEffect(effect);
                p.addPotionEffect(effect1);
                p.addPotionEffect(effect2);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 250));
            } else {
                p.removePotionEffect(PotionEffectType.SLOW);
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                p.removePotionEffect(PotionEffectType.JUMP);
            }
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (p.getInventory().getItemInMainHand().equals(menu)) {
                p.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        Player p = ((Player) e.getDamager());
        if (Main.abillity.isEmpty() == false) {
            if (abillity.get(p) == 0) {
                //e.getDamager().sendMessage("damaged1" + ((Player) e.getDamager()).getInventory().getItemInOffHand());
                if ((p.getInventory().getItemInMainHand().equals(new ItemStack(Material.DIAMOND_SWORD, 1))) && (((Player) e.getDamager()).getInventory().getItemInOffHand().equals(new ItemStack(Material.DIAMOND_SWORD, 1)))) {
                    //p.sendMessage("damaged");
                    PacketPlayOutAnimation play = new PacketPlayOutAnimation(((CraftPlayer)p).getHandle(), 4);
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(play);
                    if (e.getEntity() instanceof LivingEntity) {
                        ((LivingEntity) e.getEntity()).damage(7);
                        //e.getDamager().sendMessage("damaged2");
                    }
                }
            } else if (abillity.get(p) == 1) {
                if (e.getEntity() instanceof LivingEntity) {
                    p.sendMessage("slowed");
                    ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 5));
                }
            } else if (abillity.get(p) == 2) {
                e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 0.1f);
            } else if (abillity.get(p) == 3) {
                if ((p.getHealth() + e.getDamage()) > p.getMaxHealth()) {
                    p.setHealth(p.getMaxHealth());
                } else {
                    p.setHealth(p.getHealth() + e.getDamage());
                }
            }
        }
    }

    @Override
    public void onDisable(){
        PluginDescriptionFile file = this.getDescription();
        System.out.println(file.getName() + "version:" + file.getVersion() + " unloaded");
    }
}
