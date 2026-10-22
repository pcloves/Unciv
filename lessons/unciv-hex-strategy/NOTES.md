# 课程工作记录

## 已确认的方向

- 主线：精读 Unciv；Godot 作为 tips 和最终设计对照。
- 顶栏导航（2026-09-11 调整）：28 课全部平铺会放不下，改为「课程菜单」下拉（A～E 五个专题，跳课程路线对应锚点）+ 课程路线 + 两个速查 + 检索练习。菜单只列专题，新增 08～28 课时顶栏无需再改；具体课跳转走 index 的专题表或各课 nav-bottom。样式在 course.css 的 .topmenu，纯 CSS details/summary，无 JS 依赖。
- 主案例未定，三类目标游戏按知识点分别举例。
- 时间推进方式未定，先准确理解当前回合制，最后比较迁移方案。
- 单课 40～60 分钟，包含源码走读与检索；不要求纯正文占满一小时。
- 写作面向母语为简体中文的游戏程序员，遵循 [共享笔记中的中文写作要求](../NOTES.md#中文写作要求)；尤其注意代码注释要说清实际行为。

## 内容与学习状态

- 课程规划：28 课，详见 [COURSE.md](COURSE.md)。
- 第一课：`0001-runtime-map.html`，主题为下一回合的运行链路；学习者已确认本课全部学完。
- 第一课配套：`reference/architecture.html`，含术语表与调用链速查。
- 第二课：`0002-data-ownership.html`，主题为一局游戏的数据归谁（状态树、坐标存储、@Transient 引用重建与 GameView 视角）；学习者已确认本课全部学完。
- 速查已同步第二课：architecture.html 新增“状态树与三类数据”及序列化/运行时引用术语。
- 第三课：`0003-rule-pipeline.html`，主题为一条规则怎样进入游戏（JSON 定义 → RulesetCache → 每局组合规则集 → Unique 惰性解析 → builtBuildingUniqueMap → 使用点查询）；学习者已确认本课全部学完。
- 速查已同步第三课：architecture.html 新增“规则集与 Unique”及规则/占位文本/判定场景术语。
- 第四课：`0004-reading-with-evidence.html`，主题为怎样用证据读源码（测试作为一等源码、given/when/then 与关键断点、TestGame 造最小对局、LoadGameTest 用真实存档、本地环境核验）；学习者已确认本课全部学完。
- 速查已同步第四课：architecture.html 新增“测试与证据”速查及 TestGame/断言/关键断点术语。
- 第五课：`0005-hex-coordinates.html`，主题为六边形如何编号（HexCoord 两条斜轴、邻居偏移表、距离两分支、屏幕换算与 Red Blob 约定差异）；学习者已确认本课全部学完。
- 速查已同步第五课：新增 reference/hexmath.html 地图与坐标速查（坐标轴、邻居、距离、屏幕换算、环与编号、测试、Red Blob 对照）。
- 第六课：`0006-tilemap.html`，主题为地块怎样组成地图（TileMap 的 tileList/tileMatrix 两份存储与 setTransients 重建、Tile 的地形/特征/奇观/资源/改良分层与河流边存储、neighbors 惰性缓存与初始化顺序、环绕边界经度补偿、两种形状构造与分布测试）；学习者已确认本课全部学完。
- 速查已同步第六课：hexmath.html 新增“地块存储与环绕”一节。
- 第六课初版河流注释有误（4/6/8 点钟方向写反），学习者指出后已按源码修正并复检通过；写作时“对方在自己哪个方向”以 getNeighborTileClockPosition 的返回值为唯一依据。
- 第七课：`0007-map-generation.html`，主题为地图生成流水线（GameStarter 入口与三个调用方、generateMap 的十条地形阶段链与依赖、新游戏/编辑器两条分支、seed 与 gameId 的可复现边界）；学习者已确认本课全部学完。
- 第八课：`0008-landmass-climate.html`，主题为陆海与气候分布（spawnLandOrWater 阈值判定与 retryLoweringWaterLevel 重试、Perlin 噪声的 fade/lerp/梯度插值与连续性、noise3d 倍频参数与 ridged 变体、十三种地图类型骨架分组、applyHumidityAndTemperature 的温湿公式与规则集温湿矩形匹配）；学习者已口头确认学完（2026-09-15，含一轮反馈修订）。
- 速查已同步第八课：hexmath.html 新增“噪声与地形分布”速查节（陆海判定、噪声骨架、参数→形态、温湿公式、地形匹配）。
- 第九课：`0009-elevation-rivers.html`，主题为山地与河流（raiseMountainsAndHills 的 scale=2 噪声+保号幂+0.7/0.5 阈值、Map Elevation 滑块与幂前门槛换算、cellularMountainRanges/cellularHills 两轮细胞自动机与 ~Raising~/~Lowering~ 假特征、三布尔边存储与 isAdjacentToRiver 缓存、spawnRivers 三级源头降级与 chooseSpreadOutLocations、RiverCoordinate 顶点语义与贪心走线、paintTo 三分支、convertTerrains 河岸转换与 continueRiverOn 运行期加河）；学习者已确认学完（2026-09-15），学完后按反馈修订三轮（powSigned 解释与数值表、convertTerrains 配对编号、‘底地形’改‘基础地形’），详见下方“第九课的学习反馈”。
- 速查已同步第九课：hexmath.html 新增“山地与河流”速查节（幂与门槛、修形规则、边存储、顶点走线、两条加河路径）。
- 第十课：`0010-starts-resources.html`，主题为可玩的出生地与资源（MapRegions 的区域切分与区域类型、RegionStartFinder 的三环评价与 closeStartPenalty、StartNormalizer 的粮食/生产力保底与 startBias 分桶、resourceplacement 的 impact 图与三类资源分配）；学习者已确认本课全部学完（2026-09-17）。学完后按反馈多轮修订：跨接缝例子换成宽 10/左 3 右 2 列、splitRegion 右侧改写为窄带累加（含 Rectangle 拼读示例 [−6,−7,13,1]→[−6,−6,13,1]…）、全课 15 个配对块头部注释改为“问：XXX”并右侧加脑内模型导入（分蛋糕/排考场/体检单/气味/两趟采购等）、calculateFoodBonusesNeeded 重写为“体检单：粮势+底子→分级表”、修正 placeFoodBonuses 环序重排错误（正确为 2,3,1 与 3,1,2）、补齐三处左侧编号缺失（placeFoodBonuses ④、setRegionStart ③、startBias ②③④ 骨架注释、placeImpact ③ 半径注释）并脚本校验左右编号全配对、区域“方正”疑问加了 Voronoi/背包对比的延伸 callout、“锤”在首次出现处加俗语定义。详见“第十课的制作核对”与“第十课的验证记录”。
- 速查已同步第十课：hexmath.html 新增“区域与资源”速查节（调用位置、区域切分与类型、起点评价与分数表、起点修正、资源分配与 impact、REPL 实测）。
- 第 11 课：`0011-movement-pathing.html`，主题为移动代价与寻路（MovementCost 代价判定链与 100f 哨兵、getMovementToTilesAtPosition 的水波扩散与钉价、canPassThrough/canMoveTo 的能过能停、getShortestPath 按回合数长树与只返回落点、A* 引擎的 RouteNode 位域/base-30 定点数/缓存键失效/0.1 启发式、两代实现的行为差异表）；学习者已学完（2026-09-17，自述“没怎么细学”——寻路属细节性质课程，不逐项深究；本课问答覆盖了道路判定 hasConnection 的存储模型与 civ 参数含义、河流无顺流航行机制、术语「费」改「代价」并全文修订）。未收到本课口述回顾与练习答案。
- 速查已同步第 11 课：hexmath.html 新增“移动与寻路”速查节（代价表、可达图语义、回合数优先、A* 引擎三件套、REPL 实测）。
- 第 12 课：`0012-sight-fog.html`，主题为地图怎样被看见（getViewableTiles 的按环视线算法与 maxHeightSeenToTile 传递、unitHeight/tileHeight 高度表、setNewViewableTiles 四路来源、updateViewableTiles 的侧效应（拍照/记名/建交/奇观）、isVisible/isExplored 三态、lastSeenImprovement 记忆、TileGroup 十一层与 TileGroupMap 装箱、一次移动的全链追踪）；教材已交付（2026-09-17）。本课兑现了第二课埋的钩子（viewableTiles 谁重算）。配对编号脚本扩展到 ⑳，十二块全 OK；移动端溢出修订一处（测试文件路径改成本地链接）；初版一处源码注释行过宽修订。详见“第十二课的制作核对”与“第十二课的验证记录”。
- 速查已同步第 12 课：hexmath.html 新增“视野与迷雾”速查节（高度表、视线算法、四路拼集、触发时机、三态判定、UI 侧）。
- 第 13 课：`0013-tile-yields.html`，主题为一块地怎样变成产出（TileStatFunctions.getTileStatsBreakdown 的结算单来源顺序、城市 unique 按来源行名进地形/改良/道路三桶、getTileStats 的逐桶百分数乘法、资源可见与城中心保底两道闸门、CityStats.updateTileStats 的工作格三条件（城中心/isWorked/免人口产出）、CityStats.update 的 baseStatTree→statPercentBonusTree→finalStatList→currentCityStats 接力）；教材已交付（2026-09-18）。经济专题开篇，兑现第十二课末尾「出生点评价消费的粮食与生产力就是第 13 课产出系统输入」的过渡。8 个配对块全 OK；check-course 16 页 52 题全 PASS。
- 速查：本课属经济专题，未塞进地图速查（hexmath.html 语义为地图与坐标）；经济速查留待第 19 课经济专题收官时统一整理。
- 第 14 课：`0014-population-growth.html`，主题为人口怎样工作和增长；学习者已确认本课全部学完（2026-09-24）。这是学习者自述的完成情况，本次没有新增练习答案或口述推演证据。教材制作、文案修订与验证情况见下方第十四课记录。
- 第 15 课：`0015-production-construction.html`，主题为生产力怎样完成建造；学习者已确认本课全部学完（2026-09-24）。这是学习者自述的完成情况，本次没有新增练习答案或口述推演证据。制作与验证详情见下方第十五课记录。
- 第 16 课：`0016-civilization-economy.html`，主题为城市怎样进入文明账本；已制作并完成源码、测试和页面验证（2026-09-24）。本课教材已交付，学习者尚未提交本课练习答案或口述推演，不记为已学完。
- 第 17～28 课：已设计，正文待逐课制作；根据学习反馈调整细节。
- 第一、二、三、四课完成情况来自学习者自述；尚未收到四课的整课练习答案。追问中已确认的具体理解见 [学习记录 0002](learning-records/0002-progress-notification.md)，不额外推断掌握程度。
- 下一教学动作（2026-09-24 更新）：第 15 课已由学习者确认全部学完，新会话直接制作并交付第 16 课「城市怎样进入文明账本」，先核对黄金净流量、国库、维护费、幸福度反馈与实际入账时机的源码和测试。旧课回顾可随课复习，不作为开始新课的前置门槛；用户自述学完与有答案支持的掌握证据分别记录。
- 第十六课制作核对（2026-09-24）：主链为 `CityStats.update` → `Civilization.updateStatsForNextTurn` → `CivInfoStatsForNextTurn.getStatMapForNextTurn`；文明结束由 `TurnManager.endTurn` 先取净黄金并 `addGold(nextTurnStats.gold.toInt())`，随后才执行城市结束和黄金时代。城市维护、道路维护、单位维护分属城市/文明来源；单位成功放置末尾刷新预测，不直接入账。幸福度在 `getHappinessBreakdown` 单独汇总，跨越 `Ruleset.allHappinessLevelsThatAffectUniques` 阈值时重算所有城市并关闭递归回调。G&K 当前规则边界为 -10..-1 含两端的 -75% growth，低于 -10 才 Nullifies Growth 与 -50% Production。
- 第十六课页面验证：页面清单同步为 19 页、16 课共 64 题；9 个源码配对块纳入检查，桌面代码块内部溢出检查覆盖第 15、16 课，新增 `#economy-states` 截图锚点。教材包含 4 道有反馈选择题、2 个必做推演、1 个可选复跑验证。`node --check lessons/assets/check-course.cjs` 通过；Playwright + Edge 无截图检查与带截图/PDF检查均通过，覆盖 2560×1440、1536×864、390×844、打印状态恢复、全部错误选项反馈和键盘改答；带截图输出在 `%TEMP%\opencode\unciv-course-out16`，stdout PASS、stderr 为空。不要把教材交付写成学习者已学完第 16 课。

## 第十五课的制作核对

- 源码基线及本次 HEAD：`1df6249a41301056ddc4448a95a93ddf5403e061`。核对日期：2026-09-24。
- 主链：`CityTurnManager.endTurn` L160 取 `currentCityStats`，先调用 `CityConstructions.endTurn` 再文化扩张、人口增长；`CityConstructions` 的字段 L73/L81、`getWorkDone` L238、`addProductionPoints` L310、`constructIfEnough` L322、`endTurn` L351、`completeConstruction` L485；`CityTurnManager.startTurn` L23 在城市回合开始效果后检查完成。
- 已核对所有 `constructIfEnough` 调用方：普通生产由城市开始调用；伟人加速奇观/建筑动作也会调用。购买直接走 `construct`，免费建筑有直接 `completeConstruction` 路径。因此正文将“下回合开始完成”限定为普通回合生产。
- 溢出：候选值 = 完成前进度 − 此次实际成本；成功后才读取 `currentCityStats.production.roundToInt()`，上限 = max(成本, 该读数)，保存 min(候选, 上限)。完成建筑会通过 `addBuilding → updateUniques → cityStats.update` 改变缓存；不能把上限读数写成固定的上回合产出。下一次普通生产结算直接加旧溢出并清零，不再次乘新项目加成；没有当场循环完成队列的逻辑。
- 单位链：`BaseUnit.construct` L356 → `UnitManager.addUnit` L53 → `UnitManager.placeUnitNearTile` L103 → `TileMap.placeUnitNearTile` L673。先试中心，再沿可通行邻格搜索，最多 10 轮；失败返回 null 并撤销临时登记；成功放置后初始化移动力。`TurnManager.startTurn` L69 先城市后单位；`UnitTurnManager.startTurn` L138 重置移动力和攻击次数。生成时机的理由来自 `CityTurnManager` L34 的源码注释，避免玩家控制之前先受敌方攻击。
- 场景实测：成本 40、进度 35、最终生产力 12.4 → endTurn 后进度 47、无新单位、溢出 0；真实文明 startTurn 后新单位在非中心地块生成、移动力 2、攻击次数 0、溢出 7、B 进度 0；下一次 B 输入 9.5 → 进度 17、溢出 0。另测成本 40/进度 200，普通建筑保留 40，新建建筑提供 100 生产力后缓存 101、保留 101。
- 其他实测：放置失败保留队首与 47 点进度，腾位置重试后生成且溢出 7；换队首保持 A=20/B=12；空闲回合保留旧溢出 7、不再攒当回合产出；新项目生产力已加成到 2，加旧溢出 7 后进度为 9。
- Gradle MCP：`:tests:test --tests com.unciv.logic.city.Lesson15ProductionProbeTest --tests com.unciv.uniques.GlobalUniquesTests.percentProduction* --rerun`，BuildId `b-1`，9 过、0 跳过、0 失败。其中 5 项是本课验证，4 项是既有生产加成测试。没有找到现有测试直接调用 `constructIfEnough` 或断言 `productionOverflow`；不能将加成测试说成覆盖完成时序。
- 验证用例保存在 `verification/Lesson15ProductionProbeTest.kt`，可临时复制到 `tests/src/com/unciv/logic/city/` 复跑；本次运行后的测试目录临时副本已移除。场景人为控制部分输入，没有模拟敌方战斗；完整对局行为的解释还以调用链为依据。
- 教学取舍：复用共享 CSS 和 quiz.js；10 个同序编号源码配对块、4 道有反馈选择题、2 个必做推演、可选本地核验。生产力转化、失效退款、购买与加速作为边界说明；Godot 仅讨论逻辑生成与表现分工。经济速查仍按计划留到第 19 课整理。

## 第十五课的页面验证

- 页面清单同步为 18 页，15 课共 60 题；新增截图锚点 `#overflow`。第 14 课下一课链接、课程入口、COURSE 与 RESOURCES 同步更新。
- `check-course.cjs` 已通过：18 页、60 题，本地链接与锚点、固定版本源码引用、2560×1440 / 1536×864 / 390×844 三种视口、图片、全部错误选项反馈、键盘改答、打印展开与恢复。10 个源码配对块左右编号顺序一致且连续无重号，按中文字符计双宽的代码行均不超过 100 列；新增孤立按钮/output 检查。
- 定向截图检查发现 1536 宽度下 4 处代码块存在内部横向滚动，已折行并调整注释位置；最终 2560/1536 宽度代码块无横向滚动。移动端六列状态表使用可聚焦的横向滚动区域，打印时仍完整展示。最终页面定向复检通过：10 块编号与行宽、桌面与移动布局、4 题全部 16 个选项、打印布局及状态恢复。
- 已通读题干、选项、反馈和答案汇总，复算正文及练习 A 的状态表；选择题不做等长限制。桌面源码截图、移动状态表/第 4 题截图、打印布局截图及 PDF 位于 `C:\Users\zhengtongshan\AppData\Local\Temp\opencode\unciv-course-out15`。整套截图/PDF 检查超过一分钟终端时限后改后台运行并取得 PASS，最终修订另做定向复检。
- `node --check lessons/assets/check-course.cjs` 通过；临时测试目录副本确认已移除，游戏源码未修改。以上均为教材制作证据，不代表学习者完成本课。

## 第五课的制作核对

- 源码入口：`core/src/com/unciv/logic/map/HexMath.kt`（HexCoord、轴方向注释、getDistance、hex2WorldCoords/world2HexCoords、roundHexCoords、getZeroBasedIndex）、`NeighborDirection.kt`、`tests/src/com/unciv/logic/map/HexmathTests.kt`。
- 已核对：HexCoord 数据类与 Serializer；轴方向（10/2 点钟）；六个邻居偏移（含 0/12 别名）与 TileMap.getClockPositionNeighborTile；getDistance 两分支与立方坐标 max(|Δx|,|Δy|,|Δ(y−x)|) 的等价；hex2CubicCoords = (y−x, x, −y)；hex2WorldCoords 展开为 screenX=1.5(y−x)、screenY=0.866(x+y)；纬度 x+y、经度 x−y、列号 y−x（经度符号在第六课追问时更正）；EditorMapHolder.getClosestTileTo 的点击链路；HexmathTests 8 条测试的 given/when/then 与关键断点。
- 已用 Gradle MCP 重跑 `:tests:test --tests "com.unciv.logic.map.HexmathTests" --rerun`：8 过 0 跳过 0 失败。
- 坐标结论：Unciv 为平顶六边形、屏幕 y 向上、两轴为 10/2 点钟斜轴，正上方邻居 (1,1)；与 Red Blob 轴向坐标（水平轴 + 斜轴、示意图 y 向下）不同，公式须显式转换；教程页外部内容未逐节核对，课程只使用其轴向距离公式形态并说明适配顺序。
- 术语沿用：轴向坐标、立方坐标、时钟方向；与 hexmath.html 速查一致。

## 第四课的制作核对

- 源码入口：`tests/src/com/unciv/testing/TestGame.kt`、`tests/src/com/unciv/logic/LoadGameTest.kt`、`docs/Developers/Building-Locally.md`。
- 已核对 TestGame 的类注释、构造器与 addCiv/addCity/addUnit/createBuilding 助手；CityTest 的卖建筑/毁城测试；City.getGoldForSellingBuilding/sellBuilding/destroyCity/canBeDestroyed；CityPopulationManagerTest 的结转测试与 nextTurn 的 CarryOverFood；LoadGameTest 与 UncivFiles.gameInfoFromString；BaseTestRunner/GdxTestRunner/TestRunnerFactory；tests/build.gradle.kts 的 workingDir。
- 已用 Gradle MCP 实际运行 `:tests:test`（HexmathTests 8 过、CityTest+LoadGameTest 15 过 3 跳过 0 失败），验证可运行环境；跳过项为 LoadGameTest 中 @Ignore 的存档更新工具。
- 术语沿用：TestGame、given/when/then、断言、关键断点、读档测试；与 architecture.html 的“测试与证据”速查一致。
- 第四课正文已按共享笔记的“配对排版”规则书写：一段摘录配一段讲解、同序对应；抽象总结（三要素表格、心智模型）单独成段。

## 第三课的学习反馈

- 学习者反馈第三课初版配对排版吃力：第 5 节左侧两个方法的代码与右侧编号讲解各自独立、两列扫描方向相反。
- 已按学习者选择的“全文同模式修复”方案重组第 3、4、5 节（第 1、2 节复查无同类问题），详情见下方“第三课的制作核对”。
- 后续课程写作必须遵守共享笔记新增的“配对排版”规则：一段摘录配一段讲解、同序对应；抽象总结单独成段。

## 第三课的制作核对

- 已核对 RulesetCache、Ruleset、Unique、UniqueType、UniqueMap、RulesetObject、IHasUniques、CityConstructions.updateUniques、TileStatFunctions、CityStats、UncivGame.initialize 及 jsons 目录。
- 粮仓（Granary）与神庙（Temple of Artemis）两条 unique 已从定义追到使用点；GlobalUniques 的快乐度条件已核对解析方式。
- 教学中保留两点结论：规则效果在查询时得出（拆建筑后 cityStats 当场重算）；定义对象在缓存与每局规则集间共享、约定只读。
- 学习者反馈第三课配对排版吃力（第 5 节代码与讲解各自独立）。已重组第 3、4、5 节：每个配对块只放一段摘录、右侧按代码顺序逐段讲解，参数总结改成表格；共享笔记补充“配对排版”写作规则及改写示例。第 1、2 节按同一标准复查，未发现同类错位。

## 第一课的追问

- 学习者已阅读 GameInfo.nextTurn，注意到 AI 的 startTurn、automateTurn、endTurn 按顺序调用；提问这些同步逻辑怎样配合 UI 进度显示。
- 已在第一课第 3 节补充选读，核对 Concurrency 的线程池协程、NextTurnProgress 的进度通知，以及通过 postRunnable 更新 UI 的过程。
- 学习者随后准确复述了单向通知，并追问逐个玩家等待动画的设计；已记录这项具体理解。
- 已补充第 3 节设计延伸：外层回合流程依次计算、等待表现、切换玩家；比较按回合、按动作暂停和预先计算后播放。
- 后续可结合新课简短复习：PlayTurnAsync 返回的任务何时完成、await 为什么不阻塞主线程，以及表现按动作记录播放时如何避免直接显示最终状态；不继续扩展第一课的协程专题，除非学习者追问。

## 证据与版本

- 源码基线：`1df6249a41301056ddc4448a95a93ddf5403e061`，核对日期 2026-09-09。
- 仓库架构文档保留 `CivilizationInfo`、`CityInfo`、`TileInfo` 等旧名称，当前源码为 `Civilization`、`City`、`Tile`。
- GameInfo.clone 对不同字段采用不同的复制方式，部分字段仍引用原来的对象；不能假定所有对象都被深拷贝。
- 自动存档有回合间隔条件；按钮可能执行选科技、选生产等动作，不一定推进回合。
- 不将地图 seed 等同于整局可复现；GameStarter 中还有基于 gameId 的随机上下文。
- Godot 外部 stable 文档会更新；涉及具体 API 的后续课程须核对学习者实际 4.x 版本。

## 教材验证

- 已使用 Playwright + Microsoft Edge 检查 5 个 HTML 页面，覆盖 2560×1440、1536×864 和 390×844 视口。
- 已检查本地链接、HTML 锚点、源码引用文件与行号范围、图片加载、12 题（01/02/03 课各 4 题）的对错反馈和键盘选择。
- 已检查打印时展开答案及打印后恢复原展开状态，并查看宽屏、移动截图和 PDF 排版。
- 此项仅记录教材验证，不是学习者练习结果；本轮未运行游戏构建或游戏测试。

## 第四课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 6 个页面（新增 0004-reading-with-evidence.html）和 4 课各 4 题（共 16 题）。
- 第四课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择、打印状态恢复。
- 游戏源码与测试已用 Gradle MCP 核验（见上“第四课的制作核对”），页面检查不覆盖游戏构建。

## 第八课的学习反馈

- 学习者反馈第 3 节末尾“createFractal/createBoreal 两种策略”段落与第 4 节表格看不懂：createXxx 函数名没有交代来历（对应界面地图类型下拉框），表格没有解释每列含义、没有分组，读者无法定位每行在说什么。
- 已修订（2026-09-15）：① goal 场景一与第 1 节 createPerlin 处补上“MapType 下拉框 → when 分派 → createXxx 函数”的铺垫；② 第 3 节末尾重写为“地图大小与 scale 的关系”完整段落，含 Kotlin REPL 实测代入计算（Small 33×21 maxdim=21、scale≈20、周期 1.1；Huge 87×57 maxdim=57、scale≈53、周期 1.1；Boreal 大轮廓 scale=3×√radius 实测 Small≈12/Huge≈19、湖层锁死 5.0），两种做法的取舍各给一句判据；③ 第 4 节表格重做：表前先讲 13 个函数只在“elevation 来源”与“是否重试”两件事上不同，逐列说明读法，按四组分割（纯噪声 / ridged / 噪声+几何提升 / 确定性公式+扰动），每行补界面选项中文名，组名给预期形态；表后收尾段同步改写。修订后 check-course.cjs 11 页 32 题复检通过。
- 写作教训（并入共享笔记的配对排版规则同级别）：首次出现的 createXxx 类函数名必须先交代它与界面选项的对应；表格必须先说明每列在比什么、行按什么分组，再放数据；涉及“两种做法对比”时每边都要给判据和代入例子，不能只给结论。

## 第八课的制作核对

- 源码入口：`core/src/com/unciv/logic/map/mapgenerator/MapLandmassGenerator.kt`、`core/src/com/unciv/logic/map/mapgenerator/Perlin.kt`、`core/src/com/unciv/logic/map/mapgenerator/MapGenerationRandomness.kt`（getPerlinNoise/getNoise）、`core/src/com/unciv/logic/map/mapgenerator/MapGenerator.kt`（applyHumidityAndTemperature L512、TerrainOccursRange L71、主循环 L556）、`core/src/com/unciv/logic/map/tile/Tile.kt`（humidity L172 / temperature L176 为 @Transient 生成期字段）、`android/assets/jsons/Civ V - Vanilla/Terrains.json`、`core/src/com/unciv/logic/map/MapParameters.kt`（waterThreshold/tilesPerBiomeArea/temperatureintensity/temperatureShift 默认值）、`core/src/com/unciv/ui/screens/newgamescreen/MapParametersTable.kt`（滑块范围：Water level −0.1～0.1、Temperature intensity 0.4～0.8、Biome size 1～15）、`MapGeneratorSteps.kt`（HumidityAndTemperature 步骤复制 temperatureintensity/temperatureShift）。
- 已核对：landTerrainName/waterTerrainName 取第一个无 NoNaturalGeneration 的地形名、landOnlyMod 退化；spawnLandOrWater 阈值判定（L99）；retryLoweringWaterLevel 的 0.02 步长、(waterPercent/0.7) 与 √retries 加速、30 次上限与 predicate 默认 ≤0.7；createPerlin 循环体内不动 RNG；Perlin.kt 为 Rosetta Code Kotlin 1.1.3 移植（permutation 256 项写死、noise 四步：floor+and255 定格、小数位置、fade、8 角 grad3 点积 + 三层 lerp；fade 端点导数为 0）；noise3d 倍频循环（freq×lacunarity、amp×persistence、total/max 归一化、默认 6 倍频）与 ridgedNoise3d 唯一差别 abs；13 个类型函数按骨架分组（纯噪声 / ridged / 噪声+几何提升 / 确定性公式+扰动），createFractal 的 ratio×30 scale（源码注释：大图保持平均大陆数）、createBoreal 双层不同 scale、createPangaea 的 predicate 恰一块大陆地 + 3/4 噪声 + 1/4 椭圆、createSpiral 的 sin(atan2−spin×hypot) + 0.18 扰动；applyHumidityAndTemperature：温湿各抽一次 z 层、湿度单倍频 (n+1)/2+humidityShift 截断、温度 (5×期望+1×噪声)/6 → |t|^(1−intensity) 保号 → +shift 截断、期望温度 1−2|纬度|（boreal 指数曲线、flatEarth 用 getTileRadius）、候选三组（landTerrains 排除 impassable/isRough/rareFeature）、elevationTerrains 跳过留给第 3 步、matchesHumidity 半开区间 (From,To] 与 toFloatMakeInclusive、匹配多个 randomOrNull(RNG)、ifEmpty 整组随机兜底；Snow 五条温湿矩形（越冷容忍湿度越宽）；Grassland/Plains/Desert/Tundra 矩形用于练习 B。
- 已用 Gradle MCP 重跑 `:tests:test --tests "com.unciv.logic.map.MapGenerationTests" --rerun`：4 过 0 跳过 0 失败（本会话）。
- 教学取舍：噪声倍频的环绕卷圆柱入口回指第七课不重复展开；山地（OccursInChains）只说明跳过，细节留 09 课；湖岸/植被/冰只作为温湿字段的后续使用者提及；getOceanEdgesTransform、TwoContinents 家族的 getLonLatFactors 细节收进类型分组表不逐个走读；练习 D 用 assertApproximately 容差连接 RNG 序列不确定性（randomOrNull 每格动 RNG）与第七课 seed 边界。
- 0008 初版选择题选项字数不一致（6/10/7/7 等），已按“选项等长”约定逐字修正为 7/7/8/7 后复检通过。

## 第八课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 11 个页面（新增 0008-landmass-climate.html）和 8 课各 4 题（共 32 题）；0008 新增截图锚点 #climate。
- 第八课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号（含 Terrains.json 本地链接）、图片、三种视口、4 题对错反馈与键盘选择（选项等长）、打印状态恢复。
- MapGenerationTests 已用 Gradle MCP 重跑核验：4 过 0 跳过 0 失败（本会话，--rerun）。页面检查不覆盖游戏构建。
- 以上是教材制作与验证，不记为第八课已掌握；学习记录需口述证据。

## 第九课的制作核对

- 源码入口：`core/src/com/unciv/logic/map/mapgenerator/MapElevationGenerator.kt`、`core/src/com/unciv/logic/map/mapgenerator/RiverGenerator.kt`、`core/src/com/unciv/logic/map/tile/Tile.kt`（三布尔 L89、isConnectedByRiver L699、isAdjacentToRiver L715、setConnectedByRiver L1237）、`core/src/com/unciv/logic/map/mapgenerator/MapGenerator.kt`（阶段链 L181/L199、convertTerrains L330、TerrainOccursRange L71）、`core/src/com/unciv/models/ModConstants.kt`（riverCountMultiplier=0.01 / minRiverLength=5 / maxRiverLength=666，L93）、`core/src/com/unciv/logic/map/MapParameters.kt`（elevationExponent 默认 0.7f）、`core/src/com/unciv/ui/screens/newgamescreen/MapParametersTable.kt`（Map Elevation 滑块 0.6～0.8，L484）、`core/src/com/unciv/models/ruleset/unique/UniqueTriggerActivation.kt`（OneTimeChangeTerrain → getOneTimeChangeRiverTriggerFunction → continueRiverOn，L1323/L1485）、`android/assets/jsons/Civ V - Vanilla/Terrains.json`（Mountain “Occurs in chains” L136、Hill “Occurs in groups” L171、虚拟 River L311、Tundra/Desert/Snow 的 “Becomes … when adjacent to [River]” L91/L106/L153）。
- 已核对：三候选表按 unique 分桶不写死地形名（flats 非 impassable 非粗糙、hills=occursInGroups、mountains=occursInChains，均排除 rareFeature，hills/mountains 允许 Land 或 TerrainFeature）；raiseMountainsAndHills 主循环（elevationSeed 抽 z 层、boreal +0.03 偏移、exponent=1−滑块−偏移、powSigned 保号幂、scale 写死 2.0、>0.7 山/>0.5 丘/否则平地）；applyTerrain（温湿过滤+ifEmpty 整组、dropElevatedFeatures、Land 写 baseTerrain、TerrainFeature 选 flats 承载底地形再叠特征）；cellularMountainRanges（目标=初始×2、四条标记规则：孤山 1/4 拆、单邻山 1/10 升、邻 3 不可通行 1/2 拆、>3 必拆、兑现段 rising/lowering 闸门 totalMountains 判定用 ×2 与目标比较、~Raising~/~Lowering~ 波浪号假特征不进存档）；cellularHills（孤丘/密丘 1/2 拆、山+丘邻 2..3 升丘、hasTerrain(mountains) 排除山格、闸门差异：第一轮放开、后轮 90% 线）；RiverGenerator（numberOfRivers=陆地×0.01 四舍五入、源头三级降级 山→丘→陆地 且 isFarEnoughFromWater=1..4 环无水、chooseSpreadOutLocations、getClosestWaterTile 逐环找水 maxRiverLength 上限、spawnRiver 起点随机 5/7 点钟、停止条件=顶点 touches 水或候选耗尽、三候选按最小 aerialDistanceTo 分组贪心+组内随机、repeat(666) 兜底）；RiverCoordinate（锚定格+BottomRightOrLeft 5/7 点钟、五个邻居引用构造时取齐、getAdjacentPositions 左右互斥规律、getAdjacentTiles 三格、paintTo 三分支：同格换侧 paintBottom/移向共同格 paint 自己下边/移向下新格让 newCoordinate paint、三个 paint 只置一个布尔+resultingTiles 记录）；Tile 侧（isAdjacentToRiver=自有三边任一或任一邻居间有河、@Cache 双字段、底部无邻居时自有三布尔仍算数、setConnectedByRiver 返回属性引用统一 get/set、无变化返回 false、失效两侧缓存、convertTerrains 参数）；convertTerrains（getMatchingUniques(ChangesTerrain)+isAdjacentTo(params[1])、Land 改底地形/特征过 occursOn、整图与编辑器 resultingTiles 两个调用时机）；continueRiverOn 与生成路径四差异（边 vs 顶点、评分 vs 贪心、stateBasedRandom vs RNG、setConnectedByRiver 统一入口）。
- Kotlin REPL 实测（2026-09-15，:tests test 源集 + HeadlessFiles 绝对路径 load 规则集）：规则集常量 0.01/5/666；Mountain 带 OccursInChains、Hill 为 TerrainFeature 带 OccursInGroups 且 occursOn=[Tundra,Plains,Grassland,Desert,Snow]；虚拟 River 为 TerrainFeature/gold=1/Doesn't generate naturally；Desert→Flood plains、Tundra→Plains、Snow→Tundra 三条 Becomes unique；幂前门槛 0.7^(1/0.3)≈0.30、0.5^(1/0.3)≈0.099；Small 六边图 seed 42：705 格/510 陆地（72.3%）/山 22/丘陵 112/5 条河（=round(510×0.01)）/81 条河边/80 格临河/2 格泛滥平原/样例山格 2 个山邻；离水 ≥5 环候选：山 8、丘 64、陆地 268。
- 已用 Gradle MCP 重跑 `:tests:test --tests "com.unciv.logic.map.MapGenerationTests" --rerun`：4 过 0 跳过 0 失败（本会话）。
- 教学取舍：continueRiverOn 的优先级表（连海 +9/−1、Y 形 −2、开放端 +2/+3）只讲结构与两处汇合点，不逐数走读；numberOfConnectedRivers 标注 @Suppress("unused") 的 how-to 定位不展开；RiverDirections 枚举（控制台用）不进正文；练习 B 用 3×3 手推顶点走线替代 REPL 复现（顶点逻辑纸上更清楚）；第八课 elevationTerrains 跳过的回指放在目标节口述提示。
- REPL 环境备注：`:tests` 源集的 REPL 无法解析 com.unciv.models.ruleset（main 输出未进 REPL 类路径的符号解析）——用 RulesetCache.getVanillaRuleset() 需先手动 `Ruleset().load(FileHandle(绝对路径))` 并放入缓存；`uniques` 元素是 String（按 placeholderText 解析在 Unique 对象上，直接 .text 会解析失败）；会话第三次 run 后会出现 NoClassDefFoundError: ReplState，重启会话即可。

## 第九课的学习反馈

- 学习者指出第 1 节 powSigned 段看不懂：① 原文“负海拔幂完更接近 0、正海拔也更接近 0”方向写反——指数 <1 的幂对 0..1 的值是抬升不是压低（0.3^0.3≈0.70）；原文还把“0.55^0.3 ≈ 0.83 够得着 0.7”与错误的“压低”叙事混在一起，读者无法自洽。② 学习者追问“为什么不能直接拿噪声当 elevation、滑块 0.8 当阈值”——这是量程问题：噪声 ±0.55 够不着写死的 0.7/0.5 阈值，滑块直接当阈值时 0.6~0.8 段永远切不出山。③ “1/0.3 的 1/3 从哪来”——反解开方：n^指数 > 阈值两边取 1/指数 次幂。
- 已修订（2026-09-15）：④⑤ 两段重写为“量程对不上 + 滑块要能调多少”两个原因，代入 REPL 实测值（0.05^0.3≈0.41、0.3^0.3≈0.70、0.55^0.3≈0.84、越小的数抬得越狠），并补“滑块直接当阈值”的反例；幂前门槛段补 1/指数 是开方逆运算的推导；表格数字复算修正（0.43→0.41、0.22→0.17、0.28→0.27、0.19→0.18）；练习 A 核对要点同步；hexmath 速查同步。修订后 check-course.cjs 12 页 36 题复检通过。
- 写作教训：数值表格交稿前必须逐格复算（本课 8 个门槛数字有 4 个算错，靠学习者追问暴露）；“幂后更接近 0”这类方向性表述要用一个代入数字自检（0.3 幂后是 0.70，比 0.3 大，一句话就能证伪原文）。
- 术语反馈（2026-09-15）：学习者指出“底地形”不像人话，第六课原用词是“基础地形”。已把 0008（1 处）、0009（13 处）统一替换为“基础地形”，architecture.html 术语表新增 #base-terrain 条目，共享笔记与课程 NOTES 的写作要求同步记录；后续课程沿用。
- 编号错乱修正（2026-09-15）：第 5 节 convertTerrains 配对块在修 ③ 时重复了 ② 的内容、④ 出现两次，代码注释与右侧讲解 ①～④ 失去一一对应。已整段重排为四段各归其位：①三条 Becomes 规则与临河判定、②Land 改基础地形、③特征过 occursOn（含 lastTerrain 与合法性门）、④刷新缓存与调用时机。教训：修订配对块时同步检查编号对应。
- 选择题残句修正（2026-09-15）：第 3 题选项曾出现“离够水”这类为凑等长硬造的残句；已改为 a 贴着水边的山 / b 贴着水边的丘 / c 均匀分散的格 / d 山地且远离水（全 6 字，d 为答案）。教训：选项等长约束下也要保证每项是自然中文，先拟语义再配平字数。

## 第九课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 12 个页面（新增 0009-elevation-rivers.html）和 9 课各 4 题（共 36 题）；0009 新增截图锚点 #elevation。
- 第九课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择（选项等长）、打印状态恢复。
- 0009 初版第 2、3 题选项字数不一致（9/7、7/6 混排），已按“选项等长”约定逐字修正（Q2 全 7 字：两边各自存一份/虚拟地形的条目/记在B的布尔上/独立河流的结构；Q3 全 6 字：离水最近的陆/丘陵且离够水/均匀分散的格/山地且离够水）后复检通过。
- MapGenerationTests 已用 Gradle MCP 重跑核验：4 过 0 跳过 0 失败（本会话，--rerun）。页面检查不覆盖游戏构建。
- 以上是教材制作与验证，不记为第九课已掌握；学习记录需口述证据。

## 第十课的制作核对

- 源码入口：`core/src/com/unciv/logic/map/mapgenerator/mapregions/`（MapRegions.kt、Region.kt、RegionStartFinder.kt、MapGenTileData.kt、StartNormalizer.kt、MinorCivPlacer.kt）、`core/src/com/unciv/logic/map/mapgenerator/resourceplacement/`（LuxuryResourcePlacementLogic.kt、StrategicBonusResourcePlacementLogic.kt、MapRegionResources.kt）、`core/src/com/unciv/logic/map/mapgenerator/MapGenerator.kt`（L204 分支、L217 奇观先于资源、L211 区域数）、`core/src/com/unciv/logic/map/mapgenerator/MapResourceSetting.kt`、`core/src/com/unciv/logic/map/MapParameters.kt`（getMapResources/getStrategicBalance/getLegendaryStart L153-159）、`core/src/com/unciv/ui/screens/newgamescreen/MapParametersTable.kt`（Resource Setting L320、Strategic Balance L395、Legendary Start L402）、`android/assets/jsons/Civ V - Vanilla/Terrains.json` 与 `TileResources.json`。
- 已核对：generateRegions 的两条分支（numRegions≤0 返回、continentSizes 空抛错、radius 按形状、usingArchipelagoRegions 阈值 0.25、大陆按 fertility/(1+已分数) 分配与列号包矩形）；divideRegion/splitRegion 的整数取整与 fertile 目标；assignRegionTypes 的排除先于入选、按 priority 升序 break、Hybrid 默认；getTileFertility 的 +1 临河/+1 临淡水/+2 临海；findStart 三层收缩与 findGoodPosition 的 river/wet/dry 分组；evaluateTileForStart 的三环累计、三组下限常量、两张分数表、第 2 环有效锤折算、maximumJunk=9、closeStartPenalty 否决与扣分；setRegionStart 的 closeStartPenaltyForRing 与 addCloseStartPenalty 的 max×1.2 封顶 97；StartNormalizer 的清冰/补丘陵/粮²除4 档位/传奇+2/绿洲等价物/环序重排/AddProductionBonuses/Strategic Balance 保底；startBias 分桶与处理顺序；MinorCivPlacer 的分组与 impact 避让；placeResourcesAndMinorCivs 的调用顺序（奇观→奢侈品分配→城邦落位→奢侈品落图→战略与奖励）；TileDataMap.placeImpact（中心 99、向外递减、重叠 +2 封顶 50）与四类半径；assignLuxuries 的 maxRegionsWithLuxury/水域门槛/权重递减/城邦 4 种；placeLuxuries 四批与 fillSpecialLuxuries；placeStrategicAndBonuses 的 ruleLists 去重与四轮放置 + 保底 + 奖励 + 三环；MapRegionResources 两趟与 ratio。
- Kotlin REPL 实测（2026-09-15，:tests test 源集 + 手动载入 G&K 规则集放入 RulesetCache 供 TestGame 使用）：Small 六边图（seed 42，4 大文明 + 4 城邦）→ 705 格 / 509 陆地 / 4 块区域（146·84·128·128，类型 Hybrid/Plains/Hybrid/Grassland）/ 8 个文明全部落位 / 起点 1～2 环各 3～4 份奢侈品 / 174 个资源格（14 种奢侈品、7 种奖励、6 种战略）；四张分数表与全部常量逐格对照源码复算无误。
- 教学取舍：MinorCivPlacer 只讲“分桶与避让”结构，不逐条展开五种分配路径；BiasTypes 的日志枚举不进正文；LuxuryResourcePlacementLogic 的 randomLuxuryRatios 明细只在速查里给出入口；Tile.resourceAmount 与 sparse/abundant 的数值属第 17 课，本课只说明“战略资源分大小矿”。
- REPL 环境备注：TestGame 依赖 RulesetCache（REPL 工作目录不对会加载失败）——需先 `Ruleset().load(FileHandle(绝对路径))` 并 `RulesetCache[BaseRuleset.Civ_V_GnK.fullName] = it`，再 `TestGame()`；导入语句必须单独一次 run，混写会报 “Expecting an element”。
- 2026-09-17 质量修订（学习者反馈“整篇质量下降、读不出每段代码在干啥”）：全课 15 个配对块的头部注释从“①～N 与右侧对应”改为“问：XXX”；右侧 ① 前统一加一句脑内模型导入（分蛋糕/排考场/相亲排序/打地段分/圈地挂牌/体检单/气味/撒种子/两趟采购/分房间）；calculateFoodBonusesNeeded 右侧重写为“体检单：粮势（粮²/4）+ 底子（原生 2 粮格）→ 分级表读档位”，前 3 行看粮势、后 2 行看底子；normalizeStart 右侧改为“验收员逐项体检”；placeFoodBonuses 右侧改为“井字格摆子”，并修正环序重排的错误（2,3,1 与 3,1,2，按源码注释与 abs 值复算：abs(2×10−22)=2 < abs(3×10−22)=8 < abs(1×10−22)=12 升序即 2,3,1；abs(3×10−17)=13 > abs(1×10−17)=7 > abs(2×10−17)=3 降序即 3,1,2——旧文“优先三环其次一环”是错的）；“锤”在 MapGenTileData ② 首次出现处加俗语定义；矩形跨接缝例子换成宽 10/左 3 列右 2 列。

## 第十课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 13 个页面（新增 0010-starts-resources.html）和 10 课各 4 题（共 40 题）；0010 新增截图锚点 #regions。
- 第十课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择（选项等长：Q1 全 7 字、Q2～Q4 全 9 字）、打印状态恢复。
- MapGenerationTests 已用 Gradle MCP 重跑核验：4 过 0 跳过 0 失败（本会话，--rerun）。页面检查不覆盖游戏构建。
- 以上是教材制作与验证，不记为第十课已掌握；学习记录需口述证据。

## 第十一课的制作核对

- 源码入口：`core/src/com/unciv/logic/map/MapPathing.kt`（roadPreferredMovementCost 0.5/1、isValidRoadPathTile、getPath/getConnection 的逐步 AStar 循环）、`core/src/com/unciv/logic/map/PathingMap.kt`（类注释的未来计划、fetchCache 键失效与 CAS、createUnitPathingMap/createRoadPathingMap 等 4 个工厂）、`core/src/com/unciv/logic/map/PathingMapAStarPathfinder.kt`（PrioritizedNode、calculateUnderestimatedMovement、stepUntilDestination、FASTEST_ROAD_COST=0.1 与 init 断言）、`core/src/com/unciv/logic/map/PathingMapCache.kt`（RouteNode 位域注释、FixedPointMovement base-30、PathingMapCacheKey、fork/merge）、`core/src/com/unciv/logic/map/mapunit/movement/MovementCost.kt`（判定链顺序、ZoC 两个私有函数、hasDoubleMovement 三层）、`core/src/com/unciv/logic/map/mapunit/movement/UnitMovement.kt`（getMovementToTilesAtPosition 松弛、getShortestPath 主循环、canPassThrough/canMoveTo、PathfindingCache、PathsToTilesWithinTurn）、`core/src/com/unciv/logic/map/tile/RoadStatus.kt`（Road 0.5/1/3、Railroad 0.1/0.1）、`core/src/com/unciv/models/metadata/GameSettings.kt`（useAStarPathfinding 默认 false）、`core/src/com/unciv/logic/map/BFS.kt`（getPathBetweenTiles 用）。
- 已核对：代价链 12 层顺序（cannotMove/上下船/ZoC 100f → allTilesCosts1 → 铁路 movement 0.1 → 道路 movementSpeedOnRoads（TechManager L52 声明默认 1f，L530 按 RoadMovementSpeed unique 改写；实测无科技 0.5、机器后 1/3——TechManager 初始化路径会把它设成 0.5，正文按实测写）→ ignoresTerrainCost → 渡河 100f → lastTerrain.movementCost（城市 1f）→ 双倍移动 ×0.5 → 兜底；100f 哨兵语义；可达图的钉价规则（totalDistanceToTile = usableMovement）与满价标记攻击目标；canPassThrough/canMoveTo 分工与测试的六种邻居用例；getShortestPath 按回合数优化（movementTreeParents 只记落点、visitedTilesBitset 按 zeroBasedIndex、边缘剪枝、cannot-move 返回空）；A* 侧 RouteNode 位域 20+3+9+9+6+3+1+2 位、damagingTiles 最高位、parentClockDir 存钟点/2、FixedPointMovement base-30（1.0=30、3.666→110、9 位上限 17.0）、PathingMapCacheKey 三元组、启发式 = aerialDistanceTo×0.1 与 init 断言、图级缓存与“早先路径轻微偏好”偏差（类注释自述）、两代实现差异表（中立平民穿行/零移动力邻居/铁路环线/进山暂停/缓存级别/数值表示，均有 assumeThat 标注的测试对应）。
- Kotlin REPL 实测（2026-09-17，:tests test 源集 + HeadlessFiles 绝对路径载入 G&K 规则集 + TestGame.makeHexagonalMap(8)，217 格）：G&K 地形 movementCost 非 1 的只有 Hill/Forest/Jungle 2、Marsh 3、Fallout 2（River 0 是虚拟地形）；平地一步 1、丘陵 2、沼泽 3；道路无科技 0.5、addTechnology("Machinery") 后 1/3；铁路 0.1；FPM_ONE.bits=30、FPM_POINT_FIVE.bits=15、fpmFromMovement(3.666f).bits=110 读回 3.6666667；移动力 2 平地图可达 19 格（起点+一环 6×1+二环 12×2），一格丘陵（addTerrainFeature("Hill")）后 18 格、丘陵格钉价 2.0、其后 (0,2) 无记录；1.5 移动力 18 格、丘陵格 1.5、远格 1.0；两丘直穿返回 [(0,1),(0,2),(0,3)]（3 落点）；移动力 3 返回 [(0,2),(0,3)]（2 落点）；移动力 5 到达 (0,3) 剩 1.0；14 格铁路环线（A* 关与开结果一致：[(3,4),(0,4)]）；山围死 (2,0) 返回空；渡河测试场景 [(2,0),(5,0)]；迦太基过山 avoidDamagingTerrain=[(0,1),(0,4),(0,5),(0,8)]；canMoveTo 相邻 true 与 canReach false（移动力 0）。
- 已用 Gradle MCP 重跑 `:tests:test --tests "com.unciv.logic.map.PathfindingTests" --rerun`：114 个用例（参数化双算法）108 过 6 跳过 0 失败（本会话）。
- 教学取舍：MapPathing 的老 AStar（getPath/getConnection）只在第 4 节的类分工里提名字不逐行走读（PathingMap 类注释声明它要完全替代）；calculateNeighborNode 的五分支（同回合/跨回合/占位 stub/追补暂停/带伤）只展开语义不逐行；ZoC 的 anyTilesExertingZoneOfControl 实现细节（只查两个公共邻居、海军对陆海都生效）进正文但 #4085 的性能讨论不展开；escort/护卫的成对移动只给一句“取更贵的价”；PathsToTilesWithinTurn.getPathToTile 与 moveToTile 的执行细节留给第 20 课；测试的 36 个“能过/能停”参数化用例只节选两条做配对块，其余收进一句话总结。
- REPL 环境备注：本课沿用第十课的会话方法（Ruleset().load(FileHandle(绝对路径)) + RulesetCache[BaseRuleset.Civ_V_GnK.fullName] + TestGame()）；import 单独一次 run 的规则不变；本次会话早期连续两次 import 后即出现 NoClassDefFoundError: ReplState，重启会话一次后全程稳定，共约 30 次 run。
- 写作范式：沿用第十课“问：XXX + 脑内模型”范式（全课 9 个配对块，脑内模型依次为：收费站设卡/水波扩散/门口的六种邻居/一圈圈扎营/出差选路线/两套电梯/登记卡压进行李箱/导游估时）；配对块编号用临时脚本校验（Circled number 序列左右一一对应、无缺号重号），初稿发现 3 处不配对（断言用例块 ①①/②② 重号、两套缓存块 ⑥ 出现两次、目标块右侧段内引用误读），已全部修正后复检 ALL PAIRED BLOCKS OK。

## 第十一课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 14 个页面（新增 0011-movement-pathing.html）和 11 课各 4 题（共 44 题）；0011 新增截图锚点 #cost。
- 第十一课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择（选项等长：Q1～Q3 全 7 字、Q4 全 7 字，Q4 初版“铁路价当每格下限”8 字已改为“铁路价当格下限”）、打印状态恢复。截图与 PDF 输出至 out11 目录。
- 0011 初版一处 GitHub 链接 commit hash 笔误（1df6249a…dc4448 漏字符）、练习 D 的 setTileTerrain/addTerrainFeature 环境描述不准（实测两条路径都有效，setTileTerrain 后需 setTransients），已修正后复检。
- PathfindingTests 已用 Gradle MCP 重跑核验：108 过 6 跳过 0 失败（本会话，--rerun）。页面检查不覆盖游戏构建。
- 以上是教材制作与验证，不记为第十一课已掌握；学习记录需口述证据。
- 交付后的两轮学习者反馈修订（2026-09-17）：① hasConnection 问答确认道路是“格”属性非“边”属性（civilopedia 原文为证；渲染层把相邻道路格自动连线，视觉的线段是涌现的；civ 参数是给易洛魁 forestsAndJunglesAreRoads 的例外）；河流才是边存储需查公共边（Tile.kt:701 钟点方位）。② 术语修订：把 cost 的生造量词「费」（1 费/0 费/费猜）全文统一为「代价」，量词场合说「耗几点移动力」——0011 19 处、RESOURCES.md 2 处、hexmath.html 1 处，Q1 选项「按格子数算一费」→「按格子数算一格」保持 7 字等长；配对校验与 check-course 复检通过。此词已列入写作检查项：新课文交稿前 grep「费」排除生造用法（收费站/消费/附加保留）。
- 河流追问确认：文明 5 无顺河行舟机制（河流是边属性罚金非航道，核心源码无 navigable 相关代码）；“坐船”=海军单位走水格（同一套寻路）+embark 登船（代价链顶部两行）；顺流航行是文明 7 机制，超 Unciv 复刻范围。已向学习者解释，未改教材正文。
## 第十二课的制作核对

- 源码入口：`core/src/com/unciv/logic/map/TileMap.kt`（ViewableTile L486、getViewableTiles L490 的按环算法与 a/b/c 三高度注释）、`core/src/com/unciv/logic/map/tile/Tile.kt`（unitHeight/tileHeight L976-979、isVisible L308、isExplored L316、exploredBy L54、setExplored L1198、getShownImprovement L390）、`core/src/com/unciv/logic/civilization/transients/CivInfoTransientCache.kt`（updateViewableTiles L107、updateViewableInvisibleTiles L154、setNewViewableTiles L189、updateLastSeenImprovements L220、updateOurTiles L177、viewedCivs 与 makeCivilizationsMeet、discoverNaturalWonders）、`core/src/com/unciv/logic/map/mapunit/MapUnit.kt`（getVisibilityRange L424、updateVisibleTiles L822-857 的两个免算条件、moveThroughTile L990 末尾的 updateVisibleTiles L1029、destroy L946）、`core/src/com/unciv/logic/civilization/Civilization.kt`（viewableTiles L101、lastSeenImprovement L226 / setLastSeenImprovement L1293）、`core/src/com/unciv/logic/civilization/managers/TurnManager.kt`（startTurn L61）、`core/src/com/unciv/logic/GameInfo.kt`（updateCivilizationState L824-836）、`core/src/com/unciv/ui/components/tilegroups/TileGroup.kt`（十一层字段 L48-58、isViewable L82、update L102 的三态分支）、`core/src/com/unciv/ui/components/tilegroups/TileGroupMap.kt`（init 装箱、sortedGroups L142、tileGroupLayer 影子层）、`core/src/com/unciv/ui/components/tilegroups/layers/TileMapLayer.kt`、`TileLayerOverlay.kt`（fog/unexplored doUpdate L78）、`core/src/com/unciv/ui/screens/worldscreen/worldmap/WorldMapTileUpdater.kt`（updateTiles L19）、`core/src/com/unciv/ui/screens/worldscreen/WorldScreen.kt`（mapHolder.updateTiles L430、selectedGameView/spectatorGameView L112-121）、`core/src/com/unciv/view/TileView.kt`（isVisible(unit) L46）、`core/src/com/unciv/view/CivView.kt`（canSeeTile L34、hasExplored L54）、`tests/src/com/unciv/logic/map/VisibilityTests.kt`（18 条）。
- Kotlin REPL 实测（2026-09-17，:tests test 源集 + HeadlessFiles + 手动 Ruleset().apply{load(绝对路径)} 放入 RulesetCache + TestGame.makeHexagonalMap(5)，91 格）：平地视野 2 → 19 格（起点+6+12）；第 3 环 (3,0) 丘陵 → 20 格（界外一环只留高的）；(1,0) 森林挡 (2,0) 平地后 → 18 格；山 (2,0) 在森林后仍可见；可打不含（canSeeButNotAttackHillForestOverHill：见 true、attack false）；丘陵+森林 unitHeight=1 / tileHeight=2；Forest 的 tileHeight=1；Catapult Sight −1 → 视野 1 → 7 格；单位从 (0,0) 到 (1,0) exploredBy 19→24；视野外拆 Farm 后 getShownImprovement（人类）仍 Farm、AI 读真值 null。REPL 环境备注：Ruleset.load 返回 Unit，要用 `Ruleset().apply { load(FileHandle(...)) }` 才能拿到对象；TestGame 放 putInTile 前若被占格会抛不合法状态，需要 removeFromTile 清干净。
- 已用 Gradle MCP 重跑 `:tests:test --tests "com.unciv.logic.map.VisibilityTests" --rerun`：18 过 0 跳过 0 失败（本会话）。
- 教学取舍：CanSeeOverObstacles/NoSight 只讲结构不逐行；viewableInvisibleUnitsTiles（隐身单位）一句带过（海军内容留后续）；espionage/witcher 等条目点到为止；TileGroupMap 的滚动 wrap 搬运代码不进正文；LobbyView/cityButton/多层 hit 行为只讲职责不逐行；lesson 11 的 hasConnection 问答与「费→代价」全文检查项沿用（0012 自检无生造「费」）。
- 第 12 课配对校验：临时脚本 `C:\Users\zhengtongshan\AppData\Local\Temp\opencode\pairing-check-0012.cjs`（圈号集扩到 ⑳），12 块全部 OK。
- 移动端溢出修订（2026-09-17）：第 1 节 VisibilityTests 段把测试文件路径改为本地链接（长路径不断行造成 390 视口 2 像素溢出）；顺道把脑内模型段中的编号引用从正文句首去掉（配对脚本只认句首圈号）、把正文里的 ASCII 引号统一为中文引号、右侧 ④⑤/⑥⑦⑧⑨ 连写处理、改良层 ⑥ 的尾随注释收窄。修订后 pairing-check 12 块 OK、check-course 15 页 48 题复检通过。
- 第 4 题残留按钮修订（2026-09-18，学习者发现并指出）：题 4 表单结尾多出一段 `<button>核对答案</button><output></output></form>`（无对应 form 容器），渲染成全宽大按钮且与题 4 无关。原因是上次修订时复制模板留下的孤儿标记；check-course 只统计 form.quiz 的 input required 而未检测孤立按钮。已删除孤儿标记并重跑 pairing-check 与 check-course 双双通过。后续写作规则：选择题 form 结构严格四件套（fieldset + button + output + /form），禁止在 form 外再追加配套 button/output。
- 视线算法脑内模型扩写（2026-09-18，学习者提问“高格能挡住哪些格，描述像射线检测”）：第 1 节脑内模型段重写为“门槛接力棒”四步模型（每格拿两样：身高 + maxHeightSeenToTile 门槛；判定取内圈门槛最低；入账时 max(自身身高, 门槛) 继续外传；被挡 = 所有内圈门槛都≥外圈身高、且 c≤b）。新增几何句点出“轴格只有 1 个内圈入口，斜角格 2 个——单个高格沿轴投笔直窄影（实测 19→18 印证只挡 (2,0)），成排高地才封出一片”。几何句不落具体坐标，避免把第五课的 (1,1)（实际是环 1 轴线格）误写成斜角例；坐标级核对已记入 RESOURCES.md 第十二课条目，注明“轴格 (2,0) 内圈邻居仅 (1,0)；斜角格 (2,1) 内圈邻居 (1,0) 与 (1,1)”并按 Unciv 邻居偏移表推过。螺旋校验：配对脚本 12 块 OK、check-course 15 页 48 题通过。

## 第十二课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 15 个页面（新增 0012-sight-fog.html）和 12 课各 4 题（共 48 题）；0012 新增截图锚点 #sight。
- 第 12 课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口（修订后）、4 题对错反馈与键盘选择（选项等长：Q1 全 7 字、Q2 全 7 字、Q3 全 7 字、Q4 全 7 字）、打印状态恢复。截图与 PDF 输出至 out12 目录。
- 术语自检：grep「费」无生造用法（仅「消费/免费」正常词）。
- VisibilityTests 已用 Gradle MCP 重跑核验：18 过 0 跳过 0 失败（本会话，--rerun）。页面检查不覆盖游戏构建。
- 以上是教材制作与验证，不记为第十二课已掌握；学习记录需口述证据。

## 第十三课的制作核对

- 源码入口：`core/src/com/unciv/logic/map/tile/TileStatFunctions.kt`（getTileStats L31/L36、getTileStatsBreakdown L57、getTerrainStatsBreakdown L164、getTilePercentageStats L192、getExtraImprovementStats L294、missingFromMinimum L138、TilePercentageCategory L183）、`core/src/com/unciv/logic/city/CityStats.kt`（updateTileStats L349、update L485、updateBaseStatList L435、updateCityHappiness、getStatPercentBonusList、updateFinalStatList L513、StatTreeNode L25）、`core/src/com/unciv/models/stats/Stats.kt`（七字段、DefaultCityCenterMinimum L277、add/clone/isEmpty、StatMap）、`core/src/com/unciv/logic/map/tile/Tile.kt`（getUnpillagedTileImprovement L336、getUnpillagedRoadImprovement L347、isCityCenter L322、setConnectedByRiver L1237、setTileResource L918）、`android/assets/jsons/Civ V - Gods & Kings/Terrains.json`（Grassland L24 / Plains L49 / Flood plains L275 / 虚拟 River L311）、`android/assets/jsons/Civ V - Gods & Kings/TileImprovements.json`（Farm L4）。
- 已核对：getTileStatsBreakdown 的完整来源顺序（地形票面 → 城市三桶 → 河流 → 资源（观察方）→ 改良/道路合计 → 保底补差 → 滤空/克隆）；三桶优先级 改良 > 地形/通用 > 道路（matchesFilter 全匹配）；NullifyYields 终止遍历、overrideStats 整层替换；getExtraImprovementStats 的三段（票面 Stats / ImprovementStatsForAdjacencies 按邻居数 / ImprovementStatsOnTile 按本格条件含淡水与非淡水）；百分数三桶 TilePercentageCategory 按结算单行来源名归类（改良名→Improvement、道路名→Road、其余→Terrain）；getTilePercentageStats 的收集分支持城市用 city.forEachMatchingUnique、否则用 observingCiv.forEachMatchingUnique；updateTileStats 的工作格三条件（城中心 || isWorked || 拥有 TileProvidesYieldWithoutPopulation）与被封锁格的停派+标重派；update 的顺序依赖（updateTileStats 先于 happiness，百分树在 finalStatList 后逐项落乘）；currentCityStats 是给 UI 钉住的最终七数。
- 实测（2026-09-18，测试类替换 REPL（REPL 「code is required」异常复现），TestGame 半径 2 六边形图 19 格 + G&K 规则集手动载入 + HeadlessFiles 绝对路径）：草地+农田（带 civ）结算单 2 行（Grassland +2 粮 / Farm +1 粮），合计 3 粮；加河边（未 Civil Service）多一行 River +1 金、合计 3 粮 1 金；解锁 Civil Service 后 Farm 变 +2 粮（票面 1 + 淡水条件 1），合计 4 粮 1 金；纯沙漠结算单无行；城中心草地合计 = 2 粮 1 锤（Grassland +2 粮 + Minimum +1 锤）；Wheat 资源 civ 可见时加一行 +1 粮，civ 不可见时无此行；no-civ 预览同格 3 粮（文明条件阻绝）；人口 1 首都（城中心草地+保底 + 派工农场）statsFromTiles = +1⚙+5⁂，Population 行 baseStatTree = +1 科学 → final[−2 粮，+1 科学]，currentCityStats = +1⚙+3⁂+1⍾；人口 2 自动安排人口到另一块草地，statsFromTiles 变 +1⚙+7⁂（freePop 的 1 生产走 Population 行不进 statsFromTiles——与第十课“锤从哪来”的区分一致）。
- 测试证据（本课未重跑新断言，全部沿用既有测试作行为钉）：GlobalUniquesTests 的 statsFromTiles / statsFromTilesMultifilter / statsFromTilesWithout / statsFromObject / statsFromCitiesOnSpecificTiles 一组用例钉住了城市 unique 三桶分拣；CityFounderTest 的「should exploit city center yields without requiring pop」用例（statsFromTiles.production == 2f）钉住了城中心免费产出路径；PercentProductionUnits 相关用例钉住了百分树的来源分支。
- 教学取舍：getTileStartScore / getTileStartYield / getStatDiffForImprovement 不进正文（起点评价已第十课、交换差逻辑属 17 课）；NullifiesStat / ConvertGoldToScience / Excess food to production 等特殊转化一句话提及不逐一走读；happinessList 只列入口（updateCityHappiness 的 unhappinessModifier 双重乘法，源码注释里 xlenstra 已有疑问，留 19/27 课复盘）；Population/Specialist/Trade route 行只报名字，分配给 14/15/16 课；本课重心全在 statsFromTiles 这条地块进账的唯一管道。
- REPL 环境备注（第十三课，沿用第九课协议）：kotlin_repl 会话常用 import 正常、run 即报「code is required」参数被吞；第二次 start 新鲜会话也会复现；stop 后改写测试类实测最省事（写完即删）。测试类小抄：BaseRuleset 在 com.unciv.models.metadata 包、Tile.setTileResource("Wheat") 是资源写入标准入口、t1.setConnectedByRiver(neighbor, true) 按钟点方位落边（第九课签名）、TestGame 规则集来自 RulesetCache[BaseRuleset.Civ_V_GnK.fullName].clone()。
- 配对与校验（2026-09-18）：全课 8 个配对块左右 ①～⑩/①～⑦ 一一对应，pairing-check-0013.cjs 全 OK；check-course.cjs 扩为 16 页 52 题全 PASS，0013 新增截图锚点 #receipt；选择题 Q1～Q4 全 7/6/7/7 字等长（已实测）、选项自然中文；「费」grep 0013 仅「免费算/维护费/消费」等正常词；基线 commit hash 仅一种写法。

## 第十三课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 16 个页面（新增 0013-tile-yields.html）和 13 课各 4 题（共 52 题）；0013 新增截图锚点 #receipt。
- 第 13 课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择（选项等长）、打印状态恢复。截图与 PDF 输出至 out13 目录。
- 以上是教材制作与验证，不记为第十三课已掌握；学习记录需口述证据。

## 第十四课的制作核对

- 源码入口：`core/src/com/unciv/logic/city/managers/CityPopulationManager.kt`（getFreePopulation L45、getFoodToNextPopulation L51、nextTurn L99、addPopulation L136、autoAssignPopulation L155）、`core/src/com/unciv/logic/city/City.kt`（tiles/workedTiles/lockedTiles L102、getWorkableTiles/getWorkedTiles/isWorked L211、foodForNextTurn L292、workTile L387、reassignPopulation L449）、`core/src/com/unciv/logic/city/CityStats.kt`（updateTileStats L349、updateBaseStatList L435、update L485、updateFinalStatList L513、calcFoodEaten L643）、`core/src/com/unciv/logic/city/managers/CityTurnManager.kt`（startTurn L23、endTurn L160）、`core/src/com/unciv/logic/city/managers/CityExpansionManager.kt`（takeOwnership L178、relinquishOwnership L151）、`core/src/com/unciv/logic/map/tile/Tile.kt`（owningCity L128、getOwner L410、getWorkingCity L460、providesYield L496、setOwningCity L894）、`tests/src/com/unciv/logic/city/managers/CityPopulationManagerTest.kt`。
- 已核对：所有权由 `City.tiles`/`Tile.owningCity` 表示，候选工作地由 `getWorkableTiles` 再按 owner 筛选，人口实际工作的坐标在 `City.workedTiles`；`CityStats.updateTileStats` 的三类并集为城中心、有人工作的地块、免人口产出格；专家不进入 `workedTiles`，`getFreePopulation` 另行扣除；`foodForNextTurn` 读取 `currentCityStats.food`，不是地块粮食或 `foodStored`；`CityTurnManager.endTurn` 将净粮食传给 `CityPopulationManager.nextTurn`。
- 最小案例已用临时测试类核对并删除：标准速度、普通人类城市、人口 1、城中心草地 + 有人工作的草地农场；地块收入 5 粮，人口消耗 2 粮，`foodForNextTurn=3`；`foodStored=12` 时本回合达到 15，人口升到 2，存粮为 0，自动安排人口后自由人口为 0。
- 已用 Gradle MCP 运行：临时探针 1 过；`CityPopulationManagerTest` 15 过 0 跳过 0 失败。REPL `code is required` 坑沿用第 13 课记录，本课实测仍走临时测试类。
- 教学取舍：不展开 `Automation.rankStatsForCityWork` 的全部权重；不展开专家产出、`CarryOverFood`、`NullifiesGrowth`、`avoidGrowth` 的全部规则，只在 `nextTurn` 分支和扩展说明中定位；本课核心是所有权/人口分配边界、净粮食、存粮阈值和增长后的重新分配。

## 第十四课的验证记录

- 第 14 课页面已制作：`0014-population-growth.html`；新增锚点 `#ownership`；课程校验脚本同步为 17 页、14 课、56 题。
- 页面需通过 Playwright + Edge 检查：本地链接、源码链接、锚点、三种视口、4 题对错反馈（含键盘选择）、打印状态恢复；输出截图目录为 `out14`。
- 2026-09-18 文案修订：按用户反馈清理生造词，全文「派工」改为「人口分配/有人工作的地块」，删除「主算例」「工位」「柜台」「招聘」等表述；选择题选项改为自然完整措辞、不再等长（`check-course.cjs` 已删等长断言；`lessons/README.md` 第 6 条同步改写）；第 4 题题干与选项重写为「所有权与人口分配的区别」。修订后重跑 `check-course.cjs` 通过：17 页、56 题、三种视口、键盘答题、打印恢复，输出目录 `out14v2`；本课未改源码与行为，`CityPopulationManagerTest` 15 过记录沿用。
- 以上是教材制作与验证，不记为第十四课已掌握；学习记录需口述证据。


- 源码入口：`core/src/com/unciv/logic/GameStarter.kt`、`core/src/com/unciv/logic/map/mapgenerator/MapGenerator.kt`、`core/src/com/unciv/logic/map/mapgenerator/MapGenerationRandomness.kt`，以及分支相关 `MapEditorGenerateTab.kt`、`MapGeneratorSteps.kt`、`MainMenuScreen.kt`、`GameContext.kt`、`GameInfo.kt`、`MapParameters.kt`。
- 已核对：GameStarter 类注释（地形确定、开局设置基于 gameId）与 init 的文明挂载-生成-清空顺序；generateMap 入口（seed==0 换毫秒、seedRNG、按形状构造 TileMap、createdWithVersion、MapType.empty 提前返回）；十条地形阶段链的顺序与 runAndMeasure 的计时/中止两职责；isMapEditor 判定（gameInfo?.civilizations?.isEmpty() ?: true）、新游戏区域分支与编辑器 spreadResources 兜底、mirror 仅编辑器、TileNormalizer 收尾；generateSingleStep 与 MapGeneratorSteps 枚举的对应；三个调用方（GameStarter 传 gameInfo、MapEditorGenerateTab 与 MainMenuScreen 不传，主菜单背景传协程作用域）。
- 已核对 seed 边界：地形阶段共用 randomness.RNG（seedRNG 重置）；噪声的 seed 参数是“切 z 层”而非地图种子，各阶段从 RNG 抽取；环绕噪声把平面卷成圆柱保证左右无缝；GameContext.stateBasedRandom 哈希混入 gameId（无 gameInfo 时退到 mapParameters.seed），GameStarter 的 rng 用于出生点等开局设置。
- 已用 Gradle MCP 重跑 `:tests:test --tests "com.unciv.logic.map.MapGenerationTests" --rerun`：4 过 0 跳过 0 失败。
- 教学取舍：applyHumidityAndTemperature 调用 setTransients 时与第六课重入校验呼应；噪声倍频、陆海分布细节留给 08 课，山地河流留给 09 课，区域与资源留给 10 课。出生点“每局不同”引用 GameStarter 类注释作为证据，未深入 GameStarter 内部选点实现（练习 C 证据链只到 stateBasedRandom 与 gameId 层）。

## 第七课的学习反馈

- 学习者指出第 2 节入口块的排版问题：右侧讲解的“第一件/第二件”挤在一段、“第三件”独立成段，分段无逻辑；且左侧代码没有与“第几件”对应的注释，读者无法把讲解和代码行对上。
- 已按“编号落到代码行”修正：代码注释加 ①～⑤，右侧讲解按 ①～⑤ 各占一段、顺序一一对应；入口改称“五步准备”。同节阶段链讲解的“第六步/第七步”与阶段表序号不一致（大洲=8、河流=9），已改为与表格一致的编号并注明“序号对应阶段表”。
- 后续课程写作规则：凡右侧用“第几步/第几件”的讲解，左侧代码必须在对应行加同编号注释；段与编号一一对应，多步共享一段时需说明分组理由。
- 学习者随后指出第八课存在同样问题（15 个配对块代码与讲解各自为战）。已按同一规则逐块返工：左侧代码注释加 ①～④（或 ② 等按需），右侧讲解按编号各占一段、按代码顺序排列；顺带修正 fade/lerp/grad3 讲解顺序与代码顺序不一致的问题。0008 已复检通过。

## 第七课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 10 个页面（新增 0007-map-generation.html）和 7 课各 4 题（共 28 题）；0007 新增截图锚点 #stages。
- 第七课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择（选项等长）、打印状态恢复。
- MapGenerationTests 已用 Gradle MCP 重跑核验：4 过 0 跳过 0 失败（本次会话，--rerun）。页面检查不覆盖游戏构建。
- 以上是教材制作与验证，不记为第七课已掌握；学习记录需口述证据。

## 第六课的验证记录

### 河流与环绕的教学反馈

- 学习者指出河流的“自己/对方”描述不清，以及环绕解释的术语不对称、篇幅长却缺少清晰推导。尤其认可选择题反馈中“一个量不变，另一个量减少地图宽度”的短解释。
- 河流段改为 A、B 公共边示例和示意图，说明双向查询读取同一个 B.hasBottomLeftRiver；同时更正 else 行为为抛异常。
- 第 4 节改为“需求 → S/D 两个位置量 → 两行代入 → 宽 6 小图 → 查询源码”；经纬度命名、距离和方向方法收进折叠的延伸阅读。旧练习 (15,−15) 不在半径 15 的图内，已替换。
- 本次直接调用源码发现并更正此前的教材错误：getLongitude=x−y，getColumn=y−x；两条正向坐标轴夹 120°；坐标示意行须按实际左右方向排列。第五课、速查和本记录同步修正。
- 已通过 Gradle MCP Kotlin REPL 验证宽 6、高 5 的矩形环绕图：P(−1,1) 的 2 点钟邻居为 R(2,−1)，候选 Q(−1,2) 与 R 的竖直位置相同；河流双向查询均读 B.hasBottomLeftRiver。补跑第二轮核验：R 的 8 点钟邻居为 P、往返钟点为 2/8、aerialDistanceTo(R,P)=1、getUnwrappedPosition(R)=(−1,2)、旧练习 (15,−15) 到原点距离 30。练习 B 全部断言通过。
- 本轮更正后重新运行 check-course.cjs：9 页、24 题全部通过，覆盖本地链接、锚点、源码引用、图片、三种视口、键盘操作及打印展开恢复；已查看第 4 节桌面与移动截图，延伸内容默认折叠。页面检查验证显示和交互，不替代公式或源码行为核对。
- 以上是教材更正与偏好反馈，不记为第六课已掌握。

- 检查脚本 `check-course.cjs` 已扩展为 9 个页面（新增 0006-tilemap.html）和 6 课各 4 题（共 24 题）；0006 新增截图锚点 #storage。
- 第六课页面已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择（选项等长）、打印状态恢复。
- 本课引用的测试已用 Gradle MCP 重跑核验：HexmathTests + MapGenerationTests 共 12 过 0 跳过 0 失败（本次会话，--rerun）；MapGenerationTests 的地形分布断言（Ocean 4079 等）与教材摘录一致。页面检查不覆盖游戏构建。

## 第五课的验证记录

- 检查脚本 `check-course.cjs` 已扩展为 8 个页面（新增 0005-hex-coordinates.html、reference/hexmath.html）和 5 课各 4 题（共 20 题）；参考页 PDF 输出改为按文件名命名。
- 第五课与地图速查已通过 Playwright + Edge 检查：本地链接、锚点、源码引用行号、图片、三种视口、4 题对错反馈与键盘选择、打印状态恢复。
- 初版选择题第 1 题选项字数不一致（5 与 6 字符），已按“选项等长”约定修正后复检通过。
- HexmathTests 已用 Gradle MCP 重跑核验：8 过 0 跳过 0 失败（本次会话，--rerun）。页面检查不覆盖游戏构建。
