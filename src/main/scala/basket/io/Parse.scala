package basket.io

import basket.{InputMap, PriceMap}
import basket.products.{ConditionalPromotion, Promotion, SimplePromotion}


object Parse {
  implicit class RegexOps(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  case class ParsedPromotions(parsed: List[Promotion], invalid: List[String])
  def csvToPromotions(args: List[String]): ParsedPromotions = {
    args.foldLeft(ParsedPromotions(List[Promotion](), List[String]()))(
      (acc, el) => {
        el match {
          case r"SimplePromotion,(\w*)${prod},(\d{4}-\d{2}-\d{2})${date},(\d*)${percentage}" => acc.copy(parsed = SimplePromotion(prod, Some(java.time.LocalDate.parse(date)), percentage.toInt) :: acc.parsed)
          case r"ConditionalPromotion,(\w*)${prod},(\d*)${prodCnt},(\w*)${promProd},(\d*)${promProdCnt},(\d*)${percentage}" =>acc.copy(parsed = ConditionalPromotion(prod, prodCnt.toInt, promProd, promProdCnt.toInt, percentage.toInt) :: acc.parsed)
          case s:String => acc.copy(invalid = s"Invalid promotion in file: $s" :: acc.invalid)
        }
      }
    )
  }

  case class ParsedPriceList(parsed: PriceMap, invalid: List[String])
  def csvToMapPrice(args: List[String]): ParsedPriceList = {
    args.foldLeft(ParsedPriceList(Map[String, Int](), List[String]()))(
      (acc, el) => {
        el match {
          case r"(\w*)${prod},(\d*)${price}" => acc.copy(parsed = acc.parsed + (prod -> price.toInt))
          case s:String => acc.copy(invalid = s"Invalid product price in file: $s" :: acc.invalid)
        }
      }
    )
  }

  def inputToMapCnt(args: Array[String]): InputMap = {
    if (args(0) == "PriceBasket"){
      args.tail.foldLeft(Map[String, Int]())( (acc, el) => {
        acc + (el -> (acc.getOrElse(el, 0) + 1))
      })
    }else{
      Map[String, Int]()
    }
  }
}


