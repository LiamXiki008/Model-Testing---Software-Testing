import enums.PostAlertStates;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class PostFunctionality implements FsmModel {
    private SUT systemUnderTest = new SUT();

    private PostAlertStates state = PostAlertStates.INITIAL_STATE;
    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void reset(boolean b) {
        if(b){
            systemUnderTest = new SUT();
        }
        state = PostAlertStates.INITIAL_STATE;
    }

    public boolean SuccessfulPost(){
        state = PostAlertStates.POSTALERT;
        System.out.println("Successful Post");
        return getState().equals(PostAlertStates.INITIAL_STATE);
    }
    public @Action void ValidPostRequest() throws Exception {
        state = PostAlertStates.POSTALERT;
        System.out.println("Successful Post");
        assertTrue(SUT.PostRequest(true));
    }

    public boolean FailedPostRequest(){
        System.out.println("Failed Post");
        return getState().equals(PostAlertStates.INITIAL_STATE);
    }
    public @Action void FailedPost() throws Exception {
        System.out.println("Failed Post");
        state = PostAlertStates.BADSTATE;
        assertFalse(SUT.PostRequest(false));
    }

    @Test
    public void PostTesterRunner() {
        final GreedyTester tester = new GreedyTester(new PostFunctionality());
        tester.setRandom(new Random());
        tester.buildGraph();
        tester.addListener(new StopOnFailureListener());
        tester.addListener("verbose");
        tester.addCoverageMetric(new TransitionPairCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());
        tester.generate(5);
        tester.printCoverage();
    }
}
