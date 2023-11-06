package com.sunlei.xlstools;

import com.sunlei.xlstools.util.AlertUtil;
import com.sunlei.xlstools.util.FileUtil;
import com.sunlei.xlstools.util.StringUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class HelloController {

    @FXML
    private Button selectXlsBtn;


    @FXML
    private Label xlsLabel;

    @FXML
    private Button submitBtn;

    @FXML
    private Button setDefault;

    @FXML
    protected void selectXlsClick() {
        String sysVal = FileUtil.getSysVal();
        if (!StringUtils.isEmpty(sysVal)) {
            defFolder.setText(sysVal);
        }
        Stage stage = (Stage) selectXlsBtn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        if (!StringUtils.isEmpty(defFolder.getText())) {
            fileChooser.setInitialDirectory(new File(defFolder.getText()));
        }
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // 处理选择的文件
            xlsLabel.setText(selectedFile.getAbsolutePath());
        }

    }


    @FXML
    protected void submitClick() throws InterruptedException {
        String xls = xlsLabel.getText();
        try {
            File excelFile = new File(xls);
            String filePath = excelFile.getParent();
            FileInputStream fis = new FileInputStream(excelFile);
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                Cell cell = row.getCell(0);
                if (map.containsKey(cell.toString())) {
                    map.put(cell.toString(), map.get(cell.toString()) + 1);
                } else {
                    map.put(cell.toString(), 1l);
                }
            }
            workbook.close();
            fis.close();


            // 创建工作簿对象
            HSSFWorkbook addWorkBook = new HSSFWorkbook();
            // 创建工作表对象
            HSSFSheet addSheet = addWorkBook.createSheet("快递统计");
            int i = 0;
            for (ConcurrentHashMap.Entry<String, Long> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                HSSFRow addRow = addSheet.createRow(i);
                HSSFCell title = addRow.createCell(0);
                title.setCellValue(entry.getKey());
                HSSFCell quantity = addRow.createCell(1);
                quantity.setCellValue(entry.getValue());
                i++;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String day = simpleDateFormat.format(new Date());
            String outFile = filePath + File.separator + day + ".xls";
            FileOutputStream outputStream = new FileOutputStream(outFile);
            addWorkBook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Thread.sleep(1000);
            AlertUtil.showSuccessAlert("统计完成！请到文件目录查看");
        }
    }

    @FXML
    private Label defFolder;
    @FXML
    protected  void setDefaultClick(){
        Stage stage = new Stage();
        // 创建新的Stage对象
        // 设置窗口标题
        stage.setTitle("设置默认路径");
        stage.setWidth(300);
        stage.setHeight(200);
        String sysVal = FileUtil.getSysVal();
        if (!StringUtils.isEmpty(sysVal)) {
            defFolder.setText(sysVal);
        }
        // 创建文本标签
        TextField field = new TextField(defFolder.getText());

        // 创建按钮
        Button button = new Button("保存");
        button.setStyle("-fx-background-color: green;");
        button.setTextFill(Paint.valueOf("#fcf8f8"));
        // 创建垂直布局容器
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(field, button);

        // 创建场景并将容器添加到场景中
        Scene scene = new Scene(vbox, 300, 200);

        // 将场景设置到舞台
        stage.setScene(scene);
        // 显示窗口
        stage.show();



        button.setOnAction(e -> {

            String path = field.getText();
            File file = new File(path);
            if (!file.exists()) {
                AlertUtil.showWarningAlert("保存失败,路径不存在！");
                return;
            }
            defFolder.setText(field.getText());
            AlertUtil.showSuccessAlert("保存成功!");
            FileUtil.setSysVal(field.getText());
            stage.close();
        });
    }
}