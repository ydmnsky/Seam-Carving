import kotlin.math.*

fun main() {
    val (x1, y1) = readLine()!!.split(" ").map { it.toDouble() }
    val (x2, y2) = readLine()!!.split(" ").map { it.toDouble() }
    val cs = (x1 * x2 + y1 * y2) / (sqrt(x1.pow(2) + y1.pow(2)) * sqrt(x2.pow(2) + y2.pow(2)))
    val angle = acos(cs)
    println(angle / PI * 180)
}