package com.icicles.wmms.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

public class QrUtils {

        private static final String QR_CODE_IMAGE_PATH = "D://MyQRCode.png";

    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            HashMap<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height,hints);

            Path path = FileSystems.getDefault().getPath(filePath);

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        }

        public static void main(String[] args) {
            try {
                generateQRCodeImage("交费金额：200元\n应交电费：21元\n账户剩余：179元\n交费时间：2020-07-24 14:20:20", 100, 100, QR_CODE_IMAGE_PATH);
            } catch (WriterException e) {
                System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
            }

        }


}
