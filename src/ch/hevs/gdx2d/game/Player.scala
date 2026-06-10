package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import scala.collection.mutable.ArrayBuffer

class Player(startX: Float, startY: Float) {
  var x: Float = startX
  var y: Float = startY

  var spawnX: Float = startX
  var spawnY: Float = startY

  var vx: Float = 0f
  var vy: Float = 0f

  var onGround: Boolean = false
  var isDashing: Boolean = false
  var canDash: Boolean = true

  var reachedExit: Boolean = false

  var dashFrames: Int = 0
  var dashDirX: Float = 0f
  var dashDirY: Float = 0f

  val width = 48
  val height = 64

  val MAX_SPEED = 12f
  val ACCELERATION = 2.22f
  val GRAVITY = 0.667f
  val MAX_FALL = 26.67f
  val JUMP_VELOCITY = 20f
  val DASH_SPEED = 33.33f
  val DASH_DURATION = 9

  def update(level: Level): Unit = {
    reachedExit = false

    if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && canDash && !isDashing) {
      startDash()
    }

    if (isDashing) {
      updateDash(level.walls, level.springs)
    } else {
      updateHorizontal()
      updateVertical()
      moveAndCollide(level.walls, level.springs)
    }

    checkLevel(level)
  }

  def updateHorizontal(): Unit = {
    val left = Gdx.input.isKeyPressed(Input.Keys.LEFT)
    val right = Gdx.input.isKeyPressed(Input.Keys.RIGHT)

    if (left && !right) {
      vx = vx - ACCELERATION
      if (vx < -MAX_SPEED) vx = -MAX_SPEED
    }
    else if (right && !left) {
      vx = vx + ACCELERATION
      if (vx > MAX_SPEED) vx = MAX_SPEED
    }
    else {
      if (vx > 0) {
        vx = vx - ACCELERATION
        if (vx < 0) vx = 0
      }
      else if (vx < 0) {
        vx = vx + ACCELERATION
        if (vx > 0) vx = 0
      }
    }
  }

  def updateVertical(): Unit = {
    vy = vy - GRAVITY
    if (vy < -MAX_FALL) vy = -MAX_FALL

    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
      vy = JUMP_VELOCITY
      onGround = false
    }
  }

  def startDash(): Unit = {
    println("dash")
    var dx = 0f
    var dy = 0f

    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx = dx - 1
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx = dx + 1
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) dy = dy + 1
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy = dy - 1
    if (dx == 0 && dy == 0) {
      if (vx >= 0) dx = 1 else dx = -1
    }

    val length = Math.sqrt(dx * dx + dy * dy).toFloat
    dashDirX = dx / length
    dashDirY = dy / length

    isDashing = true
    dashFrames = DASH_DURATION
    canDash = false
  }

  def updateDash(walls: ArrayBuffer[Wall], springs: ArrayBuffer[Spring]): Unit = {
    vx = dashDirX * DASH_SPEED
    vy = dashDirY * DASH_SPEED

    moveAndCollide(walls, springs)
    dashFrames = dashFrames - 1

    if (dashFrames <= 0) {
      isDashing = false
      vx = vx / 2
      vy = vy / 2
    }
  }

  def moveAndCollide(walls: ArrayBuffer[Wall], springs: ArrayBuffer[Spring]): Unit = {
    x = x + vx
    for (w <- walls) {
      if (w.hits(x, y, width, height)) {
        if (vx > 0) {
          x = w.x - width
        }
        else if (vx < 0) {
          x = w.x + w.width
        }
        vx = 0
      }
    }
    for (s <- springs) {
      if (s.hits(x, y, width, height)) {
        if (vx > 0) {
          x = s.x - width
        }
        else if (vx < 0) {
          x = s.x + s.width
        }
        vx = 0
      }
    }

    onGround = false
    y = y + vy
    for (w <- walls) {
      if (w.hits(x, y, width, height)) {
        if (vy < 0) {
          y = w.y + w.height
          onGround = true
          canDash = true
        }
        else if (vy > 0) {
          y = w.y - height
        }
        vy = 0
      }
    }
    for (s <- springs) {
      if (s.hits(x, y, width, height)) {
        if (vy < 0) {
          y = s.y + s.height
          vy = s.BOUNCE_VELOCITY
          onGround = false
          canDash = true
          println("ressort")
        }
        else if (vy > 0) {
          y = s.y - height
          vy = 0
        }
      }
    }
  }

  def checkLevel(level: Level): Unit = {
    for (s <- level.spikes) {
      if (s.hits(x, y, width, height)) {
        respawn()
      }
    }
    for (o <- level.orbs) {
      if (o.hits(x, y, width, height)) {
        canDash = true
        o.active = false
        o.respawnAt = System.currentTimeMillis() + o.RESPAWN_MS
        println("orbe")
      }
    }
    for (e <- level.exits) {
      if (e.hits(x, y, width, height)) {
        reachedExit = true
      }
    }
  }

  def respawn(): Unit = {
    println("mort")
    x = spawnX
    y = spawnY
    vx = 0
    vy = 0
    isDashing = false
    canDash = true
  }

  def setSpawn(sx: Float, sy: Float): Unit = {
    spawnX = sx
    spawnY = sy
    x = sx
    y = sy
  }

  def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(x + width / 2, y + height / 2, width, height, 0, Color.CYAN)
  }
}
