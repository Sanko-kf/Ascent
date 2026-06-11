package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Ascent extends PortableApplication(1920, 1080) {

  val levelFiles = Array("level1.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt", "level6.txt")
  var currentLevel = 0

  var player: Player = null
  var level: Level = null

  override def onInit(): Unit = {
    setTitle("Ascent")
    loadLevel(0)
  }

  def loadLevel(index: Int): Unit = {
    currentLevel = index
    level = new Level(levelFiles(index))
    player = new Player(level.spawnX, level.spawnY)
    player.setSpawn(level.spawnX, level.spawnY)
  }

  def nextLevel(): Unit = {
    var next = currentLevel + 1
    if (next >= levelFiles.length) next = 0
    loadLevel(next)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear(Color.BLACK)
    level.update()
    level.draw(g)
    player.update(level)
    player.draw(g)
    g.drawFPS()

    if (player.reachedExit) {
      nextLevel()
    }
  }
}