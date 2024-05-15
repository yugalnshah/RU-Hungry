package restaurant;

 //To run: java -cp bin restaurant.Menu menu.in menu.out

public class Menu {
        public static void main(String[] args) {

                // 1. Read input files
                // Option to hardcode these values if you don't want to use the command line arguments
                   
                String inputFile = args[0];
                String inputFile2 = args[1];
                String outputFile = args[2];
                
                // 2. Instantiate an RUHungry object
                RUHungry rh = new RUHungry();
        
                // 3. Call the menu() method to read the menu
                rh.menu(inputFile);
                rh.createStockHashTable(inputFile2);
                rh.updatePriceAndProfit();
        
                // // order method
                StdIn.setFile("order1.in");
                StdIn.readInt();
                StdIn.readLine();
        
                while(StdIn.hasNextLine()){
        
                int quantity = StdIn.readInt();
                StdIn.readChar();
                String item = StdIn.readLine();
        
                rh.order(item, quantity);
                }
                
        
                // // donation method
                // StdIn.setFile("donate1.in");
                // StdIn.readInt();
                // StdIn.readLine();
        
                // while (StdIn.hasNextLine()){
                // int quantity = StdIn.readInt();
                // StdIn.readChar();
                // String item = StdIn.readLine();
        
                // rh.donation(item, quantity);
                // }
        
                // 
        
                //restock method
        
                StdIn.setFile("restock1.in");
                StdIn.readInt();
                StdIn.readLine();
        
                while (StdIn.hasNextLine()){
                int quantity = StdIn.readInt();
                StdIn.readChar();
                String item = StdIn.readLine();
        
                rh.restock(item, quantity);
                }
        
                // transaction method
                StdIn.setFile("transaction1.in");
                StdIn.readInt();
                StdIn.readLine();
        
                while (StdIn.hasNextLine()){
        
                String type = StdIn.readString();
                StdIn.readChar();
                int quantity = StdIn.readInt();
                StdIn.readChar();
                String item = StdIn.readLine();
        
                        if (type.equals("order")){
                                rh.order(item,quantity);
                        } else if (type.equals("restock")){
                                rh.restock(item, quantity);
                        } else if (type.equals("donation")){
                                rh.donation(item, quantity);
                        }
        
                }
        
                // 4. Set output file
                // Option to remove this line if you want to print directly to the screen
                StdOut.setFile(outputFile);
        
                // 5. Print restaurant
                rh.printRestaurant();
            }
}
