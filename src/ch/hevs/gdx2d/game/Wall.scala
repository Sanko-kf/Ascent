package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Wall(val x: Float, val y: Float, val width: Float, val height: Float) {

  def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(x + width / 2, y + height / 2, width, height, 0, Color.GRAY)
  }

  def hits(px: Float, py: Float, pw: Float, ph: Float): Boolean = {
    px < x + width && px + pw > x && py < y + height && py + ph > y
  }
}
