import basket.io.ReadWrite._
import org.scalatest.flatspec.AnyFlatSpec


class ReadWriteTest extends AnyFlatSpec {
  trait MockOutput extends Output {
    var messages: Seq[String] = Seq()

    override def toConsole(s: String) = messages = messages :+ s
  }

  "SimpleOutput.toConsole" should "work fine" in {
    val logger = new SimpleOutput() with MockOutput
    logger.toConsole("test message")
    assert(logger.messages == Seq("test message"))
  }

  "fileToListImpure" should "work fine" in {
    val res = fileToListImpure("priceList.csv")
    assert(res == List("product,price","Soup,65","Bread,80"))
  }
}
