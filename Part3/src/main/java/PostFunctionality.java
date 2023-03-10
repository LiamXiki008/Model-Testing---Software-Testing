//Imports
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


//Post Functionality class used to test the post functionality of the system
public class PostFunctionality implements FsmModel {
    //System Under Test
    private SUT systemUnderTest = new SUT();

    //State of the system
    private PostAlertStates state = PostAlertStates.INITIAL_STATE;
    @Override
    public Object getState() {
        return state;
    }

    //Reset the system
    @Override
    public void reset(boolean reset) {
        if(reset){
            systemUnderTest = new SUT();
        }
        state = PostAlertStates.INITIAL_STATE;
    }

    //Transitions
    public boolean SuccessfulPostGuard(){
        System.out.println("Successful Post");
        return getState().equals(PostAlertStates.INITIAL_STATE);
    }
    //Action to post successfully
    public @Action void ValidPostRequest() throws Exception {
        SUT.checkLimit();
        state = PostAlertStates.POSTALERT;
        System.out.println("Successful Post");
        assertTrue(SUT.PostRequest(true));
    }

    //Transitions
    public boolean FailedPostRequestGuard(){
        System.out.println("Failed Post");
        return getState().equals(PostAlertStates.INITIAL_STATE);
    }
    //Action to post unsuccessfully
    public @Action void FailedPost() throws Exception {
        System.out.println("Failed Post");
        state = PostAlertStates.BADSTATE;
        assertFalse(SUT.PostRequest(false));
    }

    //Testing the View Alerts functionality Test Runner
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
        tester.generate(100);
        tester.printCoverage();
    }
}
