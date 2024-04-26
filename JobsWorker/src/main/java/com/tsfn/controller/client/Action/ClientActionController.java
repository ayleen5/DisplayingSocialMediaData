package com.tsfn.controller.client.Action;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="action-service", url = "http://localhost:9090")
public interface ClientActionController {

    @PostMapping("/actions/create")
    ResponseEntity<Action> createAction(@RequestBody Action action);

    @GetMapping("/actions/{id}")
    ResponseEntity<Action> getActionById(@PathVariable("id") int id);

    @GetMapping("/actions/getall")
    ResponseEntity<List<Action>> getAllActions();

    @PutMapping("/actions/update/{id}")
    ResponseEntity<Void> updateAction(@PathVariable("id") int id, @RequestBody Action action);

    @DeleteMapping("/actions/delete/{id}")
    ResponseEntity<Void> deleteAction(@PathVariable("id") int id);
}
