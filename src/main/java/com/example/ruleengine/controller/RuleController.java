package com.example.ruleengine.controller;

import com.example.ruleengine.model.Node;
import com.example.ruleengine.service.RuleEngine;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")  // Allow all origins
@RestController
@RequestMapping("/rules")
public class RuleController {
    private final RuleEngine ruleEngine = new RuleEngine();

    @PostMapping("/create")
    public Node createRule(@RequestBody Map<String, String> payload) {
        String ruleString = payload.get("ruleString");
        try {
            return ruleEngine.createRule(ruleString);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating rule", e);
        }
    }

    @PostMapping("/combine")
    public Node combineRules(@RequestBody List<String> ruleStrings) {
        try {
            return ruleEngine.combineRules(ruleStrings);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error combining rules", e);
        }
    }

    @PostMapping("/evaluate")
    public boolean evaluateRule(@RequestBody Map<String, Object> payload) {
        try {
            Node ast = (Node) payload.get("ast");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            return ruleEngine.evaluateRule(ast, data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error evaluating rule", e);
        }
    }
}
