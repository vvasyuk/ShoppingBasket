import basket.io.Parse.inputToMapCnt
import org.scalatest.flatspec.AnyFlatSpec

class ParseTest extends AnyFlatSpec {
  "inputToMapCnt" should "work fine" in {
    val res = inputToMapCnt(Array("Apples", "Milk", "Bread", "Bread", "Apples", "Soup", "Soup"))
    assert(res == Map("Apples" -> 2,"Milk" -> 1, "Bread" -> 2, "Soup" -> 2))
  }
}