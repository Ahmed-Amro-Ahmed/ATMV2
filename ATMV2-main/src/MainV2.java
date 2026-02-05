public class MainV2 {
    public static void main(String[] args) {
        System.setProperty("atm.version", PersistenceManagerV2.loadSystemVersion());
        ICashDispenser cashBin = new CashBinV2(1000);
        PrinterV2 printer = new PrinterV2();
        BankV2 bank = new BankV2(PersistenceManagerV2.loadAccounts());
        ConsoleUI ui = new ConsoleUI();

        IPersistence persistence = new PersistenceServiceV2();
        ATM atm = new ATM(cashBin, printer, printer, printer, bank, persistence);
        MenuController menu = new MenuController(atm, ui);
        menu.start();
    }
}
