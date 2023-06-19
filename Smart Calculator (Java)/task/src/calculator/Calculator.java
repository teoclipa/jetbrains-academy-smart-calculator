package calculator;

import java.math.BigInteger;
import java.util.*;

public class Calculator {
    private final Map<String, BigInteger> variables = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                continue;
            }
            if (input.equals("/exit")) {
                System.out.println("Bye!");
                break;
            } else if (input.equals("/help")) {
                printHelp();
            } else if (input.startsWith("/")) {
                System.out.println("Unknown command");
            } else if (input.contains("=")) {
                handleAssignment(input);
            } else if (input.matches("[a-zA-Z]+")) {
                evaluateVariable(input);
            } else {
                evaluateExpression(input);
            }
        }
    }

    private void handleAssignment(String input) {
        String[] parts = input.split("=");

        if (parts.length != 2) {
            System.out.println("Invalid assignment");
            return;
        }

        String variable = parts[0].trim();
        String valueStr = parts[1].trim();

        if (!variable.matches("[a-zA-Z]+")) {
            System.out.println("Invalid identifier");
            return;
        }

        BigInteger value;
        try {
            value = new BigInteger(valueStr);
        } catch (NumberFormatException e) {
            if (!variables.containsKey(valueStr)) {
                System.out.println("Invalid assignment");
                return;
            }
            value = variables.get(valueStr);
        }

        variables.put(variable, value);
    }

    private void evaluateVariable(String input) {
        if (variables.containsKey(input)) {
            System.out.println(variables.get(input));
        } else {
            System.out.println("Unknown variable");
        }
    }


    private void evaluateExpression(String input) {
        List<String> postfixExpression = convertToPostfix(input);

        if (postfixExpression == null) {
            return; // Abort evaluation if postfix expression is null
        }

        BigInteger result = evaluatePostfix(postfixExpression);

        System.out.println(result);
    }

    private List<String> convertToPostfix(String infixExpression) {
        List<String> postfixExpression = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        // Remove all whitespace
        infixExpression = infixExpression.replaceAll("\\s+", "");

        // Replace consecutive operators
        infixExpression = replaceConsecutiveOperators(infixExpression);

        // Split the input into numbers, variables, and operators
        String[] tokens = infixExpression.split("(?<=[-+*/()])|(?=[-+*/()])");

        for (String token : tokens) {
            if (token.matches("[a-zA-Z]+")) {
                postfixExpression.add(token);  // Variable
            } else if (token.matches("-?\\d+")) {
                postfixExpression.add(token);  // Number
            } else if (token.equals("(")) {
                stack.push(token);  // Left parenthesis
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfixExpression.add(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop();  // Discard the left parenthesis
                } else {
                    System.out.println("Invalid expression");  // Unbalanced parentheses
                    return null;
                }
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && !stack.peek().equals("(") && hasHigherPrecedence(token, stack.peek())) {
                    postfixExpression.add(stack.pop());
                }
                stack.push(token);
            } else {
                System.out.println("Invalid expression");
                return null;
            }
        }

        while (!stack.isEmpty()) {
            String top = stack.pop();
            if (top.equals("(")) {
                System.out.println("Invalid expression");  // Unbalanced parentheses
                return null;
            }
            postfixExpression.add(top);
        }

        return postfixExpression;
    }

    private BigInteger evaluatePostfix(List<String> postfixExpression) {
        Deque<BigInteger> stack = new ArrayDeque<>();

        for (String token : postfixExpression) {
            if (token.matches("[a-zA-Z]+")) {
                if (variables.containsKey(token)) {
                    stack.push(variables.get(token));  // Push variable value
                } else {
                    System.out.println("Unknown variable");
                    return BigInteger.ZERO;
                }
            } else if (token.matches("-?\\d+")) {
                stack.push(new BigInteger(token));  // Push number
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    System.out.println("Invalid expression");
                    return BigInteger.ZERO;
                }
                BigInteger operand2 = stack.pop();
                BigInteger operand1 = stack.pop();
                BigInteger result = performOperation(token, operand1, operand2);
                stack.push(result);
            } else {
                System.out.println("Invalid expression");
                return BigInteger.ZERO;
            }
        }

        if (stack.size() != 1) {
            System.out.println("Invalid expression");
            return BigInteger.ZERO;
        }

        return stack.pop();
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private boolean hasHigherPrecedence(String operator1, String operator2) {
        int precedence1 = getPrecedence(operator1);
        int precedence2 = getPrecedence(operator2);
        return precedence1 <= precedence2;
    }

    public static int getPrecedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }


    private BigInteger performOperation(String operator, BigInteger operand1, BigInteger operand2) {
        switch (operator) {
            case "+" -> {
                return operand1.add(operand2);
            }
            case "-" -> {
                return operand1.subtract(operand2);
            }
            case "*" -> {
                return operand1.multiply(operand2);
            }
            case "/" -> {
                if (operand2.equals(BigInteger.ZERO)) {
                    System.out.println("Division by zero");
                    return BigInteger.ZERO;
                }
                return operand1.divide(operand2);
            }
            default -> {
                System.out.println("Invalid operator");
                return BigInteger.ZERO;
            }
        }

    }

    private static String replaceConsecutiveOperators(String expression) {
        while (expression.matches(".*[+-]{2,}.*")) {
            expression = expression.replaceAll("--", "+");
            expression = expression.replaceAll("\\+\\+", "+");
            expression = expression.replaceAll("-\\+", "-");
            expression = expression.replaceAll("\\+-", "-");
        }

        return expression;
    }

    private void printHelp() {
        System.out.println("This calculator supports the following operators:");
        System.out.println("+ : addition");
        System.out.println("- : subtraction");
        System.out.println("* : multiplication");
        System.out.println("/ : integer division");
        System.out.println("( ) : parentheses for grouping");
        System.out.println("You can use variables (e.g., a, b) and assign values to them (e.g., a = 5)");
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.run();
    }
}
