// Lesson 15 verification: temporarily copy into tests/src/com/unciv/logic/city/ to run.
package com.unciv.logic.city

import com.unciv.UncivGame
import com.unciv.logic.city.managers.CityTurnManager
import com.unciv.logic.civilization.managers.TurnManager
import com.unciv.logic.map.HexCoord
import com.unciv.models.ruleset.PerpetualConstruction
import com.unciv.models.stats.Stats
import com.unciv.testing.BaseTestRunner
import com.unciv.testing.TestGame
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(BaseTestRunner::class)
class Lesson15ProductionProbeTest {
    private val game = TestGame().apply { makeHexagonalMap(1) }
    private val civ = game.addCiv(isPlayer = true)
    private val city = game.addCity(civ, game.getTile(HexCoord.Zero), replacePalace = true)
    private val constructions = city.cityConstructions

    init {
        UncivGame.Current.settings.autoAssignCityProduction = false
        UncivGame.Current.settings.automatedUnitsMoveOnTurnStart = false
        game.gameInfo.currentPlayerCiv = civ
        game.gameInfo.currentPlayer = civ.civID
        constructions.constructionQueue.clear()
    }

    @Test
    fun accumulationCompletionOverflowAndMovement() {
        val unit = game.createBaseUnit("Sword").apply { cost = 40; movement = 2; strength = 8 }
        val next = game.createBuilding().apply { cost = 60 }
        assertEquals(40, unit.getProductionCost(civ, city))
        constructions.addToQueue(unit.name)
        constructions.addToQueue(next.name)
        constructions.addProductionPoints(35)
        val guard = game.addUnit("Warrior", civ, city.getCenterTile())
        guard.currentMovement = 0f
        city.cityStats.currentCityStats = Stats(production = 12.4f)

        CityTurnManager(city).endTurn()
        assertEquals(47, constructions.getWorkDone(unit.name))
        assertEquals(1, civ.units.getCivUnitsSize())
        assertEquals(0, constructions.productionOverflow)

        TurnManager(civ).startTurn()
        val created = civ.units.getCivUnits().single { it.baseUnit == unit }
        assertNotEquals(city.getCenterTile(), created.currentTile)
        assertEquals(2f, created.currentMovement, 0f)
        assertEquals(0, created.attacksThisTurn)
        assertEquals(guard.getMaxMovement().toFloat(), guard.currentMovement, 0f)
        assertEquals(0, constructions.getWorkDone(unit.name))
        assertEquals(next.name, constructions.currentConstructionName())
        assertEquals(0, constructions.getWorkDone(next.name))
        assertEquals(7, constructions.productionOverflow)

        constructions.endTurn(Stats(production = 9.5f))
        assertEquals(17, constructions.getWorkDone(next.name))
        assertEquals(0, constructions.productionOverflow)
        constructions.constructIfEnough()
        assertFalse(constructions.isBuilt(next.name))
    }

    @Test
    fun overflowIsCappedAndUsesStatsAfterCompletion() {
        val plain = game.createBuilding().apply { cost = 40 }
        constructions.addToQueue(plain.name)
        constructions.addProductionPoints(200)
        city.cityStats.update()
        assertEquals(1f, city.cityStats.currentCityStats.production, 0f)
        constructions.constructIfEnough()
        assertEquals(40, constructions.productionOverflow)

        val productive = game.createBuilding().apply { cost = 40; production = 100f }
        constructions.productionOverflow = 0
        constructions.addToQueue(productive.name)
        constructions.addProductionPoints(200)
        constructions.constructIfEnough()
        assertEquals(101f, city.cityStats.currentCityStats.production, 0f)
        assertEquals(101, constructions.productionOverflow)
    }

    @Test
    fun placementFailurePreservesProgressForRetry() {
        val unit = game.createBaseUnit("Sword").apply { cost = 40; movement = 2; strength = 8 }
        constructions.addToQueue(unit.name)
        constructions.addProductionPoints(47)
        val blockers = game.tileMap.values.map { game.addUnit("Warrior", civ, it) }.toList()
        constructions.constructIfEnough()
        assertEquals(47, constructions.getWorkDone(unit.name))
        assertEquals(unit.name, constructions.currentConstructionName())
        assertEquals(0, constructions.productionOverflow)
        assertEquals(blockers.size, civ.units.getCivUnitsSize())
        blockers.first { it.currentTile != city.getCenterTile() }.destroy()
        constructions.constructIfEnough()
        assertEquals(0, constructions.getWorkDone(unit.name))
        assertEquals(7, constructions.productionOverflow)
        assertEquals(1, civ.units.getCivUnits().count { it.baseUnit == unit })
    }

    @Test
    fun switchingAndIdleKeepSeparateProgress() {
        val first = game.createBuilding().apply { cost = 40 }
        val second = game.createBuilding().apply { cost = 60 }
        constructions.addToQueue(first.name)
        constructions.addToQueue(second.name)
        constructions.addProductionPoints(20)
        constructions.moveEntryToTop(1)
        constructions.endTurn(Stats(production = 12.4f))
        assertEquals(20, constructions.getWorkDone(first.name))
        assertEquals(12, constructions.getWorkDone(second.name))
        constructions.constructionQueue.clear()
        constructions.setCurrentConstruction(PerpetualConstruction.Idle.name)
        constructions.currentConstructionIsUserSet = true
        constructions.productionOverflow = 7
        constructions.endTurn(Stats(production = 12f))
        assertEquals(7, constructions.productionOverflow)
    }

    @Test
    fun overflowIsAddedWithoutApplyingNewConstructionBonusAgain() {
        val target = game.createBuilding().apply { cost = 60 }
        city.cityConstructions.addBuilding(game.createBuilding(
            "[+100]% Production when constructing [${target.name}] buildings [in all cities]"
        ))
        constructions.addToQueue(target.name)
        city.cityStats.update()
        assertEquals(2f, city.cityStats.currentCityStats.production, 0f)
        constructions.productionOverflow = 7
        constructions.endTurn(city.cityStats.currentCityStats)
        assertEquals(9, constructions.getWorkDone(target.name))
        assertEquals(0, constructions.productionOverflow)
    }
}
