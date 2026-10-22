# 从 Unciv 到 Godot：六边形策略游戏系统

## 课程定位

适合熟悉 Java、读得懂基础 Kotlin、有 Godot 2D 经验的开发者。学完后，应能找到负责某项功能的代码，推演游戏状态的变化，并说明设计上的取舍。主线始终是 Unciv，门派经营、城池建设、武侠探险按需作为对照案例。

共 28 课，每课约 40～60 分钟，基础投入约 19～28 小时，不含可选扩展。第 01～16 课正文已制作，其余是课程设计。后续根据学习反馈逐课制作，不把阅读完成当成已验证的掌握。

## 每课结构

| 环节 | 参考时间 | 产出 |
| --- | --- | --- |
| 回顾与本课问题 | 5 分钟 | 找回前置概念；第一课改为建立背景 |
| 必要机制与源码走读 | 25～30 分钟 | 注释代码、对象关系或调用顺序 |
| 设计取舍与 Godot tip | 5 分钟 | 说明可迁移点和适用边界 |
| 心智模型 | 3 分钟 | 3～6 条一句话总结 |
| 检索与有反馈的推演 | 10～15 分钟 | 不看正文给出解释、预测和证据 |

先顺序建立整体，再深入地图和经济。每课正文仅围绕一个主问题；旁支资料收进 tip 或可选阅读。代码摘录保留真实标识符和相对顺序，省略与改写必须标注。

正文按简体中文游戏程序员的阅读习惯书写：先说具体场景和代码行为，再总结概念。用“相关字段”“部分代码”等常用说法交代省略，不让注释本身成为理解障碍。写作示例见 [共享笔记](../NOTES.md#中文写作要求)。

## A. 全局模型：01～04

| 课 | 主问题与源码入口 | 先修 | 学习产出与验收 |
| --- | --- | --- | --- |
| 01 下一回合由谁推进 | `WorldScreen.nextTurn`、`GameInfo.nextTurn`、`TurnManager` | 无 | 画出按钮到新屏幕的主链，区分整局、文明、城市三级推进 |
| 02 一局游戏的数据归谁 | `GameInfo`、`Civilization`、`City`、`TileMap`、`Tile`、`view/GameView` | 01 | 为状态、规则定义、运行时引用分类，解释为何城市存坐标而不是复制地块 |
| 03 一条规则怎样进入游戏 | `RulesetCache`、`Ruleset`、`Unique`、`android/assets/jsons/` | 02 | 从一个定义追到解析与使用点，区分静态定义和对局实例 |
| 04 怎样用证据读源码 | `tests/.../testing/TestGame.kt`、`LoadGameTest.kt`、构建文档 | 01～03 | 选择一个现有测试，写出输入、预期、关键断点；完成可运行环境核验 |

阶段验收：给出“显示值不对”的症状，先判断该追 UI、状态还是规则，并指出验证入口。

## B. 地图专题：05～12

其中 07～10 是连续的地图生成专课。

| 课 | 主问题与源码入口 | 先修 | 学习产出与验收 |
| --- | --- | --- | --- |
| 05 六边形如何编号 | `HexMath.kt` 中的 `HexCoord`、`HexMath`，`HexmathTests.kt` | 02、04 | 手算相邻与距离，画出当前 x/y 轴；辨认与 Red Blob 坐标约定的差异 |
| 06 地块怎样组成地图 | `TileMap`、`tile/Tile.kt`、`NeighborDirection` | 05 | 解释 tileList 与 tileMatrix、地形/特征/资源/改良分层及环绕边界 |
| 07 地图生成流水线 | `GameStarter`、`MapGenerator.generateMap`、`MapGenerationRandomness` | 03、06 | 按依赖排列生成阶段，比较新游戏与编辑器分支，界定 seed 可复现范围 |
| 08 陆海与气候分布 | `MapLandmassGenerator`、`Perlin`、`applyHumidityAndTemperature` | 07 | 从参数和中间结果解释连续分布为何形成地形，预测调整的影响 |
| 09 山地与河流 | `MapElevationGenerator`、`RiverGenerator`、地块河流字段 | 06、08 | 追踪山地和河流生成，区分格子与边的语义，解释约束与修正 |
| 10 可玩的出生地与资源 | `mapregions/MapRegions`、`RegionStartFinder`、`StartNormalizer`、`resourceplacement/` | 07～09 | 解释区域分配、出生点评价和资源摆放怎样服务玩法，而非只追求外观 |
| 11 移动代价与寻路 | `MapPathing`、`PathingMap`、`PathingMapAStarPathfinder`、`mapunit/` | 05、06 | 在小地图推演可达范围与代价，区分几何距离、道路、通行规则和路径 |
| 12 地图怎样被看见 | `WorldTileGroup`、`TileGroupMap`、`CivInfoTransientCache.updateViewableTiles` | 06、11 | 区分地块状态、可见性和画面层，追踪一次移动对视野与显示的影响 |

阶段验收：从生成参数到地块数据、出生点、寻路和显示，画出各环节如何使用前面生成的数据。Godot tip 分别讨论门派选址、城池农业区、探险可达区域。

## C. 经济专题：13～19

| 课 | 主问题与源码入口 | 先修 | 学习产出与验收 |
| --- | --- | --- | --- |
| 13 一块地怎样变成产出 | `tile/TileStatFunctions.kt`、`CityStats.updateTileStats/update`、`models/stats/Stats.kt` | 03、06、10 | 分清产出类型、来源和修正层，列出某地块进入城市产出的必要条件 |
| 14 人口怎样工作和增长 | `CityPopulationManager`、`City.workedTiles`、`City.foodForNextTurn` | 13 | 手算一个小城市的粮食结余与增长，说明人口分配和地块所有权的区别 |
| 15 生产力怎样完成建造 | `CityConstructions.endTurn/constructIfEnough`、`CityTurnManager` | 01、13、14 | 推演生产累积、完成时机和溢出，说明单位生成的时机会怎样影响玩家行动 |
| 16 城市怎样进入文明账本 | `CivInfoStatsForNextTurn`、`Civilization.updateStatsForNextTurn`、`TurnManager.endTurn` | 13～15 | 追踪黄金净流量、国库、维护费与幸福度反馈，区分预测和实际入账 |
| 17 资源怎样被接入与消耗 | `CityResources`、`CivInfoTransientCache`、`CapitalConnectionsFinder` | 06、13、16 | 区分地块资源、可用供应、库存与每回合流量；核对改良、揭示和连接条件 |
| 18 贸易怎样改变经济 | `logic/trade/`、`diplomacy/DiplomacyTurnManager`、`DiplomacyManager` | 16、17 | 追踪一笔持续性交易的签订、每回合影响、失效与清理 |
| 19 经济因果链复盘 | `WorkerAutomation`、地块改良入口、`CityStats`、`TurnManager` | 11、13～18 | 对“改良已完成但收入没增加”提出假设，以工作人口、规则、刷新和结算证据逐项排除 |

阶段验收：建立“地块条件 → 工作分配 → 城市产出 → 文明汇总 → 入账/消费 → 下一次决策”的账本。门派弟子分工、城池财政、探索资源消耗仅作对照，不改写 Unciv 规则。

## D. 系统联动：20～24

| 课 | 主问题与源码入口 | 先修 | 学习产出与验收 |
| --- | --- | --- | --- |
| 20 单位行动的生命周期 | `mapunit/MapUnit`、`UnitTurnManager`、移动与单位动作入口 | 11、12、15 | 解释移动力、状态、自动命令和回合重置的先后约束 |
| 21 战斗如何改写局面 | `battle/Battle`、`BattleDamage`、`BattleUnitCapture` | 03、20 | 追踪一次攻击的判定、结果及后续影响，区分预览与提交 |
| 22 科技政策怎样改变规则 | `TechManager`、`PolicyManager`、`unique/UniqueTriggerActivation` | 03、16、20 | 从解锁追到条件、产出/行动能力变化及刷新位置 |
| 23 AI 怎样使用同一套系统 | `NextTurnAutomation`、`ConstructionAutomation`、`WorkerAutomation` | 10、19～22 | 区分候选、评分、执行、规则约束，解释一个 AI 决策为何合法但可能不理想 |
| 24 跨系统事件与外交 | `logic/event/`、`ReligionManager`、`CityReligionManager`、`DiplomacyManager` | 18、21、22 | 选一个触发事件画影响链，说明执行时机和防止重复应用的检查点 |

阶段验收：从一个变化追踪到地图、经济、行动或外交中的至少三个相关点；明确直接调用与规则条件检查的差别。

## E. 工程复盘与迁移：25～28

| 课 | 主问题与源码入口 | 先修 | 学习产出与验收 |
| --- | --- | --- | --- |
| 25 保存的状态怎样活过来 | `UncivFiles`、`GameInfo.setTransients`、`BackwardCompatibility`、`GameSerializationTests` | 02、03、19 | 解释读档、引用重建、旧版本迁移与缓存恢复，预测漏字段的后果 |
| 26 下一回合为何变慢 | `WorldScreen.nextTurn`、`GameInfo.clone`、`Timers`、`NextTurnLatencyTest` | 01、23、25 | 分开测量克隆、重建、AI、结算、显示，给出基于测量的优化判断 |
| 27 用最小场景验证联动 | `TestGame`、`MapGenerationTests`、相关城市/寻路测试 | 04、19、21、26 | 缩小一个问题，比较预测和实际，用现有测试或调试复现提供证据 |
| 28 带走什么到 Godot | 回顾状态、地图、经济、回合链及 Godot 官方资料 | 05～27 | 为三类游戏填写设计对照表，比较回合/即时/混合推进，说明可复用思想与改造点 |

阶段验收：给出一份可讨论的系统设计草图和取舍理由；不要求编写 Godot 原型。

## 知识串联与复习

- 第 02～04 课回指第一课的状态/推进/显示分工；每个对象都放回完整运行链。
- 地图生成各课回指坐标、数据层和规则集；第 10 课为第 13 课的经济条件铺路。
- 经济每课回指产出与存量的区别；在建造、文明收支和交易中交错练习不同结算位置。
- 第 19、24、27 课通过综合练习检查前面哪些内容还没弄懂，再有针对性地复习。
- 建议在下一次学习前、约一周后复述关键模型；复习间隔按实际遗忘情况调整。
- 每次反馈记录误解与证据；有真正理解才写 learning-record。课程目录本身不显示虚构的掌握百分比。

## 资料与版本策略

本地源码优先，固定提交链接便于复查，官方开发文档作背景，Godot 官方资料用于核对引擎机制。项目更新后保留课程原来使用的版本，说明新旧实现的差异。外部文章和源码索引见 [RESOURCES.md](RESOURCES.md)。
