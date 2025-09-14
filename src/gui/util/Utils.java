package gui.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static <T> void formatTableColumnDate(TableColumn<T, LocalDate> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, LocalDate> cell = new TableCell<T, LocalDate>() {
				private DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);

				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(fmt.format(item));
					}
				}
			};

			return cell;
		});
	}

	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {

				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};

			return cell;
		});
	}

}
