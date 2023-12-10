package util

data class Vec2(val x: Int, val y: Int)

operator fun Vec2.plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
operator fun Vec2.times(scalar: Int) = Vec2(x * scalar, y * scalar)
