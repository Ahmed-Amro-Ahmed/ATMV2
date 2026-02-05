public class MenuController {
    private final ATM atm;
    private final ConsoleUI ui;

    public MenuController(ATM atm, ConsoleUI ui) {
        this.atm = atm;
        this.ui = ui;
    }

    public void start() {
        ui.displayMessage("Welcome to SOLID Bank");
        while (true) {
            showMainMenu();
            int choice = ui.promptInt("Select option: ");

            switch (choice) {
                case 0:
                    ui.displayMessage("ATM shutting down.");
                    return;
                case 1:
                    runCustomerSession();
                    break;
                case 2:
                    runTechnicianSession();
                    break;
                default:
                    ui.displayMessage("Invalid selection.");
            }
        }
    }

    private void runCustomerSession() {
        AccountV2 account = authenticateCustomer();
        if (account == null) {
            ui.displayMessage("Authentication failed.");
            return;
        }
        ui.displayMessage("Authentication successful.");

        while (true) {
            showCustomerMenu();
            int choice = ui.promptInt("Select option: ");

            switch (choice) {
                case 0:
                    return;
                case 1:
                    ui.displayMessage("Balance: $" + atm.getBalance(account));
                    break;
                case 2:
                    handleWithdrawal(account);
                    break;
                case 3:
                    if (handleDeposit(account)) {
                        return;
                    }
                    break;
                case 4:
                    handleTransfer(account);
                    break;
                default:
                    ui.displayMessage("Invalid selection.");
            }
        }
    }

    private void runTechnicianSession() {
        String username = ui.promptString("Technician username: ");
        String password = ui.promptString("Technician password: ");

        if (!atm.authenticateTechnician(username, password)) {
            ui.displayMessage("Access denied.");
            return;
        }

        handleTechnicianMenu();
    }

    private void handleTechnicianMenu() {
        while (true) {
            int choice = ui.promptInt("1. Diagnostic Report\n2. Reload Cash\n3. Dispense Deposits\n4. Update ATM Firmware\n5. Update Printer Firmware\n6. Refill Ink\n7. Refill Paper\n0. Back\nSelect option: ");
            boolean result;

            switch (choice) {
                case 1:
                    result = atm.runDiagnosticReport();
                    break;
                case 2:
                    int bill10Count = ui.promptInt("Number of $10 bills to add: ");
                    int bill20Count = ui.promptInt("Number of $20 bills to add: ");
                    int bill50Count = ui.promptInt("Number of $50 bills to add: ");
                    int bill100Count = ui.promptInt("Number of $100 bills to add: ");
                    result = atm.reloadCash(bill10Count, bill20Count, bill50Count, bill100Count);
                    break;
                case 3:
                    result = atm.dispenseDeposits();
                    break;
                case 4:
                    result = atm.updateAtmFirmware();
                    break;
                case 5:
                    result = atm.updatePrinterFirmware();
                    break;
                case 6:
                    result = atm.refillInk();
                    break;
                case 7:
                    result = atm.refillPaper();
                    break;
                case 0:
                    return;
                default:
                    ui.displayMessage("Invalid selection.");
                    continue;
            }

            if (result) {
                ui.displayMessage("Technician task completed.");
            } else {
                ui.displayMessage("Technician task failed.");
            }
        }
    }

    private void handleWithdrawal(AccountV2 account) {
        int amount = ui.promptInt("Enter amount: ");

        if (atm.withdraw(account, amount)) {
            ui.displayMessage("Please take your cash.");
        } else {
            ui.displayMessage("Transaction failed.");
        }
    }

    private boolean handleDeposit(AccountV2 account) {
        int amount = ui.promptInt("Deposit amount (multiples of $10, $20, $50, $100 only): ");

        if (atm.deposit(account, amount)) {
            ui.displayMessage("Deposit successful.");
            return true;
        }

        ui.displayMessage("Transaction failed.");
        return false;
    }

    private void handleTransfer(AccountV2 currentAccount) {
        String targetUser = ui.promptString("Enter target username: ");
        int amount = ui.promptInt("Enter amount: ");

        if (atm.transfer(currentAccount, targetUser, amount)) {
            ui.displayMessage("Transfer Successful!");
        } else {
            ui.displayMessage("Transfer Failed.");
        }
    }

    private AccountV2 authenticateCustomer() {
        String username = ui.promptString("Enter username: ");
        String pin = ui.promptString("Enter PIN: ");
        return atm.authenticateCustomer(username, pin);
    }

    private void showMainMenu() {
        ui.displayMessage("\n=== ATM MAIN MENU ===");
        ui.displayMessage("1) Customer");
        ui.displayMessage("2) Technician");
        ui.displayMessage("0) Exit");
    }

    private void showCustomerMenu() {
        ui.displayMessage("\n--- CUSTOMER MENU ---");
        ui.displayMessage("1) Balance");
        ui.displayMessage("2) Withdraw");
        ui.displayMessage("3) Deposit");
        ui.displayMessage("4) Transfer");
        ui.displayMessage("0) Back");
    }
}
