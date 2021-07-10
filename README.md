Description
-----------
Program written in Scala that can price a basket of goods taking into account some special offers

Building
--------

### Requirements

* Java 8+
* SBT

### Installation

* clone repo
* cd to root folder
* sbt package

### Run
* cd to ShoppingBasket\target\scala-2.13
* scala .\shopping-basket_2.13-0.1.0.jar PriceBasket Apples Milk Bread

Usage
--------
One can modify priceList.csv, promotionsList.csv contained in jar file in order to update the price list of available products and promotions without recompiling the code.