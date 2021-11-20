package seamcarving
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt

open class ImageWithEnergy (img:BufferedImage) {
    var image = img
    private fun xGradSquare(i: Int, j: Int): Int {
        val redGrad = Color(image.getRGB(i-1, j)).red - Color(image.getRGB(i+1, j)).red
        val greenGrad = Color(image.getRGB(i-1, j)).green - Color(image.getRGB(i+1, j)).green
        val blueGrad = Color(image.getRGB(i-1, j)).blue - Color(image.getRGB(i+1, j)).blue
        return redGrad * redGrad + greenGrad * greenGrad + blueGrad * blueGrad
    }
    private fun yGradSquare(i: Int, j: Int): Int {
        val redGrad = Color(image.getRGB(i, j-1)).red - Color(image.getRGB(i, j+1)).red
        val greenGrad = Color(image.getRGB(i, j-1)).green - Color(image.getRGB(i, j+1)).green
        val blueGrad = Color(image.getRGB(i, j-1)).blue - Color(image.getRGB(i, j+1)).blue
        return redGrad * redGrad + greenGrad * greenGrad + blueGrad * blueGrad
    }
    private fun energyOfPixel(i: Int, j: Int): Double {
        val a: Int = when (i) {
            0 -> xGradSquare(1, j)
            image.width - 1 -> xGradSquare(image.width - 2, j)
            else -> xGradSquare(i, j)
        }
        val b: Int = when (j) {
            0 -> yGradSquare(i, 1)
            image.height - 1 -> yGradSquare(i, image.height - 2)
            else -> yGradSquare(i, j)
        }
        return sqrt(a.toDouble() + b.toDouble())
    }
    private fun imageToEnergyMatrix(image: BufferedImage): MutableList<MutableList<Double>> {
        val energyMatrix = mutableListOf<MutableList<Double>>()
        for (i in 0 until image.width) {
            energyMatrix.add(i, mutableListOf())
            for (j in 0 until image.height) {
                energyMatrix[i].add(0.0)
            }
        }
        for (i in 0 until image.width) {
            for (j in 0 until image.height) {
                energyMatrix[i][j] = energyOfPixel(i, j)
            }
        }
        return energyMatrix
    }
    private var energyMatrix: MutableList<MutableList<Double>> = imageToEnergyMatrix(image)
    private fun searchMaxEnergy(energyMatrix: MutableList<MutableList<Double>>): Double {
        var max = 0.0
        for (i in 0 until energyMatrix.size - 1) {
            for (j in 0 until energyMatrix[0].size - 1)
                if (energyMatrix[i][j] > max) {
                    max = energyMatrix[i][j]
                }
        }
        return max
    }
    private var maxEnergy = searchMaxEnergy(energyMatrix)
    private fun createEnergyImage(energyMatrix: MutableList<MutableList<Double>>): BufferedImage {
        val eng = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        for (i in 0 until eng.width) {
            for(j in 0 until eng.height) {
                val intensity = (255.0 * energyMatrix[i][j] / maxEnergy).toInt()
                eng.setRGB(i, j, Color(intensity, intensity, intensity).rgb)
            }
        }
        return eng
    }
    val energyImage: BufferedImage = createEnergyImage(energyMatrix)
    private var matrix = energyMatrix
    private fun findSumMatrix(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Double>> {
        val sumMatrix = mutableListOf<MutableList<Double>>()
        for (i in matrix.indices) {
            sumMatrix.add(mutableListOf())
            for (j in matrix[0].indices) sumMatrix[i].add(0.0)
        }
        for (i in matrix.indices) sumMatrix[i][0] = matrix[i][0]
        for (y in 1..sumMatrix[0].lastIndex) {
            for (x in sumMatrix.indices) {
                sumMatrix[x][y] = sumMatrix[x][y-1]
                if (x > 0 && sumMatrix[x-1][y-1] < sumMatrix[x][y]) sumMatrix[x][y] = sumMatrix[x-1][y-1]
                if (x < sumMatrix.lastIndex && sumMatrix[x+1][y-1] < sumMatrix[x][y]) sumMatrix[x][y] = sumMatrix[x+1][y-1]
                sumMatrix[x][y] += matrix[x][y]
            }
        }
        return sumMatrix
    }
    private var sumMatrix = findSumMatrix(matrix)
    private fun findVerticalSeam(sumMatrix: MutableList<MutableList<Double>>): MutableList<Int> {
        val seam = mutableListOf<Int>()
        for (y in sumMatrix[0].indices) seam.add(0)
        for (x in sumMatrix.indices) {
            if (sumMatrix[x].last() < sumMatrix[seam.last()].last()) seam[seam.lastIndex] = x
        }
        for (y in sumMatrix[0].lastIndex-1 downTo 0) {
            val downwardPixel = seam[y+1]
            seam[y] = downwardPixel
            if (downwardPixel > 0 && sumMatrix[seam[y]][y] > sumMatrix[downwardPixel-1][y]) seam[y] = downwardPixel - 1
            if (downwardPixel < sumMatrix.lastIndex && sumMatrix[seam[y]][y] > sumMatrix[downwardPixel+1][y]) seam[y] = downwardPixel + 1
        }
        return seam
    }
    private var img = image
    fun redVerticalSeam(): BufferedImage {
        val image = img
        val seam = findVerticalSeam(sumMatrix)
        for (y in seam.indices) {
            image.setRGB(seam[y], y, Color.RED.rgb)
        }
        return image
    }
    fun redHorizontalSeam(): BufferedImage {
        val image = img
        val image1 = BufferedImage(image.height, image.width, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until image1.width) {
            for (y in 0 until image1.height) {
                image1.setRGB(x, y, image.getRGB(y, x))
            }
        }
        val redImage = ImageWithEnergy(image1).redVerticalSeam()
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                image.setRGB(x, y, redImage.getRGB(y, x))
            }
        }
        return image
    }
    fun deleteVerticalSeam(n: Int) {
        repeat(n) {
            val seam = findVerticalSeam(sumMatrix)
            for (y in seam.indices) {
                for (x in seam[y] until image.width-1)
                    image.setRGB(x, y, image.getRGB(x+1, y))
            }
            val image1 = BufferedImage(image.width-1, image.height, BufferedImage.TYPE_INT_RGB)
            for (x in 0 until image1.width) {
                for (y in 0 until image1.height) {
                    image1.setRGB(x, y, image.getRGB(x, y))
                }
            }
            image = image1
            energyMatrix = imageToEnergyMatrix(image)
            maxEnergy = searchMaxEnergy(energyMatrix)
            matrix = energyMatrix
            sumMatrix = findSumMatrix(matrix)
        }
    }
    fun deleteHorizontalSeam(n: Int) {
        val transposedImage = BufferedImage(image.height, image.width, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until transposedImage.width) {
            for (y in 0 until transposedImage.height)
                transposedImage.setRGB(x, y, image.getRGB(y, x))
        }
        val image1 = ImageWithEnergy(transposedImage)
        image1.deleteVerticalSeam(n)
        val image2 = BufferedImage(image1.image.height, image1.image.width, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until image2.width) {
            for (y in 0 until image2.height)
                image2.setRGB(x, y, image1.image.getRGB(y, x))
        }
        image = image2
    }
}



fun main(args: Array<String>) {

    val inName = args[1]
    val outName = args[3]
    val inFile = File(inName)
    val inImg = ImageIO.read(inFile)
    val widthCarving = args[5].toInt()
    val heightCarving = args[7].toInt()
    val engImg = ImageWithEnergy(inImg)
    engImg.deleteVerticalSeam(widthCarving)
    engImg.deleteHorizontalSeam(heightCarving)
    ImageIO.write(engImg.image, "png", File(outName))
}
