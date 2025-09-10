module workshop_javafx_jdbc {
	
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.sql;
	requires transitive javafx.controls;

	opens application to javafx.fxml;
	opens gui to javafx.fxml;

	exports application;
	exports gui;
	exports model.services;
	exports model.entites;

	
}
