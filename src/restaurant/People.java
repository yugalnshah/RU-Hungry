package restaurant;

public class People{
    private int    numberInParty; // the number of people in a party
    private String nameOfParty;   // the last name of the party
    private int    tableIndex;    // the index of the table the party is sitting at

    // constructor
    public People (int peopleInParty, String nameOfParty, int tableIndex){
        this.numberInParty = peopleInParty;
        this.nameOfParty = nameOfParty;
        this.tableIndex = tableIndex;
    }

    // Getter/Setter methods
    public int getNumberInParty(){ return numberInParty;}

    public String getNameOfParty(){ return nameOfParty;}

    public void setNumberInParty(int numberInPartyInput){ numberInParty = numberInPartyInput;}

    public void setNameOfParty (String nameOfPartyInput){ nameOfParty = nameOfPartyInput;}

    public int getTableIndex(){ return tableIndex;}

    public void setTableIndex(int tableIndexInput){ tableIndex = tableIndexInput;}

}