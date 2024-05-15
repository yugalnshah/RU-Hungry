# RU Hungry

In this assignment, I will implement a program to manage the menu, stock, transactions, and guests of a fictional restaurant. I will work with hash tables, separate chaining, referencing objects, and the object-oriented programming (OOP) paradigm.

## Overview 

The assignment uses:

* a 1D array to hold information about the restaurant number of seats per table.
* a 1D array to hold the people currently sitting at a certain table.
* a hashtable to hold the stock.
* two 1D parallel arrays. One holds the menu categories’ name and the other that holds the dishes in a linked list. 
* a linked list to hold the transactions that happened during the day.

### Methods

#### 1. menu(String inputFile)

This method simulates creating a Menu for the RUHungry restaurant. There is only ONE input file to test this method (menu.in). The input file format is as follows:
1. the first number corresponds to the number of categories (aka length of menuVar and categoryVar).
2. the next line states the name of the category (populate categoryVar as you read each category name).
3. the next number represents how many dishes are in that category.
4. the next line states the name of the dish.
5. the first number in the next line represents how many ingredient IDs there are.
6. the next few numbers (all in the 100s) are each the ingredient ID.

Use the StdIn library to read from the input file:
* **StdIn.setFile(filename)** opens a file to be read
* **StdIn.readInt()** reads the next integer value from the opened file (whether the value is in the current line or in the next line)
* **StdIn.readString()** reads the next String value from the opened file.
* **StdIn.readLine()** reads the rest of the line as a String

The job is to populate **categoryVar** and **menuVar** which is a “hashtable” (not exactly since we won’t have a hash function).

* These are parallel arrays. For example, if index 0 (zero) at categoryVar contains “Appetizers” then menuVar, at index 0, contains a linked list of MenuNodes that are appetizers.
* At each array index of menuVar (each index represents a category) contains a reference to the front node of a linked list of **MenuNodes.**
* As I read through the input file, populate the **categoryVar** array, populate **menuVar** depending on which index (aka which category).
* To populate **menuVar** 
     1. make a Dish object with filled parameters (do not worry about “price” and “profit” right now).
     2. create a MenuNode that refers to the Dish object just created.
     3. insert the MenuNode at the front of menuVar (NOTE: since this is a linked list, there will be multiple MenuNodes in one index).

This is the expected output for menu.in: Note: keep in mind that menu won’t have price and profit updated until I run the stock method so $0.0 is NORMAL.

<img width="410" alt="Screenshot 2024-05-14 at 10 32 35 PM" src="https://github.com/yugalnshah/RU-Hungry/assets/162384655/35c583dd-a04f-4da6-bcbf-96a7043c6244">

#### 2. addStockNode(StockNode newNode)

This method adds the parameter StockNode to the **stockVar** hashtable. The hashtable <key,value> pair is the ingredient ID (key) and the Ingredient stored in the MenuNode is they value.

* The task is to retrieve the ingredientID of the StockNode parameter and use the hash function to get the index at which the StockNode will be inserted.
      * Use “ingredientID % **stockVarSize**” as the hash function.
* Then, insert into the **front** of the linked list at that specific index in **stockVar**.
* Keep in mind that at that specific index, there may OR may not already be a linked list, so insert accordingly.

**Note**: no resizing will occur in the hash table.

#### 3. findStockNode(int ingredientID)

This method finds and returns the StockNode (from **stockVar**) containing the ingredient with the parameter ingredientID.

* Find the ingredient based upon the ingredientID. Use the hash function ingredientID % stockVarSize to find the hashtable index where the ingredient is located.
      * This is an efficient search as it looks only at the linked list which the key hashes to.
* Return the StockNode if ingredientID is found, null otherwise.

#### 4. updateStock(String ingredientName, int ingredientID, int stockAmountToAdd)

This method updates the stock amount of an ingredient in **stockVar**.

The method searches for an ingredient using either it’s name or it’s ID.

   * if the ingredientName parameter is null, use the ingredientID to search.
   * if the ingredientID is -1, use the ingredientName to search.

Once the ingredient is found, update the stock amount by adding the parameter stockAmountToAdd to the current stock amount.

**Hint**: I can use findStockNode() to search for the ingredient in stockVar.

**Note**: printRestaurant() prints out all states of the restaurant.

#### 5. updatePriceAndProfit()

This method iterates over the entire **menuVar** to update the price and profit of each dish, using the stockVar hashtable to lookup for the ingredients’ cost.

* compute dish cost: add up the cost variable for each ingredient (found in stockVar).
* compute dish price: multiply the dish cost by 1.2.
* update the price of the dish with it’s price.
* update the profit of the dish with the difference between price and cost.

**Hint**: I can use findStockNode() to search stockVar for an ingredient.

**Note**: Call menu() and createStockHashTable() before calling this method.

This is the expected output for stock.in (the menu part below):

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/19d9a107-783a-4501-ac52-60d5dffbad99)

#### 6. createStockHashTable(String inputFile)

This method creates the **stockVar** hashtable, essentially the pantry of the restaurant with all the ingredients, the prices for all the ingredients, and how much there is of each ingredient.

Read the input file that contains the size of **stockVar** and a several ingredients.

There is only ONE input file to test this method (stock.in). The format is as follows:

1. the first number corresponds to the size of StockVar. Make sure to update stockVarSize with this value.
2. the first integer of the next line represents the ingredientID.
3. Use StdIn.readChar() to get rid of the space between the id and the name.
4. the string that follows is the ingredient name (NOTE -> there are spaces between certain strings).
5. the double on the next line corresponds to the ingredient’s cost.
6. the next integer is the stock amount for that ingredient.

Use the StdIn library to read from the input file:

* **StdIn.setFile(filename)** opens a file to be read
* **StdIn.readInt()** reads the next integer value from the opened file (whether the value is in the current line or in the next line).
* **StdIn.readDouble()** reads the next double value from the opened file (whether the value is in the current line or the next line).
* **StdIn.readChar()** reads the next char value from the opened file (you’ll need to use this so items are read as “Water” and not “ Water”).
* **StdIn.readLine()** reads the rest of the line as a String

While reading the stock.in file, I will do the following for each ingredient in the file:

1. create an **Ingredient** object.
2. create a **StockNode** that refers to the Ingredient object just created.
3. call **addNode()** to insert the StockNode into stockVar.

Once I create the hashtable, then print out the stockroom as well as the updated menu with the updated price and profit.

**Note**: Call menu() before calling this method.

This is the expected output for stock.in (the stockroom part below):

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/d8cc9e93-d59a-41ec-8693-d315c5f3e922)

#### 7. addTransactionNode(TransactionData data)

The daily transactions that occur in RUHungry are customer’s orders, donations to food pantries, or restocking of ingredients.
* The daily transactions are kept in a linked list of **TransactionNodes**.
* The front of the transaction linked list is the instance variable **transactionVar**.

This method inserts a new transaction to the end of **transactionVar.** 

1. create a **TransactionNode** that refers to the parameter **TransactionData.**
2. insert the TransactionNode at the end of the transaction linked list.

#### 8. order(String dishName, int quantity)

This method simulates a customer ordering a dish. Use the checkDishAvailability() method to check whether the dish can be ordered.

If the dish **can** be prepared

1. create a TransactionData object of type “order” where the item is the dishName, the amount is the quantity being ordered, and profit is the dish profit multiplied by quantity.
2. then add the transaction as a successful transaction (call addTransactionNode())
3. Call updateStock() for each dish’s Ingredient to update stock accordingly. 

If the dish **cannot** be prepared

1. create a TransactionData object of type “order” where the item is the dishName, the amount is the quantity being ordered, and profit is 0 (zero).
2. then add the transaction as an UNsuccessful transaction and,
3. simulate the customer trying to order other dishes in the same category linked list:
     * if the dish that comes right after the dishName can be prepared, great. If not, try the next one and so on.
     * might have to traverse through the entire category searching for a dish that can be prepared. If you reach the end of the list, start from the beginning until you have visited 
       EVERY dish in the category.
     * It is possible that no dish in the entire category can be prepared.

**Note**: the next dish the customer chooses is always the one that comes right after the one that could not be prepared.

**Note**: Call menu(), createStockHashTable(), and updatePriceAndProfit() before calling this method.

**Input File for Order**: In your own test code, read from the order input file and call order on each order. The input file is formatted as follows:

* 1 line containing the number of orders, say n
* n lines containing order quantity and dish name, separated by a space
* To read one order do:
                     int amount = StdIn.readInt();
                     StdIn.readChar();
                     String item = StdIn.readLine();
This is the expected output for order1.in (just testing order method):

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/65aedc29-da79-4d2e-8fd2-04b503ab6272)

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/0ae67fd3-28f0-4334-a42c-c71dfce52c39)

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/5ad63226-d3d3-4028-b26d-47f1c23350f4)

#### 9. profit()

This method returns the total profit for the day so far. It will be called in the donation and restock methods to check for sufficient profit.

The profit is computed by traversing the transaction linked list (transactionVar) adding up all the profits for the day. Then, return the total profit.

#### 10. donation(String ingredientName, int quantity)

This method simulates the donation of ingredients to Rutgers Pantry. Donations can only happen if the profit for the day is **greater than** $50 (50 dollars).

Therefore, this method runs checks on whether the total profit for the day is greater than $50 and if there’s enough stock in the stockroom for the donation request to be successful. If there is, the stock is updated accordingly.

The transaction is recorded wether the donation is successful or not:

* create a TransactionData object of type “donation” where the item is the ingredientName, the amount is the quantity being ordered, and profit is 0 (zero).
* then add the transaction as a successful (if donation is happens) or failed (if donation cannot occur) transaction (call addTransactionNode()).
* Call updateStock() to update the stock accordingly.

**Note**: to test the donation method.

1. call the order() method on all orders of a order file;
2. then run the donate on all donations of its correspondent file.
   * order1.in corresponds to donate1.in
   * order2.in corresponds to donate2.in
   * order3.in corresponds to donate3.in

**Input File for Donation**: In my own test code, read from the donation input file and call donation on each donation. The input file is formatted as follows:

* 1 line containing the number of donations, say n
* n lines containing donation quantity and ingredient name, separated by a space
* To read one donation do:
                   int amount = StdIn.readInt();
                   StdIn.readChar();
                   String item = StdIn.readLine();
**Note**: Call menu(), createStockHashTable(), updatePriceAndProfit() and order() before calling this method. 

This is the expected output using order1.in and donation1.in:

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/7e8e24cc-7ecd-443a-8e2f-9cede907e0f9)

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/3af91ddc-0415-4f73-9e65-0518c9502f33)

#### 11. restock(String ingredientName, int quantity)

This method simulates restock orders.

This method runs checks on whether there’s enough total profit in the day to pay for the restock request.

* The cost of restocking is the ingredient’s cost multiplied by the quantity the restaurant needs to buy.

The restock happens as long as there’s enough profit. Call updateStock() to update the stockroom accordingly.

The transaction is recorded wether the restock is successful or not:

1. create a TransactionData object of type “restock” where the item is the ingredientName, the amount is the quantity being ordered, and profit is:
      * 0 (zero) if there isn’t enough profit to restock.
      * cost of restocking (negative) if the restocking is successful. 
2. then add the transaction as a successful (if restocking is happens) or failed (if restocking cannot occur) transaction (call addTransactionNode()) and updates the stock accordingly.

**Note**: to test the restock method.

1. call the order() method on all orders of a order file;
2. then run the restock on all restocks of its correspondent file.
      * order1.in corresponds to restock1.in
      * order2.in corresponds to restock2.in
      * order3.in corresponds to restock3.in

**Note**: Call menu(), createStockHashTable(), updatePriceAndProfit() and order() before calling this method.

**Input File for Restock**: In your own test code, read from the restock input file and call restock on each restock. The input file is formatted as follows:

* 1 line containing the number of orders, say n
* n lines containing order quantity and ingredient name, separated by a space
     * To read one restock do:
                 int amount = StdIn.readInt();
                 StdIn.readChar();
                 String item = StdIn.readLine();
 
This is the expected output using order1.in and restock1.in:

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/bafc707c-af16-4426-848a-b668765fbfb8)

**RUNNING THE ENTIRE TRANSACTION METHOD**

**Input File for Transactions**: In your own test code, read from the transaction input file. The input file is formatted as follows:

* 1 line containing the number of transactions, say n
* n lines containing the transaction type, quantity, and item/ingredient separated by a space.
* To read one line do:
                String type = StdIn.readString();
                StdIn.readChar();
                int amount = StdIn.readInt();
                StdIn.readChar();
                String item = StdIn.readLine();

This is the expected output for transaction1.in:

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/34c3b0eb-4be6-45be-bbfe-a51c36000c2d)

![image](https://github.com/yugalnshah/RU-Hungry/assets/162384655/fd1a613f-5084-41cb-aefc-108091f3ac0b)
