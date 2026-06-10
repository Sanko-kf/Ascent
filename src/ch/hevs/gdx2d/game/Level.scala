package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.lib.GdxGraphics

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class Level(fileName: String) {

  val tileSize = 48

  val walls: ArrayBuffer[Wall] = ArrayBuffer.empty
  val spikes: ArrayBuffer[Spike] = ArrayBuffer.empty
  val exits: ArrayBuffer[Exit] = ArrayBuffer.empty
  val orbs: ArrayBuffer[Orb] = ArrayBuffer.empty
  val springs: ArrayBuffer[Spring] = ArrayBuffer.empty

  var spawnX: Float = 0
  var spawnY: Float = 0

  load(fileName)

  def load(fileName: String): Unit = {
    val src = Source.fromFile(s"levels/$fileName")
    val lines = src.getLines().toArray
    src.close()

    val nbLignes = lines.length

    for (row <- lines.indices) {
      val line = lines(row)
      for (col <- 0 until line.length) {
        val char = line.charAt(col)

        val worldX = col * tileSize
        val worldY = (nbLignes - 1 - row) * tileSize

        char match {
          case '#' => walls.addOne(new Wall(worldX, worldY, tileSize, tileSize))
          case 'X' => spikes.addOne(new Spike(worldX, worldY, tileSize, tileSize))
          case 'O' => orbs.addOne(new Orb(worldX, worldY, tileSize, tileSize))
          case 'E' => exits.addOne(new Exit(worldX, worldY, tileSize, tileSize))
          case 'S' => springs.addOne(new Spring(worldX, worldY, tileSize, tileSize))
          case 'P' =>
            spawnX = worldX
            spawnY = worldY
          case _ =>
        }
      }
    }
    println("niveau charge : " + walls.length + " murs")
  }

  def update(): Unit = {
    for (o <- orbs) {
      o.update()
    }
  }

  def draw(g: GdxGraphics): Unit = {
    for (w <- walls) w.draw(g)
    for (s <- springs) s.draw(g)
    for (s <- spikes) s.draw(g)
    for (e <- exits) e.draw(g)
    for (o <- orbs) o.draw(g)
  }
}
