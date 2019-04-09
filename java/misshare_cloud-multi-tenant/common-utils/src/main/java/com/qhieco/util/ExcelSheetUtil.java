package com.qhieco.util;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-15 上午10:32
 * <p>
 * 类说明：
 * excel导出通用工具类，将java对象转换为map<String,value>的键值对，通过编辑好的excel模板进行导出
 */
@Slf4j
public class ExcelSheetUtil<T> {

    private static CellStyle style;

    private static final String EXCEL_URL = "/excelTemplates/";

    private static final String BASIC_CLASS = "java.lang.object";

    public static void write(String file, List<List<Map<String, Object>>> mapList, OutputStream outputStream) throws IOException {
        HSSFWorkbook wb = null;
        try {
            InputStream inputStream = ExcelUtil.class.getResourceAsStream(EXCEL_URL + file + ".xls");
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        sheet(mapList, wb);

        wb.write(outputStream);
    }

    public static void sheet(List<List<Map<String, Object>>> mapLists, HSSFWorkbook wb) throws IOException {
        int sheetNum = wb.getNumberOfSheets();
        for (int index = 0; index < sheetNum; index++) {
            List<Map<String, Object>> mapList = mapLists.get(index);
            HSSFSheet hssfSheet = wb.getSheetAt(index);
            HSSFRow namerow = hssfSheet.getRow(1);
            List<String> rowName = new ArrayList<>();
            style = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            style.setDataFormat(format.getFormat("￥#,##0.00"));
            for (int i = 0; i < namerow.getLastCellNum(); i++) {
                if (!StringUtils.isEmpty(namerow.getCell(i))) {
                    rowName.add(namerow.getCell(i).getStringCellValue());
                }
            }
            for (int i = 0; i < mapList.size(); i++) {
                HSSFRow row = hssfSheet.getRow(i + 1);
                if (row == null) {
                    row = hssfSheet.createRow(i + 1);
                }
                for (int k = 0; k < rowName.size(); k++) {
                    HSSFCell cell = row.getCell(k);
                    if (cell == null) {
                        cell = row.createCell(k);
                    }
                    val map = mapList.get(i);
                    Object value = map.get(rowName.get(k));
                    dataToCell(cell, value, wb);
                }
            }
        }
    }

    public static <T> List<Map<String, Object>> dataToMap(List<T> dataList, Class<T> clazz) {
        val mapList = new ArrayList<Map<String, Object>>();
        val fields = getField(clazz);
        for (val field : fields) {
            field.setAccessible(true);
        }
        for (T data : dataList
                ) {
            val fieldHashMap = new HashMap<String, Object>();
            for (val field : fields
                    ) {
                try {
                    val value = field.get(data);
                    fieldHashMap.put(field.getName(), value);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                    continue;
                }
            }
            mapList.add(fieldHashMap);
        }
        return mapList;
    }

    private static List<Field> getField(Class<?> cls) {
        if (cls == null || cls.getName().toLowerCase().equals(BASIC_CLASS)) {
            return new ArrayList<>();
        }
        List<Field> fields = getField(cls.getSuperclass());
        for (Field field : cls.getDeclaredFields()) {
            fields.add(field);
        }
        return fields;
    }

    private static void dataToCell(HSSFCell cell, Object value, HSSFWorkbook wb) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof Double) {
            cell.setCellValue(String.format("%.2f", (Double) value));
        } else if (value instanceof Long) {
            cell.setCellValue(TimeUtil.stampToDate((Long) value));
        } else if (value instanceof BigDecimal) {

            double doubleVal = ((BigDecimal) value).doubleValue();
            cell.setCellValue(String.format("%.2f", (Double) doubleVal));
            cell.setCellStyle(style);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    public static Integer paramCount(Object... paramList) {
        Integer count = 0;
        for (Object param : paramList
                ) {
            if (param != null) {
                count++;
            }
        }
        return count;
    }

}
