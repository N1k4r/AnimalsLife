package com.example.project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class Controller {
    @Getter @Setter
    public static Controller controller;
    @FXML
    private TextArea actionTextArea;

    @FXML
    private TextArea countIteration;

    @FXML
    private TextArea countPopulationTextArea;

    @FXML
    private TextField fieldHeight;

    @FXML
    private TextField fieldLength;

    @FXML
    private TextArea fieldTextArea;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Label notificationLabel;

    @FXML
    private TextArea populationTextArea;

    @FXML
    private AnchorPane startConfigWindow;

    @FXML
    private VBox vBox;


    @SneakyThrows
    public void setConfig() {
        if (correctFieldSizeConfig() && correctAnimalsConfig()){
            startConfigWindow.setVisible(false);
            Field.getField().setAnimalsOnField();
            new Start().start();
            Field.getField().runThreads();
        }
    }

    private boolean correctFieldSizeConfig(){
        if (!fieldHeight.getText().equals("") && !fieldLength.getText().equals("")) {
            if (fieldHeight.getText().matches("\\d+") && fieldLength.getText().matches("\\d+")) {
                int height = Integer.parseInt(fieldHeight.getText());
                int length = Integer.parseInt(fieldLength.getText());
                if (height < 10 || height > 100 || length < 10 || length > 100) {
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
        int selectedAnimal = 0;
        for (int i = 0; i < vBox.getChildren().size(); i++){
            HBox hBox = (HBox) vBox.getChildren().get(i);
            if (((CheckBox) hBox.getChildren().get(0)).isSelected()){
                TextField textField = (TextField) hBox.getChildren().get(1);
                if (!textField.getText().equals("")) {
                    if (textField.getText().matches("\\d+")) {
                        int count = Integer.parseInt(textField.getText());
                        if (count < 1 || count > 400) {
                            notificationLabel.setText(hBox.getChildren().get(0).getAccessibleHelp() + " invalid number");
                            return false;
                        }
                        selectedAnimal++;
                        valuesConfig[i+1].setOccupancyRate(Integer.parseInt(textField.getText()));
                    } else {
                        notificationLabel.setText(hBox.getChildren().get(0).getAccessibleHelp() + " invalid value");
                        return false;
                    }
                } else
                    selectedAnimal++;
            } else
                valuesConfig[i+1].setOccupancyRate(0);
        }
        if (selectedAnimal == 0){
            notificationLabel.setText("Selected 0 animal");
            return false;
        }
        return true;
    }

    class Start extends Thread{
        @SneakyThrows
        @Override
        public void run() {
            int i = 0;
            while(true){
                synchronized (CountDownLatch.getCountDownLatch()){
                    CountDownLatch.getCountDownLatch().wait();
                }
                fieldTextArea.setText(Field.getField().getGraphicsField());
                Field.getField().updateAnimalsOnCells();
                countIteration.setText(String.valueOf(i / 3));
                actionTextArea.setText(FieldService.action);
                countPopulationTextArea.setText(Field.getField().getNumberOfAnimals());
                infoTextArea.setText(Field.getField().getStatistic());
                populationTextArea.setText(Field.getField().getTypePopulation());
                sleep(5000);
                synchronized (Controller.this){
                    Controller.this.notifyAll();
                }
                i++;
            }
        }
    }
}