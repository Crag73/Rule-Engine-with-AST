package com.example.ruleengine.service;

import com.example.ruleengine.model.Node;
import java.util.*;

public class RuleEngine {

    public Node createRule(String ruleString) {
        return parseRuleStringToAST(ruleString);
    }

    public Node combineRules(List<String> ruleStrings) {
        Node combinedAST = null;
        for (String ruleString : ruleStrings) {
            Node ast = parseRuleStringToAST(ruleString);
            if (combinedAST == null) {
                combinedAST = ast;
            } else {
                combinedAST = new Node("operator", combinedAST, ast, "AND");
            }
        }
        return combinedAST;
    }

    public boolean evaluateRule(Node ast, Map<String, Object> data) {
        return evaluateAST(ast, data);
    }

    private Node parseRuleStringToAST(String ruleString) {
        Stack<Node> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();
        StringBuilder currentToken = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < ruleString.length(); i++) {
            char c = ruleString.charAt(i);

            if (c == '\"') {
                insideQuotes = !insideQuotes;
                currentToken.append(c);
            } else if (Character.isWhitespace(c) && !insideQuotes) {
                processToken(operandStack, currentToken.toString().trim());
                currentToken.setLength(0);
            } else if (c == '(') {
                operatorStack.push(String.valueOf(c));
            } else if (c == ')') {
                processToken(operandStack, currentToken.toString().trim());
                currentToken.setLength(0);
                while (!operatorStack.peek().equals("(")) {
                    processOperator(operandStack, operatorStack);
                }
                operatorStack.pop(); // Remove the '('
            } else if (c == 'A' && i + 2 < ruleString.length() && ruleString.substring(i, i + 3).equals("AND")) {
                processToken(operandStack, currentToken.toString().trim());
                currentToken.setLength(0);
                processOperators(operandStack, operatorStack, 1);
                operatorStack.push("AND");
                i += 2; // Skip "AND"
            } else if (c == 'O' && i + 1 < ruleString.length() && ruleString.substring(i, i + 2).equals("OR")) {
                processToken(operandStack, currentToken.toString().trim());
                currentToken.setLength(0);
                processOperators(operandStack, operatorStack, 0);
                operatorStack.push("OR");
                i += 1; // Skip "OR"
            } else if (c == '>' || c == '<' || c == '=') {
                processToken(operandStack, currentToken.toString().trim());
                currentToken.setLength(0);
                StringBuilder operator = new StringBuilder();
                operator.append(c);
                if (i + 1 < ruleString.length() && ruleString.charAt(i + 1) == '=') {
                    operator.append('=');
                    i++;
                }
                operatorStack.push(operator.toString());
            } else {
                currentToken.append(c);
            }
        }

        processToken(operandStack, currentToken.toString().trim());

        while (!operatorStack.isEmpty()) {
            processOperator(operandStack, operatorStack);
        }

        Node root = operandStack.pop();
        System.out.println("Constructed AST: " + printNode(root));
        return root;
    }

    private void processToken(Stack<Node> stack, String token) {
        if (!token.isEmpty()) {
            if (isOperator(token)) {
                stack.push(new Node("operator", null, null, token));
            } else {
                stack.push(new Node("operand", null, null, token));
            }
        }
    }

    private boolean isOperator(String token) {
        return token.equals("AND") || token.equals("OR") || token.equals(">") || token.equals("<") || token.equals("=");
    }

    private void processOperators(Stack<Node> stack, Stack<String> operators, int precedence) {
        while (!operators.isEmpty() && precedence(operators.peek()) >= precedence) {
            processOperator(stack, operators);
        }
    }

    private void processOperator(Stack<Node> stack, Stack<String> operators) {
        String operator = operators.pop();
        Node right = stack.pop();
        Node left = stack.pop();
        stack.push(new Node("operator", left, right, operator));
    }

    private int precedence(String operator) {
        if (operator.equals("AND")) return 1;
        if (operator.equals("OR")) return 0;
        return -1;
    }

    private boolean evaluateAST(Node node, Map<String, Object> data) {
        if (node == null) {
            return false;
        }

        if ("operand".equals(node.type)) {
            boolean result = evaluateCondition(node.value.toString(), data);
            System.out.println("Evaluating operand: " + node.value + " -> " + result);
            return result;
        }

        if ("operator".equals(node.type)) {
            boolean leftResult = evaluateAST(node.left, data);
            boolean rightResult = evaluateAST(node.right, data);
            boolean result = false;
            switch (node.value.toString()) {
                case "AND":
                    result = leftResult && rightResult;
                    break;
                case "OR":
                    result = leftResult || rightResult;
                    break;
            }
            System.out.println("Evaluating operator: " + node.value + " with left=" + leftResult + " right=" + rightResult + " -> " + result);
            return result;
        }

        return false;
    }

    private boolean evaluateCondition(String condition, Map<String, Object> data) {
        String[] parts = condition.split(" ");
        if (parts.length != 3) {
            return false;
        }

        String field = parts[0];
        String operator = parts[1];
        String value = parts[2].replace("'", ""); // Remove single quotes from string values

        Object fieldValue = data.get(field);

        if (fieldValue instanceof Number) {
            double numericFieldValue = ((Number) fieldValue).doubleValue();
            double numericValue = Double.parseDouble(value);
            switch (operator) {
                case ">":
                    return numericFieldValue > numericValue;
                case "<":
                    return numericFieldValue < numericValue;
                case "=":
                    return numericFieldValue == numericValue;
                default:
                    return false;
            }
        } else if (fieldValue instanceof String) {
            switch (operator) {
                case "=":
                    return fieldValue.equals(value);
                default:
                    return false;
            }
        }
        return false;
    }

    private String printNode(Node node) {
        if (node == null) return "null";
        if ("operand".equals(node.type)) return node.value.toString();
        if ("operator".equals(node.type)) {
            return "(" + printNode(node.left) + " " + node.value + " " + printNode(node.right) + ")";
        }
        return "unknown";
    }
}
