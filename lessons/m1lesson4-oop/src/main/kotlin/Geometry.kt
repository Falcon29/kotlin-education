class Rectangle(
    val width: Int,
    val height: Int
) : Figure {
    override fun area() = width * height

    override fun toString(): String {
        return "Rectangle(${width}x${height})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rectangle) return false
        return this.width == other.width && this.height == other.height
    }

    override fun hashCode(): Int {
        return 29 * width.hashCode() + height
    }
}

class Square(
    val side: Int
) : Figure {
    override fun area() = side * side

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Square) return false
        return this.side == other.side
    }

    override fun hashCode(): Int {
        return 29 * side.hashCode()
    }
}

interface Figure {
    fun area(): Int
}

fun diffArea(first: Figure, second: Figure) = first.area() - second.area()