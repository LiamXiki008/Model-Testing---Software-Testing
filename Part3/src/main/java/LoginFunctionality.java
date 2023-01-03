//Imports
import enums.LoginStates;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Assert;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

//Login Functionality class used to test the login functionality of the system
public class LoginFunctionality implements FsmModel {

    //State of the system
    private LoginStates state = LoginStates.INITIAL_STATE;
    @Override
    public Object getState() {
        return state;
    }

    //Reset the system
    @Override
    public void reset(boolean reset) {
        if(reset){
            //System Under Test
            SUT systemUnderTest = new SUT();
        }
        state = LoginStates.INITIAL_STATE;
    }

    //Transitions
    public boolean successfulLoginGuard(){
        System.out.println("Login Successful");
        return getState().equals(LoginStates.VALIDLOGIN);
    }
    //Action to login successfully
    public @Action void loginFunction() {
        System.out.println("Viewing Alerts");
        state = LoginStates.VIEWALERTS;
        assertTrue(SUT.LoginFunction(true));
        assertTrue(SUT.viewAlerts(true));
    }

    //Transitions
    public boolean loginFailedFunctionGuard(){
        System.out.println("Login Failed");
        return getState().equals(LoginStates.INVALIDLOGIN);
    }
    //Action to login unsuccessfully
    public @Action void loginFunctionFailed(){
        System.out.println("Login Failed");
        assertFalse(SUT.LoginFunction(false));
    }

    //Testing the View Alerts functionality
    public boolean logoutFromAlertScreen(){
        return  getState().equals(LoginStates.VIEWALERTS);
    }
    public @Action void logoutFunction(){
        state = LoginStates.INITIAL_STATE;
        assertTrue(SUT.LogoutFunction());
        assertFalse(SUT.viewAlerts(false));
    }

    //Testing the View Alerts functionality Test Runner
    public @Action void viewAlertsFunction(){
        boolean loggedIn = SUT.isLoggedIn();
        state = LoginStates.VIEWALERTS;
        assertTrue(SUT.viewAlerts(loggedIn));
    }

    //Test Runner
    @Test
    public void LoginTesterRunner() {
        final GreedyTester tester = new GreedyTester(new LoginFunctionality()); //Creates a test generator that can generate random walks. A greedy random walk gives preference to transitions that have never been taken before. Once all transitions out of a state have been taken, it behaves the same as a random walk.
        tester.setRandom(new Random()); //Allows for a random path each time the model is run.
        tester.buildGraph(); //Builds a model of our FSM to ensure that the coverage metrics are correct.
        tester.addListener(new StopOnFailureListener()); //This listener forces the test class to stop running as soon as a failure is encountered in the model.
        tester.addListener("verbose"); //This gives you printed statements of the transitions being performed along with the source and destination states.
        tester.addCoverageMetric(new TransitionPairCoverage()); //Records the transition pair coverage i.e. the number of paired transitions traversed during the execution of the test.
        tester.addCoverageMetric(new StateCoverage()); //Records the state coverage i.e. the number of states which have been visited during the execution of the test.
        tester.addCoverageMetric(new ActionCoverage()); //Records the number of @Action methods which have been executed during the execution of the test.
        tester.generate(100); //Generates transitions
        tester.printCoverage(); //Prints the coverage metrics specified above.
    }
}
