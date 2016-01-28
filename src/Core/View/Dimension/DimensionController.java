package Core.View.Dimension;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;

public class DimensionController implements Initializable {

    @FXML
    Line mainLine;

    @FXML
    Line leftTopArrowLine;

    @FXML
    Line leftBotArrowLine;

    @FXML
    Line rightTopArrowLine;

    @FXML
    Line rightBotArrowLine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //textField.setText("Just click the button!");
    }
}