# Java Swing Calculator

A desktop calculator built with Java Swing.

## Features
- Add, subtract, multiply, divide (with chaining: `1 + 2 + 3 =`)
- Decimals, +/- , percent, square, square root, reciprocal (1/x)
- Backspace, CE (clear entry) and C (clear all)
- History line showing the current expression
- Friendly errors (divide by zero, invalid input, overflow)
- Full keyboard support: digits, `+ - * /`, `Enter` or `=`, `Backspace`, `Delete` (CE), `Esc` (C), `%`
- Resizable window

## Project structure
| File | Purpose |
|------|---------|
| `Calc.java` | The window: buttons, display, keyboard bindings |
| `CalculatorEngine.java` | All the maths and state, with no GUI code |

## Run it
```bash
javac Calc.java CalculatorEngine.java
java Calc
```
Requires JDK 11 or newer.
