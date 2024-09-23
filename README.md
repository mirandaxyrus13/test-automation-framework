# test-automation-framework

This was created using Java programming language. The test automation API/library used for this 
is Selenium WebDriver-Java. The Automation framework tool used is mainly TestNG, but JUnit was also used for assertions. The Project Management Tool used for downloading, updating and importing depedencies/libraries is Maven.


### Hybrid Testing Framework

The Test Automation Framework applied This called Hybrid Test Framework.

It is a combination of any various frameworks set up to leverage the advantages of some and mitigate the weaknesses of others. 

As the concept implicates, it leverages the benefits of all the test frameworks.

Frameworks applied are:
- Module Based Testing Framework
- Library Architecture Testing Framework
- Data Driven Testing Framework

### Design Pattern
For the Hybrid Testing Framework, Page Object Model pattern is used.

Page Object Model also known as POM is the most popular structural design pattern in web/mobile automation.

When working in an Agile environment, the test framework should always be ready for changes in the software and 
ultimately in the automation code base. When any requirement is changed/deleted/added, the automation code
is also necessary for update as well to keep it up and running. 

In the Hybrid Testing Framework:
 - a class represents a page
 - a subclass represents a part/section of a certain page
 - a private static element represents a web element/component of a page
   - technically speaking, it represents an element of a subclass/section
 - an action is created and represents on how that certain element of certain elements should be interacted with
   - e.g. enter text into, click on, select, validation, etc...

Here's the basic structure of the Page Object Model pattern applied in this framework

```Java
//Represents a page
public class HomePage {
    
    //Represents a part/section of this page
    public static class Header{
        //represents a web element/component of a page
        private static Element logoSwagLabs = new Element("Swag Labs Logo", By.xpath("//div[@class='app_logo']"));
        //represents a web element/component of a page
        private static Button buttonCart = new Button("Cart", By.xpath("//a[@class='shopping_cart_link']"));
        
        //represents on how that certain element of certain elements should be interacted with
        public static void verifySwagLabsLogo() {
            logoSwagLabs.verifyDisplayed();

        }
        //represents on how that certain element of certain elements should be interacted with
        public static void clickCart() {
            buttonCart.click();
        }
    }

    //Represents a part/section of this page
    public static class InventoryItems{
        
        //represents a web element/component of a page
        private static ListElement listInventoryProducts = new ListElement("Inventory Products", By.xpath("//div[@class='inventory_item']//div[@class='inventory_item_name']"));

        //represents on how that certain element of certain elements should be interacted with
        public static void clickProduct(String text) {
            listInventoryProducts.clickByText(text);
        }

        //represents on how that certain element of certain elements should be interacted with
        public static void clickAddToCart(String text) {
            Button buttonAddToCart = new Button(text + " Add to Cart", By.xpath("//div[@id]//div[text()='"+text+"']//ancestor::div[@class='inventory_item_description']//button"));
            buttonAddToCart.click();
        }
    }
}
```


### Framework Structure

![](src/main/resources/documentation/diagram.png)

### Execution

Tests can be executed via maven command:

 - mvn clean test -DsuiteXmlFile="xmlfile.xml" -Denvironment="<environment>" -DbrowserInstance="<browserName>"
   - e.g.:
     - *mvn clean test -DsuiteXmlFile="SmokeTesting.xml" -Denvironment="test" -DbrowserInstance="chrome"*