

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class MiniCalculator extends JFrame {

    private JTextField display;

    public MiniCalculator() {
        // Create the Frame
        JFrame frame = new JFrame("Mini Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // Display Area
        display = new JTextField();
        display.setEditable(false);
        frame.add(display, BorderLayout.NORTH);
        display.setPreferredSize(new Dimension(400, 150));

        // Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5));

        // Buttons
        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();

            if (buttonText.equals("=")) {
                try {
                    String result = evaluateExpression(display.getText());
                    display.setText(result);
                } catch (Exception ex) {
                    display.setText("Error");
                }
            } else if (buttonText.equals("C")) {
                display.setText("");
            } else {
                display.setText(display.getText() + buttonText);
            }
        }

        private String evaluateExpression(String expression) {
            return String.valueOf(calculate(expression));
        }

        private double calculate(String expression) {
            Stack<Double> numbers = new Stack<>();
            Stack<Character> operations = new Stack<>();
            StringBuilder num = new StringBuilder();

            for (char c : expression.toCharArray()) {
                if (Character.isDigit(c) || c == '.') {
                    num.append(c);
                } else {
                    if (num.length() > 0) {
                        numbers.push(Double.parseDouble(num.toString()));
                        num.setLength(0);
                    }
                    while (!operations.isEmpty() && precedence(c, operations.peek())) {
                        numbers.push(applyOp(operations.pop(), numbers.pop(), numbers.pop()));
                    }
                    operations.push(c);
                }
            }

            if (num.length() > 0) {
                numbers.push(Double.parseDouble(num.toString()));
            }

            while (!operations.isEmpty()) {
                numbers.push(applyOp(operations.pop(), numbers.pop(), numbers.pop()));
            }

            return numbers.pop();
        }

        private boolean precedence(char op1, char op2) {
            if (op2 == '(' || op2 == ')') return false;
            return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
        }

        private double applyOp(char op, double b, double a) {
            switch (op) {
                case '+': return a + b;
                case '-': return a - b;
                case '*': return a * b;
                case '/': return a / b;
                default: throw new UnsupportedOperationException("Invalid operation");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MiniCalculator::new);
    }
}
