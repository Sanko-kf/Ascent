package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.desktop.PortableApplication
import com.badlogic.gdx.graphics.Color

object Main {

  def main(args: Array[String]): Unit = {
    new HelloWorldScala
  }
}

class HelloWorldScala extends PortableApplication(1920, 1080) {

  override def onInit(): Unit = {
    setTitle("Ascent")
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    // Clears the screen
    g.clear(Color.BLACK)

    g.drawFPS()
  }
}
