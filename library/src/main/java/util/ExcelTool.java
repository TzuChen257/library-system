package util;

import model.Books;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 工具：POI 產生匯入範本、讀取書籍 Excel
 */
public class ExcelTool {

    public static File exportBooksImportTemplate(File outFile) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sh = wb.createSheet("books");
            Row h = sh.createRow(0);
            String[] cols = {"isbn","title","author","publisher","publish_year","edition","category","call_number","price","state"};
            for (int i = 0; i < cols.length; i++) h.createCell(i).setCellValue(cols[i]);

            // sample row
            Row r = sh.createRow(1);
            r.createCell(0).setCellValue("978xxxxxxxxxx");
            r.createCell(1).setCellValue("書名");
            r.createCell(2).setCellValue("作者;譯者");
            r.createCell(3).setCellValue("出版社");
            r.createCell(4).setCellValue("2020");
            r.createCell(5).setCellValue("1");
            r.createCell(6).setCellValue("100 或 A-Z");
            r.createCell(7).setCellValue("索書號");
            r.createCell(8).setCellValue(399);
            r.createCell(9).setCellValue(0);

            for (int i=0;i<cols.length;i++) sh.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                wb.write(fos);
            }
            return outFile;
        } catch (Exception e) {
            throw new RuntimeException("exportBooksImportTemplate failed: " + e.getMessage(), e);
        }
    }

    public static List<Books> readBooksFromExcel(File xlsx) {
        List<Books> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(xlsx);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sh = wb.getSheetAt(0);

            // 預設位置（完全配合您的 books_import_template 欄位順序）
            int idxIsbn = 0;
            int idxTitle = 1;
            int idxAuthor = 2;
            int idxPublisher = 3;
            int idxPublishYear = 4;
            int idxEdition = 5;
            int idxCategory = 6;
            int idxCallNumber = 7;
            int idxPrice = 8;
            int idxState = 9;

            // --- 修改點：完全略過 row(0) 的標題判斷，直接從 row(1) 開始讀取書籍資料 ---
            for (int i = 1; i <= sh.getLastRowNum(); i++) {
                Row row = sh.getRow(i);
                if (row == null) continue;
                
                String title = getString(row.getCell(idxTitle));
                // 若書名為空，則視為無效資料並跳過
                if (title == null || title.trim().isEmpty()) continue;

                Books b = new Books();
                b.setBook_barcode(Tool.newBookBarcode());
                b.setIsbn(getString(row.getCell(idxIsbn)));
                b.setTitle(title);
                b.setAuthor(getString(row.getCell(idxAuthor)));
                b.setPublisher(getString(row.getCell(idxPublisher)));
                b.setPublish_year(getString(row.getCell(idxPublishYear)));
                b.setEdition(getString(row.getCell(idxEdition)));
                b.setCategory(getString(row.getCell(idxCategory)));
                b.setCall_number(getString(row.getCell(idxCallNumber)));
                b.setPrice((int)getNumeric(row.getCell(idxPrice)));
                b.setState((int)getNumeric(row.getCell(idxState)));
                list.add(b);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("readBooksFromExcel failed: " + e.getMessage(), e);
        }
    }

    private static String getString(Cell c) {
        if (c == null) return "";
        c.setCellType(CellType.STRING);
        return c.getStringCellValue();
    }

    private static double getNumeric(Cell c) {
        if (c == null) return 0;
        if (c.getCellType() == CellType.NUMERIC) return c.getNumericCellValue();
        if (c.getCellType() == CellType.STRING) {
            try { return Double.parseDouble(c.getStringCellValue()); } catch (Exception ignore) { return 0; }
        }
        return 0;
    }


    /**
     * 報表輸出（簡化版）：依年份輸出 4 個 sheet
     * - 每月閱讀人次（以借閱紀錄筆數計算，可重複讀者）
     * - 每月借出冊數（借閱紀錄數）
     * - 讀者借閱數量（每位讀者借閱筆數 + 類別借閱筆數）
     * - 書籍借閱數量（可輸出 TopN）
     *
     * 你原規格希望含長條圖；POI 做圖較複雜，這裡先輸出可直接做圖的表格資料。
     */
    public static File exportReportWorkbook(int year, int topN, File outFile) {
        try (Workbook wb = new XSSFWorkbook()) {

            // sheet 1
            Sheet s1 = wb.createSheet("每月閱讀人次");
            // 需求：閱讀人次=被閱讀幾次，可重複讀者
            String[] h1 = new String[]{"月份", "閱讀人次(可重複讀者)"};
            Row r0 = s1.createRow(0);
            for (int i = 0; i < h1.length; i++) r0.createCell(i).setCellValue(h1[i]);

            // sheet 2
            Sheet s2 = wb.createSheet("每月借出冊數");
            String[] h2 = new String[]{"月份", "借出冊數(借閱筆數)"};
            Row r20 = s2.createRow(0);
            for (int i = 0; i < h2.length; i++) r20.createCell(i).setCellValue(h2[i]);

            // sheet 3
            Sheet s3 = wb.createSheet("讀者借閱數量");
            Row r30 = s3.createRow(0);
            r30.createCell(0).setCellValue("讀者ID");
            r30.createCell(1).setCellValue("姓名");
            r30.createCell(2).setCellValue("借閱筆數(年)");

            // sheet 4
            Sheet s4 = wb.createSheet("書籍借閱數量");
            Row r40 = s4.createRow(0);
            r40.createCell(0).setCellValue("排行");
            r40.createCell(1).setCellValue("條碼");
            r40.createCell(2).setCellValue("書名");
            r40.createCell(3).setCellValue("借閱筆數(年)");

            // query via JDBC
            try (java.sql.Connection conn = Tool.getDB()) {

                // monthly readers (可重複讀者)：以借閱紀錄筆數統計
                String q1 = "SELECT MONTH(borrow_date) m, COUNT(*) c " +
                        "FROM borrow_records WHERE YEAR(borrow_date)=? GROUP BY MONTH(borrow_date) ORDER BY m";
                try (java.sql.PreparedStatement ps = conn.prepareStatement(q1)) {
                    ps.setInt(1, year);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        int row = 1;
                        while (rs.next()) {
                            Row rr = s1.createRow(row++);
                            rr.createCell(0).setCellValue(rs.getInt("m"));
                            rr.createCell(1).setCellValue(rs.getInt("c"));
                        }
                    }
                }

                // monthly borrows
                String q2 = "SELECT MONTH(borrow_date) m, COUNT(*) c " +
                        "FROM borrow_records WHERE YEAR(borrow_date)=? GROUP BY MONTH(borrow_date) ORDER BY m";
                try (java.sql.PreparedStatement ps = conn.prepareStatement(q2)) {
                    ps.setInt(1, year);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        int row = 1;
                        while (rs.next()) {
                            Row rr = s2.createRow(row++);
                            rr.createCell(0).setCellValue(rs.getInt("m"));
                            rr.createCell(1).setCellValue(rs.getInt("c"));
                        }
                    }
                }

                // reader borrow count
                String q3 = "SELECT r.reader_id, r.reader_name, COUNT(br.id) c " +
                        "FROM readers r LEFT JOIN borrow_records br " +
                        "ON r.reader_id = br.reader_id AND YEAR(br.borrow_date)=? " +
                        "GROUP BY r.reader_id, r.reader_name ORDER BY c DESC";
                try (java.sql.PreparedStatement ps = conn.prepareStatement(q3)) {
                    ps.setInt(1, year);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        int row = 1;
                        while (rs.next()) {
                            Row rr = s3.createRow(row++);
                            rr.createCell(0).setCellValue(rs.getString("reader_id"));
                            rr.createCell(1).setCellValue(rs.getString("reader_name"));
                            rr.createCell(2).setCellValue(rs.getInt("c"));
                        }
                    }
                }

                // top books borrow count
                String q4 = "SELECT b.book_barcode, b.title, COUNT(br.id) c " +
                        "FROM books b JOIN borrow_records br " +
                        "ON b.book_barcode = br.book_barcode " +
                        "WHERE YEAR(br.borrow_date)=? " +
                        "GROUP BY b.book_barcode, b.title ORDER BY c DESC LIMIT ?";
                try (java.sql.PreparedStatement ps = conn.prepareStatement(q4)) {
                    ps.setInt(1, year);
                    ps.setInt(2, topN);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        int row = 1;
                        int rank = 1;
                        while (rs.next()) {
                            Row rr = s4.createRow(row++);
                            rr.createCell(0).setCellValue(rank++);
                            rr.createCell(1).setCellValue(rs.getString("book_barcode"));
                            rr.createCell(2).setCellValue(rs.getString("title"));
                            rr.createCell(3).setCellValue(rs.getInt("c"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 欄寬：autoSize 後補最小寬度，避免中文字被截斷
            autoSizeWithMinWidth(s1, h1.length, 14);
            autoSizeWithMinWidth(s2, h2.length, 14);
            autoSizeWithMinWidth(s3, 3, 14);
            autoSizeWithMinWidth(s4, 4, 14);

            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                wb.write(fos);
            }
            return outFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** autoSizeColumn 在不同 OS/字型下可能偏窄，這裡補一個最小欄寬（以字元數估算）。 */
    private static void autoSizeWithMinWidth(Sheet sheet, int columns, int minChars) {
        for (int i = 0; i < columns; i++) {
            sheet.autoSizeColumn(i);
            int min = minChars * 256;
            int current = sheet.getColumnWidth(i);
            if (current < min) sheet.setColumnWidth(i, min);
        }
    }
}
