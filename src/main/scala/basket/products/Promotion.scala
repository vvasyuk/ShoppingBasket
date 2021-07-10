package basket.products

import basket.{InputMap, PriceMap}

import java.time.LocalDate
import scala.language.postfixOps

sealed trait Promotion {
  def getDiscount(products: InputMap, prices: PriceMap): Option[Discount]

  def memoizeFnc[K, V](f: K => V): K => V = {
    val cache = collection.mutable.Map.empty[K, V]
    k =>
      cache.getOrElse(k, {
        cache.update(k, f(k))
        cache(k)
      })
  }

  val memoizeGetDiscount = memoizeFnc(getDiscount _ tupled)
}

case class Discount(product: String, percentage: Int, sum: Int)

case class SimplePromotion(promProd: String, endDate: Option[LocalDate], percentage: Int) extends Promotion{
  override def getDiscount(products: InputMap, prices: PriceMap): Option[Discount] = {
    if (products.contains(promProd) && endDate.forall(_.isAfter(LocalDate.now()))){
      val prodCnt = products(promProd)
      val prodPrice = prices(promProd)
      val discount = (prodCnt * prodPrice) / 100.0 * percentage
      Some(Discount(promProd, percentage, discount.toInt))
    }else{
      None
    }
  }
}

case class ConditionalPromotion(promProd: String, promProdCnt: Int, condProd: String, condProdCnt: Int, percentage: Int) extends Promotion{
  override def getDiscount(products: InputMap, prices: PriceMap): Option[Discount] = {
    if (products.contains(promProd) && products.contains(condProd) && products(condProd) >= condProdCnt){
      val numberOfDiscounts = products(condProd) / condProdCnt
      val promProdCnt = products(promProd)
      val promProdPrice = prices(promProd)
      val discount = if (promProdCnt < numberOfDiscounts) {
        (promProdCnt * promProdPrice) / 100.0 * percentage
      }else{
        (numberOfDiscounts * promProdPrice) / 100.0 * percentage
      }
      Some(Discount(promProd, percentage, discount.toInt))
    }else{
      None
    }
  }
}

