public class CashBinV2 implements ICashDispenser, IInventoryTracker, ICustomerAction, IStatusView {

    private int bill10Count;
    private int bill20Count;
    private int bill50Count;
    private int bill100Count;
    private int totalDeposits;
    private String firmwareVersion = "1.4";

    public CashBinV2(int startCash) {
        // Try to load state from inventory_v2.json
        try {
            java.io.File file = new java.io.File("inventory_v2.json");
            if (file.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                org.json.JSONObject obj = new org.json.JSONObject(content);
                this.bill10Count = obj.has("bill10Count") ? obj.getInt("bill10Count") : 0;
                this.bill20Count = obj.has("bill20Count") ? obj.getInt("bill20Count") : 0;
                this.bill50Count = obj.has("bill50Count") ? obj.getInt("bill50Count") : 0;
                this.bill100Count = obj.has("bill100Count") ? obj.getInt("bill100Count") : 100;
                this.totalDeposits = obj.has("depositsHeld") ? obj.getInt("depositsHeld") : 0;
            } else {
                // Default: 100 x $100 bills = $10,000
                this.bill100Count = startCash / 100;
                this.bill10Count = 0;
                this.bill20Count = 0;
                this.bill50Count = 0;
                this.totalDeposits = 0;
            }
        } catch (Exception e) {
            // If error loading, use defaults
            this.bill100Count = startCash / 100;
            this.bill10Count = 0;
            this.bill20Count = 0;
            this.bill50Count = 0;
            this.totalDeposits = 0;
        }
    }

    @Override
    public int getCashLevel() {
        return (bill10Count * 10) + (bill20Count * 20) + (bill50Count * 50) + (bill100Count * 100);
    }

    public int getTotalDeposits() {
        return totalDeposits;
    }
    
    public int getBill10Count() { return bill10Count; }
    public int getBill20Count() { return bill20Count; }
    public int getBill50Count() { return bill50Count; }
    public int getBill100Count() { return bill100Count; }

    @Override
    public boolean withdraw(int amount) {
        if (getCashLevel() < amount) {
            System.out.println("[ATM]: Error - Insufficient Funds");
            return false;
        }
        
        int[] plan = findDispensePlan(amount);
        if (plan == null) {
            System.out.println("[ATM]: Error - Cannot dispense exact amount with available bill denominations");
            return false;
        }
        
        dispenseBills(plan);
        System.out.println("[ATM]: Dispensing $" + amount);
        return true;
    }

    @Override
    public boolean dispense(int amount) {
        return withdraw(amount);
    }

    @Override
    public void reloadCash(int addBill10Count, int addBill20Count, int addBill50Count, int addBill100Count) {
        bill10Count += addBill10Count;
        bill20Count += addBill20Count;
        bill50Count += addBill50Count;
        bill100Count += addBill100Count;
        int total = (addBill10Count * 10) + (addBill20Count * 20) + (addBill50Count * 50) + (addBill100Count * 100);
        System.out.println("[ATM]: Cash reloaded: $" + total);
    }

    @Override
    public void dispenseDeposits() {
        int depositBills = totalDeposits / 100;
        if (depositBills > 0) {
            bill100Count = Math.max(0, bill100Count - depositBills);
        }
        totalDeposits = 0;
        System.out.println("[ATM]: Deposits dispensed.");
    }
    
    private boolean canDispenseExactAmount(int amount) {
        return findDispensePlan(amount) != null;
    }
    
    private int[] findDispensePlan(int amount) {
        if (amount <= 0 || amount % 10 != 0) {
            return null;
        }
        // Exhaustive bounded search to guarantee a valid combination when one exists.
        int max100 = Math.min(bill100Count, amount / 100);
        for (int use100 = max100; use100 >= 0; use100--) {
            int rem100 = amount - (use100 * 100);
            int max50 = Math.min(bill50Count, rem100 / 50);
            for (int use50 = max50; use50 >= 0; use50--) {
                int rem50 = rem100 - (use50 * 50);
                int max20 = Math.min(bill20Count, rem50 / 20);
                for (int use20 = max20; use20 >= 0; use20--) {
                    int rem20 = rem50 - (use20 * 20);
                    if (rem20 % 10 != 0) {
                        continue;
                    }
                    int use10 = rem20 / 10;
                    if (use10 <= bill10Count) {
                        return new int[] { use10, use20, use50, use100 };
                    }
                }
            }
        }
        return null;
    }

    private void dispenseBills(int[] plan) {
        int use10 = plan[0];
        int use20 = plan[1];
        int use50 = plan[2];
        int use100 = plan[3];
        bill10Count -= use10;
        bill20Count -= use20;
        bill50Count -= use50;
        bill100Count -= use100;
    }

    @Override
    public void deposit(int amount) {
        totalDeposits += amount;
        // Assume deposits are in $100 bills for simplicity
        bill100Count += amount / 100;
        System.out.println("[ATM]: Deposit Accepted: $" + amount);
    }

    @Override
    public boolean hasCash(int amount) {
        return getCashLevel() >= amount;
    }

    @Override
    public String getStatus() {
        return "Cash Available: $" + getCashLevel() + " | Deposits Held: $" + totalDeposits +
               " | Bills: $10x" + bill10Count + " $20x" + bill20Count + 
               " $50x" + bill50Count + " $100x" + bill100Count;
    }

    @Override
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String newVersion) {
        firmwareVersion = newVersion;
    }
}
