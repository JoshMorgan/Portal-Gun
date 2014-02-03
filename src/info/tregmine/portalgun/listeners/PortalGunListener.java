package info.tregmine.portalgun.listeners;

import info.tregmine.portalgun.PortalGun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalGunListener implements Listener {
	private PortalGun plugin;

	public PortalGunListener(PortalGun instance) {
		this.plugin = instance;
	}

	int taskID = 0;

	@EventHandler
	public void onPlayerGoTroughPortal(PlayerMoveEvent e) {

		Player p = e.getPlayer();
		World world = p.getWorld();
		Location playerLoc = p.getLocation();
		Material portal = playerLoc.getWorld().getBlockAt(playerLoc).getRelative(0, 0, 0).getType();

		if (portal != Material.TRIPWIRE) {
			return;
		}

		int X = p.getLocation().getBlockX();
		int Z = p.getLocation().getBlockZ();
		int Y = p.getLocation().getBlockY();
		Block b = world.getBlockAt(X, Y, Z);

		findBlock(world, b, p);
	}

	@EventHandler
	public void onPlayerCreatePortal(PlayerInteractEvent e) {

		Player p = e.getPlayer();
		World world = p.getWorld();

		if(p.getItemInHand().getType() != Material.GOLD_HOE){
			return;
		}

		int x = e.getClickedBlock().getX();
		int y = e.getClickedBlock().getY()-1;
		int z = e.getClickedBlock().getZ();
		if(e.getClickedBlock().getType() == Material.IRON_BLOCK){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){plugin.getConfig().set("PortalGun." + p.getName() + ".redportal", null);
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".loc" + ".world", world.getBlockAt(x,y,z).getWorld().getName().toString());
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".loc" + ".x", world.getBlockAt(x,y,z).getX());
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".loc" + ".y", world.getBlockAt(x,y,z).getY());
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".loc" + ".z", world.getBlockAt(x,y,z).getZ());
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".type" + ".1", world.getBlockAt(x,y,z).getType().toString());
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".type" + ".2", world.getBlockAt(x,y+1,z).getType().toString());
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".type" + ".3", world.getBlockAt(x,y+2,z).getType().toString());
			plugin.getConfig().set("PortalGun." + p.getName() + ".redportal" + ".type" + ".4", world.getBlockAt(x,y+3,z).getType().toString());
			plugin.saveConfig();

			world.getBlockAt(x,y,z).setType(Material.REDSTONE_BLOCK);
			world.getBlockAt(x,y+1,z).setType(Material.TRIPWIRE);
			world.getBlockAt(x,y+2,z).setType(Material.TRIPWIRE);
			world.getBlockAt(x,y+3,z).setType(Material.REDSTONE_BLOCK);
			}
			else if(e.getAction() == Action.LEFT_CLICK_BLOCK){
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".world", world.getBlockAt(x,y,z).getWorld().getName().toString());
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".x", world.getBlockAt(x,y,z).getX());
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".y", world.getBlockAt(x,y,z).getY());
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".z", world.getBlockAt(x,y,z).getZ());
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".type" + ".1", world.getBlockAt(x,y,z).getType().toString());
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".type" + ".2", world.getBlockAt(x,y+1,z).getType().toString());
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".type" + ".3", world.getBlockAt(x,y+2,z).getType().toString());
				plugin.getConfig().set("PortalGun." + p.getName() + ".blueportal" + ".type" + ".4", world.getBlockAt(x,y+3,z).getType().toString());
				plugin.saveConfig();

				world.getBlockAt(x,y,z).setType(Material.LAPIS_BLOCK);
				world.getBlockAt(x,y+1,z).setType(Material.TRIPWIRE);
				world.getBlockAt(x,y+2,z).setType(Material.TRIPWIRE);
				world.getBlockAt(x,y+3,z).setType(Material.LAPIS_BLOCK);
			}
		}else{
			dealWithBlocks(p, taskID, false, true);
			p.sendMessage("" + ChatColor.BOLD + ChatColor.BLUE + "O " + ChatColor.GOLD + "O ");
		}
	}

	public void findBlock(World w, Block b, Player p) {
		int x = b.getX();
		int z = b.getZ();
		int y = p.getLocation().getBlockY();
		int take = 0;
		Block check = w.getBlockAt(x, y, z);

		while (w.getBlockAt(x, y, z).getType() == Material.TRIPWIRE) {
			y--;
			take++;
			continue;
		}
		Block m = check.getLocation().subtract(0, take, 0).getBlock();

		checkTypes(m, p);
	}

	public void checkTypes(Block m, Player p){

		if(m.getType() == Material.REDSTONE_BLOCK){
			p.sendMessage(ChatColor.DARK_RED + "Red Portal");
			dealWithBlocks(p, taskID, true, false);
		}else if(m.getType() == Material.LAPIS_BLOCK){
			p.sendMessage(ChatColor.BLUE + "Blue Portal");
			dealWithBlocks(p, taskID, false, false);
		}
	}

	public void dealWithBlocks(final Player p, int task, Boolean red, Boolean clear) {

		Material red1 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".redportal" + ".type" + ".1"));
		Material red2 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".redportal" + ".type" + ".2"));
		Material red3 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".redportal" + ".type" + ".3"));
		Material red4 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".redportal" + ".type" + ".4"));

		String redw = plugin.getConfig().getString("PortalGun." + p.getName() + ".redportal" + ".loc" + ".world");
		int redx = plugin.getConfig().getInt("PortalGun." + p.getName() + ".redportal" + ".loc" + ".x");
		int redy = plugin.getConfig().getInt("PortalGun." + p.getName() + ".redportal" + ".loc" + ".y");
		int redz = plugin.getConfig().getInt("PortalGun." + p.getName() + ".redportal" + ".loc" + ".z");

		Material blue1 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".blueportal" + ".type" + ".1"));
		Material blue2 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".blueportal" + ".type" + ".2"));
		Material blue3 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".blueportal" + ".type" + ".3"));
		Material blue4 = Material.getMaterial(plugin.getConfig().getString("PortalGun." + p.getName() + ".blueportal" + ".type" + ".4"));

		String bluew = plugin.getConfig().getString("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".world");
		int bluex = plugin.getConfig().getInt("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".x");
		int bluey = plugin.getConfig().getInt("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".y");
		int bluez = plugin.getConfig().getInt("PortalGun." + p.getName() + ".blueportal" + ".loc" + ".z");

		if(clear = true){

			Bukkit.getWorld(redw).getBlockAt(redx, redy, redz).setType(red1);
			Bukkit.getWorld(redw).getBlockAt(redx, redy+1, redz).setType(red2);
			Bukkit.getWorld(redw).getBlockAt(redx, redy+2, redz).setType(red3);
			Bukkit.getWorld(redw).getBlockAt(redx, redy+3, redz).setType(red4);

			Bukkit.getWorld(bluew).getBlockAt(bluex, bluey, bluez).setType(blue1);
			Bukkit.getWorld(bluew).getBlockAt(bluex, bluey+1, bluez).setType(blue2);
			Bukkit.getWorld(bluew).getBlockAt(bluex, bluey+2, bluez).setType(blue3);
			Bukkit.getWorld(bluew).getBlockAt(bluex, bluey+3, bluez).setType(blue4);

		}else{

			Location redexit = new Location(Bukkit.getWorld(redw), redx, redy+1, redz);
			Location blueexit = new Location(Bukkit.getWorld(bluew), bluex, bluey+1, bluez);

			if(red = true){
				p.teleport(blueexit);
			}else{
				p.teleport(redexit);
			}
		}
	}
}
