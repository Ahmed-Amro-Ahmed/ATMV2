public class ReloadCashTransaction implements ITransaction {
    private final ICashDispenser dispenser;
    private final int bill10Count;
    private final int bill20Count;
    private final int bill50Count;
    private final int bill100Count;
    private final IPrinterSupplies printerSupplies;
    private final IMaintainable printerFirmware;
    private final IPersistence persistence;

    public ReloadCashTransaction(ICashDispenser dispenser, int bill10Count, int bill20Count, int bill50Count,
                                 int bill100Count, IPrinterSupplies printerSupplies,
                                 IMaintainable printerFirmware, IPersistence persistence) {
        this.dispenser = dispenser;
        this.bill10Count = bill10Count;
        this.bill20Count = bill20Count;
        this.bill50Count = bill50Count;
        this.bill100Count = bill100Count;
        this.printerSupplies = printerSupplies;
        this.printerFirmware = printerFirmware;
        this.persistence = persistence;
    }

    @Override
    public boolean execute() {
        if (bill10Count < 0 || bill20Count < 0 || bill50Count < 0 || bill100Count < 0) {
            return false;
        }
        if (bill10Count + bill20Count + bill50Count + bill100Count == 0) {
            return false;
        }
        dispenser.reloadCash(bill10Count, bill20Count, bill50Count, bill100Count);
        persistence.updateInventory(dispenser, printerSupplies, printerFirmware);
        return true;
    }
}
