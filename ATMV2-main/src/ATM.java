public class ATM {
    private static final String TECH_USER = "tech";
    private static final String TECH_PASS = "1234";

    private final ICashDispenser cashDispenser;
    private final IPrinter printer;
    private final IPrinterSupplies printerSupplies;
    private final IMaintainable printerFirmware;
    private final BankV2 bank;
    private final IReceiptService receiptService;
    private final IPersistence persistence;

    public ATM(ICashDispenser dispenser, IPrinter printer, IPrinterSupplies printerSupplies,
               IMaintainable printerFirmware, BankV2 bank, IPersistence persistence) {
        this.cashDispenser = dispenser;
        this.printer = printer;
        this.printerSupplies = printerSupplies;
        this.printerFirmware = printerFirmware;
        this.bank = bank;
        this.receiptService = new ReceiptService(printer);
        this.persistence = persistence;
    }

    public AccountV2 authenticateCustomer(String username, String pin) {
        for (AccountV2 acc : bank.getAccounts()) {
            if (acc.getUsername().equals(username) && acc.authenticate(pin)) {
                return acc;
            }
        }
        return null;
    }

    public boolean authenticateTechnician(String username, String password) {
        return TECH_USER.equals(username) && TECH_PASS.equals(password);
    }

    public int getBalance(AccountV2 account) {
        return account.getBalance();
    }

    public boolean withdraw(AccountV2 account, int amount) {
        ITransaction withdrawal = new WithdrawalTransaction(
                account,
                amount,
                bank,
                cashDispenser,
                printerSupplies,
                printerFirmware,
                receiptService,
                persistence
        );
        return withdrawal.execute();
    }

    public boolean deposit(AccountV2 account, int amount) {
        ITransaction deposit = new DepositTransaction(
                account,
                amount,
                bank,
                receiptService,
                cashDispenser,
                printerSupplies,
                printerFirmware,
                persistence
        );
        return deposit.execute();
    }

    public boolean transfer(AccountV2 currentAccount, String targetUser, int amount) {
        AccountV2 targetAcc = bank.findAccount(targetUser);
        if (targetAcc == null) {
            return false;
        }
        ITransaction transfer = new TransferTransaction(
                currentAccount,
                targetAcc,
                amount,
                bank,
                receiptService,
                cashDispenser,
                printerSupplies,
                printerFirmware,
                persistence
        );
        return transfer.execute();
    }

    public boolean runDiagnosticReport() {
        ITransaction report = new DiagnosticReportTransaction(
                cashDispenser,
                printerSupplies,
                printerFirmware,
                System.getProperty("atm.version", "1.0")
        );
        return report.execute();
    }

    public boolean reloadCash(int bill10Count, int bill20Count, int bill50Count, int bill100Count) {
        return new ReloadCashTransaction(cashDispenser, bill10Count, bill20Count, bill50Count, bill100Count,
                printerSupplies, printerFirmware, persistence).execute();
    }

    public boolean dispenseDeposits() {
        return new DispenseDepositsTransaction(cashDispenser, printerSupplies, printerFirmware, persistence).execute();
    }

    public boolean updateAtmFirmware() {
        return new UpdateFirmwareTransaction(cashDispenser, printerSupplies, printerFirmware, persistence).execute();
    }

    public boolean updatePrinterFirmware() {
        return new UpdatePrinterFirmwareTransaction(printerFirmware, cashDispenser, printerSupplies, persistence).execute();
    }

    public boolean refillInk() {
        return new RefillInkTransaction(printerSupplies, cashDispenser, printerFirmware, persistence).execute();
    }

    public boolean refillPaper() {
        return new RefillPaperTransaction(printerSupplies, cashDispenser, printerFirmware, persistence).execute();
    }
}
