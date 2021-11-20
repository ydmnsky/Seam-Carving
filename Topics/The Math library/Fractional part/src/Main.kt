import kotlin.math.*

fun main() {
    val a = readLine()!!.toDouble()
    println((floor(a * 10)).roundToInt() % 10)
}