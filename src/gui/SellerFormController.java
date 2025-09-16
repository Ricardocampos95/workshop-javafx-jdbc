package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import model.entites.Department;
import model.entites.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;


public class SellerFormController implements Initializable {

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker datePickerBirthDate;
	
	@FXML
	private TextField txtBaseSalaty;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDay;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	
	private Seller entity;
	private SellerService service;
	private DepartmentService departmentService;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null!");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null"); //verificação de injeção de dependencia
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object!", null, e.getMessage(), AlertType.ERROR);
		}
		
		
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}


	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldInteger(txtId);
	}
	
	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("Department Service was null!");
		}
		initializeComboBoxDepartment();
	}
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		txtBaseSalaty.setText(String.valueOf(entity.getBaseSalary()));
		datePickerBirthDate.setValue(entity.getBirthDate());
		comboBoxDepartment.setValue(entity.getDepartment());
		
		
	}
	
	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");
		obj.setId( Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("") ) {
			exception.addError("name", "Field can't be empty!");
		}
		obj.setName(txtName.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty!");
		}
		obj.setEmail(txtEmail.getText());
		
		if (txtBaseSalaty.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty!");
		}
		obj.setBaseSalary(Double.valueOf(txtBaseSalaty.getText()));
		
		
		obj.setBirthDate(datePickerBirthDate.getValue());
		
		if (comboBoxDepartment.getValue() == null) {
			exception.addError("department", "Department is null!");
		}
		
		obj.setDepartment(comboBoxDepartment.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
		
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void initializeComboBoxDepartment() {
	    if (departmentService == null) {
	        throw new IllegalStateException("DepartmentService was null");
	    }

	    ObservableList<Department> obsList = FXCollections.observableArrayList(departmentService.findAll());
	    comboBoxDepartment.setItems(obsList);

	    
	    comboBoxDepartment.setCellFactory(cb -> new ListCell<>() {
	        @Override
	        protected void updateItem(Department item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(empty ? "" : item.getName());
	        }
	    });

	    comboBoxDepartment.setButtonCell(new ListCell<>() {
	        @Override
	        protected void updateItem(Department item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(empty ? "" : item.getName());
	        }
	    });
	}
	
	
	
	
	

}
