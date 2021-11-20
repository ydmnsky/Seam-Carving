fun main() {
    val rooms = readLine()!!.toInt()
    val price = readLine()!!.toInt()
    val house = when (rooms) {
        2, 3 -> Bungalow(rooms, price)
        4 -> Cottage(rooms, price)
        5, 6, 7 -> Mansion(rooms, price)
        in 8..Int.MAX_VALUE -> Palace(rooms, price)
        else -> Cabin(rooms, price)
    }
    println(totalPrice(house))
}

fun totalPrice(house: House): Int {
    return (house.coefficient * house.price).toInt()
}

open class House(val rooms: Int, price: Int) {
    val price = if (price <= 0) 0 else if (price >= 1_000_000) 1_000_000 else price
    open val coefficient = 1.0
}

class Cabin(rooms: Int, price: Int) : House(rooms, price) {
    override val coefficient = 1.0
}

class Bungalow(rooms: Int, price: Int) : House(rooms, price) {
    override val coefficient = 1.2
}

class Cottage(rooms: Int, price: Int) : House(rooms, price) {
    override val coefficient = 1.25
}

class Mansion(rooms: Int, price: Int) : House(rooms, price) {
    override val coefficient = 1.4
}

class Palace(rooms: Int, price: Int) : House(rooms, price) {
    override val coefficient = 1.6
}
