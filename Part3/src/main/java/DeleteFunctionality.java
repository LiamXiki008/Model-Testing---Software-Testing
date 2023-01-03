//Imports
import enums.DeleteAlertStates;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertEquals;

//Delete Functionality class used to test the delete functionality of the system
public class DeleteFunctionality implements FsmModel {

    //State of the system
    private DeleteAlertStates state = DeleteAlertStates.INITIAL_STATE;
    @Override
    public Object getState() {
        return state;
    }

    //Reset the system
    @Override
    public void reset(boolean reset) {
        if(reset){
            //System Under Test
            SUT sut = new SUT();
        }
        state = DeleteAlertStates.INITIAL_STATE;
    }

    //Transitions
    public boolean DeleteAlertsGuard(){
        System.out.println("Alerts Deleted");
        return getState().equals(DeleteAlertStates.INITIAL_STATE);
    }
    //Action to delete alerts
    public @Action void DeleteAlerts() {
        System.out.println("Successful delete request");
        //state = DeleteAlertStates.DELETEALERTS;
        assertEquals(200,SUT.DeleteRequest("aba2df1c-5441-4581-9dc2-5413c9691825"));
       // state = DeleteAlertStates.INITIAL_STATE;
    }

    //Transitions
    public boolean InvalidDeleteGuard(){
        System.out.println("Invalid Delete");
        return getState().equals(DeleteAlertStates.INITIAL_STATE);
    }
    //Action to delete unsuccessfully
    public @Action void InvalidDelete(){
        state = DeleteAlertStates.BADSTATE;
        System.out.println("Failed Delete Request");
        assertEquals(400,SUT.DeleteRequest("InvalidId"));
    }

    //Test the system
    @Test
    public void DeleteTesterRunner() {
        final GreedyTester tester = new GreedyTester(new DeleteFunctionality());
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
