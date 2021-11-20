fun main() {
    val productType = readLine()!!
    val price = readLine()!!.toInt()
    val product = when (productType) {
        "headphones" -> Headphones(price)
        "smartphone" -> Smartphone(price)
        "tv" -> TV(price)
        "laptop" -> Laptop(price)
        else -> Product(price)
    }
    println(totalPrice(product))
}

fun totalPrice(x: Product): Int {
    return x.price + (x.tax * x.price).toInt()
}

open class Product(val price: Int) {
    open val tax: Double = 0.0
}

class Headphones(price: Int): Product(price) {
    override val tax = 0.11
}

class Smartphone(price: Int): Product(price) {
    override val tax = 0.15
}

class TV(price: Int): Product(price) {
    override val tax = 0.17
}

class Laptop(price: Int): Product(price) {
    override val tax = 0.19
}
