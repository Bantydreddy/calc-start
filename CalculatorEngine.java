import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The "brain" of the calculator. It knows nothing about buttons or windows:
 * you send it commands with press(), then read getDisplay() and getHistory().
 *
 * Commands: "0".."9", ".", "+", "-", "*", "/", "=",
 *           "C" (clear all), "CE" (clear entry), "BACK" (backspace),
 *           "NEG" (+/-), "PCT" (%), "SQRT", "SQR" (x squared), "INV" (1/x)
 */
public class CalculatorEngine {

    private static final int MAX_DIGITS = 12;

    private double accumulator;          // running result, e.g. the 12 in "12 + 3"
    private String pendingOp = null;     // operator waiting for its second number
    private String entry = "0";          // what the display shows
    private String history = "";         // small line above the display
    private boolean startNew = true;     // next digit starts a brand new number
    private boolean opJustPressed = false;
    private boolean error = false;

    public String getDisplay() { return entry; }
    public String getHistory() { return history; }

    public void press(String cmd) {
        // After an error, any key wipes the slate clean first
        if (error) {
            clearAll();
            if (cmd.equals("C") || cmd.equals("CE")) return;
        }

        switch (cmd) {
            case ".":    enterDecimal();  break;
            case "+": case "-": case "*": case "/":
                         operator(cmd);   break;
            case "=":    equals();        break;
            case "C":    clearAll();      break;
            case "CE":   entry = "0"; startNew = false; break;
            case "BACK": backspace();     break;
            case "NEG":  negate();        break;
            case "PCT": case "SQRT": case "SQR": case "INV":
                         unary(cmd);      break;
            default:
                if (cmd.matches("[0-9]")) enterDigit(cmd);
        }
    }

    // ---------- typing numbers ----------

    private void enterDigit(String d) {
        if (startNew)                  { entry = d; startNew = false; }
        else if (entry.equals("0"))    entry = d;
        else if (entry.equals("-0"))   entry = "-" + d;
        else if (countDigits(entry) < MAX_DIGITS) entry += d;
        opJustPressed = false;
        if (pendingOp == null) history = "";
    }

    private void enterDecimal() {
        if (startNew)                  { entry = "0."; startNew = false; }
        else if (!entry.contains("."))  entry += ".";
        opJustPressed = false;
        if (pendingOp == null) history = "";
    }

    private void backspace() {
        if (startNew) return;                       // can't edit a result
        entry = entry.substring(0, entry.length() - 1);
        if (entry.isEmpty() || entry.equals("-")) entry = "0";
    }

    private void negate() {
        if (entry.equals("0")) return;
        entry = entry.startsWith("-") ? entry.substring(1) : "-" + entry;
        opJustPressed = false;
    }

    // ---------- operators ----------

    private void operator(String op) {
        // Changed their mind: "5 + *" just swaps the operator
        if (pendingOp != null && opJustPressed) {
            pendingOp = op;
            history = format(accumulator) + " " + symbol(op);
            return;
        }

        double value = parse(entry);
        if (pendingOp != null) {                    // chaining: 1 + 2 + ...
            Double result = calculate(accumulator, value, pendingOp);
            if (result == null) return;
            accumulator = result;
        } else {
            accumulator = value;
        }

        pendingOp = op;
        entry = format(accumulator);
        history = entry + " " + symbol(op);
        startNew = true;
        opJustPressed = true;
    }

    private void equals() {
        if (pendingOp == null) return;

        double second = parse(entry);
        Double result = calculate(accumulator, second, pendingOp);
        if (result == null) return;

        history = format(accumulator) + " " + symbol(pendingOp) + " " + format(second) + " =";
        accumulator = result;
        entry = format(result);
        pendingOp = null;
        startNew = true;
        opJustPressed = false;
    }

    // ---------- one-number functions ----------

    private void unary(String cmd) {
        double v = parse(entry);
        double r;

        switch (cmd) {
            case "SQRT":
                if (v < 0) { setError("Invalid input"); return; }
                r = Math.sqrt(v);
                break;
            case "SQR":
                r = v * v;
                break;
            case "INV":
                if (v == 0) { setError("Cannot divide by zero"); return; }
                r = 1 / v;
                break;
            default: // PCT: "200 + 10%" means 10% OF 200
                r = (pendingOp != null) ? accumulator * v / 100 : v / 100;
        }

        if (Double.isNaN(r) || Double.isInfinite(r)) { setError("Overflow"); return; }

        entry = format(r);
        startNew = true;
        opJustPressed = false;
        if (pendingOp == null) history = "";
    }

    // ---------- helpers ----------

    /** Returns null (and sets an error) if the maths is impossible. */
    private Double calculate(double a, double b, String op) {
        double r;
        switch (op) {
            case "+": r = a + b; break;
            case "-": r = a - b; break;
            case "*": r = a * b; break;
            default:
                if (b == 0) { setError("Cannot divide by zero"); return null; }
                r = a / b;
        }
        if (Double.isNaN(r) || Double.isInfinite(r)) { setError("Overflow"); return null; }
        return r;
    }

    private void clearAll() {
        accumulator = 0;
        pendingOp = null;
        entry = "0";
        history = "";
        startNew = true;
        opJustPressed = false;
        error = false;
    }

    private void setError(String message) {
        clearAll();
        entry = message;
        error = true;
    }

    private static double parse(String s) {
        return Double.parseDouble(s);
    }

    private static int countDigits(String s) {
        int n = 0;
        for (char c : s.toCharArray()) if (Character.isDigit(c)) n++;
        return n;
    }

    private static String symbol(String op) {
        switch (op) {
            case "*": return "\u00D7";   // ×
            case "/": return "\u00F7";   // ÷
            default:  return op;
        }
    }

    /** 0.1 + 0.2 shows as 0.3, 5.0 shows as 5, and so on. */
    static String format(double v) {
        BigDecimal bd = new BigDecimal(v).round(new MathContext(12)).stripTrailingZeros();
        double abs = Math.abs(v);
        if (abs >= 1e15 || (abs != 0 && abs < 1e-9)) return bd.toString();   // e.g. 1E+20
        return bd.toPlainString();
    }
}
