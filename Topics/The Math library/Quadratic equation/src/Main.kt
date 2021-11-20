import kotlin.math.*

fun main() {
    val a = readLine()!!.toDouble()
    val b = readLine()!!.toDouble()
    val c = readLine()!!.toDouble()
    val d = b.pow(2.0) - 4 * a * c
    val x1 = (-b + sqrt(d)) / (2 * a)
    val x2 = (-b - sqrt(d)) / (2 * a)

    println("${min(x1, x2)} ${max(x1, x2)}")
}