package com.moviedb.controllers;

import com.moviedb.models.session.Session;
import com.moviedb.models.session.SessionDTO;
import com.moviedb.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/session")
public class SessionController {

    private SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/{id}")
    public SessionDTO listById(@PathVariable("id") String id){
        return sessionService.listById(id);
    }

    @GetMapping("/theater/{idTheater}")
    public List<Session> listByIdTheater(@PathVariable("idTheater") String idTheater){
        return sessionService.listByIdTheater(idTheater);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Session create(@Valid @RequestBody SessionDTO sessionDTO){
        return sessionService.create(sessionDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") String id, @Valid @RequestBody SessionDTO sessionDTO){
        sessionService.update(id, sessionDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        sessionService.delete(id);
    }

}
