//Imports
import enums.DeleteAlertStates;
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class CheckLimitFunctionality implements FsmModel{

    private PostAlertStates state = PostAlertStates.INITIAL_STATE;
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
        state = PostAlertStates.INITIAL_STATE;
    }

    public boolean checkLimitGuard(){
        return getState().equals(PostAlertStates.INITIAL_STATE);
    }

    public @Action void checkLimit() throws Exception {
        state = PostAlertStates.CHECKLIMIT;
        assertFalse(SUT.checkLimit());
    }

    public boolean checkOverLimitGuard(){
        return getState().equals(PostAlertStates.INITIAL_STATE);
    }

    public @Action void checkOverLimit() throws Exception {
        state = PostAlertStates.CHECKLIMIT;
        assertTrue(SUT.checkLimit());
    }

    public void CheckLimitRunner(){
CheckLimitFunctionality model = new CheckLimitFunctionality();
        GreedyTester tester = new GreedyTester(model);
        tester.setRandom(new Random(1));
        tester.addListener(new StopOnFailureListener());
        tester.addListener(new StateCoverage());
        tester.addListener(new ActionCoverage());
        tester.addListener(new TransitionPairCoverage());
        tester.buildGraph();
        tester.generate(100);
        tester.printCoverage();
    }
}
