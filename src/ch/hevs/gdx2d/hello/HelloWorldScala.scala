package ch.hevs.gdx2d.hello

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.desktop.PortableApplication
import com.badlogic.gdx.graphics.Color


/**
 * Hello World demo in Scala
 *
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
object HelloWorldScala {

  def main(args: Array[String]): Unit = {
    new HelloWorldScala
  }
}

class HelloWorldScala extends PortableApplication(1920, 1080) {

  override def onInit(): Unit = {
    setTitle("Ascent")
  }

  /**
   * This method is called periodically by the engine
   *
   * @param g
   */
  override def onGraphicRender(g: GdxGraphics): Unit = {
    // Clears the screen
    g.clear(Color.BLACK)

    g.drawFPS()
  }
}
