package cinema

import java.text.DecimalFormat

const val CONST_FRONT_HALF = 10
const val CONST_BACK_HALF = 8
const val ONE_HUNDRED = 100
fun main() {
    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val cols = readLine()!!.toInt()
    val total = rows * cols
    var resultTotal = calculateTotal(rows, cols, total)
    val grid = Array(rows) { CharArray(cols) { 'S' } }
    var selection = -1
    var soldTickets = 0
    var currentIncome = 0
    var currentPercentage = 0.00

    while (selection != 0) {
        showMenu()
        selection = readLine()!!.toInt()
        when (selection) {
            1 -> printSeats(grid)
            2 -> {
                var rowAndColumn = buyTicket()
                var seatRow = rowAndColumn.first()
                var seatColumn = rowAndColumn.last()

                //val available = isAvailable(grid, seatRow, seatColumn)
                // && (seatRow > rows - 1 && seatColumn > cols - 1)
                // !isAvailable(grid, seatRow, seatColumn)
                while (!isAvailable(grid, seatRow, seatColumn)) {
                    if (seatRow > rows  || seatColumn > cols) {
                        println()
                        println("Wrong input!")
                        println()
                    } else {
                        println()
                        println("That ticket has already been purchased!")
                        println()
                    }
                    rowAndColumn = buyTicket()
                    seatRow = rowAndColumn.first()
                    seatColumn = rowAndColumn.last()
                }

                val ticketPrice = calculateTicketPrice(row = seatRow, total, rows)

                println()
                println("Ticket price: \$$ticketPrice")
                println()

                asignSeat(seatRow, seatColumn, grid)
                ++soldTickets
                currentIncome += ticketPrice
            }
            3 ->  {
                val dimension = total
                showStatistics(soldTickets, currentIncome, resultTotal, dimension )
            }
        }
    }
}


fun isAvailable(grid: Array<CharArray>, row: Int, col: Int): Boolean {
    val trueRow = row -1
    val trueColumn = col -1
    return try {
        grid[trueRow][trueColumn] != 'B'
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun showStatistics(soldTickets: Int, currentIncome: Int, resultTotal: Int, dimension: Int) {
    val new = ((ONE_HUNDRED.toDouble() / dimension.toDouble())) * soldTickets.toDouble()
    //val currentPercentage = String.format("%.2f", new).toDouble()
    //println(new)
    val df = DecimalFormat("#.00")
    val currentPercentage = String.format("%.2f", new).toDouble()

    if (currentPercentage == 0.0) {
        println("""
        
        Number of purchased tickets: $soldTickets
        Percentage: 0.00%
        Current income: $$currentIncome
        Total income: $${resultTotal}
        
    """.trimIndent())
        return
    }
    println("""
        
        Number of purchased tickets: $soldTickets
        Percentage: ${df.format(new)}%
        Current income: $$currentIncome
        Total income: $${resultTotal}
        
    """.trimIndent())
}


fun buyTicket(): List<Int> {
    println("Enter a row number:")
    val row = readLine()!!.toInt()
    println("Enter a seat number in that row:")
    val column = readLine()!!.toInt()
    return listOf(row, column)
}
fun showMenu() {
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")
}
fun asignSeat(seatRow: Int, seatCol: Int, array: Array<CharArray>) {
    val row = seatRow - 1
    val col = seatCol - 1
    array[row][col] = 'B'
}
fun calculateTicketPrice(row: Int, total: Int, rows: Int): Int {
    val CONST_ONE_TICKET = 1
    val trueRow = row - 1

    return if (total < 60) {
        CONST_ONE_TICKET * CONST_FRONT_HALF
    } else if (rows % 2 == 0) {
        val half = (rows / 2) - 1
        if (trueRow in 0..half) CONST_ONE_TICKET * CONST_FRONT_HALF else CONST_ONE_TICKET * CONST_BACK_HALF
    } else { // si es impar
        val half = (rows / 2) - 1
        if (trueRow in 0..half) CONST_ONE_TICKET * CONST_FRONT_HALF else CONST_ONE_TICKET * CONST_BACK_HALF
    }
}
fun printSeats(grid: Array<CharArray>) {
    val rows = grid.size
    val cols = grid[0].size

    println()
    println("Cinema:")
    for (i in 0..cols) {
        if (i == 0) print("  ") else print("$i ")
    }
    println()
    for (i in 0 until rows) {
        print("${i + 1} ") // imprime los numeros de las filas
        for (j in 0 until cols) {
            print("${grid[i][j]} ") // imprime la matriz
        }
        println()
    }
    println()
}

fun calculateTotal(rows: Int, cols: Int, total: Int): Int {

    val result = if (total < 60) { // si es menor que 60
        total * CONST_FRONT_HALF
    } else if (rows % 2 == 0) { // si las filas son un numero par
        val half = rows / 2
        half * cols * CONST_FRONT_HALF + half * cols * CONST_BACK_HALF
    } else { // si las filas son un numero impar
        val half = rows / 2
        val backHalf = half + 1
        //println("front half:$half, backHalf:$backHalf")
        half * cols * CONST_FRONT_HALF + backHalf * cols * CONST_BACK_HALF
    }
    return result
}