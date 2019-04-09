package com.qhieco.util;

import com.qhieco.TenantSupport;
import com.qhieco.commonentity.Tenant;
import com.qhieco.constant.Status;
import lombok.Data;
import lombok.Setter;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-15 上午10:32
 * <p>
 * 类说明：
 * excel导出通用工具类，将java对象转换为map<String,value>的键值对，通过编辑好的excel模板进行导出
 */
@Slf4j
public class ExcelUtil<T> {

    private List<ReverseFormat> formatList;
    private Class<T> cls;
    private OutputStream outputStream;
    @Setter
    private Consumer<Map<String, Object>> propertyFormat;

    private static CellStyle style;
    private static String fileName;

    @Data
    public class ReverseFormat {
        private String name;
        private Function<Integer,String> find;

        public ReverseFormat(String n, Function<Integer,String> s){
            name = n;
            find = s;
        }
    }


    public ExcelUtil(OutputStream outputStream, Class<T> cls ){
        formatList = new ArrayList<>();
        this.cls = cls;
        this.outputStream = outputStream;
    }

    public ExcelUtil(OutputStream outputStream, Class<T> cls, String fileNames ){
        formatList = new ArrayList<>();
        fileName = fileNames;
        this.cls = cls;
        this.outputStream = outputStream;
    }

    private static final String EXCEL_URL = "/excelTemplates/";

    private static final String BASIC_CLASS = "java.lang.object";

    public void write(List<T> dataList) throws IOException {
        List<Map<String, Object>> mapList = dataToMap(dataList, cls);
        if(propertyFormat!=null){
            mapList.forEach(propertyFormat);
        }
        reverseLookup(mapList);
        write(cls.getSimpleName(), mapList, outputStream);
    }

    public void buildFormat(String name, Function<Integer, String> status){
        formatList.add(new ReverseFormat(name, status));
    }

    public void reverseLookup(List<Map<String, Object>> mapList){
        for (Map<String, Object> map : mapList
                ) {
            formatList.forEach(format -> {
                val value = map.get(format.getName());
                if(value!=null && value instanceof Integer){
                    map.put(format.getName()+"Str", format.getFind().apply((Integer) value));
                }
            });
        }
    }

    public static void write(String file, List<Map<String, Object>> mapList, OutputStream outputStream) throws IOException {
        if(fileName!=null){
            file=fileName;
        }
        fileName=null;

        HSSFWorkbook wb = null;
        try {
            InputStream inputStream = ExcelUtil.class.getResourceAsStream(EXCEL_URL+file+".xls");
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            wb = new HSSFWorkbook(fs);}
        catch (IOException e1) {
            log.error(e1.getMessage());
        }
        HSSFSheet hssfSheet = wb.getSheetAt(0);
        HSSFRow namerow = hssfSheet.getRow(1);
        if(TenantContext.getCurrentTenant()==null){
            addTenantRow(hssfSheet);

        }
        List<String> rowName = new ArrayList<>();
        style = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("￥#,##0.00"));
        for (int i = 0; i < namerow.getLastCellNum(); i++){
            if (!StringUtils.isEmpty(namerow.getCell(i))) {
                rowName.add(namerow.getCell(i).getStringCellValue());
            }
        }
        if(mapList==null || mapList.size()==0){
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getOutputStream().write(Status.WebErr.EMPTY_EXCEL.getMsg().getBytes());
            return;
        }
        for (int i = 0; i < mapList.size(); i++){
            HSSFRow row = hssfSheet.getRow(i+1);
            if(row == null) {
                row = hssfSheet.createRow(i+1);
            }
            for (int k = 0; k < rowName.size(); k++){
                HSSFCell cell = row.getCell(k);
                if (cell == null){
                    cell = row.createCell(k);
                }
                val map = mapList.get(i);
                Object value = map.get(rowName.get(k));
                dataToCell(cell, value, wb);
            }

        }

        wb.write(outputStream);
    }

    public static <T> List<Map<String, Object>> dataToMap(List<T> dataList, Class<T> clazz){
        val mapList = new ArrayList<Map<String, Object>>();
        val fields = getField(clazz);
        for (val field:fields) {
            field.setAccessible(true);
        }
        if(TenantSupport.class.isAssignableFrom(clazz) && TenantContext.getCurrentTenant() == null){
            TenantHelper.fillTenantName((List<? extends TenantSupport>) dataList);
        }
        for (T data:dataList
             ) {
            val fieldHashMap = new HashMap<String, Object>();
            for (val field:fields
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

    private static List<Field> getField(Class<?> cls){
        if (cls == null || cls.getName().toLowerCase().equals(BASIC_CLASS)){
            return new ArrayList<>();
        }
        List<Field> fields= getField(cls.getSuperclass());
        for (Field field:cls.getDeclaredFields()) {
            fields.add(field);
        }
        return fields;
    }

    private static void dataToCell(HSSFCell cell, Object value, HSSFWorkbook wb){
        if(value == null){
            cell.setCellValue("");
            return;
        }
             if(value instanceof Double) {
                 cell.setCellValue(String.format("%.2f", (Double) value));
             }
            else if(value instanceof Long) {
                 cell.setCellValue(TimeUtil.stampToDate((Long) value));
             }
            else if(value instanceof BigDecimal) {

                 double doubleVal = ((BigDecimal) value).doubleValue();
                 cell.setCellValue(String.format("%.2f", (Double) doubleVal));
                 cell.setCellStyle(style);
             }
            else {
                 cell.setCellValue(value.toString());
             }
    }

    private static void addTenantRow(HSSFSheet hssfSheet){
        HSSFRow firstRow = hssfSheet.getRow(0);
        HSSFRow secondRow = hssfSheet.getRow(1);
        Short index = firstRow.getLastCellNum() < secondRow.getLastCellNum()?firstRow.getLastCellNum():secondRow.getLastCellNum();
        log.info("last index is:{}",index);
        HSSFCell firstCell = firstRow.getCell(index);
        if (firstCell == null){
            firstCell = firstRow.createCell(index);
        }
        firstCell.setCellValue("企业名称");
        HSSFCell secondCell = secondRow.getCell(index);
        if (secondCell == null){
            secondCell = secondRow.createCell(index);
        }
        secondCell.setCellValue("tenantName");
    }

    public static Integer paramCount(Object... paramList){
        Integer count = 0;
        for (Object param:paramList
             ) {
            if (param != null){
                count++;
            }
        }
        if(TenantContext.getCurrentTenant()!=null){
            count++;
        }
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tenantInfo = req.getParameter("tenantId");
        if(tenantInfo!=null){
            count++;
        }
        return count;
    }

}