package gui.util;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	public static <T> void loadViewInAnchorPane(String fxmlPath, AnchorPane container, Consumer<T> initAction) {
	    try {
	        FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlPath));
	        Parent newPane = loader.load();

	        container.getChildren().setAll(newPane);

	        T controller = loader.getController();
	        initAction.accept(controller);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
