import basket.products.{ConditionalPromotion, Discount, SimplePromotion}
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate

class PromotionTest extends AnyFlatSpec{

  "Promotions" should "work fine" in {
    val basket = Map("Apples" -> 2,"Milk" -> 1,"Bread" -> 2,"Soup" -> 2)
    val priceList = Map("Soup" -> 65, "Bread" -> 80, "Milk" -> 130, "Apples" -> 100)
    val promotions = List(
      SimplePromotion("Apples",Some(LocalDate.parse("2021-08-01")),10),
      ConditionalPromotion("Bread",1,"Soup",2,50)
    )
    val res = promotions.map(_.memoizeGetDiscount(basket,priceList))
    assert(res == List(Some(Discount("Apples",10,20)), Some(Discount("Bread",50,40))))

  }
}
