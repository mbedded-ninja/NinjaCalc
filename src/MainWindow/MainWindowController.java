package MainWindow;

import Core.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;

import CalculatorGridElement.CalculatorGridElementController;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindowController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane calcGridOverlay;

    @FXML
    private TilePane calculatorGridTilePane;

    private ArrayList<Calculator> calculatorTemplates;

    public void handleButtonOnAction(ActionEvent actionEvent) {
        //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "blah");
        //alert.showAndWait();

        // Show the calculator grid overlay
        calcGridOverlay.setVisible(true);

    }

    public void loadCalculatorTemplate(Calculator calculator) {
        System.out.println("loadCalculatorTemplate() called.");

        CalculatorGridElementController gridElement =
                new CalculatorGridElementController(calculator.Name, this::openCalculatorButtonPressed);

        // Add new tile to tile pane in main window
        calculatorGridTilePane.getChildren().add(gridElement);

    }

    public void openCalculatorButtonPressed(String calculatorName) {
        System.out.println("openCalculatorButtonPressed() called, with calculatorName = \"" + calculatorName +  "\" .");
        //calculatorTemplates.add(calculator);
        // Create a new FXML loader, pointing to the provided fxml path
        /*FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        try {
            GridPane root = loader.load();
            //calculatorGridTilePane.getChildren().add(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    /*private Pane getOverlay() {
        StackPane p = new StackPane();
        Rectangle r = RectangleBuilder.create()
                .height(100).width(100)
                .arcHeight(40).arcWidth(40)
                .stroke(Color.RED)
                .fill(Color.web("red", 0.1))
                .build();

        Text txt= TextBuilder.create().text("Overlay")
                .font(Font.font("Arial", FontWeight.BOLD, 18))
                .fill(Color.BLUE)
                .build();
        p.getChildren().addAll(r, txt);
        return p;
    }*/
}
