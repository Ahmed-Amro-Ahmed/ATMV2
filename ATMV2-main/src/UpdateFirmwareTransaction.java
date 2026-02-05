public class UpdateFirmwareTransaction implements ITransaction {
    private final ICashDispenser dispenser;
    private final IPrinterSupplies printerSupplies;
    private final IMaintainable printerFirmware;
    private final IPersistence persistence;

    public UpdateFirmwareTransaction(ICashDispenser dispenser, IPrinterSupplies printerSupplies,
                                     IMaintainable printerFirmware, IPersistence persistence) {
        this.dispenser = dispenser;
        this.printerSupplies = printerSupplies;
        this.printerFirmware = printerFirmware;
        this.persistence = persistence;
    }

    @Override
    public boolean execute() {
        String currentText = System.getProperty("atm.version", "1.0");
        String normalized = PersistenceManagerV2.loadSystemVersion();
        if (normalized != null) {
            currentText = normalized;
        }
        double currentVersion = Double.parseDouble(currentText);
        double newVersion = currentVersion + 0.1;
        String newVersionText = String.format("%.1f", newVersion);

        System.setProperty("atm.version", newVersionText);
        persistence.updateInventory(dispenser, printerSupplies, printerFirmware);

        System.out.println("ATM firmware updated to version: " + newVersionText);
        return true;
    }
}
