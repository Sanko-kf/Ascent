package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Ascent extends PortableApplication(1920, 1080) {

  private var player: Player = null

  override def onInit(): Unit = {
    setTitle("Ascent")
    player = new Player(getWindowWidth / 2f, getWindowHeight / 2f)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear(Color.BLACK)
    player.update()
    player.draw(g)
    g.drawFPS()
  }
}