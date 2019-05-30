package org.mepper.utils;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.zhiwu.app.AppManager;
import org.zhiwu.utils.AppLogging;

/**
 *
 * @author: Liu Ke
 * @email:  soulnew@gmail.com
 */
public class ImageUtil {

    public static Image scalImage(Image image, int width, int height) {
        ImageIcon icon = new ImageIcon(image);
        // scale to 64 pix in largest dimension
        Image img = icon.getImage();
        float factor = 1.0f;
        int imageWidth = img.getWidth(null);
        int imageHeigth = img.getHeight(null);
        float f1 = (float) width / imageWidth;
        float f2 = (float) height / imageHeigth;
        factor=f1 <f2?f1:f2;
        return scalImage(img, factor);
    }

    public static Image scalImage(Image img, double factor) {

        if (factor == 1.0 || factor <= 0 
        		|| img.getWidth(null)==0|| img.getHeight(null)==0) { //if the size of willn't be changed,do nothing
            return img;
        }
        Image scaledImage =
                img.getScaledInstance((int) (img.getWidth(null) * factor),
                (int) (img.getHeight(null) * factor),
                Image.SCALE_FAST);
        return scaledImage;
    }

    public static Image scalImage(int[][] image, int smallWidth, int smallHeight) {
        int[] smallImagePixels;

        int[][] smallFront = ArrayUtil.Big2Small(image,smallWidth,smallHeight);

        smallImagePixels = ArrayUtil.array2Dto1D(smallFront);

        MemoryImageSource ms = new MemoryImageSource(smallWidth, smallHeight, smallImagePixels, 0, smallWidth);
        ms.setAnimated(true);
        Image smallImage = new JLabel().createImage(ms);
        return smallImage;
    }

	public static BufferedImage subImage(Image image, Shape shape) {
		return subImage(image, shape, 0, 0);
	}
	

	public static BufferedImage subImage(Image image, Shape shape, int offsetX, int offsetY) {
		Rectangle bounds=shape.getBounds();
		PixelGrabber grabber=new PixelGrabber(image, 
				bounds.x-offsetX, bounds.y-offsetY, bounds.width, bounds.height, true);//XXX
		try {
			grabber.grabPixels(-1);
			Object obj=grabber.getPixels();
			BufferedImage subImage=new BufferedImage(bounds.width, bounds.height, 
					BufferedImage.TYPE_4BYTE_ABGR);
			if(obj instanceof int[]){
				int[] pixels=(int[]) grabber.getPixels();
				for(int i=0;i<bounds.width;i++){
					for(int j=0;j<bounds.height;j++){
						int argb=pixels[j*bounds.width+i];
						if(! shape.contains(i+bounds.x, j+bounds.y)){
							argb=( argb& 0x00ffff);
						}
						subImage.setRGB(i, j, argb);
					}
				}
				return subImage;
			}
//			else { // XX does not support gray here
//				byte[] pixels=(byte[]) grabber.getPixels();
//				for(int i=0;i<bounds.width;i++){
//					for(int j=0;j<bounds.height;j++){
//						int argb=pixels[j*bounds.width+i];
//						if(! shape.contains(i+bounds.x, j+bounds.y)){
//							argb=argb & 0x00ffff;
//						}
//						subImage.setRGB(i, j, argb);
//					}
//				}
//				return subImage;
//			}
		} catch (InterruptedException e) {
			AppLogging.handleException(e);
		} 
		return null;
	}
	
	
	public static int[][] imageTo2DArray(Image image){
		int[][] resval=new int[0][0];
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		resval = ArrayUtil.array1Dto2D(imageToArray(image), imageWidth, imageHeight);
		return resval;
	}
	
	public static int[] imageToArray(Image image){
		int[] resval=new int[0];
		int imageWidth=image.getWidth(null);
		int imageHeight=image.getHeight(null);
		PixelGrabber grabber=new PixelGrabber(
				image, 0,0,imageWidth,imageHeight, true);//XXX
		try {
			grabber.grabPixels(-1);
			Object obj=grabber.getPixels();
			if(obj instanceof int[]){
				 resval=(int[])obj;
			}
		} catch (InterruptedException e) {
			AppLogging.handleException(e);
		} 
		return resval;
	}
	
    
	/**
	 * 用数组生成图片
	 */
	public static Image createImage(int[][] image) {
        int[] savaImage = ArrayUtil.array2Dto1D(image);
        MemoryImageSource source = new MemoryImageSource(image[0].length, image.length, savaImage, 0, image[0].length);
        source.setAnimated(true);
        return new JLabel().createImage(source);
    }
	public static BufferedImage createImage(int[] pixels, int width, int height) {
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				image.setRGB(i,j, pixels[j*width+i]);
			}
		}
		return image;
	}
	
	public static BufferedImage readImageFromResources(String resource){
		String RESOURCE_PATH= AppManager.getResources().getString("resources");
		File f=new File(RESOURCE_PATH+resource);
		if (!f.exists()) {
			System.out.println("icon not exist: "+RESOURCE_PATH+resource);
			//throw new FileNotFoundException("icon not exist: "+RESOURCE_PATH+resource);
		}
		return readImage(f);
	}

	public static BufferedImage readImage(File f) {
		BufferedImage image=null;
		try {
			image =ImageIO.read(f);
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
		return image;
	}

}
