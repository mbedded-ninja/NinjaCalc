package Core.View.Dimension;


import java.io.IOException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class Dimension extends Pane {

    private Node view;
    private DimensionController controller;

    public Dimension() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dimension.fxml"));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return controller = new DimensionController();
            }
        });
        try {
            view = (Node) fxmlLoader.load();

        } catch (IOException ex) {
        }
        getChildren().add(view);
    }

    public void setLength(Double length) {

        // Set the length of the main line
        controller.mainLine.setStartX(0);
        controller.mainLine.setStartY(0);

        controller.mainLine.setEndX(length);
        controller.mainLine.setEndY(0);

        controller.leftTopArrowLine.setStartX(0);
        controller.leftTopArrowLine.setStartY(0);

        controller.leftTopArrowLine.setEndX(10);
        controller.leftTopArrowLine.setEndY(-10);

        controller.leftBotArrowLine.setStartX(0);
        controller.leftBotArrowLine.setStartY(0);

        controller.leftBotArrowLine.setEndX(10);
        controller.leftBotArrowLine.setEndY(10);

        controller.rightTopArrowLine.setStartX(length);
        controller.rightTopArrowLine.setStartY(0);

        controller.rightTopArrowLine.setEndX(length - 10);
        controller.rightTopArrowLine.setEndY(-10);

        controller.rightBotArrowLine.setStartX(length);
        controller.rightBotArrowLine.setStartY(0);

        controller.rightBotArrowLine.setEndX(length - 10);
        controller.rightBotArrowLine.setEndY(10);

    }

    public Double getLength() {
        return controller.mainLine.getEndX();
    }

    public DoubleProperty lengthProperty() {
        return controller.mainLine.endXProperty();
    }
}