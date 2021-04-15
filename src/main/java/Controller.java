import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import metrics.MetricExtractor;
import readers.ExelReader;
import rules.Metric;
import rules.Rule;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {

    private File projectDir; // Pasta do projeto java
    private File codeSmells; // CodeSmell selecionado

    @FXML private Text path; // Path que aparece o GUI após a seleção da pasta
    @FXML private Button createButton;
    @FXML private GridPane excelBox;

    /**
     Usar o método setText(String text) para mudar o texto no GUI
     EX, para mudar o número de packages para 15: packNum.setText("15 packages");
     */
    @FXML private Text packNum;
    @FXML private Text classNum;
    @FXML private Text methodNum;
    @FXML private Text locNum;

    //Rules Tab
    @FXML private ChoiceBox<Metric> metric1;
    @FXML private ChoiceBox<Rule.Operation> metric1op;
    @FXML private TextField metric1value;

    @FXML private ChoiceBox<Metric> metric2;
    @FXML private ChoiceBox<Rule.Operation> metric2op;
    @FXML private TextField metric2value;

    @FXML private ChoiceBox<Rule.LogicOp> rulelogic;
    @FXML private ChoiceBox<Rule.Smell> rulesmell;

    @FXML private ListView<Rule> listrules;

    @FXML private void initialize(){
        //Para quando o programa inicia
        clearGUIElements();

        //For Rules Tab
        metric1.getItems().setAll(Metric.values());
        metric1op.getItems().setAll(Rule.Operation.values());
        metric2.getItems().setAll(Metric.values());
        metric2op.getItems().setAll(Rule.Operation.values());
        rulelogic.getItems().setAll(Rule.LogicOp.values());
        rulesmell.getItems().setAll(Rule.Smell.values());
        ObservableList<Rule> regras = FXCollections.observableArrayList(Rule.DeserializedRule());
        listrules.setItems(regras);

       // /*
        //for updating rules when listview is clicked
        listrules.setOnMouseClicked(mouseEvent -> {
            Rule ruleSelected = listrules.getSelectionModel().getSelectedItem();
            metric1.setValue(ruleSelected.getMetric(ruleSelected.getRule1()));
            metric1op.setValue(ruleSelected.getMetricOperation(ruleSelected.getRule1()));
            metric1value.setText(ruleSelected.getMetricValue(ruleSelected.getRule1()).toString());

            metric2.setValue(ruleSelected.getMetric(ruleSelected.getRule2()));
            metric2op.setValue(ruleSelected.getMetricOperation(ruleSelected.getRule2()));
            metric2value.setText(ruleSelected.getMetricValue(ruleSelected.getRule2()).toString());

            rulelogic.setValue(ruleSelected.getOperation());
            rulesmell.setValue(ruleSelected.getSmell());
        });
      //   */

    }

    /*
        Função a executar quando se clica no botão "selecionar pasta" que abre um directory chooser e após seleção
        atualiza da variável "projectDir" e o valor "path" do GUI
    */
    @FXML private void openProject(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage();
        directoryChooser.setTitle("Escolher pasta do projeto");
        File dir = directoryChooser.showDialog(stage);

        projectDir = dir;
        path.setText(dir.getAbsolutePath());

        //If there is code smells in project
        String pathCodeSmell = projectDir.getAbsolutePath() + "/" + projectDir.getName() + "_metrics/" + projectDir.getName() + "_metrics.xlsx";

        if(Files.exists(new File(pathCodeSmell).toPath())) {
            updateGUIElements(pathCodeSmell);
        }else{
            clearGUIElements();
        }
    }

    // Função que será executada para criar o code smell excutada através do botão para tal
    @FXML private void createCodeSmell() throws IOException {
        try {
            String pathSave = projectDir.getAbsolutePath() + "/" + projectDir.getName() + "_metrics/";
            String pathCodeSmell = pathSave + projectDir.getName() + "_metrics.xlsx";

            // retirar criar um objeto para retirar as métricas
            MetricExtractor metrics = new MetricExtractor(projectDir.toPath());

            // criar code smells
            CodeSmells codeSmells = new CodeSmells(metrics, projectDir.getName());

            // criar a pasta onde se vai guardar o ficheiro xlsx
            try{
                Files.createDirectory(Paths.get(pathSave));
                codeSmells.createCodeSmellsXlsxFile(new File(pathSave));
            }catch (FileAlreadyExistsException e){
                System.out.println("A pasta já existe");
            }

            // guardar code smells
            codeSmells.addCodeSmellsToXlsx(new File(pathSave));

            updateGUIElements(pathCodeSmell);

            if(!createButton.getText().equals("Atualizar Code Smells")) createButton.setText("Atualizar Code Smells");

            showInformationMessage("Informação","Os code smells foram criados com sucesso.", Alert.AlertType.INFORMATION);

        }catch (NullPointerException e){
            showInformationMessage("Informação","Por favor selecione a pasta do projeto.", Alert.AlertType.INFORMATION);
        }

    }

    @FXML private void AddRuleToHistory(){
        if(metric1.getValue() == null || metric1op.getValue() == null || metric1value.getText().isEmpty() ||
                metric2.getValue() == null || metric2op.getValue() == null || metric2value.getText().isEmpty() ||
                rulelogic.getValue() == null || rulesmell.getValue() == null){

            showInformationMessage("Informação", "Certifique-se que todos os valores estão preenchidos", Alert.AlertType.INFORMATION);
        } else {
            Rule rule = new Rule(metric1.getValue(), metric1op.getValue(), Integer.parseInt(metric1value.getText()),
                    metric2.getValue(), metric2op.getValue(), Integer.parseInt(metric2value.getText()),
                    rulelogic.getValue(), rulesmell.getValue());

            ArrayList<Rule> rules2add = Rule.DeserializedRule();
            rules2add.add(rule);
            Rule.SerializeRule(rules2add);

            Rule.checkRulesInHistory();

            ObservableList<Rule> regras = FXCollections.observableArrayList(Rule.DeserializedRule());
            listrules.setItems(regras);
            showInformationMessage("Informação", "A regra foi adicionada com sucesso.", Alert.AlertType.INFORMATION);
        }
    }

    private void clearGUIElements(){
        excelBox.getChildren().clear();
        packNum.setText("");
        classNum.setText("");
        methodNum.setText("");
        locNum.setText("");
    }

    private void updateGUIElements(String pathCodeSmell){
        ExelReader exelReader = new ExelReader(pathCodeSmell);
        ArrayList<String> lines = exelReader.read();
        /*
         * TODO
         *   -Usar o ArrayList lines num método separado para obter as caracteristicas
         */
        writeCharacteristicsGUI(lines);
        writeCodeSmellsGUI(lines);

        createButton.setText("Atualizar Code Smells");
    }

    private void writeCharacteristicsGUI(ArrayList<String> lines) {
        int packNum = 0, classNum = 0, methodNum = 0, locNum = 0;
        ArrayList<String> packNames = new ArrayList<String>();
        String className = "";

        for(String l : lines){

            String[] line = l.split(";");

            if(!line[0].equals("MethodID")){

                methodNum++; //Adiciona sempre

                if(!packNames.contains(line[1]) && !line[1].equals("none")){ //Se for um package novo e não for o 'none'
                    packNum++;
                    packNames.add(line[1]);

                }

                if(!line[2].equals(className)) {//Se for uma classe nova
                    classNum++;
                    className = line[2];
                    locNum += Integer.parseInt(line[5]); //Vai buscar o valor do LOC_class
                }
            }
        }
        this.packNum.setText(packNum+" Packages");
        this.classNum.setText(classNum + " Classes");
        this.methodNum.setText(methodNum+ " Métodos");
        this.locNum.setText(locNum+ " Linhas de Código");
    }


    private void writeCodeSmellsGUI(ArrayList<String> lines){
        excelBox.getChildren().clear();

        int rowIndex = 0;
        for(String line: lines){
            excelBox.addRow(rowIndex);
            int columnIndex = 0;
            for(String metric : line.split(";")){
                Text t = new Text();
                t.setText(metric);
                if(rowIndex == 0) {
                    t.setFont(Font.font(15));
                }
                excelBox.add(t,columnIndex,rowIndex);
                columnIndex++;
            }
            rowIndex++;
        }

    }

    public static void showInformationMessage(String title, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
