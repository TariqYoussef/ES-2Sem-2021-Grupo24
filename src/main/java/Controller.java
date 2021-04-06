import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import metrics.MetricExtractor;
import readers.ExelReader;

import javax.swing.plaf.synth.SynthEditorPaneUI;
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

    @FXML private void initialize(){
        //Para quando o programa inicia
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

    private void clearGUIElements(){
        excelBox.getChildren().clear();
        packNum.setText("");
        classNum.setText("");
        methodNum.setText("");
    }

    private void updateGUIElements(String pathCodeSmell){
        ExelReader exelReader = new ExelReader(pathCodeSmell);
        ArrayList<String> lines = exelReader.read();
        /*
         * TODO
         *   -Usar o ArrayList lines num método separado para obter as caracteristicas
         */

        writeCodeSmellsGUI(lines);

        createButton.setText("Atualizar Code Smells");
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
