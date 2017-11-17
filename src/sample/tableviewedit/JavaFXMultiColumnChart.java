package sample.tableviewedit;


import javafx.application.Application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class JavaFXMultiColumnChart extends Application {

    public class Record {
        private SimpleStringProperty fieldMonth;
        private SimpleDoubleProperty fieldValue1;
        private SimpleDoubleProperty fieldValue2;

        Record(String fMonth, double fValue1, double fValue2) {
            this.fieldMonth = new SimpleStringProperty(fMonth);
            this.fieldValue1 = new SimpleDoubleProperty(fValue1);
            this.fieldValue2 = new SimpleDoubleProperty(fValue2);
        }

        public void setFieldMonth(String fMonth) {
            fieldMonth.set(fMonth);
        }

        void setFieldValue1(Double fValue1) {
            fieldValue1.set(fValue1);
        }

        void setFieldValue2(Double fValue2) {
            fieldValue2.set(fValue2);
        }
    }

    private TableView<Record> tableView = new TableView<>();

    private ObservableList<Record> dataList =
            FXCollections.observableArrayList(
                    new Record("January", 100, 120),
                    new Record("February", 200, 210),
                    new Record("March", 50, 70),
                    new Record("April", 75, 50),
                    new Record("May", 110, 120),
                    new Record("June", 300, 200),
                    new Record("July", 111, 100),
                    new Record("August", 30, 50),
                    new Record("September", 75, 70),
                    new Record("October", 55, 50),
                    new Record("November", 225, 225),
                    new Record("December", 99, 100));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("java-buddy.blogspot.com");

        Group root = new Group();

        tableView.setEditable(true);

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {

                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };

        TableColumn columnMonth = new TableColumn("Month");
        columnMonth.setCellValueFactory(
                new PropertyValueFactory<>("fieldMonth"));
        columnMonth.setMinWidth(60);

        TableColumn columnValue1 = new TableColumn("Value 1");
        columnValue1.setCellValueFactory(
                new PropertyValueFactory<Record, Double>("fieldValue1"));
        columnValue1.setMinWidth(60);

        TableColumn columnValue2 = new TableColumn("Value 2");
        columnValue2.setCellValueFactory(
                new PropertyValueFactory<Record, Double>("fieldValue2"));
        columnValue2.setMinWidth(60);

        //--- Add for Editable Cell of Value field, in Double
        columnValue1.setCellFactory(cellFactory);
        columnValue1.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Record, Double>>() {

                    @Override
                    public void handle(TableColumn.CellEditEvent<Record, Double> t) {
                        ((Record) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setFieldValue1(t.getNewValue());
                    }
                });

        columnValue2.setCellFactory(cellFactory);
        columnValue2.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Record, Double>>() {

                    @Override
                    public void handle(TableColumn.CellEditEvent<Record, Double> t) {
                        ((Record) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setFieldValue2(t.getNewValue());
                    }
                });

        //---
        tableView.setItems(dataList);
        tableView.getColumns().addAll(columnMonth, columnValue1, columnValue2);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().add(tableView);

        root.getChildren().add(vBox);

        primaryStage.setScene(new Scene(root, 300, 350));
        primaryStage.show();
    }

    class EditingCell extends TableCell<Record, Double> {
        private TextField textField;

        EditingCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }

                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed(t -> {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(Double.parseDouble(textField.getText()));
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

}