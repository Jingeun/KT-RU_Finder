import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Data {
	public String RU_ID, RU_NAME;
	public String Frequence;
	public double Latitude, Longitude, distance;
	public String City, Gu, Dong;
	public int ShortData;
	
	
	public Data(String[] d) {
		this.RU_ID = d[0];
		this.RU_NAME = d[1];
		this.Frequence = d[2];
		this.Latitude = Translate_GPS(d[3]);
		this.Longitude = Translate_GPS(d[4]);
		this.City = d[5];
		this.Gu = d[6];
		this.Dong = d[7];
		this.ShortData = -1;
		this.distance = Double.MAX_VALUE;
//		System.out.println(this.RU_ID+" : "+this.Latitude+" "+this.Longitude);
	}
	public double Translate_GPS(String S){
		if(S.equals("") || S.length()<1 || S == null || S.equals("false")) 
			return 0;
		String T[] = S.split("-");
		double pos = Double.parseDouble(T[0].trim()) + (Double.parseDouble(T[1].trim())/(double)60) + (Double.parseDouble(T[2].trim())/(double)3600);
		NumberFormat df = new DecimalFormat("#.000000");
		return Double.parseDouble(df.format(pos));
	}
}
