package basket

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import basket.io.Parse._
import basket.io.ReadWrite._
import basket.products.Basket._
import basket.products.Promotion


object Main {

  case class PriceBasketReceipt(priceListUsed: PriceMap, promotionsUsed: List[Promotion], receipt: String)

  def main(args: Array[String]): Unit = {
    val receiptProgram = for {
      priceBasket        <- calculateBasket(args)
      receiptProgram     <- IO(new SimpleOutput().toConsole(priceBasket.receipt))
    } yield receiptProgram

    receiptProgram.unsafeRunSync()
  }

  def calculateBasket(args: Array[String]): IO[PriceBasketReceipt] = {
    val basket = inputToMapCnt(args)

    val priceBasketReceiptProgram = for {
      priceList         <- IO(fileToListImpure("priceList.csv"))
      priceMap          = csvToMapPrice(priceList.tail)
      promotionsStrings <- IO(fileToListImpure("promotionsList.csv"))
      promotions        = csvToPromotions(promotionsStrings)

      receipt           = getReceipt(
        basket,
        priceMap.parsed,
        (in, price) => {
          val subTotal = getSubTotal(in,price)
          val warnings = priceMap.invalid ::: promotions.invalid ::: subTotal.prodNotInPrice
          subTotalMessage(subTotal.sum) +
            discountMessage(promotions.parsed.map(_.memoizeGetDiscount(basket, priceMap.parsed))) +
            totalMessage(getTotal(subTotal.sum, promotions.parsed.map(_.memoizeGetDiscount(basket, priceMap.parsed)))) +
          "\n\n" + warnings.mkString("\n")
        }
      )
    } yield PriceBasketReceipt(priceMap.parsed, promotions.parsed, receipt)
    priceBasketReceiptProgram
  }
}