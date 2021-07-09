package basket.io

import basket.{InputMap, PriceMap}
import basket.products.{ConditionalPromotion, Promotion, SimplePromotion}


object Parse {
  implicit class RegexOps(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def csvToPromotions(args: List[String]): List[Promotion] = {
    args.foldLeft(List[Option[Promotion]]())(
      (acc, el) => {
        val newProm: Option[Promotion] = el match {
          case r"SimplePromotion,(\w*)${prod},(\d{4}-\d{2}-\d{2})${date},(\d*)${percentage}" => Some(SimplePromotion(prod, Some(java.time.LocalDate.parse(date)), percentage.toInt))
          case r"ConditionalPromotion,(\w*)${prod},(\d*)${prodCnt},(\w*)${promProd},(\d*)${promProdCnt},(\d*)${percentage}" => Some(ConditionalPromotion(prod, prodCnt.toInt, promProd, promProdCnt.toInt, percentage.toInt))
          case s:String => {
            println(s"warning: promotions file contained invalid row which will be ignored: $s")
            None
          }
        }
        newProm :: acc
      }
    ).flatten
  }

  def csvToMapPrice(args: List[String]): PriceMap = {
    args.foldLeft(Map[String, Int]())(
      (acc, el) => {
        val newPrice = el match {
          case r"(\w*)${prod},(\d*)${price}" => Some(prod -> price.toInt)
          case s:String => {
            println(s"warning: price list file contained invalid row which will be ignored: $s")
            None
          }
        }
        (for(el <- newPrice) yield acc + el).getOrElse(acc)
      }
    )
  }

  def inputToMapCnt(args: Array[String]): InputMap = {
    args.foldLeft(Map[String, Int]())( (acc, el) => {acc + (el -> (acc.getOrElse(el, 0) + 1))})
  }
}


