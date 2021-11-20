class City(val name: String) {
    var population: Int = 0
        set(number: Int) {
            field = if (number < 0) {
                0
            } else if (number > 50_000_000) {
                50_000_000
            } else {
                number
            }
        }
}