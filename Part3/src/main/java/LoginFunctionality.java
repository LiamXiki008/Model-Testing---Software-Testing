import enums.LoginStates;
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

public class LoginFunctionality implements FsmModel {

    private SUT systemUnderTest = new SUT();

    private LoginStates state = LoginStates.INITIAL_STATE;
    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void reset(boolean reset) {
        if(reset){
            systemUnderTest = new SUT();
        }
        state = LoginStates.INITIAL_STATE;
    }

    //Transitions
    public boolean loginFunctionGuard(){
        System.out.println("Login Successful");
        return getState().equals(LoginStates.VALIDLOGIN);
    }
    public @Action void loginFunction() {
        System.out.println("Viewing Alerts");
        state = LoginStates.VIEWALERTS;
        assertTrue(SUT.LoginFunction(true));
    }

    public boolean loginFailedFunction(){
        System.out.println("Login Failed");
        return getState().equals(LoginStates.INVALIDLOGIN);
    }
    public @Action void loginFunctionFailed(){
        System.out.println("Login Failed");
        assertFalse(SUT.LoginFunction(false));
    }

    public boolean logoutFromAlertScreen(){
        return  getState().equals(LoginStates.VIEWALERTS);
    }
    public @Action void logoutFunction(){
        state = LoginStates.INITIAL_STATE;
        assertTrue(SUT.LogoutFunction());
    }

    @Test
    public void LoginTesterRunner() {
        final GreedyTester tester = new GreedyTester(new LoginFunctionality()); //Creates a test generator that can generate random walks. A greedy random walk gives preference to transitions that have never been taken before. Once all transitions out of a state have been taken, it behaves the same as a random walk.
        tester.setRandom(new Random()); //Allows for a random path each time the model is run.
        tester.buildGraph(); //Builds a model of our FSM to ensure that the coverage metrics are correct.
        tester.addListener(new StopOnFailureListener()); //This listener forces the test class to stop running as soon as a failure is encountered in the model.
        tester.addListener("verbose"); //This gives you printed statements of the transitions being performed along with the source and destination states.
        tester.addCoverageMetric(new TransitionPairCoverage()); //Records the transition pair coverage i.e. the number of paired transitions traversed during the execution of the test.
        tester.addCoverageMetric(new StateCoverage()); //Records the state coverage i.e. the number of states which have been visited during the execution of the test.
        tester.addCoverageMetric(new ActionCoverage()); //Records the number of @Action methods which have ben executed during the execution of the test.
        tester.generate(5); //Generates 500 transitions
        tester.printCoverage(); //Prints the coverage metrics specified above.
    }
}
