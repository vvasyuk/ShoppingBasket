
import cats.effect.unsafe.implicits.global
import basket.Main.calculateBasket
import org.scalatest.flatspec.AnyFlatSpec

class IntegrationTest extends AnyFlatSpec{
  "calculateBasket" should "work fine" in {
    val receiptProgram = calculateBasket(Array("PriceBasket","Apples", "Milk", "Bread", "Bread", "Apples", "Soup", "Soup"))
    val res = receiptProgram.unsafeRunSync()
    assert(res.receipt == "Subtotal: £6.20\nBread 50% off: 40p\nApples 10% off: 20p\nTotal price: £5.60\n\nInvalid product price in file: Invalid,100,abc\nInvalid promotion in file: InvalidPromotion,Bread,1,Soup,2,50,invalid")
  }

  "calculateBasket" should "work fine with product not in price list" in {
    val receiptProgram = calculateBasket(Array("PriceBasket","Apples", "Milk", "Banana"))
    val res = receiptProgram.unsafeRunSync()
    assert(res.receipt == "Subtotal: £2.30\nApples 10% off: 10p\nTotal price: £2.20\n\nInvalid product price in file: Invalid,100,abc\nInvalid promotion in file: InvalidPromotion,Bread,1,Soup,2,50,invalid\nProduct in basket is not in price list: Banana")
  }
}
