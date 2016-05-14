package Calculators.Electronics.Basic.StandardResistanceFinder;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;

public class StandardResistanceFinderModelTest extends ApplicationTest {

    StandardResistanceFinderCalcModel standardResistanceFinderCalcModel;

    @Override
    public void start(Stage stage) {
        standardResistanceFinderCalcModel =  new StandardResistanceFinderCalcModel();
        Scene scene = new Scene(standardResistanceFinderCalcModel.view, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

//    @Test
//    public void e12SeriesTest10k0() {
//        standardResistanceFinderCalcModel.desiredResistance.setRawVal(10.1);
//        assertEquals("10", standardResistanceFinderCalcModel.e12Resistance.getDispValAsString());
//        assertEquals("0.99", standardResistanceFinderCalcModel.e12Error.getDispValAsString());
//    }
//
//    @Test
//    public void e96SeriesTest246k() {
//        standardResistanceFinderCalcModel.desiredResistance.setRawVal(246);
//        assertEquals("249", standardResistanceFinderCalcModel.e96Resistance.getDispValAsString());
//        assertEquals("1.22", standardResistanceFinderCalcModel.e96Error.getDispValAsString());
//    }

}