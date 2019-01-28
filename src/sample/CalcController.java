package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class CalcController implements Initializable {

    // declaring stuff
    @FXML
    private JFXTextField height;

    @FXML
    private JFXTextField weight;

    @FXML
    private JFXRadioButton male;

    @FXML
    private JFXRadioButton female;

    @FXML
    private JFXButton calc;

    @FXML
    private Label bmi;

    @FXML
    private Label status;

    @FXML
    private Label ideal_weight;

    private boolean gender = false;

    // this function runs at the start of the program before everything else (to initialize the validators etc)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // disable calculate button until all 3 fields are filled
        BooleanBinding isData = height.textProperty().isEmpty().or(weight.textProperty().isEmpty());
        calc.disableProperty().bind(isData);

        // validators to make sure height and weight fields are not empty and only numbers.
        NumberValidator numberValidator = new NumberValidator();
        height.getValidators().add(numberValidator); // add the number validator to filed validators
        weight.getValidators().add(numberValidator); // add the number validator to filed validators
        numberValidator.setStyle("-fx-font-size: 8px;"); // set text size via css (so it wont look oversized)
        numberValidator.setMessage("only numbers!"); // error message to show if the text doesn't meet the requirements
        // add listeners to each text field focus property.
        height.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            // only validate if new value doesn't meet the requirements or is empty
            if (!newValue){
                height.validate();
                if (!calc.isDisable()) calc.setDisable(true); // disable calculate button if the values are not numbers
            }
        });
        height.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            // only validate if new value doesn't meet the requirements or is empty
            if (!newValue){
                height.validate();
                if (!calc.isDisable()) calc.setDisable(true); // disable calculate button if the values are not numbers
            }
        });
    }

    @FXML
    void calcBMI() {
        DecimalFormat df = new DecimalFormat("#.00"); // for formatting
        double cm = Double.valueOf(height.getText()) / 100; // convert height to cm first
        double calculated = Double.valueOf(weight.getText()) / (cm * cm); // calculate the BMI

        // set the status label text based on BMI
        if (calculated >= 40) status.setText("Obese Class 3");
        else if (calculated >= 35 && calculated <= 40) status.setText("Obese Class 2");
        else if (calculated >= 30 && calculated <= 35) status.setText("Obese Class 1");
        else if (calculated >= 25 && calculated <= 30) status.setText("Overweight");
        else if (calculated >= 18.5 && calculated <= 25) status.setText("Healthy");
        else if (calculated >= 17 && calculated <= 18.5) status.setText("Mild Thinness");
        else if (calculated >= 16 && calculated <= 17) status.setText("Moderate Thinness");
        else if (calculated <= 16) status.setText("Severe Thinness");

        // calculate ideal weight based on gender, height and Robinson's formula(1983) https://www.calculator.net/ideal-weight-calculator.html
        // not quiet perfect but will be enough for now
        double ideal = 0;
        if (gender){
            ideal = 48.0;
            if (Integer.valueOf(height.getText()) > 153){
                do {
                    ideal += 2.7;
                } while(ideal + 2.54 < (Integer.valueOf(height.getText()) - 100));
            }
        }
        if (!gender){
            ideal = 45.5;
            if (Integer.valueOf(height.getText()) > 153){
                do {
                    ideal += 2.2;
                } while(ideal + 2.54 < (Integer.valueOf(height.getText()) - 100));
            }
            ideal = ideal - 10; // different measures for females
        }

        bmi.setText(df.format(calculated) + "kg/m^2"); // set bmi label text
        ideal_weight.setText(df.format(ideal)+ "kg"); // set ideal weight label text

        // show all 3 labels
        bmi.setVisible(true);
        status.setVisible(true);
        ideal_weight.setVisible(true);
    }

    // set the gender variable and disable the other radio button
    @FXML
    void handleFemale() {
        if (female.isSelected()){
            male.setSelected(false);
            gender = false;
        }
    }

    // set the gender variable and disable the other radio button
    @FXML
    void handleMale() {
        if (male.isSelected()){
            female.setSelected(false);
            gender = true;
        }
    }

}
