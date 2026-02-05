public class UpdatePrinterFirmwareTransaction implements ITransaction {
    private final IMaintainable printerFirmware;
    private final ICashDispenser dispenser;
    private final IPrinterSupplies printerSupplies;
    private final IPersistence persistence;

    public UpdatePrinterFirmwareTransaction(IMaintainable printerFirmware, ICashDispenser dispenser,
                                            IPrinterSupplies printerSupplies, IPersistence persistence) {
        this.printerFirmware = printerFirmware;
        this.dispenser = dispenser;
        this.printerSupplies = printerSupplies;
        this.persistence = persistence;
    }

    @Override
    public boolean execute() {
        String currentText = printerFirmware.getFirmwareVersion();
        if (currentText == null || currentText.trim().isEmpty()) {
            currentText = "1.0";
        }
        double currentVersion = Double.parseDouble(currentText);
        double newVersion = currentVersion + 0.1;
        String newVersionText = String.format("%.1f", newVersion);

        printerFirmware.updateFirmware(newVersionText);
        persistence.updateInventory(dispenser, printerSupplies, printerFirmware);

        System.out.println("Printer firmware updated to version: " + newVersionText);
        return true;
    }
}
