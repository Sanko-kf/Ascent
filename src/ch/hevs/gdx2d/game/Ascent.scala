package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color

import java.io.File

class Ascent extends PortableApplication(1920, 1080) {

  var levelFiles: Array[String] = Array.empty
  var currentLevel = 0

  var player: Player = null
  var level: Level = null

  var runStartMs: Long = 0
  var elapsedMs: Long = 0
  var bestTimeMs: Long = 0

  override def onInit(): Unit = {
    setTitle("Ascent")
    discoverLevels()
    startNewRun()
  }

  def discoverLevels(): Unit = {
    val dir = new File("levels")
    if (!dir.exists || !dir.isDirectory) {
      println("dossier levels introuvable")
      levelFiles = Array.empty
      return
    }

    levelFiles = dir.listFiles()
      .filter(f => f.isFile && f.getName.toLowerCase.endsWith(".txt"))
      .map(_.getName)
      .sortBy(levelSortKey)
      .toArray

    println("niveaux trouves : " + levelFiles.mkString(", "))
  }

  def levelSortKey(fileName: String): (Int, String) = {
    val number = """\d+""".r.findFirstIn(fileName).map(_.toInt).getOrElse(Int.MaxValue)
    (number, fileName)
  }

  def resetTimer(): Unit = {
    runStartMs = System.currentTimeMillis()
    elapsedMs = 0
  }

  def startNewRun(): Unit = {
    resetTimer()
    loadLevel(0)
  }

  def loadLevel(index: Int): Unit = {
    if (levelFiles.isEmpty) return

    currentLevel = index
    level = new Level(levelFiles(index))
    player = new Player(level.spawnX, level.spawnY)
    player.setSpawn(level.spawnX, level.spawnY)
  }

  def nextLevel(): Unit = {
    val finishedRun = currentLevel == levelFiles.length - 1
    var next = currentLevel + 1
    if (next >= levelFiles.length) next = 0
    loadLevel(next)
    if (finishedRun) finishRun()
  }

  def finishRun(): Unit = {
    if (bestTimeMs == 0 || elapsedMs < bestTimeMs) {
      bestTimeMs = elapsedMs
    }
    resetTimer()
  }

  def updateTimer(): Unit = {
    elapsedMs = System.currentTimeMillis() - runStartMs
  }

  def formatTime(ms: Long): String = {
    val minutes = ms / 60000
    val seconds = (ms % 60000) / 1000
    val millis = ms % 1000
    f"$minutes%02d:$seconds%02d.$millis%03d"
  }

  def bestTimeText(): String = {
    if (bestTimeMs == 0) "--:--.---" else formatTime(bestTimeMs)
  }

  def drawTimer(g: GdxGraphics): Unit = {
    g.drawString(5, 15, s"Time: ${formatTime(elapsedMs)}")
    g.drawString(5, 35, s"Best: ${bestTimeText()}")
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear(Color.BLACK)
    updateTimer()

    if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
      startNewRun()
    }

    level.update()
    level.draw(g)
    player.update(level)
    player.draw(g)
    drawTimer(g)

    if (player.reachedExit) {
      nextLevel()
    }
  }
}
