Description
-----------
Program written in Scala that can price a basket of goods taking into account some special offers


Building & Running
--------

### Requirements

* Java 8+
* SBT

### Installation & Launch

* clone repo
* open cmd and cd to root folder of cloned repo (could be for example: ShoppingBasket)
* run sbt command (it should load global plugins, project definition, settings for ShoppingBasket project)
* after you see "sbt:shopping-basket>" command prompt - type: "package" as shown below
* sbt:shopping-basket> package
* Run the program:
* sbt:shopping-basket> run PriceBasket Apples Milk Bread
* you should see result as below
sbt:shopping-basket> run PriceBasket Apples Milk Bread<br/>

[info] running basket.Main PriceBasket Apples Milk Bread<br/>
Subtotal: £3.10<br/>
Apples 10% off: 10p<br/>
Total price: £3.00<br/>

Price List & Promotions modification
--------
One can modify priceList.csv, promotionsList.csv contained in jar file (after package or simply in resource folder) in order to update the price list of available products and promotions without recompiling the code.
