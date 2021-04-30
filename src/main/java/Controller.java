import codeSmells.CodeSmellsCreator;
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
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
public class Controller {

    private File projectDir; // Pasta do projeto java
    private File codeSmells; // CodeSmell selecionado

    @FXML private Text path; // Path que aparece o GUI após a seleção da pasta
    @FXML private Button createButton;
    @FXML private GridPane excelBox;

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

    @FXML private Button addrule;
    @FXML private Button removerule;
    @FXML private Button changerule;
    @FXML private Button donerulebtn;

    //Confusion matrix
    private File excelToCompare; //Excel for confusion matrix

    @FXML private Text path2; // Path of Excel file
    @FXML private Label totalnumber;
    @FXML private Label tpnumber;
    @FXML private Label fpnumber;
    @FXML private Label fnnumber;
    @FXML private Label tnnumber;

    private Rule oldRule;

    public ObservableList<Rule> getRegras() {
        return listrules.getItems();
    }

    /**
     * The FXML when initializing fills the choiceboxes with the corresponding values
     * Once started the FXML will be observing the clicks the user does
     * Once the user clicks in a rule from the rule history table the choiceboxes will fill with the values from the rule clicked
     */
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

        try {
            listrules.setItems(FXCollections.observableArrayList(Rule.deserializedRule()));

                listrules.setOnMouseClicked(mouseEvent -> {
                    if(listrules.getSelectionModel().getSelectedItem()!=null){
                        Rule ruleSelected = listrules.getSelectionModel().getSelectedItem();
                        metric1.setValue(ruleSelected.getMetric(ruleSelected.getRule1()));
                        metric1op.setValue(ruleSelected.getMetricOperation(ruleSelected.getRule1()));
                        metric1value.setText(ruleSelected.getMetricValue(ruleSelected.getRule1()).toString());
                        metric2.setValue(ruleSelected.getMetric(ruleSelected.getRule2()));
                        metric2op.setValue(ruleSelected.getMetricOperation(ruleSelected.getRule2()));
                        metric2value.setText(ruleSelected.getMetricValue(ruleSelected.getRule2()).toString());

                        rulelogic.setValue(ruleSelected.getOperation());
                        rulesmell.setValue(ruleSelected.getSmell());
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();

        }

    }

    /**
     *  This method starts when select project button is clicked.
     *  It checks if a code smells file exists in the selected project to update GUI.
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

    /**
     * Used to launch the creation of the code smells excel file.
     * This method starts when create code smells button is clicked.
     * It creates a MetricExtractor object that will be used to extract the metrics needed to create the code smells file
     * then it filters the rules.
     * After this, it creates a codeSmellsCreator object that will be responsible to create the excel file. This object will
     * be used to call different functions responsible of the creation of the excel file.
     */
    // Função que será executada para criar o code smell excutada através do botão para tal
    @FXML private void createCodeSmell(){
        try {
            String pathSave = projectDir.getAbsolutePath() + "/" + projectDir.getName() + "_metrics/";
            String pathCodeSmell = pathSave + projectDir.getName() + "_metrics.xlsx";

            // retirar criar um objeto para retirar as métricas
            MetricExtractor metrics = new MetricExtractor(projectDir.toPath());

            // filtrar as rules
            Predicate<Rule> pr1 = (Rule r) -> (r.getSmell().equals(Rule.Smell.God_Class));
            List<Rule> godRules = getRegras().filtered(pr1);
            Predicate<Rule> pr2 = (Rule r) -> (r.getSmell().equals(Rule.Smell.Long_Method));
            List<Rule> longRules = getRegras().filtered(pr2);

            // criar code smells
            CodeSmellsCreator codeSmellsCreator = new CodeSmellsCreator(metrics, projectDir.getName(), godRules, longRules);

            // criar a pasta onde se vai guardar o ficheiro xlsx
            try{
                Files.createDirectory(Paths.get(pathSave));
                codeSmellsCreator.createCodeSmellsXlsxFile(new File(pathSave));
            }catch (FileAlreadyExistsException e){
                System.out.println("A pasta já existe");
            }

            // guardar code smells
            codeSmellsCreator.addCodeSmellsToXlsx(new File(pathSave));

            updateGUIElements(pathCodeSmell);

            if(!createButton.getText().equals("Atualizar Code Smells")) createButton.setText("Atualizar Code Smells");

            showInformationMessage("Informação","Os code smells foram criados com sucesso.", Alert.AlertType.INFORMATION);

        }catch (NullPointerException e){
            showInformationMessage("Informação","Por favor selecione a pasta do projeto.", Alert.AlertType.INFORMATION);
        }catch (Exception e){
            showInformationMessage("Erro", "Erro inesperado.", Alert.AlertType.ERROR);
        }

    }

    /**
     *  Once all the values from the choiceboxes are filled a new rule with those values is created
     *  The program checks if the rule created already exists in the set of rules, if it doesn't exist then it proceeds
     *  That rule is then added to the current set of rules and sent back to the serialized file with the serializeRule method
     * @throws IOException IO operation failed
     * @throws ClassNotFoundException Class Rule doesn't exist
     */
    @FXML private void addRuleToHistory() throws IOException, ClassNotFoundException {
        Rule rule = getRule();
        if(rule==null)
            return;

        boolean addRule = Rule.doesRuleExist(rule);
        ArrayList<Rule> rules2add = new ArrayList<>(getRegras());

        if(!addRule) {
            rules2add.add(rule);
            Rule.serializeRule(rules2add);
            ObservableList<Rule> regras = FXCollections.observableArrayList(rules2add);
            listrules.setItems(regras);
            showInformationMessage("Informação", "A regra foi adicionada com sucesso.", Alert.AlertType.INFORMATION);
        } else {
            showInformationMessage("Informação", "Já existe essa regra.", Alert.AlertType.INFORMATION);
        }
    }

    /**
     *  The program calls the static method from Rule class to delete the rule selected by the user
     * @throws IOException IO operation failed
     * @throws ClassNotFoundException Class Rule doesn't exist
     */
    @FXML private void removeRuleFromHistory() throws IOException, ClassNotFoundException {
        if(listrules.getSelectionModel().getSelectedItems().isEmpty())
            showInformationMessage("Informação", "Nenhuma regra selecionada.", Alert.AlertType.INFORMATION);
        else {
            Rule.deleteRule(listrules.getSelectionModel().getSelectedItem());
            ObservableList<Rule> regras = FXCollections.observableArrayList(Rule.deserializedRule());
            listrules.setItems(regras);
            showInformationMessage("Informação", "A regra foi eliminada com sucesso.", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * The user chooses the rule he wants to edit
     * The program hides the remove and edit button and swaps for a done button
     * The follow up to this method is finishChangeRuleFromHistory().
     */
    @FXML private void changeRuleFromHistory(){
        if(listrules.getSelectionModel().getSelectedItems().isEmpty())
            showInformationMessage("Informação", "Nenhuma regra selecionada.", Alert.AlertType.INFORMATION);
        else {
            this.oldRule =listrules.getSelectionModel().getSelectedItem();
            removerule.setVisible(false);
            removerule.setDisable(true);

            changerule.setVisible(false);
            changerule.setDisable(true);

            addrule.setVisible(false);
            addrule.setDisable(true);

            donerulebtn.setVisible(true);
            donerulebtn.setDisable(false);

            showInformationMessage("Informação", "Preencha com os valores que deseja alterar", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * After selecting the rule to be edited and changing the button layout from changeRuleFromHistory()
     * The program check if the updated rule already exists and if not it updates the rule set
     * Once it updates the remove and edit button reappear and the done button goes hidden again
     */
    @FXML private void finishChangeRuleFromHistory() {
        Rule newRule = getRule();
        if(newRule==null)
            return;

        try {
            boolean addRule = Rule.doesRuleExist(newRule);
            if(!addRule) {
                Rule.changeRule(this.oldRule,newRule);
                ObservableList<Rule> regras = FXCollections.observableArrayList(Rule.deserializedRule());
                listrules.setItems(regras);
                showInformationMessage("Informação", "A regra foi alterada com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                showInformationMessage("Informação", "Já existe essa regra.", Alert.AlertType.INFORMATION);
            }
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
        donerulebtn.setDisable(true);
        donerulebtn.setVisible(false);

        removerule.setDisable(false);
        removerule.setVisible(true);

        changerule.setDisable(false);
        changerule.setVisible(true);

        addrule.setDisable(false);
        addrule.setVisible(true);
        this.oldRule=null;
    }

    /**
     * The program gets the values selected from header and creates a new rule with those values
     * @return Rule selected for removal or editing
     */
    private Rule getRule(){
        if(metric1.getValue() == null || metric1op.getValue() == null || metric1value.getText().isEmpty() ||
                metric2.getValue() == null || metric2op.getValue() == null || metric2value.getText().isEmpty() ||
                rulelogic.getValue() == null || rulesmell.getValue() == null){

            showInformationMessage("Informação", "Certifique-se que todos os valores estão preenchidos.", Alert.AlertType.INFORMATION);
            return null;
        }
        return new Rule(metric1.getValue(), metric1op.getValue(), Integer.parseInt(metric1value.getText()),
                metric2.getValue(), metric2op.getValue(), Integer.parseInt(metric2value.getText()),
                rulelogic.getValue(), rulesmell.getValue());
    }

    /**
     * Method used to clear all GUI elements of a project in the program.
     */
    private void clearGUIElements(){
        excelBox.getChildren().clear();
        packNum.setText("");
        classNum.setText("");
        methodNum.setText("");
        locNum.setText("");
    }

    /**
     * Method used to update GUI elements of a project in the program.
     * @param pathCodeSmell path to code smells excel file
     */
    private void updateGUIElements(String pathCodeSmell){
        ArrayList<String> lines = ExelReader.read(new File(pathCodeSmell));
        /*
         * TODO
         *   -Usar o ArrayList lines num método separado para obter as caracteristicas
         */
        writeCharacteristicsGUI(lines);
        writeCodeSmellsGUI(lines);

        createButton.setText("Atualizar Code Smells");
    }

    /**
     * @param lines
     */
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


    /**
     * This method is used to write the code smells excel file in the GUI of the program
     * @param lines each line represents a row in the excel file
     */
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

    /**
     * This method is used to show a informative popup message.
     * @param title title of the window
     * @param content content of the message
     * @param alertType alert type EX: ERROR, INFORMATION, etc
     */
    public static void showInformationMessage(String title, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
