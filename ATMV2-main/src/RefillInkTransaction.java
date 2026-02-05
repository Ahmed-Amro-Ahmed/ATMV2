public class RefillInkTransaction implements ITransaction {
    private final IPrinterSupplies printerSupplies;
    private final ICashDispenser dispenser;
    private final IMaintainable printerFirmware;
    private final IPersistence persistence;

    public RefillInkTransaction(IPrinterSupplies printerSupplies, ICashDispenser dispenser,
                                IMaintainable printerFirmware, IPersistence persistence) {
        this.printerSupplies = printerSupplies;
        this.dispenser = dispenser;
        this.printerFirmware = printerFirmware;
        this.persistence = persistence;
    }

    @Override
    public boolean execute() {
        printerSupplies.refillInk();
        persistence.updateInventory(dispenser, printerSupplies, printerFirmware);
        return true;
    }
}
