package com.oceane.dm.feedback.controller;

import com.oceane.dm.models.model.Dummy;
import com.oceane.dm.models.repository.DummyRepository;
import com.oceane.dm.feedback.service.DummyFeedBackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dummy")
public class DummyController {

    private final DummyRepository dummyRepository;
    private final DummyFeedBackService dummyService;

    public DummyController(DummyRepository dummyRepository, DummyFeedBackService dummyService) {
        this.dummyRepository = dummyRepository;
        this.dummyService = dummyService;
    }

    @GetMapping("/test")
    public String dummyOperation() {
        return "It works !";
    }

    // Direct call to repository from controller is possible for basic CRUD without business rule
    @GetMapping
    public List<Dummy> fetchDummies() {
        return dummyRepository.findAll();
    }

    @PostMapping
    public Dummy createDummy(/*@Valid*/ @RequestBody Dummy dummy) {
        return dummyService.createDummy(dummy);
    }

    @GetMapping("/init")
    public boolean initDummies() {
        Dummy dummy1 = new Dummy();
        dummy1.setName("dummy1");
        dummyRepository.save(dummy1);

        Dummy dummy2 = new Dummy();
        dummy2.setName("dummy2");
        dummyRepository.save(dummy2);

        return true;
    }
}
