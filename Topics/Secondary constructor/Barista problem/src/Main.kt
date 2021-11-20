import kotlin.math.floor

// write the EspressoMachine class here
class EspressoMachine {
    val costPerServing: Float
    constructor(coffeeCapsulesCount: Int, totalCost: Float) {
        this.costPerServing = totalCost / coffeeCapsulesCount
    }
    constructor(coffeeBeansWeight: Float, totalCost: Float) {
        this.costPerServing = totalCost / floor(coffeeBeansWeight / 10)
    }
}