# SidewalkPaver
+ Pave sidewalks beside a line, and auto place customizable template buildings(trees etc.) on the sideroad.
+ Create, paste, and use a template with just one click.
+ All you operations can be undo and redo.

> 目前插件整体功能还不是很完善，之后会根据需求来进行完善。

# 使用方法

## 模板
> 通过简单的点击操作来快速复制粘贴建筑，还可储存模板用于铺路。

![Screen Recording 2022-12-05 at 13 46 33](https://user-images.githubusercontent.com/87828006/205822652-78a9509c-a7b2-4568-9556-de5a29340721.GIF)
### 创建模板
手持金斧头，副手手持骨粉，左键点击要录入的建筑最下面的一个方块，即可完成录入。
### 粘贴模板
手持金斧头，副手手持骨粉，右键点击要粘贴的位置，即可完成粘贴。
### 注意事项
+ 必须确保整个建筑不与任何无关方块相邻（最下面的方块可以接地）。
+ 此功能只会录入与所点击方块有连接的方块。
+ 暂时只允许最大长宽20*20，高50的建筑，超过部分会被截去。
### 更新计划
+ 支持根据玩家操作时的朝向来旋转模板；

## 铺路
> 在将直线导入游戏后，快速在直线某侧铺好人行道。

![Screen Recording 2022-12-05 at 13 45 30](https://user-images.githubusercontent.com/87828006/205823109-a10d639e-3ee2-4ba8-a1c1-1d8efbeffc52.GIF)
### 使用方法
1. 设定路宽：`/swp set width <路宽>`；
2. 选择起点终点：手持金斧头（副手不能持有骨粉），左键点击方块以选中起点，右键点击方块以选择终点；
3. 选择路在直线的哪一侧：手持金斧头，面朝所选直线需要铺路的一侧，左键点击空气；
4. 手持金斧头，右键点击空气，执行铺路。

### 可选配置项：
+ 设定直线材质：即用于引导铺路的方块材质，`/swp set line <材质名称>`，若不设置则默认为`SMOOTH_STONE_SLAB`平滑石台阶；
+ 设定铺路材质：即要用什么方块来铺路，`/swp set road <材质名称>`，若不设置则默认为`SMOOTH_STONE_SLAB`平滑石台阶；
+ 设定Marker材质：即在铺路时，遇到什么方块时在此处放置模板建筑物，`/swp set marker <材质名称>`，若不设置则默认为`DIAMOND_BLOCK`钻石块；
+ 录入模板：详见“模板 - 创建模板”，如果没有设置模板，则Marker处会保持原状。

## 注意事项
+ 直线必须是一条完全独立的线段，起点、终点以及线段上任何地方都不能与同一高度上的其它方块相邻。

+ 直线上的拐角处只能有2个方块，不能有三个方块。若直线拐角处如图，则左上角不能有方块；
<img width="64" alt="Screenshot 2022-11-27 at 01 37 57" src="https://user-images.githubusercontent.com/87828006/204101768-691bebcf-f6e1-47f9-a6bd-64a99cee8c37.png">

+ 目前一次任务只支持一个Marker、一个模板；

+ 目前尚不支持曲线，只支持直线，例如图中绿色部分是受支持的，在红色部分使用效果会很差；
<img width="217" alt="image" src="https://user-images.githubusercontent.com/87828006/204101908-7c3ced7d-1337-4c25-ae1c-a6ee717a2e8e.png">

+ 铺路任务在服务器主进程同步执行，一次进行太大面积的操作可能导致服务器长时间失去响应甚至崩溃，目前一次任务最多允许200格左右长度的直线引导。

## 更新计划
+ 不铺路，只对范围内的Marker做操作

# 撤销和重做
## 使用方法
+ 如果发现做错了，可以使用命令`/swp undo`来撤销上一次操作；
+ 如果撤销之后反悔了，可以使用命令`/swp redo`来恢复上一次操作。
## 注意事项
+ undo后redo的逻辑可能会比较奇怪。
