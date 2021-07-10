import basket.io.Parse.{csvToMapPrice, csvToPromotions, inputToMapCnt}
import basket.products.{ConditionalPromotion, SimplePromotion}
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate

class ParseTest extends AnyFlatSpec {
  "inputToMapCnt" should "work fine" in {
    val res = inputToMapCnt(Array("PriceBasket","Apples", "Milk", "Bread", "Bread", "Apples", "Soup", "Soup"))
    assert(res == Map("Apples" -> 2,"Milk" -> 1, "Bread" -> 2, "Soup" -> 2))
  }

  "csvToMapPrice" should "work fine" in {
    val res = csvToMapPrice(List("Soup,65", "Bread,80", "Milk,130", "Apples,100", "Apples,abc", "Apples,100,invalid"))
    assert(res.invalid == List("Invalid product price in file: Apples,100,invalid", "Invalid product price in file: Apples,abc"))
    assert(res.parsed == Map("Soup" -> 65, "Bread" -> 80, "Milk" -> 130, "Apples" -> 100))
  }

  "csvToPromotions" should "work fine" in {
    val res = csvToPromotions(List("SimplePromotion,Apples,2021-08-01,10", "ConditionalPromotion,Bread,1,Soup,2,50", "InvalidPromotion,Bread,1,Soup,2,50,invalid"))
    assert(res.invalid == List("Invalid promotion in file: InvalidPromotion,Bread,1,Soup,2,50,invalid"))
    assert(res.parsed == List(ConditionalPromotion("Bread",1,"Soup",2,50), SimplePromotion("Apples",Some(LocalDate.parse("2021-08-01")),10)))
  }
}