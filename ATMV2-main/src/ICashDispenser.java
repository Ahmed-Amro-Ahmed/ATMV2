public interface ICashDispenser extends IInventoryTracker {
    boolean hasCash(int amount);
    boolean dispense(int amount);
    void reloadCash(int bill10Count, int bill20Count, int bill50Count, int bill100Count);
    void deposit(int amount);
    void dispenseDeposits();
    int getCashLevel();
    int getTotalDeposits();
    int getBill10Count();
    int getBill20Count();
    int getBill50Count();
    int getBill100Count();
}
