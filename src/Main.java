import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	public ArrayList<Data> data;
	public Main(){
		data = new ArrayList<Data>();
	}
	public static void main(String[] args) {
		Main M = new Main();
		M.readData();
		M.Search();
		M.ExportData();
	}
	private void ExportData() {
		String currentDir = System.getProperty("user.dir");
		SimpleDateFormat df = new SimpleDateFormat("yyMMdd_HHmmss");
		Date date = new Date();
			
		try {
			XSSFWorkbook work = new XSSFWorkbook();
			XSSFSheet sheet = work.createSheet();
			XSSFRow row = sheet.createRow(0);   // row 생성
			String menu[] = {"RU_ID", "RU_NAME", "구분", "위도", "경도", "광역시/도", "시군구", "동면읍리", "거리(m)",
					"RU_ID", "RU_NAME", "구분", "위도", "경도", "광역시/도", "시군구", "동면읍리",};
			XSSFCellStyle styleCenter = work.createCellStyle();
			styleCenter.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			int SHEET_SIZE[] = {3650,4500,2000,2800,2800,2800,2800,2800,2800,3650,4500,2000,2800,2800,2800,2800,2800};
			for(int i=0;i<menu.length;i++){
				Cell cell = row.createCell(i);
				cell.setCellValue(menu[i]);
				cell.setCellStyle(styleCenter);
				sheet.setColumnWidth(i, SHEET_SIZE[i]);
			}
			for(int i=0;i<data.size();i++){
				row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(data.get(i).RU_ID);
				row.createCell(1).setCellValue(data.get(i).RU_NAME);
				row.createCell(2).setCellValue(data.get(i).Frequence);
				row.createCell(3).setCellValue(data.get(i).Latitude);
				row.createCell(4).setCellValue(data.get(i).Longitude);
				row.createCell(5).setCellValue(data.get(i).City);
				row.createCell(6).setCellValue(data.get(i).Gu);
				row.createCell(7).setCellValue(data.get(i).Dong);
				row.createCell(8).setCellValue(data.get(i).distance);
				int index = data.get(i).ShortData;
				row.createCell(9).setCellValue(data.get(index).RU_ID);
				row.createCell(10).setCellValue(data.get(index).RU_NAME);
				row.createCell(11).setCellValue(data.get(index).Frequence);
				row.createCell(12).setCellValue(data.get(index).Latitude);
				row.createCell(13).setCellValue(data.get(index).Longitude);
				row.createCell(14).setCellValue(data.get(index).City);
				row.createCell(15).setCellValue(data.get(index).Gu);
				row.createCell(16).setCellValue(data.get(index).Dong);
			}
			String FileName = currentDir+"최단거리_"+df.format(date)+".xlsx";
			FileOutputStream outStream = new FileOutputStream(FileName);
			work.write(outStream);
			outStream.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	private void Search() {
		for(int i=0;i<data.size();i++){
			if(i%100==0)
			System.out.println((i+1)/100+"/"+data.size()/100);
			for(int j=0;j<data.size();j++){
				if(i==j) continue;
				double distance = Distance_Calc(data.get(i).Latitude, data.get(i).Longitude, data.get(j).Latitude, data.get(j).Longitude);
				if(data.get(i).distance>distance){
					data.get(i).ShortData = j;
					data.get(i).distance = distance;
				}
			}
		}
	}
	private double Distance_Calc(double T_lati, double T_long, double R_lati, double R_long) {
		double d2r = Math.PI/180;
	    double dLon = (R_long - T_long) * d2r;
		double dLat = (R_lati - T_lati) * d2r;
		double a = Math.pow(Math.sin(dLat / 2.0), 2)
		               + Math.cos(T_lati * d2r)
		               * Math.cos(R_lati * d2r)
		               * Math.pow(Math.sin(dLon / 2.0), 2);
		double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * 2;
		double distance = c * 6378;
		return distance*1000;
	}
	private void readData() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\ttt.xlsx");
		try {
			XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheet = work.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			for(int i=1;i<rows;i++){
				XSSFRow row = sheet.getRow(i);
				int cells = row.getPhysicalNumberOfCells();
				String d[] = new String[8];
				for(int j=0;j<8;j++){
					XSSFCell cell = row.getCell(j);
					d[j] = cell.toString();
				}
				if(d[3].trim().equals("#N/A") || d[3].trim().equals("-") || d[3].trim().equals("---."))
					continue;
				data.add(new Data(d));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
