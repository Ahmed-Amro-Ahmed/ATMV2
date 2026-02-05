public interface IPersistence {
    void saveAccount(AccountV2 account);
    void updateInventory(ICashDispenser dispenser, IPrinterSupplies printerSupplies, IMaintainable printerFirmware);
    void logTransaction(String type, int amount);
}
