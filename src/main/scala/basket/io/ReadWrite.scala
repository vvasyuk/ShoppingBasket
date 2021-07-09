package basket.io

import scala.io.Source

object ReadWrite {
  def fileToListImpure(name: String): List[String] = Source.fromResource(name).getLines().toList

  trait Output {
    def toConsole(s: String): Unit = println(s)
  }

  class SimpleOutput extends Output{
    def writeToConsole(s: String): Unit = toConsole(s)
  }
}

