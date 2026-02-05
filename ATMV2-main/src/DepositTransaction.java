public class DepositTransaction implements ITransaction {
    private final AccountV2 account;
    private final int amount;
    private final BankV2 bank;
    private final IReceiptService receiptService;
    private final ICashDispenser cashDispenser;
    private final IPrinterSupplies printerSupplies;
    private final IMaintainable printerFirmware;
    private final IPersistence persistence;

    public DepositTransaction(AccountV2 account, int amount, BankV2 bank, IReceiptService receiptService,
                              ICashDispenser cashDispenser, IPrinterSupplies printerSupplies,
                              IMaintainable printerFirmware, IPersistence persistence) {
        this.account = account;
        this.amount = amount;
        this.bank = bank;
        this.receiptService = receiptService;
        this.cashDispenser = cashDispenser;
        this.printerSupplies = printerSupplies;
        this.printerFirmware = printerFirmware;
        this.persistence = persistence;
    }

    @Override
    public boolean execute() {
        if (!bank.authenticateDeposit(account, amount)) {
            return false;
        }

        account.deposit(amount);
        receiptService.printReceipt("Deposit");
        persistence.saveAccount(account);
        persistence.logTransaction("DEPOSIT", amount);
        persistence.updateInventory(cashDispenser, printerSupplies, printerFirmware);
        return true;
    }
}
