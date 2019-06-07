package com.github.alarit.engine;

import com.github.alarit.enums.WfStatus;
import com.github.alarit.exception.InvalidWfException;
import com.github.alarit.model.InnerVertex;
import com.github.alarit.model.OuterVertex;
import com.github.alarit.model.Vertex;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class WfGraphTest {

    private StringBuilder sb;
    private Vertex root;
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vertex v4;
    private Vertex v5;
    private Vertex v6;


    @Before
    public void setUp() {
        sb = new StringBuilder();

        root = new InnerVertex() {
            @Override
            public String getName() {
                return "Root";
            }

            @Override
            public WfStatus test() {
                sb.append(this.getName()).append("-");
                return WfStatus.SUCCESS;
            }
        };

        v1 = new InnerVertex() {

            @Override
            public String getName() {
                return "V1";
            }

            @Override
            public WfStatus test() {
                sb.append(this.getName()).append("-");
                return WfStatus.FAIL;
            }
        };

        v2 = new InnerVertex() {
            @Override
            public String getName() {
                return "V2";
            }

            @Override
            public WfStatus test() {
                sb.append(this.getName()).append("-");
                return WfStatus.SUCCESS;
            }
        };

        v3 = new OuterVertex() {

            @Override
            public String getName() {
                return "V3";
            }

            @Override
            public void perform() {
                sb.append(this.getName()).append("-");
            }
        };

        v4 = new OuterVertex() {

            @Override
            public String getName() {
                return "V4";
            }

            @Override
            public void perform() {
                sb.append(this.getName()).append("-");
            }
        };

        v5 = new OuterVertex() {

            @Override
            public String getName() {
                return "V5";
            }

            @Override
            public void perform() {
                sb.append(this.getName()).append("-");
            }
        };

        v6 = new OuterVertex() {

            @Override
            public String getName() {
                return "V6";
            }

            @Override
            public void perform() {
                sb.append(this.getName()).append("-");
            }
        };

    }

    @Test(expected = InvalidWfException.class)
    public void emptyRoot() throws InvalidWfException {
        new WfGraph(null).invoke();
    }

    @Test(expected = InvalidWfException.class)
    public void emptyPointer() throws InvalidWfException {
        new WfGraph(null).addPredicateSuccess(v1);
    }

    @Test(expected = InvalidWfException.class)
    public void emptyPredicate() throws InvalidWfException {
        new WfGraph(root).addPredicateSuccess(null);
    }

    @Test(expected = InvalidWfException.class)
    public void testFlowWithMissingPredicate() throws InvalidWfException {
        new WfGraph(root)
                .addPredicateSuccess(v1)
                .invoke();
    }

    @Test
    public void testNormalFlow() {
        try {
            new WfGraph(root)
                    .addPredicateSuccess(v1)
                    .addPredicateFail(v2)
                    .moveToVertex(v1)
                    .addPredicateSuccess(v3)
                    .addPredicateFail(v4)
                    .moveToVertex(v2)
                    .addPredicateSuccess(v5)
                    .addPredicateFail(v6)
                    .invoke();
            assertEquals("Root-V1-V4-", sb.toString());
        } catch (InvalidWfException e) {
            fail();
        }
    }

}
