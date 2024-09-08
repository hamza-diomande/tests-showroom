package com.oceane.dm.projet.controller;

import com.oceane.dm.models.model.Dummy;
import com.oceane.dm.models.repository.DummyRepository;
//import com.oceane.dm.feedback.service.DummyFeedBackService;
import com.oceane.dm.projet.model.DummyRequest;
import com.oceane.dm.projet.service.CaptchaService;
import com.oceane.dm.projet.service.DummyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dummy")
public class DummyController {

    private final DummyRepository dummyRepository;
    private final DummyService dummyService;
    private final CaptchaService captchaService;

    public DummyController(DummyRepository dummyRepository,
                           DummyService dummyService,
                           CaptchaService captchaService
    ) {
        this.dummyRepository = dummyRepository;
        this.dummyService = dummyService;
        this.captchaService=captchaService;
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
    public Dummy createDummy(@Valid @RequestBody DummyRequest dummyRequest) {
        if(this.captchaService.checkCaptcha(dummyRequest.getCaptchaSession(),dummyRequest.getCaptchaAnswer())){
            Dummy dummy = new Dummy();
            dummy.setName(dummyRequest.getName());
            return dummyService.createDummy(dummy);
        }
        return null;
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
