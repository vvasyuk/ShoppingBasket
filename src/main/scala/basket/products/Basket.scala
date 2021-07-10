package basket.products

import basket.{InputMap, PriceMap}


object Basket{

  def getReceipt(productList: InputMap, priceList: PriceMap, f: (InputMap, PriceMap) => String): String = {
    f(productList, priceList)
  }

  case class SubTotal(sum: Int, prodNotInPrice: List[String])
  def getSubTotal(productList: InputMap, priceList: PriceMap): SubTotal = {
    productList.foldLeft(SubTotal(0, List[String]())){ (acc, el) =>
      val (prod, cnt) = el
      priceList.get(prod) match{
        case Some(p) => acc.copy(sum = acc.sum + (cnt * p))
        case None    => acc.copy(prodNotInPrice = s"Product in basket is not in price list: $prod" :: acc.prodNotInPrice)
      }
    }
  }

  def getTotal(subTotal: Int, discounts: List[Option[Discount]]): Int = {
    if (discounts.forall(_ == None)){
      subTotal
    }else{
      val discount = discounts.foldLeft(0)((acc, el)=>{
        el match{
          case Some(d) => acc + d.sum
          case _ => acc
        }
      })
      subTotal - discount
    }
  }

  def subTotalMessage(subTotal: Int): String = s"Subtotal: ${intToCur(subTotal)}\n"

  def discountMessage(discounts: List[Option[Discount]]): String = {
    if (discounts.forall(_ == None)){
      "(No offers available)\n"
    }else{
      discounts.foldLeft(new StringBuilder(""))((acc, el)=>{
        el match{
          case Some(d) => acc.append(s"${d.product} ${d.percentage}% off: ${intToCur(d.sum)}\n")
          case _ => acc
        }
      }).toString()
    }
  }

  def totalMessage(total: Int): String = s"Total price: ${intToCur(total)}"

  def intToCur(sum: Int): String = {
    if (sum < 100){
      s"${sum}p"
    } else{
      val intStr = sum.toString
      val left = intStr.slice(0,intStr.size-2)
      val right = intStr.slice(intStr.size-2,intStr.size)
      s"£${left}.${right}"
    }
  }
}