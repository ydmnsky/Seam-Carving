class City(val name: String) {
    private val averageTem = when (name) {
        "Dubai" -> 30
        "Moscow" -> 5
        "Hanoi" -> 20
        else -> 5
    }
    var degrees: Int = averageTem
        set(temp) {
            field = if (temp < -92 || temp > 57) {
                averageTem
            } else {
                temp
            }
        }
}

fun main() {
    val first = readLine()!!.toInt()
    val second = readLine()!!.toInt()
    val third = readLine()!!.toInt()
    val firstCity = City("Dubai")
    firstCity.degrees = first
    val secondCity = City("Moscow")
    secondCity.degrees = second
    val thirdCity = City("Hanoi")
    thirdCity.degrees = third

    //implement comparing here
    if (firstCity.degrees < secondCity.degrees) {
        if (firstCity.degrees < thirdCity.degrees) {
            println(firstCity.name)
        } else if (thirdCity.degrees < firstCity.degrees) {
            println(thirdCity.name)
        } else {
            println("neither")
        }
    } else if (secondCity.degrees < firstCity.degrees) {
        if (secondCity.degrees < thirdCity.degrees) {
            println(secondCity.name)
        } else if (thirdCity.degrees < secondCity.degrees) {
            println(thirdCity.name)
        } else {
            println("neither")
        }
    } else {
        println("neither")
    }
}