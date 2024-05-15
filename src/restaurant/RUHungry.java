package restaurant;

public class RUHungry {
   /*
   * Instance variables
   */

  // Menu: two parallel arrays. The index in one corresponds to the same index in the other.
  private   String[] categoryVar; // array where containing the name of menu categories (e.g. Appetizer, Dessert).
  private MenuNode[] menuVar;     // array of lists of MenuNodes where each index is a category.
   // Stock: hashtable using chaining to resolve collisions.
  private StockNode[] stockVar;  // array of linked lists of StockNodes (use hashfunction to organize Nodes: id % stockVarSize)
  private int stockVarSize;

  // Transactions: orders, donations, restock transactions are recorded
  private TransactionNode transactionVar; // refers to the first front node in linked list

  // Queue keeps track of parties that left the restaurant
  private Queue<Party> leftQueueVar;

  // Tables Information - parallel arrays
  // If tableSeats[i] has 3 seats then parties with at most 3 people can sit at tables[i]
  private Party[] tables;      // Parties currently occupying the tables
  private   int[] tableSeats;  // The number of seats at each table

  /*
   * Default constructor
   */
  public RUHungry () {
      categoryVar    = null;
      menuVar        = null;
      stockVar       = null;
      stockVarSize   = 0;
      transactionVar = null;
      leftQueueVar   = null;
      tableSeats     = null;
      tables         = null;
  }

  /*
   * Getter and Setter methods
   */
  public MenuNode[] getMenu() { return menuVar; }
  public String[] getCategoryArray() { return categoryVar;}
  public StockNode[] getStockVar() { return stockVar; }
  public TransactionNode getFrontTransactionNode() { return transactionVar; }
  public TransactionNode resetFrontNode() {return transactionVar = null;} // method to reset the transactions for a new day
  public Queue<Party> getLeftQueueVar() { return leftQueueVar; }
  public Party[] getTables() { return tables; }
  public int[] getTableSeats() { return tableSeats; }

   public void menu(String inputFile) {
      StdIn.setFile(inputFile); // opens the inputFile to be read

      int numCat = StdIn.readInt(); // # of categories
      categoryVar = new String[numCat]; // category array
      menuVar = new MenuNode[numCat]; // menu array

      for (int i = 0; i < categoryVar.length; i++){ // loop through each category

          categoryVar[i] = StdIn.readString(); // name of category
          int numDish = StdIn.readInt(); // # of dishes
          int j = 0;

          while (j < numDish){ // loop through each dish in the category

              StdIn.readLine(); // skip line
              String nameDish = StdIn.readLine(); // name of dish
              int numIng = StdIn.readInt(); // # of ingredients
              int[] ingArr = new int[numIng]; // array to store the ingredients
              int k = 0;

              while (k < numIng){ // loop through each ingredient ID

                  ingArr[k] = StdIn.readInt(); // store the ingredient ID
                  k++;

              }

              Dish obj = new Dish(categoryVar[i], nameDish, ingArr);
              MenuNode menuObj = new MenuNode(obj, null);

              if (menuVar[i] == null){ // insert the menuNode at the front of menuVar
                  menuVar[i] = menuObj;
              } 
              else {
                  menuObj.setNextMenuNode(menuVar[i]);
                  menuVar[i] = menuObj; // update menuVar[i] to point to the new MenuNode
              }
              j++;

          }

      }
  }

  public MenuNode findDish ( String dishName ) {
      MenuNode menuNode = null; // variable to store menuNode
      // Search all categories since we don't know which category dishName is at
      for ( int category = 0; category < menuVar.length; category++ ) {

          MenuNode ptr = menuVar[category]; // set ptr at the front (first menuNode)
          while ( ptr != null ) { // searches the LL of the category to find the itemOrdered

              if ( ptr.getDish().getName().equalsIgnoreCase(dishName) ) {
                  return ptr; // if dish found
              } 
              else{
                  ptr = ptr.getNextMenuNode();
              }

          }

      }
      return menuNode; // null if dish not found
  }

  public int findCategoryIndex (String category) {
      int index = 0;

      for ( int i = 0; i < categoryVar.length; i++ ){ // loop through each category

          if ( category.equalsIgnoreCase(categoryVar[i]) ) { // check if category matches
              index = i; // store the index
              break; // exit if category found
          }

      }
      return index; // return index of category
  }

   public void addStockNode ( StockNode newNode ) {
        int ingID = newNode.getIngredient().getID(); // get ingredient ID
        int index = ingID % stockVarSize; // get index using hash function
        if (stockVar[index] == null){
            stockVar[index] = newNode; // new node as first node
        } 
        else {
            newNode.setNextStockNode(stockVar[index]); // insert new node at front of LL
            stockVar[index] = newNode;
        }
  }

   public StockNode findStockNode (int ingredientID) {
        int index = ingredientID % stockVarSize; // index using the hash function
        StockNode node = null; // variable to store stockNode

        for (StockNode ptr = stockVar[index]; ptr != null; ptr = ptr.getNextStockNode()) { // traverse the LL

            if (ptr.getIngredient().getID() == ingredientID) { // check if ingredient ID matches
                   return ptr;
            }

        }
        return node; // null if ingredient not found
   }

  public StockNode findStockNode (String ingredientName) {
    
      StockNode stockNode = null;
    
      for ( int index = 0; index < stockVar.length; index ++ ){ // traverse through each index in stockVar
        
          StockNode ptr = stockVar[index]; // start at front of LL
        
          while ( ptr != null ){ // check for ingredient name

              if ( ptr.getIngredient().getName().equalsIgnoreCase(ingredientName) ){
                  return ptr;
              } else {
                  ptr = ptr.getNextStockNode();
              }

          }

      }
      return stockNode; // null if ingredient not found
  }

    public void updateStock (String ingredientName, int ingredientID, int stockAmountToAdd) {
       if (ingredientName == null){ // check if searcing by ingredient ID
           int oStock = findStockNode(ingredientID).getIngredient().getStockLevel(); // update stock for ingredient ID found by ID
           findStockNode(ingredientID).getIngredient().setStockLevel(oStock + stockAmountToAdd);
       }
       else if (ingredientID == -1){ // check if searching by ingredient name
           StockNode ptr = findStockNode(ingredientName);
           if ((ptr.getIngredient().getName()).equals(ingredientName)){ // update stock for ingredient ID found by name
               ptr.getIngredient().setStockLevel(ptr.getIngredient().getStockLevel() + stockAmountToAdd);
           }
       }
   }

  public void updatePriceAndProfit() {
      for (int i = 0; i < menuVar.length; i++){ // traverse through each category in menuVar

          MenuNode ptr = menuVar[i]; // start at front of LL

          while (ptr != null){ // traverse LL of dishes for the category

              double costDish = 0.0; // dish cost
              int[] stockID = ptr.getDish().getStockID(); // get IDs of ingredient for the dish
              int j = 0;

              while (j < stockID.length){ // iterate over each ingredient ID

                  costDish += findStockNode(stockID[j]).getIngredient().getCost(); // add cost of ingredient to dish cost 
                  j++;

              }
              ptr.getDish().setPriceOfDish(costDish * 1.2); // update dish price
              ptr.getDish().setProfit(ptr.getDish().getPriceOfDish() - costDish);// update dish profit
              ptr = ptr.getNextMenuNode();

          }

      }
  }

   public void createStockHashTable (String inputFile){
    
      StdIn.setFile(inputFile); // opens inputFile to be read by StdIn
      int stockSize = StdIn.readInt(); // read size of stockVar
      this.stockVarSize = stockSize; // update stockVarSize
      stockVar = new StockNode[stockSize]; // stockVar array
      StdIn.readLine(); // skip line

      while (StdIn.hasNextLine()){ // loop until there are more lines in the file

          int ingredientID = StdIn.readInt();
          StdIn.readChar(); // skip line
          String ingredientName = StdIn.readLine();
          double cost = StdIn.readDouble();
          int count = StdIn.readInt();
          Ingredient obj = new Ingredient(ingredientID, ingredientName, count, cost); // create ingredient object
          StockNode ingredientStock = new StockNode(obj, null); // stockNode referring to Ingredient
          addStockNode(ingredientStock); // insert stockNode into stockVar

        }
  }

  public void addTransactionNode ( TransactionData data ) { 
    TransactionNode tNode = new TransactionNode(data, null);
    TransactionNode ptr = transactionVar; // start at the front of LL
    if (transactionVar == null){
        transactionVar = tNode; // new node as first node
    }
    else {

        while (ptr.getNext() != null){ //  traverse to the end of LL

            ptr = ptr.getNext();

        }
        ptr.setNext(tNode); // insert new node at the end of LL
        return;
    }

  }

  public boolean checkDishAvailability (String dishName, int numberOfDishes){

    MenuNode node = findDish(dishName); // menuNode for the dish
    int[] sarr = node.getDish().getStockID(); // get the array of stock IDs

    for (int i = 0; i < sarr.length; i++){ // loop through the array of stock IDs
        if (findStockNode(sarr[i]).getIngredient().getStockLevel() < numberOfDishes){ // check if stock level for ingredient is sufficient
            return false; // insufficient stock level
        }
    }
    return true; // if all stock levels are sufficient
  }

    // Helper method - update stock levels and transactions
    private void helperUpdate(MenuNode temp, int quant) {
        String category = temp.getDish().getCategory();
        int i = 0, j = 0;

        while (i < menuVar.length) { // loop through menuVar to find dishes in the same category

            if (category.equals(menuVar[i].getDish().getCategory())) { // check if category matches
                MenuNode ptr = menuVar[i];

                while (ptr != temp) { // traverse through dishes in category

                    if (checkDishAvailability(ptr.getDish().getName(), quant)) {
                        TransactionData passed = new TransactionData("order", ptr.getDish().getName(), quant, (findDish(ptr.getDish().getName()).getDish().getProfit() * quant), true); // add successful transactions and update stock levels
                        addTransactionNode(passed);
                        int[] ingredients = ptr.getDish().getStockID();
                        while (j < ingredients.length) {

                            findStockNode(ingredients[j]).getIngredient().updateStockLevel(-quant);
                            j++;

                        }
                        return;
                    } else {
                        TransactionData failed = new TransactionData("order", ptr.getDish().getName(), quant, 0, false); // add unsucessfull transactions 
                        addTransactionNode(failed);
                    }
                    ptr = ptr.getNextMenuNode();

                }
            }
            i++;

        }
        return;
    }

  public void order(String dishName, int quantity) {
        Dish dish = findDish(dishName).getDish();

        if (checkDishAvailability(dishName, quantity)) { // check if dish is available
            TransactionData transaction = new TransactionData("order", dishName, quantity, (dish.getProfit() * quantity), true); // add successful transactions and update stock levels
            addTransactionNode(transaction);
            int[] ingredients = dish.getStockID();
            for (int i = 0; i < ingredients.length; i++) {

                findStockNode(ingredients[i]).getIngredient().updateStockLevel(-quantity);

            }
            return;
        } 
        else {
            TransactionData transNode = new TransactionData("order", dishName, quantity, 0, false); // add unsuccessful transactions
            addTransactionNode(transNode);
            MenuNode node = findDish(dishName).getNextMenuNode();
            while (node != null) {

                if (checkDishAvailability(node.getDish().getName(), quantity)){
                    order(node.getDish().getName(), quantity); // if another dish in the category is available, orcer it
                    return;
                } 
                else {
                    TransactionData failTransaction = new TransactionData("order", node.getDish().getName(), quantity, 0, false);
                    addTransactionNode(failTransaction);
                }
                node = node.getNextMenuNode();

            }
        }
        helperUpdate(findDish(dishName), quantity); // if no dish in the category can be prepared, update transactions and stock levels
    }

  public double profit () {
      double profit = 0.0;
      TransactionNode transNode = transactionVar; // start from the front of LL

      while (transNode != null){ // traverse the LL

          profit += transNode.getData().getProfit(); // add profit from each transaction
          transNode = transNode.getNext();

      }
      return profit; // total profit for the day
  }

   public void donation (String ingredientName, int quantity){
           if (profit() > 50 && findStockNode(ingredientName).getIngredient().getStockLevel() >= quantity) { // check if it meets the requirements
               TransactionData temp = new TransactionData("donation", ingredientName, quantity, 0, true); // successful transaction
               addTransactionNode(temp); // add the transaction
               updateStock(ingredientName, -1, -1*quantity); // update stock
           }
           else {
               TransactionData temp = new TransactionData("donation", ingredientName, quantity, 0, false); // failed transaction
               addTransactionNode(temp); // add transaction
           }
    }

   public void restock (String ingredientName, int quantity){
        double p = profit(); // get current profit
        double cost = findStockNode(ingredientName).getIngredient().getCost() * quantity; // calculate restock cost

        if (p >= cost){ // check if enough profit to restock
            TransactionData transNode = new TransactionData("restock", ingredientName, quantity, -cost, true); // successful transaction
            addTransactionNode(transNode); // add transaction
            updateStock(ingredientName, -1, quantity); // update stock with new quantity
        }
        else { // not enough profit to restock
            TransactionData unTrans = new TransactionData("restock", ingredientName, quantity, 0, false); // failed transaction
            addTransactionNode(unTrans); // add transaction
        }
    }

  public void createTables ( String inputFile ) {
      StdIn.setFile(inputFile);
      int numberOfTables = StdIn.readInt();
      tableSeats = new int[numberOfTables];
      tables     = new Party[numberOfTables];
      for ( int t = 0; t < numberOfTables; t++ ) {

          tableSeats[t] = StdIn.readInt() * StdIn.readInt();

      }
  }

  public void printRestaurant() {
      // 1. Print out menu
      StdOut.println("Menu:");
      if (categoryVar != null) {
          for (int i=0; i < categoryVar.length; i++) {
              StdOut.print(categoryVar[i] + ":");
              StdOut.println();




              MenuNode ptr = menuVar[i];
              while (ptr != null) {
                  StdOut.print(ptr.getDish().getName() + "  Price: $" +
                  ((Math.round(ptr.getDish().getPriceOfDish() * 100.0)) / 100.0) + " Profit: $" + ((Math.round(ptr.getDish().getProfit() * 100.0)) / 100.0));
                  StdOut.println();




                  ptr = ptr.getNextMenuNode();
              }
              StdOut.println();
          }
      }
      else {
          StdOut.println("Empty - categoryVar is null.");
      }
      // 2. Print out stock
      StdOut.println("Stock:");
      if (stockVar != null) {
          for (int i=0; i < 10; i++) {
              StdOut.println("Index " + i);
              StockNode ptr = stockVar[i];
              while (ptr != null) {
                  StdOut.print(ptr.getIngredient().getName() + "  ID: " + ptr.getIngredient().getID() + " Price: " +
                  ((Math.round(ptr.getIngredient().getCost() *100.0)) / 100.0) + " Stock Level: " + ptr.getIngredient().getStockLevel());
                  StdOut.println();
                   ptr = ptr.getNextStockNode();
              }
               StdOut.println();
          }
      }
      else {
          StdOut.println("Empty - stockVar is null.");
      }
      // 3. Print out transactions
      StdOut.println("Transactions:");
      if (transactionVar != null) {
          TransactionNode ptr = transactionVar;
          int successes = 0;
          int failures = 0;
          while (ptr != null) {
              String type = ptr.getData().getType();
              String item = ptr.getData().getItem();
              int amount = ptr.getData().getAmount();
              double profit = ptr.getData().getProfit();
              boolean success = ptr.getData().getSuccess();
              if (success == true){
                  successes += 1;
              }
              else if (success == false){
                  failures += 1;
              }




              StdOut.println("Type: " + type + ", Name: " + item + ", Amount: " + amount + ", Profit: $" + ((Math.round(profit * 100.0)) / 100.0) + ", Was it a Success? " + success);
            
              ptr = ptr.getNext();
          }
          StdOut.println("Total number of successful transactions: " + successes);
          StdOut.println("Total number of unsuccessful transactions: " + failures);
          StdOut.println("Total profit remaining: $" + ((Math.round(profit() * 100.0)) / 100.0));
      }
      else {
          StdOut.println("Empty - transactionVar is null.");
      }
      // 4. Print out tables
      StdOut.println("Tables and Parties:");
      restaurant.Queue<Party> leftQueue = leftQueueVar;
      if (leftQueueVar != null) {
          StdOut.println(("Parties in order of leaving:"));
          int counter = 0;
          while (!leftQueue.isEmpty()) {
              Party removed = leftQueue.dequeue();
              counter += 1;
              StdOut.println(counter + ": " + removed.getName());
          }
      }
      else {
          StdOut.println("Empty -- leftQueueVar is empty");
      }
  }
}