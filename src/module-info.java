module workshop_javafx_jdbc {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;

	opens application to javafx.fxml;
	opens gui to javafx.fxml;

	exports application;
	exports gui;
}
