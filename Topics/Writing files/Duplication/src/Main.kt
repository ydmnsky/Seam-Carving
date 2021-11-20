// Write your code here. Do not import any libraries
val text = readLine()!!
val fileName = ("MyFile.txt")
val myFile = File(fileName)
myFile.writeText(text.repeat(2))