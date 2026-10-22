// Lesson 16: temporarily place in tests/src/com/unciv/logic/city/ to run.
// Verified 2026-09-24 against 1df6249a41301056ddc4448a95a93ddf5403e061.
package com.unciv.logic.city

import com.unciv.UncivGame
import com.unciv.logic.civilization.managers.TurnManager
import com.unciv.logic.map.HexCoord
import com.unciv.logic.map.tile.RoadStatus
import com.unciv.models.stats.Stats
import com.unciv.testing.BaseTestRunner
import com.unciv.testing.TestGame
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(BaseTestRunner::class)
class Lesson16EconomyProbeTest {
    private val game = TestGame().apply {
        makeHexagonalMap(5)
        // Set the new map explicitly: the helper's terrain loop visits the old map.
        for (tile in tileMap.values) setTileTerrain(tile.position, "Desert")
    }
    private val civ = game.addCiv(isPlayer = true)
    private val city = game.addCity(civ, game.getTile(HexCoord.Zero), replacePalace = true)

    init {
        UncivGame.Current.settings.autoAssignCityProduction = false
        UncivGame.Current.settings.automatedUnitsMoveOnTurnStart = false
        UncivGame.Current.settings.citiesAutoBombardAtEndOfTurn = false
        game.gameInfo.currentPlayerCiv = civ
        game.gameInfo.currentPlayer = civ.civID
        game.gameInfo.turns = 0
        city.cityConstructions.constructionQueue.clear()
    }

    private fun unitUpkeep() = -civ.stats.getStatMapForNextTurn()["Unit upkeep"]!!.gold

    @Test
    fun cityAggregationCompletionRefreshAndTreasury() {
        val second = game.addCity(civ, game.getTile(4, 0))
        second.cityConstructions.constructionQueue.clear()
        city.cityConstructions.addBuilding(game.createBuilding().apply {
            gold = 8.5f; production = 10.4f; maintenance = 2
        })
        second.cityConstructions.addBuilding(game.createBuilding().apply {
            gold = 5.25f; maintenance = 1
        })
        val roadTile = game.getTile(1, 0)
        assertEquals(civ, roadTile.getOwner())
        // Already owned land: do not also register it as a neutral road.
        roadTile.setRoadStatus(RoadStatus.Road, null)
        repeat(4) { game.tileMap.placeUnitNearTile(city.location, game.ruleset.units["Warrior"]!!, civ) }
        val unit = game.createBaseUnit("Sword").apply { cost = 40; movement = 2; strength = 8 }
        city.cityConstructions.addToQueue(unit.name)
        city.cityConstructions.addProductionPoints(35)
        for (c in civ.cities) c.cityStats.update()
        civ.addGold(100 - civ.gold)
        repeat(3) { civ.updateStatsForNextTurn() }
        val breakdown = civ.stats.getStatMapForNextTurn()
        assertEquals(13.75f, breakdown["Buildings"]!!.gold, 0.001f)
        assertEquals(-3f, breakdown["Maintenance"]!!.gold, 0f)
        assertEquals(-1f, breakdown["Transportation upkeep"]!!.gold, 0f)
        assertEquals(0f, unitUpkeep(), 0f)
        assertEquals(9.75f, civ.stats.statsForNextTurn.gold, 0.001f)
        assertEquals(100, civ.gold)

        TurnManager(civ).endTurn()
        assertEquals(109, civ.gold)
        assertEquals(47, city.cityConstructions.getWorkDone(unit.name))
        assertEquals(4, civ.units.getCivUnitsSize())

        // Direct manager calls keep game.turns at 0 to isolate unit count from game progress.
        TurnManager(civ).startTurn()
        assertEquals(5, civ.units.getCivUnitsSize())
        assertEquals(7, city.cityConstructions.productionOverflow)
        assertEquals(1f, unitUpkeep(), 0f)
        assertEquals(8.75f, civ.stats.statsForNextTurn.gold, 0.001f)
        assertEquals(109, civ.gold)
        TurnManager(civ).endTurn()
        assertEquals(117, civ.gold)
    }

    @Test
    fun unitMaintenanceFreeAllowanceTruncationAndProgress() {
        for (count in 1..7) {
            assertNotNull(game.tileMap.placeUnitNearTile(city.location, game.ruleset.units["Warrior"]!!, civ))
            val expected = listOf(0f, 0f, 0f, 0f, 1f, 1f, 2f)[count - 1]
            assertEquals("units=$count", expected, unitUpkeep(), 0f)
        }
        game.gameInfo.turns = game.gameInfo.speed.numTotalTurns()
        civ.updateStatsForNextTurn()
        assertEquals(6f, unitUpkeep(), 0f) // (0.5 * 4 * 2)^(1 + 1/3), truncated
        val before = civ.gold
        civ.units.getCivUnits().last().destroy()
        assertEquals(4f, unitUpkeep(), 0f)
        assertEquals(before, civ.gold)
    }

    @Test
    fun happinessBoundaryRefreshesBothCitiesWithoutPayingGold() {
        val second = game.addCity(civ, game.getTile(4, 0))
        val modifier = game.createBuilding().apply { happiness = -1f }
        city.cityConstructions.addBuilding(modifier)
        for (c in civ.cities) {
            // Desert center gives 1 production; the unassigned citizen gives another 1.
            c.cityConstructions.addBuilding(game.createBuilding().apply { food = 8f; production = 8f })
            c.cityConstructions.constructionQueue.clear()
            c.cityStats.update()
        }
        civ.addGold(100 - civ.gold)
        // Prince: 9 base - 6 for two cities - 2 population = 1 before this building.
        for ((happiness, food, production) in listOf(
            Triple(0, 8f, 10f), Triple(-1, 2f, 10f),
            Triple(-10, 2f, 10f), Triple(-11, 0f, 5f), Triple(0, 8f, 10f)
        )) {
            modifier.happiness = happiness - 1f
            city.cityStats.update() // Only update the first city explicitly.
            assertEquals(happiness, civ.getHappiness())
            for (c in listOf(city, second)) {
                assertEquals("happiness=$happiness", food, c.cityStats.currentCityStats.food, 0.001f)
                assertEquals("happiness=$happiness", production, c.cityStats.currentCityStats.production, 0.001f)
            }
            assertEquals(100, civ.gold)
        }
    }

    @Test
    fun negativeFlowTruncatesTowardZeroAndDeficitNeedsNegativeTreasury() {
        val building = game.createBuilding().apply { gold = 0.25f; maintenance = 3; science = 9f }
        city.cityConstructions.addBuilding(building)
        city.cityStats.update()
        civ.addGold(1 - civ.gold)
        civ.updateStatsForNextTurn()
        assertEquals(-2.75f, civ.stats.statsForNextTurn.gold, 0f)
        assertEquals(10f, civ.stats.statsForNextTurn.science, 0f)
        assertFalse(civ.stats.getStatMapForNextTurn().containsKey("Treasury deficit"))
        TurnManager(civ).endTurn()
        assertEquals(-1, civ.gold) // 1 + (-2.75).toInt(), no clamp to zero
        civ.updateStatsForNextTurn()
        assertEquals(-2.75f, civ.stats.getStatMapForNextTurn()["Treasury deficit"]!!.science, 0f)
        assertEquals(7.25f, civ.stats.statsForNextTurn.science, 0f)
        building.maintenance = 30
        city.cityStats.update()
        assertEquals(1f, civ.stats.statsForNextTurn.science, 0f)
    }

    @Test
    fun civilizationRefreshNormallyConsumesExistingCityBreakdown() {
        val building = game.createBuilding().apply { gold = 8.5f }
        city.cityConstructions.addBuilding(building)
        city.cityStats.update()
        civ.addGold(100 - civ.gold)
        city.cityStats.currentCityStats = Stats(gold = 999f)
        civ.updateStatsForNextTurn()
        assertEquals(8.5f, civ.stats.statsForNextTurn.gold, 0f)
        // This deliberate cache manipulation proves which object the aggregate reads.
        building.gold = 10.5f
        civ.updateStatsForNextTurn()
        assertEquals(8.5f, civ.stats.statsForNextTurn.gold, 0f)
        city.cityStats.update()
        assertEquals(10.5f, civ.stats.statsForNextTurn.gold, 0f)
        assertEquals(100, civ.gold)
    }
}
