package org.example.controller.basic;

import org.example.service.consumer.basic.JsonMessageConsumer;
import org.example.model.Employee;
import org.example.service.producer.basic.JsonMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private JsonMessageProducer jsonMessageProducer;

    @Autowired
    private JsonMessageConsumer jsonMessageConsumer;

    @GetMapping("/status")
    public String getStatus() {
        return "Active";
    }
    @PostMapping("/publish")
    public ResponseEntity<String> publishJsonMessageToRMQ(@RequestParam String queueName, @RequestBody Employee employee) {
        try {
            jsonMessageProducer.publishMessage(queueName, employee);
            return new ResponseEntity<>("Message published successfully", HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Message couldn't be published", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consume")
    public ResponseEntity<Employee> consumeJsonMessageFromRMQ(@RequestParam String queueName) {
        try {
            Employee employee = jsonMessageConsumer.consumeJsonMessage(queueName);
            if(employee==null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
