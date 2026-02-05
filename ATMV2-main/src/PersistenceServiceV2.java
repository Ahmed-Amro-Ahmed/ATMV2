import java.util.List;

public class PersistenceServiceV2 implements IPersistence {
    @Override
    public void saveAccount(AccountV2 account) {
        List<AccountV2> accounts = PersistenceManagerV2.loadAccounts();
        boolean found = false;
        for (AccountV2 acc : accounts) {
            if (acc.getUsername().equals(account.getUsername())) {
                int delta = account.getBalance() - acc.getBalance();
                if (delta > 0) {
                    acc.deposit(delta);
                } else if (delta < 0) {
                    acc.withdraw(-delta);
                }
                found = true;
                break;
            }
        }
        if (!found) {
            accounts.add(account);
        }
        PersistenceManagerV2.saveAccounts(accounts);
    }

    @Override
    public void updateInventory(ICashDispenser dispenser, IPrinterSupplies printerSupplies, IMaintainable printerFirmware) {
        PersistenceManagerV2.updateInventory(dispenser, printerSupplies, printerFirmware);
    }

    @Override
    public void logTransaction(String type, int amount) {
        PersistenceManagerV2.logTransaction(type, amount, "SUCCESS", null);
    }
}
