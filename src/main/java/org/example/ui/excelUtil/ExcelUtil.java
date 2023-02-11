package org.example.ui.excelUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dto.response.ExcelFileResponse;
import org.example.model.task.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.example.Main.taskService;
import static org.example.Main.userService;


public class ExcelUtil {
     private static String path(String username) {
          return "A:\\Documents\\Java\\3-modul\\Codes\\Task_Management\\Task_Management\\src\\main\\resources\\"
                  + username + ".xlsx";
     }

     private static ExcelFileResponse createFile(String userName, List<Task> tasks) {
          XSSFWorkbook createFile = new XSSFWorkbook();

          Sheet sheet = createFile.createSheet("My Tasks completed tasks");

          XSSFCellStyle cellStyle = createFile.createCellStyle();

          Font font = new XSSFFont();

          font.setBold(true);

          cellStyle.setAlignment(HorizontalAlignment.FILL);
          cellStyle.setFont(font);
          cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
          cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

          addDescriptionAboutValuesOnTheTopOfTheRow(sheet.createRow(0), cellStyle);


          for (int i = 1; i <= tasks.size(); i++) {

               Task task = tasks.get(i - 1);

               Row row = sheet.createRow(i);
               Cell cell = row.createCell(0);
               cell.setCellValue(task.getName());
               cell.setCellStyle(cellStyle);
               Cell cell1 = row.createCell(1);
               cell1.setCellValue(task.getDescription());
               cell1.setCellStyle(cellStyle);
               Cell cell2 = row.createCell(2);
               cell2.setCellValue(task.getType().toString());
               cell2.setCellStyle(cellStyle);
               Cell cell3 = row.createCell(3);
               cell3.setCellValue(task.getCreatedDate().toString());
               cell3.setCellStyle(cellStyle);
               Cell cell4 = row.createCell(4);
               cell4.setCellValue(task.getUpdatedDate().toString());
               cell4.setCellStyle(cellStyle);
          }

          try {
               createFile.write(new FileOutputStream(path(userName)));
          } catch (IOException e) {
               throw new RuntimeException(e);
          }

          return new ExcelFileResponse(888, tasks.size() + " tasks have/task has been downloaded");
     }

     private static void addDescriptionAboutValuesOnTheTopOfTheRow(Row row, XSSFCellStyle cellStyle) {
          Cell cell = row.createCell(0);
          cell.setCellValue("Name");
          cell.setCellStyle(cellStyle);
          Cell cell1 = row.createCell(1);
          cell1.setCellValue("Description");
          cell1.setCellStyle(cellStyle);
          Cell cell2 = row.createCell(2);
          cell2.setCellValue("Type");
          cell2.setCellStyle(cellStyle);
          Cell cell3 = row.createCell(3);
          cell3.setCellValue("Created date");
          cell3.setCellStyle(cellStyle);
          Cell cell4 = row.createCell(4);
          cell4.setCellValue("Updated Date");
          cell4.setCellStyle(cellStyle);
     }

     public Optional<Set<Task>> getMyDownloadedTasks(UUID userId) {
          String userName = userService.getById(userId).getName();
          if (filePath(userName).exists()) {
               return Optional.of(getTasks(filePath(userName)));
          }
          return Optional.empty();
     }

     public ExcelFileResponse downLoadMyCompletedTasks(UUID userId) {
          List<Task> tasks = taskService.getMyCompletedTasks(userId);
          String userName = userService.getById(userId).getName();
          if (tasks.isEmpty()) {
               return new ExcelFileResponse(147, "Downloading progress is not possible, " +
                       "because any completed tasks completed by you were not found");
          }

          if (filePath(userName).exists()) {
               return addTasks(userName, tasks);
          } else {
               return createFile(userName, tasks);
          }
     }

     private File filePath(String userName) {
          return new File(path(userName));
     }

     private ExcelFileResponse addTasks(String userName, List<Task> tasks) {
          Set<Task> tasks1 = getTasks(filePath(userName));
          int counter = 0;
          for (Task task : tasks) {
               if (!tasks1.contains(task)) {
                    counter++;
                    tasks1.add(task);
               }
          }

          tasks = tasks1.stream().toList();

          createFile(userName, tasks);

          if (counter == 0) {
               return new ExcelFileResponse(777, "There are not any new tasks to download");
          } else {
               return new ExcelFileResponse(999, counter + " new tasks have/task has been downloaded");
          }
     }

     private Set<Task> getTasks(File filePath) {
          Set<Task> tasks = new HashSet<>();

          Workbook workbook;
          try {
               workbook = new XSSFWorkbook(filePath);
          } catch (IOException | InvalidFormatException e) {
               throw new RuntimeException(e);
          }

          Sheet sheet = workbook.getSheetAt(0);

          int lastRowNum = sheet.getLastRowNum();

          for (int i = 1; i <= lastRowNum; i++) {
               Row row = sheet.getRow(i);

               String taskName = row.getCell(0).getStringCellValue();
               String taskDescription = row.getCell(1).getStringCellValue();
               String taskType = row.getCell(2).getStringCellValue();
               String taskCreatedDate = row.getCell(3).getStringCellValue();
               String taskUpdatedDate = row.getCell(4).getStringCellValue();

               Task task = new Task(taskName, taskDescription, taskType, taskCreatedDate, taskUpdatedDate);
               tasks.add(task);
          }
          return tasks;
     }
}
