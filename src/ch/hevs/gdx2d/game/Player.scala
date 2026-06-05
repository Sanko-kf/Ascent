package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Player(startX: Float, startY: Float) {
  var x: Float = startX
  var y: Float = startY

  var vx: Float = 0f
  var vy: Float = 0f

  var onGround: Boolean = false
  var isDashing: Boolean = false
  var canDash: Boolean = true

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

  val FLOOR_Y = 100f

  def update(): Unit = {
    if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && canDash && !isDashing) {
      startDash()
    }

    if (isDashing) {
      updateDash()
    } else {
      updateHorizontal()
      updateVertical()
      x = x + vx
      y = y + vy
      handleFloor()
    }
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

  def updateDash(): Unit = {
    vx = dashDirX * DASH_SPEED
    vy = dashDirY * DASH_SPEED

    x = x + vx
    y = y + vy
    handleFloor()
    dashFrames = dashFrames - 1

    if (dashFrames <= 0) {
      isDashing = false
      vx = vx / 2
      vy = vy / 2
    }
  }

  // todo : enlever quand les vrais niveaux seront la (juste pour tester)
  def handleFloor(): Unit = {
    if (y <= FLOOR_Y) {
      y = FLOOR_Y
      if (vy < 0) vy = 0
      onGround = true
      canDash = true
    }
  }

  def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(x, y, width, height, 0, Color.CYAN)
  }
}