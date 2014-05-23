package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else if (c == 1 || c == (r - 1)) r
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def balanceHelper(chars: List[Char], op: Int): Boolean = {
      if(op < 0 || (chars.isEmpty && op > 0)) false
      else if (chars.isEmpty && op == 0) true
      else if (chars.head == '(') balanceHelper(chars.tail, op + 1)
      else if (chars.head == ')') balanceHelper(chars.tail, op - 1)
      else balanceHelper(chars.tail, op)
    }
    balanceHelper(chars, 0)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = ???
}
