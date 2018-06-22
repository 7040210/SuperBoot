package org.superboot.utils;


import org.apache.commons.lang.StringUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.List;

/**
 * <b> 图片合并工具 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class ImgUtil {


    /**
     * 合并PNG图片
     *
     * @param inputFileNameList 需要合并的文件
     * @param outputFileName    合并后的文件
     * @param isX               TRUE为横排 FALSE为竖排
     */
    public void append(List<String> inputFileNameList, String outputFileName, boolean isX) {
        if (inputFileNameList == null || inputFileNameList.size() == 0) {
            return;
        }

        try {
            boolean isFirstPng = true;
            BufferedImage outputImg = null;
            int outputImgW = 0;
            int outputImgH = 0;
            for (String pngFileName : inputFileNameList) {
                if (isFirstPng) {
                    isFirstPng = false;
                    outputImg = ImageIO.read(new File(pngFileName));
                    outputImgW = outputImg.getWidth();
                    outputImgH = outputImg.getHeight();
                } else {
                    BufferedImage appendImg = ImageIO.read(new File(pngFileName));
                    int appendImgW = appendImg.getWidth();
                    int appendImgH = appendImg.getHeight();

                    if (isX) {
                        outputImgW = outputImgW + appendImgW;
                        outputImgH = outputImgH > appendImgH ? outputImgH : appendImgH;
                    } else {
                        outputImgW = outputImgW > appendImgW ? outputImgW : appendImgW;
                        outputImgH = outputImgH + appendImgH;
                    }
                    Graphics2D g2d = outputImg.createGraphics();
                    BufferedImage imageNew = g2d.getDeviceConfiguration().createCompatibleImage(outputImgW, outputImgH,
                            Transparency.TRANSLUCENT);
                    g2d.dispose();
                    g2d = imageNew.createGraphics();

                    int oldImgW = outputImg.getWidth();
                    int oldImgH = outputImg.getHeight();
                    g2d.drawImage(outputImg, 0, 0, oldImgW, oldImgH, null);
                    if (isX) {
                        g2d.drawImage(appendImg, oldImgW, 0, appendImgW, appendImgH, null);
                    } else {
                        g2d.drawImage(appendImg, 0, oldImgH, appendImgW, appendImgH, null);
                    }

                    g2d.dispose();
                    outputImg = imageNew;
                }
            }
            writeImageLocal(outputFileName, outputImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeImageLocal(String fileName, BufferedImage image) {
        if (fileName != null && image != null) {
            try {
                File file = new File(fileName);
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断文件是否为图片
     *
     * @param extName
     * @return
     */
    public static boolean isImg(String extName) {
        if (null != extName) {
            if (extName.toLowerCase().endsWith("jpg")
                    || extName.toLowerCase().endsWith("jpeg")
                    || extName.toLowerCase().endsWith("gif")
                    || extName.toLowerCase().endsWith("png")
                    || extName.toLowerCase().endsWith("bmp")) {
                return true;
            }
        }

        return false;
    }


    /**
     * 加载图片信息
     *
     * @param imageFileName 文件名称
     * @return
     * @throws IOException
     */
    public static BufferedImage loadImgFile(String imageFileName) throws IOException {
        return ImageIO.read(new File(imageFileName));
    }

    /**
     * 图片裁剪
     *
     * @param imgFileName 图片地址
     * @param x           X坐标
     * @param y           Y坐标
     * @param tarWidth    图片宽度
     * @param tarHeight   图片高度
     * @throws FileNotFoundException
     */
    public static BufferedImage cutTo(BufferedImage imgFileName, int x, int y, int tarWidth, int tarHeight) throws IOException {
        if (imgFileName != null) {
            int iSrcWidth = imgFileName.getWidth(); // 得到源图宽
            int iSrcHeight = imgFileName.getHeight(); // 得到源图长

            // 剪裁 判断剪裁框是否越界，越界则取选择框所能选择的图片内容
            if (x + tarWidth > iSrcWidth) {
                tarWidth = iSrcWidth - x;
            }
            if (y + tarHeight > iSrcHeight) {
                tarHeight = iSrcHeight - y;
            }

            if (0 > tarWidth || 0 > tarHeight) {
                return null;
            }
            return imgFileName.getSubimage(x, y, tarWidth, tarHeight);
        }
        return null;
    }

    /**
     * 图片缩放 不生成新的图片
     *
     * @param srcimg    图片
     * @param tarWidth  缩放宽度
     * @param tarHeight 缩放高度
     */
    public static BufferedImage zoomTo(BufferedImage srcimg, int tarWidth, int tarHeight) {
        BufferedImage tagImage = new BufferedImage(tarWidth, tarHeight,
                BufferedImage.TYPE_INT_RGB); // 缩放图像
        Image image = srcimg.getScaledInstance(tarWidth, tarHeight,
                Image.SCALE_SMOOTH);
        Graphics g = tagImage.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制目标图
        g.dispose();
        return tagImage;
    }

    /**
     * 保存图片
     *
     * @param srcimg     图片
     * @param fileName   文件名
     * @param formatName 文件类型
     * @throws IOException
     */
    public static void saveImg(BufferedImage srcimg, String fileName, String formatName) {
        // 写文件
        FileOutputStream out = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(srcimg, formatName, bos);// 输出到bos
            out = new FileOutputStream(fileName);
            out.write(bos.toByteArray()); // 写文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 缩放图片 生成新的图片
     *
     * @param srcFile    来源图片地址
     * @param dstFile    保存图片地址
     * @param width      宽度
     * @param height     高度
     * @param formatName 文件类型
     * @return
     */
    public static boolean zoomImage(String srcFile, String dstFile, int width, int height, String formatName) {
        try {
            BufferedImage img = loadImgFile(srcFile);
            img = zoomTo(img, width, height);
            saveImg(img, dstFile, formatName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 不缩小分辨率的压缩图片
     *
     * @param srcFilePath 传入文件
     * @return
     */
    public static boolean compressPic(String srcFilePath) {
        File file = null;
        BufferedImage src = null;
        FileOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;

        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWriteParams = new JPEGImageWriteParam(null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality((float) 0.3);
        imgWriteParams.setProgressiveMode(JPEGImageWriteParam.MODE_DISABLED);
        ColorModel colorModel = ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel
                .createCompatibleSampleModel(16, 16)));

        try {
            if (StringUtils.isEmpty(srcFilePath)) {
                return false;
            } else {
                file = new File(srcFilePath);
                src = ImageIO.read(file);
                out = new FileOutputStream(file.getPath() + "_c");

                imgWrier.reset();
                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造
                imgWrier.setOutput(ImageIO.createImageOutputStream(out));
                // 调用write方法，就可以向输入流写图片
                imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 不缩小分辨率的压缩图片
     *
     * @param data 图片数组对象
     * @return
     */
    public static byte[] compressPic(byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);

        BufferedImage src = null;
        ByteArrayOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;

        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWriteParams = new JPEGImageWriteParam(null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality((float) 0.3);

        imgWriteParams.setProgressiveMode(JPEGImageWriteParam.MODE_DISABLED);
        ColorModel colorModel = ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel
                .createCompatibleSampleModel(16, 16)));

        try {
            src = ImageIO.read(is);
            out = new ByteArrayOutputStream(data.length);

            imgWrier.reset();
            // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造
            imgWrier.setOutput(ImageIO.createImageOutputStream(out));
            // 调用write方法，就可以向输入流写图片
            imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);

            out.flush();
            out.close();
            is.close();
            data = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    /**
     * 旋转图片
     *
     * @param src   图片
     * @param angel 旋转角度
     * @return
     */
    public static BufferedImage rotateImg(Image src, double angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    /**
     * 执行图片旋转
     *
     * @param src
     * @param angel
     * @return
     */
    public static Rectangle calcRotatedSize(Rectangle src, double angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }
}
