package com.zte.msg.alarmcenter.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.prefs.BackingStoreException;

public class ExcelPortUtil {

    /**
     * @param sheetName  工作表名，文件名，头部信息
     * @param listName   列名
     * @param list       需要写入的数据
     * @param listBottom 底部写入信息：<列位置，数据>
     * @param response   返回
     */
    public static void excelPort(String sheetName, List<String> listName, List<Map<String, String>> list, List<Map<Integer, String>> listBottom, HttpServletResponse response) {
        try {
            if (list.size() == 0) {
                throw new BackingStoreException("数据为空");
            }
            // 声明一个工作簿
            XSSFWorkbook wb = new XSSFWorkbook();
            // 创建sheet页
            XSSFSheet sheet = wb.createSheet(sheetName);
            sheet.setDefaultColumnWidth(19);

            // 表头
            XSSFRow rowReportTitle = sheet.createRow(0);
            Cell cell1 = rowReportTitle.createCell(0); // 0列
            // 设置值
            cell1.setCellValue(sheetName);

            // 合并表头
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, listName.size() - 1));
            rowReportTitle.setHeight((short) 600); // 行高

            //设置表头字体
            Font headFont = wb.createFont();
            headFont.setFontName("宋体");
            headFont.setFontHeightInPoints((short) 18);// 字体大小

            CellStyle headStyle = wb.createCellStyle();
            headStyle.setFont(headFont);
            headStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
            headStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
            // 头部样式添加
            cell1.setCellStyle(headStyle);

            // 全局加线样式
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框
            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中

            // 记录标题信息
            TreeMap<String, Integer> headMap = new TreeMap<>();

            // 标题写入
            XSSFRow row = sheet.createRow(1);
            for (int i = 0; i < listName.size(); i++) {
                row.setHeight((short) 450);
                XSSFCell cell = row.createCell(i);
                String headName = listName.get(i);
                cell.setCellValue(headName); // 写入列名
                headMap.put(headName, i);
                cell.setCellStyle(cellStyle);
            }

            // 写入内容数据
            int ind = 2;
            for (Map<String, String> map : list) {
                XSSFRow r = sheet.createRow(ind++);
                for (Map.Entry<String, Integer> m : headMap.entrySet()) {
                    String name = m.getKey(); // 列名
                    String value = map.get(name); // value 不一定存在
                    XSSFCell cell2 = r.createCell(m.getValue());
                    if (value != null) {
                        cell2.setCellValue(value);
                    }
                    cell2.setCellStyle(cellStyle);
                }
            }

            // 底部样式
            CellStyle bottomStyle = wb.createCellStyle();
            bottomStyle.setBorderBottom(BorderStyle.THIN); //下边框
            bottomStyle.setBorderLeft(BorderStyle.THIN);//左边框
            bottomStyle.setBorderTop(BorderStyle.THIN);//上边框
            bottomStyle.setBorderRight(BorderStyle.THIN);//右边框
            bottomStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中

            // 写入底部
            if (listBottom != null) {
                int columnNum = listName.size(); // 当前表有多少列
                for (Map<Integer, String> map : listBottom) {
                    XSSFRow bottom = sheet.createRow(ind);
                    bottom.setHeight((short) 400); // 行高
                    // 当前行分几列
                    int size = map.size();
                    if (columnNum % 2 == 0 & size % 2 == 0) {
                        // 都是偶数执行
                        int c = columnNum / size;  // 列大小
                        for (int i = 0; i < size; i++) {
                            CellRangeAddress cellAddresses0 = new CellRangeAddress(ind, ind, i * c, i * c + c - 1);
                            sheet.addMergedRegion(cellAddresses0);
                            XSSFCell c0 = bottom.createCell(cellAddresses0.getFirstColumn());
                            c0.setCellValue(map.get(i));
                            c0.setCellStyle(bottomStyle);
                            for (int n = cellAddresses0.getFirstColumn() + 1; n <= cellAddresses0.getLastColumn(); n++) {
                                XSSFCell cn = bottom.createCell(n);
                                cn.setCellStyle(bottomStyle);
                            }
                        }
                    } else if (!(columnNum % 2 == 0) & !(size % 2 == 0)) {
                        // 都是奇数执行
                        int c = columnNum - 1 / size;
                        for (int i = 0; i < size; i++) {
                            CellRangeAddress cellAddresses1;
                            if (size - i <= 1) {
                                cellAddresses1 = new CellRangeAddress(ind, ind, i * c, i * c + c);
                            } else {
                                cellAddresses1 = new CellRangeAddress(ind, ind, i * c, i * c + c - 1);
                            }
                            sheet.addMergedRegion(cellAddresses1);
                            XSSFCell c0 = bottom.createCell(cellAddresses1.getFirstColumn());
                            c0.setCellValue(map.get(i));
                            c0.setCellStyle(bottomStyle);
                            for (int n = cellAddresses1.getFirstColumn() + 1; n <= cellAddresses1.getLastColumn(); n++) {
                                XSSFCell cn = bottom.createCell(n);
                                cn.setCellStyle(bottomStyle);
                            }
                        }
                    } else {
                        // 奇偶不同
                        int c = (columnNum + 1) / size;
                        for (int i = 0; i < size; i++) {
                            CellRangeAddress cellAddresses2;
                            if (size - i <= 1) {
                                cellAddresses2 = new CellRangeAddress(ind, ind, i * c, i * c + c - 2);
                            } else {
                                cellAddresses2 = new CellRangeAddress(ind, ind, i * c, i * c + c - 1);
                            }
                            sheet.addMergedRegion(cellAddresses2);
                            XSSFCell c0 = bottom.createCell(cellAddresses2.getFirstColumn());
                            c0.setCellValue(map.get(i));
                            c0.setCellStyle(bottomStyle);
                            for (int n = cellAddresses2.getFirstColumn() + 1; n <= cellAddresses2.getLastColumn(); n++) {
                                XSSFCell cn = bottom.createCell(n);
                                cn.setCellStyle(bottomStyle);
                            }
                        }
                    }
                    ind++;
                }
            }

            // 输出Excel文件
            OutputStream output = response.getOutputStream();
            response.reset();
            wb.write(output);
            wb.close();
            // 设置文件头
            response.setHeader("Content-Disposition",
                    "attchement;filename=" + new String((sheetName + ".xls").getBytes("gb2312"), "ISO8859-1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}