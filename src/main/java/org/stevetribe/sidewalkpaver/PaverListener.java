package org.stevetribe.sidewalkpaver;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.stevetribe.sidewalkpaver.paver.pavehistory.PaveHistories;
import org.stevetribe.sidewalkpaver.template.Template;
import org.stevetribe.sidewalkpaver.template.TemplateGenerator;
import org.stevetribe.sidewalkpaver.template.Templates;

import java.util.Objects;

import static org.bukkit.inventory.EquipmentSlot.HAND;
import static org.bukkit.inventory.EquipmentSlot.OFF_HAND;

public class PaverListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        assert player.getMetadata("isPaving").get(0) != null;
        if (player.getMetadata("isPaving").get(0).asBoolean()) {
            // paving logic

        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // to make sure the key "isPaving" is always valid.
        player.setMetadata("isPaving", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), Boolean.FALSE));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand().equals(OFF_HAND)) {
            event.setCancelled(true);
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        Material material = itemStack.getType();
        Player player = event.getPlayer();
        // 如果为金斧头
        if (material.equals(Material.GOLDEN_AXE)) {
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Material materialOffhand = event.getPlayer().getInventory().getItemInOffHand().getType();
                Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();  // 用户点击的坐标

                // offhand: BONE_MEAL     left click
                if (materialOffhand.equals(Material.BONE_MEAL)) {
                    Templates.addNewTemplate(player.getName(), TemplateGenerator.generateTemplateByTheBottomBlock(location));
                    player.sendMessage("成功录入模板");
                } else {
                    // offhand: empty       left click
                    if (player.getMetadata("autoPaveFirstPoint").size() == 0) {
                        player.removeMetadata("autoPaveFirstPoint", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                    }
                    player.setMetadata("autoPaveFirstPoint", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), location));
                    player.sendMessage("已设定第一个点, x: " + location.getX() + ", y: " + location.getY() + ", z: " + location.getZ() + ";");
                    System.out.println(location.getBlock().getType());
                }
                event.setCancelled(true);
            } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(HAND)) {
                System.out.println(event.getAction());
                System.out.println(event.getHand());

                Material materialOffhand = event.getPlayer().getInventory().getItemInOffHand().getType();
                Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();  // 用户点击的坐标

                // offhand: BONE_MEAL     right click
                if (materialOffhand.equals(Material.BONE_MEAL)) {
                    Template template = Templates.getTemplateByName(player.getName());
                    if (template == null) {
                        player.sendMessage("还未录入模板");
                    } else {
                        PaveHistories.getUserHistoriesByUserName(player.getName()).addEventHistory(template.placeSingleBaseTemplate(location));
                        System.out.println(location);
                        System.out.println(event.getBlockFace());
                        player.sendMessage("成功粘贴模板");
                    }
                } else {
                    // offhand: empty       right click
                    if (player.getMetadata("autoPaveSecondPoint").size() == 0) {
                        player.removeMetadata("autoPaveSecondPoint", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                    }
                    player.setMetadata("autoPaveSecondPoint", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), location));
                    player.sendMessage("已设定第二个点, x: " + location.getX() + ", y: " + location.getY() + ", z: " + location.getZ() + ";");
                }
            } else if (event.getAction().equals(Action.LEFT_CLICK_AIR) && event.getHand().equals(HAND)) {
                // left click air
                if (player.getMetadata("autoPaveDirection").size() == 0) {
                    player.removeMetadata("autoPaveDirection", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                }
                player.setMetadata("autoPaveDirection", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), player.getLocation()));
                player.sendMessage("已设定延伸方向, yaw: " + player.getLocation().getYaw() + ";");
            } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && event.getHand().equals(HAND)) {
                // right click air
                player.performCommand("swp auto start");
            }
        }
    }
}




