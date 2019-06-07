package com.github.alarit.engine;

import com.github.alarit.enums.WfStatus;
import com.github.alarit.exception.InvalidWfException;
import com.github.alarit.model.Vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WfGraph {

    private Vertex root;
    private Vertex current;
    private boolean isValidated = false;
    private Map<Vertex, Vertex[]> vertices = new HashMap<>();

    private static final short SUCCESS = 0;
    private static final short FAIL = 1;
    private static final Logger LOGGER = Logger.getLogger(WfGraph.class.getName());

    public WfGraph(Vertex root) {
        this.vertices.put(root, new Vertex[2]);
        this.root = root;
        this.current = root;
    }

    public WfGraph moveToVertex(Vertex v) {
        addVertex(v);
        this.current = v;
        return this;
    }

    public WfGraph addPredicateSuccess(Vertex v) throws InvalidWfException {
        return addPredicate(v, SUCCESS);
    }

    public WfGraph addPredicateFail(Vertex v) throws InvalidWfException {
        return addPredicate(v, FAIL);
    }

    public void invoke() throws InvalidWfException {
        this.current = null;

        if (this.root == null) throw new InvalidWfException();

        if (!(this.isValidated || checkIsValid())) {
            throw new InvalidWfException("Wf is not valid");
        }

        Vertex v = root;
        while (!v.isLeaf()) {
            LOGGER.log(Level.INFO, "Testing vertex {0}", v.getName());

            if (WfStatus.SUCCESS.equals(v.test())) {
                v = this.vertices.get(v)[SUCCESS];
            } else {
                v = this.vertices.get(v)[FAIL];
            }
        }
        LOGGER.log(Level.INFO, "Performing leaf {0}", v.getName());
        v.perform();
    }

    private WfGraph addPredicate(Vertex v, short status) throws InvalidWfException {
        if (this.current == null) {
            throw new InvalidWfException("Current vertex pointer is null");
        }
        if (v == null) {
            throw new InvalidWfException("Vertex cannot be null");
        }

        this.isValidated = false;
        this.vertices.get(current)[status] = v;
        addVertex(v);
        return this;
    }

    private void addVertex(Vertex v) {
        if (!this.vertices.containsKey(v)) {
            this.vertices.put(v, new Vertex[2]);
        }
    }

    private boolean checkIsValid() {

        //Check there are no vertexes referencing null
        var validTmp = !this.vertices
                .keySet()
                .stream()
                .filter(v -> !v.isLeaf())
                .map(v -> this.vertices.get(v))
                .anyMatch(a -> a[FAIL] == null || a[SUCCESS] == null);

        this.isValidated = validTmp;
        return this.isValidated;
    }
}
