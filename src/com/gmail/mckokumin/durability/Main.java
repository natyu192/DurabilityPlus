package com.gmail.mckokumin.durability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		switch (cmd.getName()){
		case "setdurability":
			if (args.length == 1){
				try {
					if (sender instanceof Player){
						Player p = (Player)sender;
						ItemStack item = p.getItemInHand();
						ItemMeta im = item.getItemMeta();
						int dura = Integer.valueOf(args[0]);
						if (dura <= 0){
							sender.sendMessage(ChatColor.RED + "1以上を指定してください");
							return true;
						}
						if (item.getItemMeta().hasLore()){
							List<String> lore = item.getItemMeta().getLore();
							for (String lo : lore){
								if (lo.startsWith(ChatColor.GRAY + "Durability: ")){
									sender.sendMessage(ChatColor.RED + "すでに耐久が指定されています");
									return true;
								}
							}
							lore.add(ChatColor.GRAY + "Durability: " + dura);
							im.setLore(lore);
						} else {
							List<String> du = new ArrayList<String>();
							du.add(ChatColor.GRAY + "Durability: " + dura);
							im.setLore(du);
						}
						item.setItemMeta(im);
						item.setDurability((short) 0);
						sender.sendMessage(ChatColor.YELLOW + "耐久を設定しました: " + args[0]);
					}
				} catch (NullPointerException e){
					sender.sendMessage(ChatColor.RED + "アイテムを持ってください");
				} catch (NumberFormatException e){
					sender.sendMessage(ChatColor.RED + "数字を指定してください");
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		this.getCommand("setdurability").setExecutor(this);
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event){
		try {
			Player p = event.getPlayer();
			ItemStack item = p.getItemInHand();
			ItemMeta im = item.getItemMeta();
			List<String> l = im.getLore();
			int i = 0;
			for (String lore : l){
				if (lore.startsWith(ChatColor.GRAY + "Durability: ")){
					String sub = lore.substring(14);
					int durab = Integer.valueOf(sub);
					//p.sendMessage("." + durab + ".");
					durab--;
					//p.sendMessage("." + durab + ".");
					l.set(i, ChatColor.GRAY + "Durability: " + durab);
					if (durab == 0){
						int slot = p.getInventory().getHeldItemSlot();
						ItemStack nul = new ItemStack(Material.AIR);
						p.getInventory().setItem(slot, nul);
						p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1, 1);
					}
				}
				i++;
			}
			im.setLore(l);
			item.setItemMeta(im);
		} catch (NullPointerException e){
		} catch (NumberFormatException e){
		}
	}

	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event){
		try {
			if (!(event.getDamager() instanceof Player)){
				//return;
			}
			Player p = (Player)event.getDamager();
			ItemStack item = p.getItemInHand();
			ItemMeta im = item.getItemMeta();
			List<String> l = im.getLore();
			int i = 0;
			for (String lore : l){
				if (lore.startsWith(ChatColor.GRAY + "Durability: ")){
					String sub = lore.substring(14);
					int durab = Integer.valueOf(sub);
					durab--;
					l.set(i, ChatColor.GRAY + "Durability: " + durab);
					if (durab == 0){
						int slot = p.getInventory().getHeldItemSlot();
						ItemStack nul = new ItemStack(Material.AIR);
						p.getInventory().setItem(slot, nul);
						p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1, 1);
					}
				}
				i++;
			}
			im.setLore(l);
			item.setItemMeta(im);
		} catch (NullPointerException e){
		} catch (NumberFormatException e){
		}
	}

}
