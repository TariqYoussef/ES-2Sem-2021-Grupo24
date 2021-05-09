package gui;

import codeSmells.CodeSmellsComparator;
import codeSmells.CodeSmellsCreator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static util.Math.*;

/**
 *
 */
public class GuiController {
    public static final FileChooser.ExtensionFilter EXTENSION_FILTER = new FileChooser.ExtensionFilter("ExcelFiles (.xlsx)", "*.xlsx");

    private File projectDir; // Pasta do projeto java
    private File projectCodeSmell; // Path para o excel de codeSmells do projeto
    private CodeSmellsComparator codeSmellsComparator;

    @FXML private Text labelProjectMainTab; // Path que aparece o GUI após a seleção da pasta
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
    //Compare Tab
    private File excelToCompare; //Excel for confusion matrix

    @FXML private Text labelExcelOriginalCompareTab;
    @FXML private Text labelExcelCompareCompareTab;
    @FXML private Label totalnumberIsGodClass;
    @FXML private Label invalidComparisonsNumberIsGodClass;
    @FXML private Label tpnumberIsGodClass;
    @FXML private Label fpnumberIsGodClass;
    @FXML private Label fnnumberIsGodClass;
    @FXML private Label tnnumberIsGodClass;
    @FXML private Label totalnumberIsLongMethod;
    @FXML private Label invalidComparisonsNumberIsLongMethod;
    @FXML private Label tpnumberIsLongMethod;
    @FXML private Label fpnumberIsLongMethod;
    @FXML private Label fnnumberIsLongMethod;
    @FXML private Label tnnumberIsLongMethod;


    @FXML private Button qualityMeasuresIsGodClass;
    @FXML private Button qualityMeasuresIsLongMethod;


    private Rule oldRule;

    public ObservableList<Rule> getRegras() {
        return listrules.getItems();
    }

    /**
     * <p>The FXML when initializing fills the choiceboxes with the corresponding values</p>
     * <p>Once started the FXML will be observing the clicks the user does</p>
     * <p>Once the user clicks in a rule from the rule history table the choiceboxes will fill with the values from the rule clicked</p>
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

                listrules.setOnMouseClicked(this::listSelector);
                listrules.setOnScrollTo(this::listSelector);
                listrules.setOnKeyPressed(this::listSelector);
        } catch (Exception e) {
            GuiController.showInformationMessage("Erro","Erro a ler regras armazenadas", Alert.AlertType.ERROR);
        }
    }

    private void listSelector(Event event) {
        if (listrules.getSelectionModel().getSelectedItem() != null) {
            Rule ruleSelected = listrules.getSelectionModel().getSelectedItem();
            metric1.setValue(ruleSelected.getRule1().getMetric());
            metric1op.setValue(ruleSelected.getRule1().getOperation());
            metric1value.setText(ruleSelected.getRule1().getValue().toString());
            metric2.setValue(ruleSelected.getRule2().getMetric());
            metric2op.setValue(ruleSelected.getRule2().getOperation());
            metric2value.setText(ruleSelected.getRule2().getValue().toString());

            rulelogic.setValue(ruleSelected.getOperation());
            rulesmell.setValue(ruleSelected.getSmell());
        }
    }


    /**
     *  <p>This method starts when select project button is clicked.</p>
     *  <p>It checks if a code smells file exists in the selected project to update GUI.</p>
     */
    @FXML private void openProject(){
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            Stage stage = new Stage();
            directoryChooser.setTitle("Escolher pasta do projeto");
            File dir = directoryChooser.showDialog(stage);

            projectDir = dir;
            labelProjectMainTab.setText(dir.getAbsolutePath());

            //If there is code smells in project
            String pathCodeSmell = projectDir.getAbsolutePath() + "/" + projectDir.getName() + "_metrics/" + projectDir.getName() + "_metrics.xlsx";
            File projectCodeSmell = new File(pathCodeSmell);
            if(Files.exists(projectCodeSmell.toPath()) ){
                setExcelOriginal(projectCodeSmell);
                updateGUIElements(pathCodeSmell);
            }else{
                clearGUIElements();
            }
        } catch (NullPointerException e) {
            showInformationMessage("Informação", "Nenhum ficheiro selecionado.", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * <p>Gui method  to set the original excel file</p>
     *
     * @param filePath {@link File} file for the OriginalExcel
     */
    private void setExcelOriginal(File filePath) {
        projectCodeSmell = filePath;
        labelExcelOriginalCompareTab.setText(projectCodeSmell.getName());
    }

    /**
     * <p>Gui method  to set the second excel file</p>
     * @param filePath {@link File} file for the Comparing Excel
     */
    private void setExcelToCompare(File filePath) {
        excelToCompare = filePath;
        labelExcelCompareCompareTab.setText(filePath.getName());
    }
    /**
     * <p>Method used for choosing the Comparing Excel that will be compared to another one </p>
     */
    @FXML private void chooseExcelToCompare(){
        try {
            File exceltoCompare = getExcelFile("Escolher ficheiro excel para comparar");
            setExcelToCompare(exceltoCompare);
        } catch (NullPointerException e) {
            showInformationMessage("Informação", "Nenhum ficheiro excel selecionado.", Alert.AlertType.INFORMATION);

        }
    }

    /**
     *<p>Method used for choosing the Original Excel that will be compared to another one </p>
     */
    @FXML private void chooseExcelOriginal(){
        try {
            File exceloriginal = getExcelFile("Escolher ficheiro excel original");
            setExcelOriginal(exceloriginal);
        } catch (NullPointerException e) {
            showInformationMessage("Informação", "Nenhum ficheiro excel selecionado.", Alert.AlertType.INFORMATION);
        }

    }

    /**
     * <p>Method to encapsulate opening a filechooser with a .xlsx ExtensionFilter</p>
     * @param message {@link String}   message shown in the {@link FileChooser}
     * @return {@link File} file that the user chose with the {@link FileChooser}
     *
     * @see FileChooser
     * @see File
     * @see String
     *
     */
    private File getExcelFile(String message) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        fileChooser.setTitle(message);
        fileChooser.getExtensionFilters().add(EXTENSION_FILTER);
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * <p>Method used when the user click the CompareCodeSmells button on the Compare Tab </p>
     *
     */
    @FXML private void compareExcels(){
        // if (checkCodeSmellsOriginal() || checkCodeSmellsToCompare()  ) return;

        try {
            codeSmellsComparator = new CodeSmellsComparator(projectCodeSmell,excelToCompare);
            invalidComparisonsNumberIsLongMethod.setDisable(true);
            invalidComparisonsNumberIsLongMethod.setVisible(false);
            invalidComparisonsNumberIsGodClass.setDisable(true);
            invalidComparisonsNumberIsGodClass.setVisible(false);

            setLabels(codeSmellsComparator);

            qualityMeasuresIsGodClass.setDisable(false);
            qualityMeasuresIsGodClass.setVisible(true);
            qualityMeasuresIsLongMethod.setDisable(false);
            qualityMeasuresIsLongMethod.setVisible(true);
        } catch (NullPointerException nullPointerException) {
            showInformationMessage("Erro", "Selecione os Ficheiros excel a comparar", Alert.AlertType.ERROR);
            //nullPointerException.printStackTrace();
        }catch (Exception e){
            throwErroInesperado(e);
        }

    }

    private void setLabels(CodeSmellsComparator codeSmellsComparator) {
        tpnumberIsGodClass.setText(String.valueOf(codeSmellsComparator.getTruePositiveNumberIsGodClass()));
        tnnumberIsGodClass.setText(String.valueOf(codeSmellsComparator.getTrueNegativeNumberIsGodClass()));
        fpnumberIsGodClass.setText(String.valueOf(codeSmellsComparator.getFalsePositiveNumberIsGodClass()));
        fnnumberIsGodClass.setText(String.valueOf(codeSmellsComparator.getFalseNegativeNumberIsGodClass()));
        totalnumberIsGodClass.setText("Total: " + calculateTotal(codeSmellsComparator.getTruePositiveNumberIsGodClass(), codeSmellsComparator.getTrueNegativeNumberIsGodClass(),
                codeSmellsComparator.getFalsePositiveNumberIsGodClass(), codeSmellsComparator.getFalseNegativeNumberIsGodClass()));
        if(codeSmellsComparator.getInvalidComparisonsNumberIsGodClass()!=0) {
            invalidComparisonsNumberIsGodClass.setDisable(false);
            invalidComparisonsNumberIsGodClass.setVisible(true);
            invalidComparisonsNumberIsGodClass.setText("Invalid Comparisons: "+ codeSmellsComparator.getInvalidComparisonsNumberIsGodClass());
        }

        tpnumberIsLongMethod.setText(String.valueOf(codeSmellsComparator.getTruePositiveNumberIsLongMethod()));
        tnnumberIsLongMethod.setText(String.valueOf(codeSmellsComparator.getTrueNegativeNumberIsLongMethod()));
        fpnumberIsLongMethod.setText(String.valueOf(codeSmellsComparator.getFalsePositiveNumberIsLongMethod()));
        fnnumberIsLongMethod.setText(String.valueOf(codeSmellsComparator.getFalseNegativeNumberIsLongMethod()));
        totalnumberIsLongMethod.setText("Total: " + calculateTotal(codeSmellsComparator.getTruePositiveNumberIsLongMethod(), codeSmellsComparator.getTrueNegativeNumberIsLongMethod(),
                codeSmellsComparator.getFalsePositiveNumberIsLongMethod(), codeSmellsComparator.getFalseNegativeNumberIsLongMethod()));
        if(codeSmellsComparator.getInvalidComparisonsNumberIsLongMethod()!=0) {
            invalidComparisonsNumberIsLongMethod.setDisable(false);
            invalidComparisonsNumberIsLongMethod.setVisible(true);
            invalidComparisonsNumberIsLongMethod.setText("Invalid Comparisons: "+ codeSmellsComparator.getInvalidComparisonsNumberIsLongMethod());
        }
    }

    /**
     * <p>Method used when the user click the qualityMeasures button on the Compare Tab relative to the God Class metric </p>
     *
     */
    @FXML private void setQualityMeasuresIsGodClass(){
        try {

            createAlert(codeSmellsComparator.getTruePositiveNumberIsGodClass(), codeSmellsComparator.getTrueNegativeNumberIsGodClass(),
                    codeSmellsComparator.getFalsePositiveNumberIsGodClass(), codeSmellsComparator.getFalseNegativeNumberIsGodClass());

        } catch (NullPointerException nullPointerException) {
            showInformationMessage("Erro", "Ainda não foi criada a Matriz de Confusão", Alert.AlertType.ERROR);
        }catch (Exception e){
            throwErroInesperado(e);
        }
    }
    /**
     * <p>Method used when the user click the qualityMeasures button on the Compare Tab relative to the Long Method metric</p>
     *
     */
    @FXML private void setQualityMeasuresIsLongMethod(){
        try {

            createAlert(codeSmellsComparator.getTruePositiveNumberIsLongMethod(), codeSmellsComparator.getTrueNegativeNumberIsLongMethod(),
                    codeSmellsComparator.getFalsePositiveNumberIsLongMethod(), codeSmellsComparator.getFalseNegativeNumberIsLongMethod());

        } catch (NullPointerException nullPointerException) {
            showInformationMessage("Erro", "Ainda não foi criada a Matriz de Confusão", Alert.AlertType.ERROR);
        }catch (Exception e){
            throwErroInesperado(e);
        }
    }

    /**
     * <p>Method used to create a popup info window with all the quality measures generated from the Confusion Matrix</p>
     *
     */
    private void createAlert(double tp, double tn, double fp, double fn){
        DecimalFormat df = new DecimalFormat("###.##");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Label precisionN = new Label("Precision: ");
        Double precision = calculatePrecision(tp, fp);
        Double recall = calculateRecall(tp,fn);

        Label precisionV = new Label(df.format(precision));
        Label recallN = new Label("Recall: ");
        Label recallV = new Label(df.format(recall));
        Label errorN = new Label("Error: ");
        Label errorV = new Label(df.format(calculateError(tp,tn,fp,fn)));
        Label accuracy = new Label("Accuracy: ");
        Label accuracyV = new Label(df.format(calculateAccuracy(tp,tn,fp,fn)));
        Label precisionEva = new Label(evaluateMeasure(precision));
        Label recallEva = new Label(evaluateMeasure(recall));

        GridPane quality = new GridPane();
        quality.setMinHeight(Region.USE_COMPUTED_SIZE);
        quality.setMinWidth(300);
        quality.setMaxHeight(Region.USE_COMPUTED_SIZE);
        quality.setMaxWidth(Region.USE_COMPUTED_SIZE);
        //quality.setAlignment(Pos.CENTER_LEFT);
        quality.setHgap(10);
        quality.setVgap(5);

        quality.add(precisionN, 1, 0);
        quality.add(precisionV, 2, 0);
        quality.add(precisionEva, 3, 0);

        quality.add(recallN,1, 1);
        quality.add(recallV, 2, 1);
        quality.add(recallEva, 3, 1);

        quality.add(errorN,1,2);
        quality.add(errorV, 2, 2);

        quality.add(accuracy,1,3);
        quality.add(accuracyV, 2, 3);

        alert.getDialogPane().setContent(quality);
        alert.setTitle("Medidas de Qualidade");
        alert.setContentText("As medidas calculadas foram as seguintes");
        alert.showAndWait();
    }




    /**
     * <p>Private Method for encapsulating showInformationMessage with the specific "Erro inesperado." content</p>
     *
     * @param exception {@link Exception} exception used to print its stack Trace for easy debugging
     */
    public static void throwErroInesperado(Exception exception) {
        exception.printStackTrace();
        showInformationMessage("Erro", "Erro inesperado.", Alert.AlertType.ERROR);
    }

    /**
     * <p>Used to launch the creation of the code smells excel file.</p>
     * <p>This method starts when create code smells button is clicked.</p>
     * <p>It creates a MetricExtractor object that will be used to extract the metrics needed to create the code smells file
     * then it filters the rules.</p>
     * <p>After this, it creates a codeSmellsCreator object that will be responsible to create the excel file. This object will
     * be used to call different functions responsible of the creation of the excel file.</p>
     */
    // Função que será executada para criar o code smell excutada através do botão para tal
    @FXML private void createCodeSmell(){
        try {
            String pathSave = projectDir.getAbsolutePath() + "/" + projectDir.getName() + "_metrics/";
            String pathCodeSmell = pathSave + projectDir.getName() + "_metrics.xlsx";

            // retirar criar um objeto para retirar as métricas
            MetricExtractor metrics = new MetricExtractor(projectDir.toPath());

            // filtrar as rules
            Predicate<Rule> rulePredicateGodClass = (Rule r) -> (r.getSmell().equals(Rule.Smell.God_Class));
            List<Rule> godRules = getRegras().filtered(rulePredicateGodClass);
            Predicate<Rule> rulePredicateLongMethod = (Rule r) -> (r.getSmell().equals(Rule.Smell.Long_Method));
            List<Rule> longRules = getRegras().filtered(rulePredicateLongMethod);

            // criar code smells
            CodeSmellsCreator codeSmellsCreator = new CodeSmellsCreator(metrics, projectDir.getName(), godRules, longRules);

            // criar a pasta onde se vai guardar o ficheiro xlsx
            try{
                Files.createDirectory(Paths.get(pathSave));
                codeSmellsCreator.createCodeSmellsXlsxFile(new File(pathSave));
            }catch (FileAlreadyExistsException e){
                //System.out.println("A pasta já existe");
            }

            // guardar code smells
            codeSmellsCreator.addCodeSmellsToXlsx(new File(pathSave));

            updateGUIElements(pathCodeSmell);

            if(!createButton.getText().equals("Atualizar Code Smells")) createButton.setText("Atualizar Code Smells");

            showInformationMessage("Informação","Os code smells foram criados com sucesso.", Alert.AlertType.INFORMATION);

        }catch (NullPointerException e){
            showInformationMessage("Informação","Por favor selecione a pasta do projeto.", Alert.AlertType.INFORMATION);
        }catch (Exception e){
            throwErroInesperado(e);
            e.printStackTrace();
        }

    }

    /**
     *  <p>Once all the values from the choiceboxes are filled a new rule with those values is created</p>
     *  <p>The program checks if the rule created already exists in the set of rules, if it doesn't exist then it proceeds</p>
     *  <p>That rule is then added to the current set of rules and sent back to the serialized file with the serializeRule method</p>
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
     * <p> The program calls the static method from Rule class to delete the rule selected by the user</p>
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
     * <p>The user chooses the rule he wants to edit</p>
     * <p>The program hides the remove and edit button and swaps for a done button</p>
     * <p>The follow up to this method is finishChangeRuleFromHistory().</p>
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
     * <p>After selecting the rule to be edited and changing the button layout from changeRuleFromHistory()</p>
     * <p>The program checks if the updated rule already exists and if not it updates the rule set</p>
     * <p>Once it updates the remove and edit button reappear and the done button goes hidden again</p>
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
     * <p>The program gets the values selected from header and creates a new rule with those values</p>
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
     * <p>Method used to clear all GUI elements of a project in the program.</p>
     */
    private void clearGUIElements(){
        excelBox.getChildren().clear();
        packNum.setText("");
        classNum.setText("");
        methodNum.setText("");
        locNum.setText("");
    }

    /**
     * <p>Method used to update GUI elements of a project in the program.</p>
     * <p>Updates</p>
     * <ul>
     *     <li>CharacteristicsGUI</li>
     *     <li>CodeSmellsGUI</li>
     * </ul>
     * @param pathCodeSmell {@link String} path to code smells excel file
     */
    private void updateGUIElements(String pathCodeSmell){
        updateGUIElements(new File(pathCodeSmell));
    }

    /**
     * <p>Method used to update GUI elements of a project in the program.</p>
     * <p>Updates the following GUI elements:</p>
     * <ul>
     *     <li>CharacteristicsGUI</li>
     *     <li>CodeSmellsGUI</li>
     * </ul>
     * @param pathCodeSmell {@link File} of the code Smells excel
     */
    private void updateGUIElements(File pathCodeSmell){
        ArrayList<String> lines = ExelReader.read(pathCodeSmell);

        writeCharacteristicsGUI(lines);
        writeCodeSmellsGUI(lines);

        createButton.setText("Atualizar Code Smells");
    }

    /**
     * <p>Method that creates the characteristics GUI </p>
     * @param lines {@link ArrayList} lines
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
     * <p>This method is used to display the code smells excel file in the GUI of the program</p>
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
     * <p>This method is used to show a informative popup message.</p>
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
