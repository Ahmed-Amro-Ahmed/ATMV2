public class DispenseDepositsTransaction implements ITransaction {
    private final ICashDispenser dispenser;
    private final IPrinterSupplies printerSupplies;
    private final IMaintainable printerFirmware;
    private final IPersistence persistence;

    public DispenseDepositsTransaction(ICashDispenser dispenser, IPrinterSupplies printerSupplies,
                                       IMaintainable printerFirmware, IPersistence persistence) {
        this.dispenser = dispenser;
        this.printerSupplies = printerSupplies;
        this.printerFirmware = printerFirmware;
        this.persistence = persistence;
    }

    @Override
    public boolean execute() {
        dispenser.dispenseDeposits();
        persistence.updateInventory(dispenser, printerSupplies, printerFirmware);
        return true;
    }
}
