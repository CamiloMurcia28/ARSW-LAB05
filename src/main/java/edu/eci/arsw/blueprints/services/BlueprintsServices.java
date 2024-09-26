/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import edu.eci.arsw.blueprints.persistence.Filter;
/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {
   
    @Autowired
    @Qualifier(value = "inMemoryBluePrintPersistence")
    BlueprintsPersistence bpp;

    @Autowired
    FilterServices filter;
    
    public void addNewBlueprint(Blueprint bp){
        try {
            bpp.saveBlueprint(bp);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Adding Error.");
        }
        
    }
    
    public Set<Blueprint> getAllBlueprints(){
        Set<Blueprint> blueprints;
        try {
            blueprints = bpp.getAllBlueprints();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Getting Error.");
        }
        return blueprints;
    
    }
    
    /**
     * 
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
        Blueprint blueprint;
        blueprint = bpp.getBlueprint(author, name);
        filter.applyFilter(blueprint);
        return blueprint;
    }
    
    /**
     * 
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        Set<Blueprint> blueprints = bpp.getBlueprintsByAuthor(author);
        Set<Blueprint> blueprintsFiltered = new HashSet<>();
        for(Blueprint bp: blueprints){
            filter.applyFilter(bp);
            blueprintsFiltered.add(bp);
        }
        return blueprintsFiltered;
    }
        
}
    

