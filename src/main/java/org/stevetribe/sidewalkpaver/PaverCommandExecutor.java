package org.stevetribe.sidewalkpaver;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.stevetribe.sidewalkpaver.paver.autopaver.AutoPaver;
import org.stevetribe.sidewalkpaver.paver.paveblock.*;
import org.stevetribe.sidewalkpaver.paver.pavehistory.PaveEventHistory;
import org.stevetribe.sidewalkpaver.paver.pavehistory.PaveHistories;

public class PaverCommandExecutor implements org.bukkit.command.CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            assert player != null;

            if (args.length < 1) {  // no arguments, return false, show usage.
                return false;
            }

            // /sp <on/off>
            // Paver on/off controller
            if (args[0].equals("on")) {
                assert player.getMetadata("isPaving").get(0) != null;
                player.removeMetadata("isPaving", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                player.setMetadata("isPaving", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), Boolean.TRUE));
                player.sendMessage("人行道铺路模式现已开启");
                return true;
            } else if (args[0].equals("off")) {
                assert player.getMetadata("isPaving").get(0) != null;
                player.removeMetadata("isPaving", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                player.setMetadata("isPaving", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), Boolean.FALSE));
                player.sendMessage("人行道铺路模式现已关闭");
                return true;
            }

            // /sp new
            // set PaveBlockSet, override previous set
            if (args[0].equals("new")) {
                PaverBlockSet set = new PaverBlockSet();
                set.status = UserConfigStatus.CONFIGURING;
                PaverConfig.newConfig(player.getName(), set);
                player.sendMessage("添加成功");
                return true;
            }

            // /sp undo
            // set PaveBlockSet, override previous set
            if (args[0].equals("undo")) {
                PaveEventHistory lastUncancelledEventHistory = PaveHistories.getUserHistoriesByUserName(player.getName()).getTheLastUncancelledPaveHistory();

                if (lastUncancelledEventHistory == null) {
                    player.sendMessage("没有要撤销的任务");
                } else {
                    lastUncancelledEventHistory.undo();
                    player.sendMessage("成功撤销上一次任务");
                }

                return true;
            }

            if (args[0].equals("redo")) {
                PaveEventHistory firstCancelledEventHistory = PaveHistories.getUserHistoriesByUserName(player.getName()).getTheFirstCancelledPaveHistory();

                if (firstCancelledEventHistory == null) {
                    player.sendMessage("没有要重做的任务");
                } else {
                    firstCancelledEventHistory.redo();
                    player.sendMessage("成功重做上一次任务");
                }

                return true;
            }

            if (args.length < 2) {
                return false;
            }

            // auto pave
            if (args[0].equals("auto")) {
                if (args[1].equals("0")) {                   // set the first point
                    if (player.getMetadata("autoPaveFirstPoint").size() == 0) {
                        player.removeMetadata("autoPaveFirstPoint", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                    }
                    player.setMetadata("autoPaveFirstPoint", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), player.getLocation()));
                    player.sendMessage("已设定第一个点");
                    return true;
                } else if (args[1].equals("1")) {            // set the second point
                    if (player.getMetadata("autoPaveSecondPoint").size() == 0) {
                        player.removeMetadata("autoPaveSecondPoint", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                    }
                    player.setMetadata("autoPaveSecondPoint", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), player.getLocation()));
                    player.sendMessage("已设定第二个点");
                    return true;
                } else if (args[1].equals("direction")) {    // set the direction of road
                    if (player.getMetadata("autoPaveDirection").size() == 0) {
                        player.removeMetadata("autoPaveDirection", SidewalkPaver.getProvidingPlugin(SidewalkPaver.class));
                    }
                    player.setMetadata("autoPaveDirection", new FixedMetadataValue(SidewalkPaver.getPlugin(SidewalkPaver.class), player.getLocation()));
                    player.sendMessage("已设定延伸方向");
                    return true;
                } else if (args[1].equals("start")) {         // start auto pave
                    if (player.getMetadata("autoPaveFirstPoint").size() == 0 ||
                            player.getMetadata("autoPaveSecondPoint").size() == 0 ||
                            player.getMetadata("autoPaveDirection").size() == 0) {
                        player.sendMessage("请先设置好第一个点、第二个点、方向");
                        return true;
                    }
                    // autoPave logic
                    player.sendMessage("现在开始执行自动铺路");
                    AutoPaver paver = new AutoPaver(player);
                    assert player.getMetadata("autoPaveFirstPoint").get(0) != null;
                    boolean status = paver.autoPave(
                            (Location) player.getMetadata("autoPaveFirstPoint").get(0).value(),
                            (Location) player.getMetadata("autoPaveSecondPoint").get(0).value(),
                            (Location) player.getMetadata("autoPaveDirection").get(0).value());

                    if (status) {
                        player.sendMessage("自动铺路执行完毕");
                    } else {
                        player.sendMessage("自动铺路执行失败，可能是起点与终点不连通、线条不满足要求，或插件出现内部错误 ");
                    }
                    return true;
                } else {
                    return false;
                }
            }

            if (args.length < 3) {
                return false;
            }

            // now, all logic is related with "set", so check if it is null now.
            PaverBlockSet set = PaverConfig.getPaverBlockSetByUserName(player.getName());
            if (set == null) {
                player.sendMessage("请先使用命令/sp new新建配置");
                return true;
            }

            // /rp add <type> <PaveBlockName>
            if (args[0].equals("add")) {
                String paveBlockName = args[1];
                PaveBlockType type;
                String paveBlockTypeName = args[2];

                if (paveBlockTypeName.equals("const")) {
                    type = PaveBlockType.CONST;
                } else if (paveBlockTypeName.equals("organised")) {
                    type = PaveBlockType.ORGANISED;
                } else {
                    player.sendMessage("无效的路块类型，可选const, organised");
                    return true;
                }

                boolean status = set.addNewPaveBlock(paveBlockName, type);
                if (!status) {
                    player.sendMessage("该路块名称已存在");
                    return true;
                }
                player.sendMessage("成功添加" + paveBlockName);
                return true;
            }

            if (args.length < 4) {
                return false;
            }

            if (args[0].equals("set")) {
                // /rp set <PaveBlockName> material <MaterialName>
                PaveBlock paveBlock = set.getUserPaveBlockByName(args[1]);
                if (paveBlock == null) {
                    player.sendMessage("输入的路块名称不存在");
                    return true;
                }

                if (args[2].equals("material")) {
                    ConstantPaveBlock constantPaveBlock = (ConstantPaveBlock) paveBlock;
                    Material material = Material.getMaterial(args[3]);

                    if (material == null) {
                        player.sendMessage("输入的材质不存在");
                        return true;
                    }
                    constantPaveBlock.setMaterial(material);
                    player.sendMessage("材质设置成功");
                    return true;
                }

                // /rp set <PaveBlockName> template <TemplateName>

                // /rp set <PaveBlockName> interval <intervalSeconds>
            }

            return false;       // invalid argument, return false, show usage.
        }
        SidewalkPaver.getPlugin(SidewalkPaver.class).getLogger().warning("This is a player command");
        return false;               // console use, return false, show usage
    }
}
