package kwdpackage;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Way2smsRunner 
{
	public static void main(String[] args) throws Exception
	{
		//Connect to excel file
		File f=new File("way2smstestdata.xls");
		//Open excel file for reading
		Workbook rwb=Workbook.getWorkbook(f);
		Sheet rsh1=rwb.getSheet(0); 	//0 for Sheet1(tests)
		int nour1=rsh1.getRows();
		int nouc1=rsh1.getColumns();
		Sheet rsh2=rwb.getSheet(1); 	//1 for Sheet2(steps)
		int nour2=rsh2.getRows();
		int nouc2=rsh2.getColumns();
		//Open same excel file for writing test results
		WritableWorkbook wwb=Workbook.createWorkbook(f,rwb);
		WritableSheet wsh1=wwb.getSheet(0);		//0 for Sheet1
		WritableSheet wsh2=wwb.getSheet(1);		//1 for Sheet2
		WritableFont wf=new WritableFont(WritableFont.TIMES,12,WritableFont.BOLD);
		wf.setColour(Colour.GREEN);
		WritableCellFormat cf=new WritableCellFormat(wf);
		cf.setAlignment(Alignment.JUSTIFY);
		cf.setWrap(true);
		//Set name to results column
		SimpleDateFormat sf=new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
		Date dt=new Date();
		String cname=sf.format(dt);
		//Set name to results column in Sheet1
		Label l1=new Label(nouc1,0,cname,cf);
		wsh1.addCell(l1);
		//Set name to results column in Sheet2
		Label l2=new Label(nouc2,0,cname,cf);
		wsh2.addCell(l2);
		//Create Object to methods class
		Way2smsMethods ms=new Way2smsMethods();
		//Collect all methods info using methods class object
		Method m[]=ms.getClass().getMethods();
		//Keyword Driven
		try
		{
			//Calling methods one after other
			//1st row(index=0) have names of columns in Sheet1
			for(int i=0;i<nour1;i++)	//from 2nd row(index=2)
			{
				int flag=0;
				//Get testid and mode from Sheet1
				String tid=rsh1.getCell(0,i).getContents();
				String mode=rsh1.getCell(2,i).getContents();
				if(mode.equalsIgnoreCase("yes"))
				{
					//1st row(index=0) have names of columns in Sheet2
					for(int j=1;j<nour2;j++)	//from 2nd row(index=2)
					{
						String sid=rsh2.getCell(0,j).getContents();
						if(tid.equalsIgnoreCase(sid))
						{
							//Take stepid details from sheet2
							String mn=rsh2.getCell(2,j).getContents();
							String e=rsh2.getCell(3,j).getContents();
							String d=rsh2.getCell(4,j).getContents();
							String c=rsh2.getCell(5,j).getContents();
							System.out.println(mn+" "+e+" "+d+" "+c);
							for(int k=0;k<m.length;k++)
							{
								if(m[k].getName().equals(mn))
								{
									String r=(String)m[k].invoke(ms,e,d,c);
									Label lb=new Label(nouc2,j,r,cf);
									wsh2.addCell(lb);
									if(r.equalsIgnoreCase("Unknown browser"))
									{
										wwb.write();
										wwb.close();
										rwb.close();
										System.exit(0);		//Stop execution forcibly
									}
									if(r.contains("failed") || r.contains("interrupted") || r.contains("Failed") || r.contains("Interrupted"))
									{
										flag=1;
									}
									break;
								} //if closing
							}	//for k closing
						}	//if closing
					}	//for j closing
					if(flag==0)
					{
						Label l=new Label(nouc1,i,"Passed",cf);
						wsh1.addCell(l);
					}
					else
					{
						Label l=new Label(nouc1,i,"Failed",cf);
						wsh1.addCell(l);
					}
				}	//if closing
			}	//for i closing
		}	//try block closing
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		//Save and close excel
		wwb.write();
		wwb.close();
		rwb.close();
	}
}
