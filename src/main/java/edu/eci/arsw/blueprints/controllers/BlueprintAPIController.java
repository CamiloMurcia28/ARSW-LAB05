/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    @Qualifier(value = "BlueprintsServices")
    private BlueprintsServices bs;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getRecursosPlanos(){
        Set<Blueprint> data = bs.getAllBlueprints();
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(Exception.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> getRecursosPlanos(@PathVariable("author") String author) {
        try {
            Set<Blueprint> data = bs.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


@RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.GET)
public ResponseEntity<?> getRecursosPlanos(@PathVariable("author") String author, @PathVariable("bpname") String bpname) {
    try {
        Set<Blueprint> data = bs.getBlueprintNames(author, bpname);
        
        return new ResponseEntity<>(data, HttpStatus.OK);
    } catch (BlueprintNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception ex) {
        return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



}