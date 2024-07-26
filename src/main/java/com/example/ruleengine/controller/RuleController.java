package com.example.ruleengine.controller;

import com.example.ruleengine.model.Node;
import com.example.ruleengine.service.RuleEngine;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rules")
public class RuleController {
    private final RuleEngine ruleEngine = new RuleEngine();

    @CrossOrigin(origins = "*")  // Allow all origins
    @PostMapping("/create")
    public Node createRule(@RequestBody String ruleString) {
        return ruleEngine.create_rule(ruleString);
    }

    @CrossOrigin(origins = "*")  // Allow all origins
    @PostMapping("/combine")
    public Node combineRules(@RequestBody List<String> ruleStrings) {
        return ruleEngine.combine_rules(ruleStrings);
    }

    @CrossOrigin(origins = "*") 
    @PostMapping("/evaluate")
    @SuppressWarnings("unchecked")
    public boolean evaluateRule(@RequestBody Map<String, Object> payload) {
        Node ast = (Node) payload.get("ast");
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        return ruleEngine.evaluate_rule(ast, data);
    }
}
