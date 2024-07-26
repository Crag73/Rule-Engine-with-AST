package com.example.ruleengine.model;

public class Node {
    public enum Type {
        OPERATOR, OPERAND
    }
    
    public Type type;
    public Node left;
    public Node right;
    public String value;

    public Node(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Node(Type type, Node left, Node right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }
}
