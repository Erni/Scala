package greeter

object HelloWorkSheet {
  val m = "abcd".toLowerCase.groupBy(c => c).mapValues(v => v.length).toList.sorted
  val l = List("hhlm","hnuigni", "shduisg")
 
  l.fold("")(s => s + s)
}