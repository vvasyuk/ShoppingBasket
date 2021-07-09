package basket.products

import basket.{InputMap, PriceMap}


object Basket{

  def getReceipt(productList: InputMap, priceList: PriceMap, f: (InputMap, PriceMap) => String): String = {
    f(productList, priceList)
  }

  def getSubTotal(productList: InputMap, priceList: PriceMap): Int = {
    val sum = productList.foldLeft(0){ (acc, el) =>
      val (prod, cnt) = el
      acc + (cnt * priceList(prod))
    }
    sum
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

  def subTotalMessage(subTotal: Int): String = s"Subtotal: $subTotal\n"

  def discountMessage(discounts: List[Option[Discount]]): String = {
    if (discounts.forall(_ == None)){
      "(No offers available)\n"
    }else{
      discounts.foldLeft(new StringBuilder(""))((acc, el)=>{
        el match{
          case Some(d) => acc.append(s"${d.product} ${d.percentage}% off: ${d.sum}\n")
          case _ => acc
        }
      }).toString()
    }
  }

  def totalMessage(total: Int): String = s"Total price: $total"
}