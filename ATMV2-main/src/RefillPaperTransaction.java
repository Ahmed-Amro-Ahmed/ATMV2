public class RefillPaperTransaction implements ITransaction {
    private final IPrinterSupplies printerSupplies;
    private final ICashDispenser dispenser;
    private final IMaintainable printerFirmware;
    private final IPersistence persistence;

    public RefillPaperTransaction(IPrinterSupplies printerSupplies, ICashDispenser dispenser,
                                  IMaintainable printerFirmware, IPersistence persistence) {
        this.printerSupplies = printerSupplies;
        this.dispenser = dispenser;
        this.printerFirmware = printerFirmware;
        this.persistence = persistence;
    }

    @Override
    public boolean execute() {
        printerSupplies.refillPaper();
        persistence.updateInventory(dispenser, printerSupplies, printerFirmware);
        return true;
    }
}
