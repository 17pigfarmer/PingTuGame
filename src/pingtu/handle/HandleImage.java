package pingtu.handle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;

public class HandleImage {
	
	public void deleteAll() {
		String pathName = this.getClass().getResource("/img/subImg/").getFile();
		File file = new File(pathName);
		if(file.isDirectory()) {
			String [] fileArr = file.list()	;
			if(fileArr.length>0) {
				for(int i=0;i<fileArr.length;i++) {
					String fileName = this.getClass().getResource("/img/subImg/").getFile()+fileArr[i];
					File f = new File(fileName);
					if(f.isFile())
						f.delete();
				}
					
			}
		}
	}
	
	public void cuttingImage(int size,int rowSize,int colSize,String preName,String imgName) {
		String fileName =  this.getClass().getResource("/img/"+imgName).getFile();
		File file = new File(fileName);
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedImage bi = ImageIO.read(fis);
			fis.close();
			
			for(int row=0;row<rowSize;row++) {
				for(int col=0;col<colSize;col++) {
					int imgIndex = row*colSize+col+1;
					String smallFileName = preName+"_"+imgIndex+".jpg";
					String pathAndName = this.getClass().getResource("/img/subImg/").getFile()+smallFileName;
					BufferedImage smallImg = bi.getSubimage(col*size, row*size, size, size);
					FileOutputStream fos = new FileOutputStream(pathAndName);
					ImageIO.write(smallImg, "jpg", fos);
					fos.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HandleImage hi = new HandleImage();
		hi.deleteAll();
		hi.cuttingImage(500/5, 5, 5, "img_1", "img_1.jpg");
	}

}
