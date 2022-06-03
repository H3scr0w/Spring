package com.saintgobain.dsi.pcpeg.service.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtils {

    public final <T> ByteArrayInputStream writeToExcel(List<T> data, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(sheetName);
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            headerCellStyle.setFillForegroundColor(FillPatternType.SOLID_FOREGROUND.getCode());
            List<String> fieldNames = getFieldNamesForClass(data.get(0).getClass());
            int rowCount = 0;
            Row row = sheet.createRow(rowCount++);
            Class<? extends Object> classz = data.get(0).getClass();
            createHeader(headerCellStyle, fieldNames, row);
            rowCount = createRows(data, sheet, fieldNames, rowCount, classz);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            log.error("Exception in Excel utils " + e.getMessage());
        }
        return null;
    }

    private final void createHeader(CellStyle headerCellStyle, List<String> fieldNames, Row row) {
        AtomicInteger columnCount = new AtomicInteger(0);
        fieldNames.stream().forEach(fieldName -> {
            ExcelFileHeaders header = ExcelFileHeaders.getIfPresent(fieldName);
            if (header != null) {
                Cell cell = row.createCell(columnCount.getAndIncrement());
                cell.setCellValue(header.getValue());
                cell.setCellStyle(headerCellStyle);
            }
        });
    }

    private final <T> int createRows(List<T> data, Sheet sheet, List<String> fieldNames, int rowCount, Class<? extends Object> classz)
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {
        int columnCount;
        Row row;

        for (T t : data) {
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            for (String fieldName : fieldNames) {
                Cell cell = row.createCell(columnCount);
                Method method = null;
                try {
                    method = classz.getMethod("get" + WordUtils.capitalize(fieldName));
                } catch (NoSuchMethodException nme) {
                    method = classz.getMethod("get" + fieldName);
                }

                Object value = method.invoke(t, (Object[]) null);
                if (value != null) {
                    if (value instanceof String) {
                        String val = ((String) value).trim();

                        if (StringUtils.equalsIgnoreCase(val, "Yes")) {
                            val = "OUI";
                        } else if (StringUtils.equalsIgnoreCase(val, "No")) {
                            val = "NON";
                        }
                        cell.setCellValue(val);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value ? "OUI" : "NON");
                    } else if (value instanceof Date) {
                        cell.setCellValue(value.toString());
                    }
                }
                columnCount++;
            }
        }
        return rowCount;
    }

    private final List<String> getFieldNamesForClass(Class<?> clazz) {
        List<String> fieldsName = Arrays.asList(clazz.getDeclaredFields()).stream().map(x -> x.getName()).collect(Collectors.toList());
        return ExcelFileHeaders.getIfPresent(fieldsName);
    }

}
