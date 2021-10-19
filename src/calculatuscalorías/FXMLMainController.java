package calculatuscalorías;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

/**
 * FXML Controller class
 *
 * @author Antonio de Jesús Domínguez García
 */
public class FXMLMainController implements Initializable {

    @FXML
    private JFXComboBox<String> comboBoxGender;

    @FXML
    private RadioButton radioButtonLittleActivity;

    @FXML
    private RadioButton radioButtonLightExercise;

    @FXML
    private RadioButton radioButtonModerateExercise;

    @FXML
    private RadioButton radioButtonRegularSport;

    @FXML
    private RadioButton radioButtonEliteAthlete;

    @FXML
    private JFXComboBox<String> comboBoxAge;

    @FXML
    private JFXTextField textFieldHeight;

    @FXML
    private JFXTextField textFieldWeight;

    @FXML
    private JFXButton buttonCalculate;

    @FXML
    private Label labelResult;

    @FXML
    private Label labelHeightInvalid;

    @FXML
    private Label labelWeitghInvalid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validateTextFieldsFormat();
        ObservableList<String> ageList = FXCollections.observableArrayList();
        for (int i = 0; i <= 120; i++) {
            ageList.add("" + i);
        }
        comboBoxAge.setItems(ageList);
        comboBoxAge.getSelectionModel().select(0);
        ObservableList<String> genderList = FXCollections.observableArrayList("Femenino", "Masculino");
        comboBoxGender.setItems(genderList);
        comboBoxGender.getSelectionModel().select(0);

        final String WEIGHT_PATTERN = "^[0-9]*([.][0-9]+)?$";
        final RegexValidator WEIGHT_VALIDATOR = new RegexValidator();
        WEIGHT_VALIDATOR.setRegexPattern(WEIGHT_PATTERN);

        textFieldWeight.getValidators().add(WEIGHT_VALIDATOR);

    }

    public void validateTextFieldsFormat() {

        textFieldHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldHeight.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        textFieldWeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d+(.\\d+)")) {
                    textFieldWeight.setText(newValue.replaceAll("[^\\d+(.\\d+)]", ""));
                }
            }
        });

    }

    @FXML
    public void calculateTMB(ActionEvent event) {
        cleanLabels();
        String heightText = textFieldHeight.getText();
        String weightText = textFieldWeight.getText();
        String ageText = comboBoxAge.getSelectionModel().selectedItemProperty().getValue();
        if (!heightText.equals("") && !weightText.equals("")) {
            boolean correctWeight = textFieldWeight.validate();
            if (correctWeight) {
                int height = Integer.parseInt(heightText);
                float weight = Float.parseFloat(weightText);
                int age = Integer.parseInt(ageText);
                validateData(height, weight, age);
            } else {
                labelWeitghInvalid.setText("* Ingresa un formato correcto ");
            }
        } else if (heightText.equals("") && weightText.equals("")) {
            labelHeightInvalid.setText("* Por favor llene este campo");
            labelWeitghInvalid.setText("* Por favor llene este campo");
        } else if (heightText.equals("")) {
            labelHeightInvalid.setText("* Por favor llene este campo");
        } else if (weightText.equals("")) {
            labelWeitghInvalid.setText("* Por favor llene este campo");
        }
    }

    public void cleanLabels() {
        labelHeightInvalid.setText("");
        labelWeitghInvalid.setText("");
    }

    public void validateData(int height, float weight, int age) {
        boolean validHeight = validateHeight(height);
        boolean validWeight = validateWeitgh(weight);
        if (validHeight && validWeight) {
            calculateTMBByGender(height, weight, age);
        }
    }

    public boolean validateHeight(int height) {
        boolean validHeight = false;
        if (height >= 20 && height <= 250) {
            validHeight = true;
        } else if (height < 20) {
            labelHeightInvalid.setText("* La altura mínima es de 20 cm");
        } else if (height > 250) {
            labelHeightInvalid.setText("* Ha exedido la altura máxima (250 cm)");
        }
        return validHeight;
    }

    public boolean validateWeitgh(float weight) {
        boolean validWeight = false;
        if (weight >= 0.25 && weight <= 600) {
            validWeight = true;
        } else if (weight < 0.25) {
            labelWeitghInvalid.setText("* El peso minimo es de 0.25 kgs");
        } else if (weight > 600) {
            labelWeitghInvalid.setText("* Ha exedido el peso máximo (600 kgs)");
        }
        return validWeight;
    }

    public void calculateTMBByGender(int height, float weight, int age) {
        double tmb = (weight * 10) + (height * 6.25) * (age * 5);
        String gender = comboBoxGender.getSelectionModel().getSelectedItem();
        if (gender.equals("Femenino")) {
            tmb = tmb - 161;
        } else {
            tmb = tmb + 5;
        }
        adjustTMBByPhysicalActivity(tmb);
    }

    public void adjustTMBByPhysicalActivity(double tmb) {
        if (radioButtonLittleActivity.isSelected()) {
            tmb = tmb * 1.2;
        } else if (radioButtonLightExercise.isSelected()) {
            tmb = tmb * 1.375;
        } else if (radioButtonModerateExercise.isSelected()) {
            tmb = tmb * 1.55;
        } else if (radioButtonRegularSport.isSelected()) {
            tmb = tmb * 1.725;
        } else if (radioButtonEliteAthlete.isSelected()) {
            tmb = tmb * 1.9;
        }
        labelResult.setText("" + tmb);
    }

    @FXML
    public void selectRadioButtonEliteAthlete(ActionEvent event) {
        cleanRadioButtonSelection();
        radioButtonEliteAthlete.setSelected(true);
    }

    @FXML
    public void selectRadioButtonLightExercise(ActionEvent event) {
        cleanRadioButtonSelection();
        radioButtonLightExercise.setSelected(true);
    }

    @FXML
    public void selectRadioButtonLittleAcivity(ActionEvent event) {
        cleanRadioButtonSelection();
        radioButtonLittleActivity.setSelected(true);
    }

    @FXML
    public void selectRadioButtonModerateExercise(ActionEvent event) {
        cleanRadioButtonSelection();
        radioButtonModerateExercise.setSelected(true);
    }

    @FXML
    public void selectRadioButtonRegularSport(ActionEvent event) {
        cleanRadioButtonSelection();
        radioButtonRegularSport.setSelected(true);
    }

    public void cleanRadioButtonSelection() {
        radioButtonLittleActivity.setSelected(false);
        radioButtonLightExercise.setSelected(false);
        radioButtonModerateExercise.setSelected(false);
        radioButtonRegularSport.setSelected(false);
        radioButtonEliteAthlete.setSelected(false);
    }
}
