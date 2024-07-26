package com.example.ruleengine.model;

public class Node {
    public String type; // "operator" or "operand"
    public Node left;
    public Node right;
    public Object value; // Operator ("AND", "OR", ">", "<", "=") or operand value

    public Node(String type, Node left, Node right, Object value) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.value = value;
    }
}
