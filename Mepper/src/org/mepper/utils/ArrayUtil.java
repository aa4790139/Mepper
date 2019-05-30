/* ArrayUtil.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.mepper.utils;

import java.awt.Point;
import java.awt.Shape;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author liuke
 * @email:  soulnew@gmail.com
 */
public class ArrayUtil {


    /*
    2维数组转1维数组
     */
    public static int[] array2Dto1D(int[][] d2) {
        int hight = d2.length;
        int width = d2[0].length;
        int[] modImgArray = new int[width * hight];
        for (int i = 0; i < hight; i++) {
            System.arraycopy(d2[i], 0, modImgArray, i * width, width);
        }
        return modImgArray;
    }

    /*
    2维数组转1维数组
     */
    public static void array2Dto1D(int[][] d2, int[] d1) {
        int hight = d2.length;
        int width = d2[0].length;
        for (int i = 0; i < hight; i++) {
            System.arraycopy(d2[i], 0, d1, i * width, width);
        }
    }

    /*
    一维数组转二维数组
     */
    public static int[][] array1Dto2D(int[] d1, int imageWidth, int imageHeight) {
        int[][] d2 = new int[imageHeight][imageWidth];
        for (int h = 0; h < imageHeight; h++) {
            System.arraycopy(d1, h * imageWidth, d2[h], 0, imageWidth);
        }
        return d2;
    }

    /*
    一维数组转二维数组
     */
    public static void array1Dto2D(int[] d1, int[][] d2, int imageWidth, int imageHeight) {
        for (int h = 0; h < imageHeight; h++) {
            System.arraycopy(d1, h * imageWidth, d2[h], 0, imageWidth);
        }
    }

    /*
     */
    public static void arrayBig2Small(int[] big, int bigWidth, int[][] small, int startRow, int startCol, int endRow, int endCol) {
        int j = 0;
        for (int i = startRow; i <= endRow; i++, j++) {
//            int a = i * bigWidth + startCol;
//            int b = j * small[0].length;
//            int c = small[0].length;
//            int d = small.length;
            System.arraycopy(big, i * bigWidth + startCol, small[j], 0, endCol - startCol + 1);
        }
    }

    /*
    指定一个起点把一个数组复制到另一个数组,这两个数组可以是任意大小的
     */
    public static int[][] copy2D(Point start, int[][] front, int[][] back) {
        if (start == null) {
            start = new Point(0, 0);
        }
        int[][] dist = new int[back.length][back[0].length];
        for (int i = 0; i < back.length; i++) {
            System.arraycopy(back[i], 0, dist[i], 0, back[0].length);
        }
        int row;
        int col;
        int length;
        int rowDist;
        int colDist;
        if (start.y < 0) {
            row = -start.y;
            rowDist = 0;
        } else {
            row = 0;
            rowDist = start.y;
        }
        if (start.x < 0) {
            col = -start.x;
            colDist = 0;
            if (front[0].length + start.x > back[0].length) {
                length = back[0].length;
            } else {
                length = front[0].length + start.x;
            }
            if (length < 0) {
                length = 0;
            }
        } else {
            col = 0;
            colDist = start.x;
            if (front[0].length + start.x > back[0].length) {
                length = back[0].length - start.x;
            } else {
                length = front[0].length;
            }
        }

        for (; row < front.length && rowDist < dist.length; row++, rowDist++) {
            System.arraycopy(front[row], col, dist[rowDist], colDist, length);
        }
        return dist;
    }

    /*
    深拷贝一个数组
     */
    public static int[][] copy2D2D(int[][] src) {
        int[][] dist = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dist[i], 0, src[0].length);
        }
        return dist;
    }

    /*
    深拷贝一个数组
     */
    public static short[][] copy2D2D(short[][] src) {
        short[][] bs = new short[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, bs[i], 0, src[0].length);
        }
        return bs;
    }

    /*
    深拷贝一个数组
     */
    public static void copy2D2D(int[][] src, int[][] dist) {
        // int[][] dist = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dist[i], 0, src[0].length);
        }
    }

    /*
    指定大小生成一个颜色为白的数组
     */
    public static int[][] createBackGroud(int row, int col) {
        int[][] dist = new int[row][col];
        int fill = 0xff << 24;
        for (int i = 0; i < row; i++) {
            Arrays.fill(dist[i], fill);
        }
        return dist;
    }

    /*
    指定大小生成一个颜色为灰白相间的数组
     * row: the row number of 2-D array
     * col :the col number of 2-D array
     */
    public static int[][] createDeafaultGroud(int row, int col, int Ranger) {
        int[][] dist = new int[row][col];
        //int Ranger=20;
        int c = col / Ranger;
        int colorA = 0xff << 24 | 0xffffff;
        int colorB = 0xff << 24 | 0x888888;
//        boolean signA = true;
//        boolean signB = true;
        for (int i = 0; i < row; i++) {

            for (int j = 0; j < c; j++) {

                int endcol = (j + 1) * Ranger;
                if (endcol > col) {
                    endcol = col;
                }
                if ((i / Ranger + j) % 2 == 0) {
                    //  Arrays.fill( colorA);
                    Arrays.fill(dist[i], j * Ranger, endcol, colorA);
                } else {
                    Arrays.fill(dist[i], j * Ranger, endcol, colorB);
                }
            }
        }
        return dist;
    }

    public static int[][] Big2Small(int[][] big, int dafaultSmallWidth, int dafaultSmallHeight) {

        float scale = (float) big.length / dafaultSmallHeight;

        if (big[0].length / dafaultSmallWidth > scale) {
            scale = big[0].length / dafaultSmallWidth;
        }
        int[][] small = new int[dafaultSmallHeight][dafaultSmallWidth];

        for (int h = 0; h < dafaultSmallHeight; h++) {
            for (int w = 0; w < dafaultSmallWidth; w++) {
                int bigH = (int) (h * scale);
                int bigW = (int) (w * scale);
                if (bigH >= big.length) {
                    bigH = big.length - 1;
                }
                if (bigW >= big[0].length) {
                    bigW = big[0].length - 1;
                }
                small[h][w] = big[bigH][bigW];
            }//end w
        }//end h
        return small;
    }

    /*
    用白色填充数组
     */
    public static void fillColor(int[][] dist) {
        int white = 0xff << 24;
        for (int i = 0; i < dist.length; i++) {
            Arrays.fill(dist[i], white);
        }
    }

    /*
    用白色填充数组
     */
    public static void fillColor(int[][] dist, int color) {
        for (int i = 0; i < dist.length; i++) {
            Arrays.fill(dist[i], color);
        }
    }

    /*
    用蒙板和大树组生成一个小数组
     */
    public static int[][] maskCreateArray(int[][] big, boolean[][] mask, Point start) {
        int[][] alpha = new int[mask.length][mask[0].length];
        for (int row = start.y; (row < start.y + mask.length) && (row < big.length); row++) {
            for (int col = start.x; (col < start.x + mask[0].length) && (col < big[0].length); col++) {
                if (mask[row - start.y][col - start.x]) {
                    alpha[row - start.y][col - start.x] = big[row][col];
                } else {
                    alpha[row - start.y][col - start.x] = 0xffffff;
                }
            }
        }
        return alpha;
    }

    /*
    把一个 boolean[][]的蒙板转换成，short[][] 简单透明度
     */
    public static short[][] maskCreateArray(boolean[][] mask) {
        short[][] alpha = new short[mask.length][mask[0].length];
        for (int row = 0; row < mask.length; row++) {
            for (int col = 0; col < mask[0].length; col++) {
                if (mask[row][col]) {
                    alpha[row][col] = 0xff;
                } else {
                    alpha[row][col] = 0;
                }
            }
        }
        return alpha;
    }

    /*
    把一个shape转换成数组
     */
    public static int[][] shape2Array(int[][] big, Shape shape) {
//        Rectangle2D r = shape.getBounds2D();
        int startx;
        int starty;
        int endx;
        int endy;
        startx = (int) shape.getBounds().getMinX();
        starty = (int) shape.getBounds().getMinY();
        endx = (int) shape.getBounds().getMaxX();
        endy = (int) shape.getBounds().getMaxY();
        int[][] alpha = new int[endy - starty + 1][endx - startx + 1];
        fillColor(alpha, 0xffffff);
        for (int j = starty; j <= endy; j++) {
            for (int i = startx; i <= endx; i++) {
                if (shape.contains(i, j)) {
                    alpha[j - starty][i - startx] = big[j][i];
                }
            } //end j
        } //end i
        return alpha;
    }

   

    /*
    把shape转换成二维的布尔数组
     */
    public static boolean[][] shape2Mask(Shape shape) {

//        Rectangle2D r = shape.getBounds2D();
        int startx;
        int starty;
        int endx;
        int endy;
        startx = (int) shape.getBounds().getMinX();
        starty = (int) shape.getBounds().getMinY();
        endx = (int) shape.getBounds().getMaxX();
        endy = (int) shape.getBounds().getMaxY();
        boolean[][] alpha = new boolean[endy - starty + 1][endx - startx + 1];

        for (int j = 0; j <= endy - starty; j++) {
            for (int i = 0; i <= endx - startx; i++) {
                if (shape.contains(startx + i, starty + j)) {
                    alpha[j][i] = true;
                }
            } //end j
        } //end i
        return alpha;
    }

    /*
    把shape转换成二维的布尔数组
     */
    public static short[][] shape2Short(Shape shape, int feather) {
//        Rectangle2D r = shape.getBounds2D();
        int startx;
        int starty;
        int endx;
        int endy;
        startx = (int) shape.getBounds().getMinX();
        starty = (int) shape.getBounds().getMinY();
        endx = (int) shape.getBounds().getMaxX();
        endy = (int) shape.getBounds().getMaxY();
        short[][] alpha = new short[endy - starty + feather * 2][endx - startx + feather * 2];

        for (int j = feather; j <= alpha.length - feather; j++) {
            for (int i = feather; i <= alpha[0].length - feather; i++) {
                if (shape.contains(startx + i, starty + j)) {
                    alpha[j][i] = 0xff;
                }
            } //end j
        } //end i
        // new CoverOp().getOP(alpha, crateKernel(feather));
        return alpha;
    }

    /*
    指定大小生成平滑模板
     */
    public static float[][] crateKernel(int size) {
        if (size % 2 == 0) {
            size = size + 1;
        }
        float fill = (float) 1 / (size * size);
        float[][] dist = new float[size][size];
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[0].length; j++) {
                dist[i][j] = fill;
            }
        }
        return dist;
    }

	/**
	 * Change the string to integer array.
	 * 
	 * @param text
	 *            the text
	 * @return the int[]
	 */
	public static int[] toIntArray(String text) {
		String data=text.substring(1,text.length()-1);
		StringTokenizer t=new StringTokenizer(data,",");
		int[] array=new int[t.countTokens()];
		for(int i=0,j=t.countTokens();i<j;i++){
			array[i]=Integer.parseInt(t.nextToken().trim());
		}
		return array;
	}
    
    
}
    