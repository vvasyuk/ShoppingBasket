package basket

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import basket.io.Parse._
import basket.io.ReadWrite._
import basket.products.Basket._
import basket.products.Promotion


object Main {
  def main(args: Array[String]): Unit = {
    calculateBasket(args)
  }

  def calculateBasket(args: Array[String]): Unit = {
    println(args.mkString(","))

    // parse input
    val basket = inputToMapCnt(Array("Apples", "Milk", "Bread", "Bread", "Apples", "Soup", "Soup"))
    //    val basket = inputToMapCnt(Array("Apples", "Milk"))

    case class PriceBasketReceipt(priceListUsed: PriceMap, promotionsUsed: List[Promotion], receipt: String)

    val priceBasketReceiptProgram = for {
      priceList        <- IO(fileToListImpure("priceList.csv"))
      priceMapParsed   = csvToMapPrice(priceList.tail)
      promotions       <- IO(fileToListImpure("promotionsList.csv"))
      promotionsParsed = csvToPromotions(promotions)

      receipt          = getReceipt(
        basket,
        priceMapParsed,
        (in, price) => {
          subTotalMessage(getSubTotal(in,price)) +
            discountMessage(promotionsParsed.map(_.memoizeGetDiscount(basket, priceMapParsed))) +
            totalMessage(getTotal(getSubTotal(in,price), promotionsParsed.map(_.memoizeGetDiscount(basket, priceMapParsed))))
        }
      )
      _                <-IO(new SimpleOutput().toConsole(receipt))
    } yield PriceBasketReceipt(priceMapParsed, promotionsParsed, receipt)

    val res = priceBasketReceiptProgram.unsafeRunSync()
    //    println(s"input: $basket")
    //    println(res.priceListUsed)
    //    println(res.receipt)
  }
}

// TODO rewrite csvToMapPrice
// TODO rewrite csvToPromotions
// TODO modify readme file
// TODO make good types
// TODO promotion without end date