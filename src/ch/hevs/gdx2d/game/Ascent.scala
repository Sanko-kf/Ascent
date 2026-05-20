package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Ascent extends PortableApplication(1920, 1080) {

  override def onInit(): Unit = {
    setTitle("Ascent")
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear(Color.BLACK)

    g.drawFPS()
  }
}