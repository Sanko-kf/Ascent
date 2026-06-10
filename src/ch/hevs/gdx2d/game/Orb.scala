package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Orb(val x: Float, val y: Float, val width: Float, val height: Float) {

  var active: Boolean = true
  var respawnAt: Long = 0

  val RESPAWN_MS = 5000

  def update(): Unit = {
    if (!active && System.currentTimeMillis() >= respawnAt) {
      active = true
    }
  }

  def hits(px: Float, py: Float, pw: Float, ph: Float): Boolean = {
    active && px < x + width && px + pw > x && py < y + height && py + ph > y
  }

  def draw(g: GdxGraphics): Unit = {
    if (active) {
      g.drawFilledCircle(x + width / 2, y + height / 2, 14, Color.GREEN)
    }
  }
}
