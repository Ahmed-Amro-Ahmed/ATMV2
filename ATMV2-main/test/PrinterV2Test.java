import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrinterV2Test {
    @Test
    public void printReceiptConsumesInkAndPaper() {
        PrinterV2 printer = new PrinterV2();
        printer.refillInk();
        printer.refillPaper();

        printer.printReceipt();

        assertEquals(99, printer.getInkLevel());
        assertEquals(99, printer.getPaperLevel());
    }
}
