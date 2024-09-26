/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
@Component
@Qualifier("inMemoryBluePrintPersistence")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();

    public InMemoryBlueprintPersistence() {
        // load stub data
        Point[] pts = new Point[] { new Point(140, 140), new Point(115, 115) };
        Point[] pts1 = new Point[] { new Point(40, 40), new Point(15, 15) };
        Point[] pts2 = new Point[] { new Point(10, 10), new Point(51, 51) };
        Point[] pts3 = new Point[] { new Point(90, 90), new Point(30, 30) };

        Blueprint bp = new Blueprint("_authorname_", "_bpname_ ", pts);
        Blueprint bp1 = new Blueprint("tomas", "plano1 ", pts1);
        Blueprint bp2 = new Blueprint("tomas", "plano2 ", pts2);
        Blueprint bp3 = new Blueprint("camilo", "plano3 ", pts3);

        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);

    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        } else {
            blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        }
    }

    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        Set<Blueprint> authorBlueprints = new HashSet<>();
        for (Tuple<String, String> key : blueprints.keySet()) {
            authorBlueprints.add(getBlueprint(key.o1, key.o2));
        }
        return authorBlueprints;
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> authorBlueprints = new HashSet<>();
        for (Tuple<String, String> key : blueprints.keySet()) {
            if (author.equals(key.o1)) {
                authorBlueprints.add(getBlueprint(key.o1, key.o2));
            }
        }
        return authorBlueprints;
    }

}