package com.example.ruleengine.service;

import com.example.ruleengine.model.Node;

import java.util.*;

public class RuleEngine {

    public Node create_rule(String ruleString) {
        return parseRuleStringToAST(ruleString);
    }

    private Node parseRuleStringToAST(String ruleString) {
        Stack<Node> stack = new Stack<>();
        String[] tokens = ruleString.split(" ");
        for (String token : tokens) {
            if (token.equals("AND") || token.equals("OR")) {
                Node right = stack.pop();
                Node left = stack.pop();
                stack.push(new Node(Node.Type.OPERATOR, left, right));
            } else if (token.startsWith("(") && token.endsWith(")")) {
                stack.push(new Node(Node.Type.OPERAND, token.substring(1, token.length() - 1)));
            }
        }
        return stack.pop();
    }

    public Node combine_rules(List<String> ruleStrings) {
        List<Node> asts = new ArrayList<>();
        for (String ruleString : ruleStrings) {
            asts.add(create_rule(ruleString));
        }
        return combineASTs(asts);
    }

    private Node combineASTs(List<Node> asts) {
        if (asts.isEmpty()) return null;
        Node combined = asts.get(0);
        for (int i = 1; i < asts.size(); i++) {
            combined = new Node(Node.Type.OPERATOR, combined, asts.get(i));
        }
        return combined;
    }

    public boolean evaluate_rule(Node ast, Map<String, Object> data) {
        if (ast.type == Node.Type.OPERAND) {
            return evaluateOperand(ast.value, data);
        }
        boolean left = evaluate_rule(ast.left, data);
        boolean right = evaluate_rule(ast.right, data);
        return ast.value.equals("AND") ? left && right : left || right;
    }

    private boolean evaluateOperand(String operand, Map<String, Object> data) {
        String[] parts = operand.split(" ");
        String field = parts[0];
        String operator = parts[1];
        String value = parts[2];

        Object fieldValue = data.get(field);
        if (fieldValue == null) return false;

        switch (operator) {
            case ">":
                return Double.parseDouble(fieldValue.toString()) > Double.parseDouble(value);
            case "<":
                return Double.parseDouble(fieldValue.toString()) < Double.parseDouble(value);
            case "=":
                return fieldValue.toString().equals(value);
            default:
                return false;
        }
    }
}
