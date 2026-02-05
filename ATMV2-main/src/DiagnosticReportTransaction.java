public class DiagnosticReportTransaction implements ITransaction {
    private final ICashDispenser dispenser;
    private final IPrinterSupplies printerSupplies;
    private final IMaintainable printerFirmware;
    private final String systemVersion;

    public DiagnosticReportTransaction(ICashDispenser dispenser, IPrinterSupplies printerSupplies,
                                       IMaintainable printerFirmware, String systemVersion) {
        this.dispenser = dispenser;
        this.printerSupplies = printerSupplies;
        this.printerFirmware = printerFirmware;
        this.systemVersion = systemVersion;
    }

    @Override
    public boolean execute() {
        System.out.println("Select option: 1");
        System.out.println("ATM Firmware: V" + systemVersion);
        System.out.println("--- TECH V2 DIAGNOSTIC REPORT ---");

        String bills = "$10x" + dispenser.getBill10Count()
                + " $20x" + dispenser.getBill20Count()
                + " $50x" + dispenser.getBill50Count()
                + " $100x" + dispenser.getBill100Count();

        System.out.println("Status: Cash Available: $" + dispenser.getCashLevel()
                + " | Deposits Held: $" + dispenser.getTotalDeposits()
                + " | Bills: " + bills);

        System.out.println("---------------------------------");

        System.out.println("Status: Printer Paper Level: " + printerSupplies.getPaperLevel()
                + "% | Ink Level: " + printerSupplies.getInkLevel() + "%");
        System.out.println("Printer Firmware: " + printerFirmware.getFirmwareVersion());

        return true;
    }
}
