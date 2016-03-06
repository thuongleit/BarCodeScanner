package com.thuongleit.babr.view.qrgenerate;

import android.graphics.Bitmap;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.core.AbstractQRCode;
import net.glxn.qrgen.core.exception.QRGenerationException;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.scheme.VCard;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class QRCode extends AbstractQRCode {

    protected final String text;
    private MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig();

    protected QRCode(String text) {
        this.text = text;
        qrWriter = new QRCodeWriter();
    }


    public static QRCode from(String text) {
        return new QRCode(text);
    }


    public static QRCode from(VCard vcard) {
        return new QRCode(vcard.toString());
    }


    public QRCode to(ImageType imageType) {
        this.imageType = imageType;
        return this;
    }


    public QRCode withColor(int onColor, int offColor) {
        matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);
        return this;
    }


    public QRCode withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }


    public QRCode withCharset(String charset) {
        return withHint(EncodeHintType.CHARACTER_SET, charset);
    }


    public QRCode withErrorCorrection(ErrorCorrectionLevel level) {
        return withHint(EncodeHintType.ERROR_CORRECTION, level);
    }


    public QRCode withHint(EncodeHintType hintType, Object value) {
        hints.put(hintType, value);
        return this;
    }

    
    public Bitmap bitmap() {
        try {
            return MatrixToImageWriter.toBitmap(createMatrix(text), matrixToImageConfig);
        } catch (WriterException e) {
            throw new QRGenerationException("Failed to create QR image from text due to underlying exception", e);
        }
    }

    @Override
    public File file() {
        File file;
        try {
            file = createTempFile();
            MatrixToImageWriter.writeToFile(createMatrix(text), imageType.toString(), file, matrixToImageConfig);
        } catch (Exception e) {
            throw new QRGenerationException("Failed to create QR image from text due to underlying exception", e);
        }

        return file;
    }

    @Override
    public File file(String name) {
        File file;
        try {
            file = createTempFile(name);
            MatrixToImageWriter.writeToFile(createMatrix(text), imageType.toString(), file, matrixToImageConfig);
        } catch (Exception e) {
            throw new QRGenerationException("Failed to create QR image from text due to underlying exception", e);
        }

        return file;
    }

    @Override
    protected void writeToStream(OutputStream stream) throws IOException, WriterException {
        MatrixToImageWriter.writeToStream(createMatrix(text), imageType.toString(), stream, matrixToImageConfig);
    }
}
