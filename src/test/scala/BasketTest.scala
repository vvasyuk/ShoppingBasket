
import basket.products.Basket.{SubTotal, discountMessage, getReceipt, getSubTotal, getTotal, intToCur, subTotalMessage, totalMessage}
import basket.products.{ConditionalPromotion, Discount, Promotion, SimplePromotion}
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate

class BasketTest extends AnyFlatSpec{
  "getReceipt" should "work fine" in {
    val basket = Map("Apples" -> 2,"Milk" -> 1,"Bread" -> 2,"Soup" -> 2)
    val priceList = Map("Soup" -> 65, "Bread" -> 80, "Milk" -> 130, "Apples" -> 100)
    val promotions = List(
      SimplePromotion("Apples",Some(LocalDate.parse("2021-08-01")),10),
      ConditionalPromotion("Bread",1,"Soup",2,50)
    )

    val res = getReceipt(
      basket,
      priceList,
      (in, price) => {
        val subTotal = getSubTotal(in,price)
        subTotalMessage(subTotal.sum) +
          discountMessage(promotions.map(_.memoizeGetDiscount(basket, priceList))) +
          totalMessage(getTotal(subTotal.sum, promotions.map(_.memoizeGetDiscount(basket, priceList))))
      })
    assert(res == "Subtotal: £6.20\nApples 10% off: 20p\nBread 50% off: 40p\nTotal price: £5.60")
  }

  "getReceipt" should "work fine without discounts" in {
    val basket = Map("Apples" -> 2,"Milk" -> 1,"Bread" -> 2,"Soup" -> 2)
    val priceList = Map("Soup" -> 65, "Bread" -> 80, "Milk" -> 130, "Apples" -> 100)

    val res = getReceipt(
      basket,
      priceList,
      (in, price) => {
        totalMessage(getTotal(getSubTotal(in,price).sum, List()))}
    )
    assert(res == "Total price: £6.20")
  }

  "getSubTotal" should "work fine" in {
    val basket = Map("Apples" -> 2,"Milk" -> 1,"Bread" -> 2,"Soup" -> 2)
    val priceList = Map("Soup" -> 65, "Bread" -> 80, "Milk" -> 130, "Apples" -> 100)

    val res = getSubTotal(basket, priceList)
    assert(res == SubTotal(620,List()))
  }

  "getTotal" should "work fine with discounts" in {
    val discounts = List(Some(Discount("Apples", 10, 10)), Some(Discount("Milk", 15, 15)))
    val res = getTotal(100, discounts)
    assert(res == 75)
  }

  "getTotal" should "work fine with discount" in {
    val discounts = List(None)
    val res = getTotal(100, discounts)
    assert(res == 100)
  }

  "subTotalMessage" should "work fine" in {
    assert(subTotalMessage(130) == "Subtotal: £1.30\n")
  }

  "discountMessage" should "work fine and return no offers" in {
    val discounts = List(None, None)
    val res = discountMessage(discounts)
    assert(res == "(No offers available)\n")
  }

  "discountMessage" should "work fine and return offers" in {
    val discounts = List(Some(Discount("Apples", 10, 10)), Some(Discount("Milk", 15, 15)), None)
    val res = discountMessage(discounts)
    assert(res == "Apples 10% off: 10p\nMilk 15% off: 15p\n")
  }

  "totalMessage" should "work fine" in {
    assert(totalMessage(130) == "Total price: £1.30")
  }

  "intToCur" should "work fine" in {
    assert(intToCur(310) == "£3.10")
    assert(intToCur(10) == "10p")
    assert(intToCur(300) == "£3.00")
    assert(intToCur(350) == "£3.50")
    assert(intToCur(299) == "£2.99")
  }
}
