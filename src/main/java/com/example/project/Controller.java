package com.example.project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleRole;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @Getter @Setter
    public static Controller controller;
    @Getter
    public static boolean finishProcess = false;

    @FXML
    private TextField actionTextField;

    @FXML
    private AnchorPane allTextAnchorPane;

    @FXML
    private TextField countIterationTextField;

    @FXML
    private TextField countPopulationTextField;

    @FXML
    private TextField fieldHeight;

    @FXML
    private TextField fieldLength;

    @FXML
    private TextArea fieldTextArea;

    @FXML
    private Button finishButton;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Label notificationLabel;

    @FXML
    private TextArea populationTextArea;

    @FXML
    private AnchorPane startConfigWindow;

    @FXML
    private Spinner<Integer> spinner = new Spinner<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10);
        spinnerValueFactory.setValue(3);
        spinner.setValueFactory(spinnerValueFactory);
    }

    @FXML
    private VBox vBox;

    @SneakyThrows
    public void setConfig() {
        if (correctFieldSizeConfig() && correctAnimalsConfig()){
            new Thread(new ThreadController()).start();
            startConfigWindow.setVisible(false);
            Thread.sleep(1000);
            Field.getField().initializationField();
            Field.getField().runThreads();
            finishButton.setVisible(true);
            notificationLabel.setText("");
        }
    }

    private boolean correctFieldSizeConfig(){
        if (!fieldHeight.getText().equals("") && !fieldLength.getText().equals("")) {
            if (fieldHeight.getText().matches("\\d+") && fieldLength.getText().matches("\\d+")) {
                int height = Integer.parseInt(fieldHeight.getText());
                int length = Integer.parseInt(fieldLength.getText());
                if (height * length < 100 || height * length > 2000) {
                    notificationLabel.setText("Invalid field size");
                    return false;
                }
                Config.FIELD_HEIGHT.setValue(height);
                Config.FIELD_LENGTH.setValue(length);
                Config.FIELD_SIZE.updateSize();
            } else {
                notificationLabel.setText("Invalid field value");
                return false;
            }
        }
        return true;
    }

    private boolean correctAnimalsConfig(){
        Config[] valuesConfig = Config.values();
        Type[] types = Type.values();
        int selectedAnimal = 0;
        for (int i = 0; i < vBox.getChildren().size(); i++){
            HBox hBox = (HBox) vBox.getChildren().get(i);
            if (((CheckBox) hBox.getChildren().get(0)).isSelected()){
                TextField textField = (TextField) hBox.getChildren().get(1);
                if (!textField.getText().equals("")) {
                    if (textField.getText().matches("\\d+")) {
                        int count = Integer.parseInt(textField.getText());
                        if (count < 1 || count > 100) {
                            notificationLabel.setText(hBox.getChildren().get(0).getAccessibleHelp() + " invalid number");
                            return false;
                        }
                        selectedAnimal++;
                        types[i+1].setOccupancy(Integer.parseInt(textField.getText()));
                    } else {
                        notificationLabel.setText(hBox.getChildren().get(0).getAccessibleHelp() + " invalid value");
                        return false;
                    }
                } else{
                    types[i+1].setOccupancy(valuesConfig[i].getOccupancyRate());
                    selectedAnimal++;
                }
            } else
                types[i+1].setOccupancy(0);
        }
        if (selectedAnimal == 0){
            notificationLabel.setText("Selected 0 animal");
            return false;
        }
        return true;
    }

    public void selectAllCheckBox(){
        showHideCheckBoxSelection(true);
    }

    public void deselectAllCheckBox(){
        showHideCheckBoxSelection(false);
    }

    public void showHideCheckBoxSelection(boolean value){
        for (int i = 0; i < vBox.getChildren().size(); i++) {
            HBox hBox = (HBox) vBox.getChildren().get(i);
            ((CheckBox) hBox.getChildren().get(0)).setSelected(value);
        }
    }

    public void finishProcess() {
        finishProcess = true;
        countPopulationTextField.setText("wait...");
    }

    public void startNewProcess(){
        Field.getField().getThreadGroup().interrupt();
        allTextAnchorPane.getChildren().forEach(x -> {
            if (x.getAccessibleRole().equals(AccessibleRole.TEXT_FIELD))
                ((TextField) x).clear();
            else
                ((TextArea) x).clear();
        });
        finishButton.setVisible(false);
        startConfigWindow.setVisible(true);
        finishProcess = false;
    }

    public void clearConfigValue() {
        for (int i = 0; i < vBox.getChildren().size(); i++) {
            HBox hBox = (HBox) vBox.getChildren().get(i);
            ((TextField) hBox.getChildren().get(1)).clear();
        }
    }

    class ThreadController implements Runnable{
        @SneakyThrows
        @Override
        public void run() {
            String[] stringAction = new String[]{"Move", "Eat", "Reproduce"};
            synchronized (Field.getField()){
                Field.getField().wait();
            }
            int i = 0;
            while(Field.getField().getAnimalsList().size() != 0 && !finishProcess){
                synchronized (CountDownLatch.getCountDownLatch()){
                    CountDownLatch.getCountDownLatch().wait();
                }
                fieldTextArea.setText(Field.getField().getGraphicsField());
                Field.getField().updateAnimalsOnCells();
                countIterationTextField.setText(String.valueOf(i / 3));
                actionTextField.setText(stringAction[i % 3]);
                countPopulationTextField.setText(Field.getField().getNumberOfAnimals());
                infoTextArea.setText(Field.getField().getStatistic());
                populationTextArea.setText(Field.getField().getTypePopulation());
                Thread.sleep(spinner.getValue() * 1000);
                synchronized (Controller.this){
                    Controller.this.notifyAll();
                }
                i++;
            }
            populationTextArea.setText(!finishProcess ? "All the animals died" : "");
            startNewProcess();
        }
    }
}