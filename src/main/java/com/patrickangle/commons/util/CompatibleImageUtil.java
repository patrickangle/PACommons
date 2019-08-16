package com.patrickangle.commons.util;

import com.patrickangle.commons.logging.Logging;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.util.Objects;

/**
 * Utilities for working with BufferedImage and VolatileImage objects focused on
 * performance. All methods strive to keep images accelerated as long as
 * possible, and will log a warning if they have to unmanage an image. There are
 * also utilities for converting an image to a managed image for situations
 * where an image starts unmanaged, but could later benefit from being managed.
 *
 * @author Patrick Angle
 */
public class CompatibleImageUtil {

    /**
     * Returns the default GraphicsConfiguration for the default ScreenDevice
     * for the local GraphicsEnvironment.
     *
     * @return
     */
    private static GraphicsConfiguration defaultGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    /**
     * Returns a VolatileImage with a data layout and color model compatible
     * with the local GraphicsEnvironment's default ScreenDevice's default
     * GraphicsConfiguration. The returned VolatileImage may have data that is
     * stored optimally for the underlying graphics device and may therefore
     * benefit from platform-specific rendering acceleration.
     *
     * @param width
     * @param height
     * @param transparency
     * @return
     */
    public static VolatileImage compatibleVolatileImage(final int width, final int height, final int transparency) {
        return defaultGraphicsConfiguration().createCompatibleVolatileImage(width, height, transparency);
    }

    /**
     * Returns a BufferedImage that supports the specified transparency and has
     * a data layout and color model compatible with this GraphicsConfiguration.
     * This method has nothing to do with memory-mapping a device. The returned
     * BufferedImage has a layout and color model that can be optimally blitted
     * to a device with the local GraphicsEnvironment's default ScreenDevice's
     * default GraphicsConfiguration
     *
     * @param width
     * @param height
     * @param transparency
     * @return
     */
    public static BufferedImage compatibleBufferedImage(final int width, final int height, final int transparency) {
        return defaultGraphicsConfiguration().createCompatibleImage(width, height, transparency);
    }

    /**
     * Returns a BufferedImage that is a duplicate of the provided
     * BufferedImage. The returned BufferedImage has a layout and color model
     * that can be optimally blitted to a device with the local
     * GraphicsEnvironment's default ScreenDevice's default
     * GraphicsConfiguration.
     *
     * @param bufferedImage
     * @return
     */
    public static BufferedImage duplicateCompatibleBufferedImageFrom(final BufferedImage bufferedImage) {
        BufferedImage newBufferedImage = compatibleBufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getTransparency());

        Graphics g = newBufferedImage.getGraphics();
        g.drawImage(bufferedImage, 0, 0, null);
        g.dispose();

        return newBufferedImage;
    }

    /**
     * Returns a BufferedImage that is a duplicate of the provided
     * VolatileImage. The returned BufferedImage has a layout and color model
     * that can be optimally blitted to a device with the local
     * GraphicsEnvironment's default ScreenDevice's default
     * GraphicsConfiguration.
     *
     * @param volatileImage
     * @return
     */
    public static BufferedImage duplicateCompatibleBufferedImageFrom(final VolatileImage volatileImage) {
        BufferedImage newBufferedImage = compatibleBufferedImage(volatileImage.getWidth(), volatileImage.getHeight(), volatileImage.getTransparency());

        Graphics g = newBufferedImage.getGraphics();
        g.drawImage(volatileImage, 0, 0, null);
        g.dispose();

        return newBufferedImage;
    }

    /**
     * Returns a BufferedImage that has a layout and color model that can be
     * optimally blitted to a device with the local GraphicsEnvironment's
     * default ScreenDevice's default GraphicsConfiguration. If the provided
     * image is already optimally stored, then the image is simply returned. If
     * the provided image is not currently optimal, it is duplicated using the
     * duplicateCompatibleBufferedImageFrom(bufferedImage) function.
     *
     * @param bufferedImage
     * @return
     */
    public static BufferedImage compatibleBufferedImageFrom(final BufferedImage bufferedImage) {
        if (isCompatibleBufferedImage(bufferedImage)) {
            return bufferedImage;
        } else {
            return duplicateCompatibleBufferedImageFrom(bufferedImage);
        }
    }

    /**
     * Returns true if the provided BufferedImage has a layout and color model
     * that can be optimally blitted to a device with the local
     * GraphicsEnvironment's default ScreenDevice's default
     * GraphicsConfiguration.
     * <p>
     * Specifically, the ColorModel of the provided image is checked against
     * that of the default GraphicsConfiguration, and the image type is checked
     * to make sure it is equal to BufferedImage.TYPE_INT_ARGB,
     * BufferedImage.TYPE_INT_ARGB_PRE, or BufferedImage.TYPE_INT_RGB.
     *
     * @param bufferedImage
     * @return
     */
    public static boolean isCompatibleBufferedImage(final BufferedImage bufferedImage) {
        int imageType = bufferedImage.getType();
        return (bufferedImage.getColorModel().equals(defaultGraphicsConfiguration().getColorModel())
                || bufferedImage.getColorModel().equals(defaultGraphicsConfiguration().getColorModel(Transparency.TRANSLUCENT))) && (imageType == BufferedImage.TYPE_INT_ARGB
                || imageType == BufferedImage.TYPE_INT_ARGB_PRE
                || imageType == BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Returns a BufferedImage that has a layout and color model that can be
     * optimally blitted to a device with the local GraphicsEnvironment's
     * default ScreenDevice's default GraphicsConfiguration. The image is then
     *
     * @param pixels
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage compatibleBufferedImageFrom(final int[] pixels, final int width, final int height) {
        final BufferedImage bufferedImage = compatibleBufferedImage(width, height, BufferedImage.TRANSLUCENT);
        setPixelsOn(bufferedImage, pixels);
        return bufferedImage;
    }

    /**
     * Returns an array of pixels, stored as integers, from a BufferedImage. The
     * pixels are grabbed from a rectangular area specified. Calling this method
     * on an image of type other than BufferedImage.TYPE_INT_ARGB or
     * BufferedImage.TYPE_INT_RGB will unmanage the image.
     *
     * @param bufferedImage the source image
     * @return an array of pixels stored as integers
     */
    public static int[] getPixelsFrom(final BufferedImage bufferedImage) {
        return CompatibleImageUtil.getPixelsFrom(bufferedImage, null);
    }

    /**
     * Returns an array of pixels, stored as integers, from a BufferedImage. The
     * pixels are grabbed from a rectangular area specified. Calling this method
     * on an image of type other than BufferedImage.TYPE_INT_ARGB or
     * BufferedImage.TYPE_INT_RGB will unmanage the image.
     *
     * @param bufferedImage the source image
     * @param pixels a pre-allocated array of pixels of size width * height; can
     * be null
     * @return an array of pixels stored as integers
     * @throws IllegalArgumentException if pixels is non-null and of length &lt;
     * width * height;
     */
    public static int[] getPixelsFrom(final BufferedImage bufferedImage, int[] pixels) {
        return CompatibleImageUtil.getPixelsFrom(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels);
    }

    /**
     * Returns an array of pixels, stored as integers, from a BufferedImage. The
     * pixels are grabbed from a rectangular area specified. Calling this method
     * on an image of type other than BufferedImage.TYPE_INT_ARGB or
     * BufferedImage.TYPE_INT_RGB will unmanage the image.
     *
     * @param bufferedImage the source image
     * @param x the x location at which to start grabbing pixels
     * @param y the y location at which to start grabbing pixels
     * @param width the width of the rectangle of pixels to grab
     * @param height the height of the rectangle of pixels to grab
     * @return an array of pixels stored as integers
     */
    public static int[] getPixelsFrom(final BufferedImage bufferedImage, final int x, final int y, final int width, final int height) {
        return CompatibleImageUtil.getPixelsFrom(bufferedImage, x, y, width, height, null);
    }

    /**
     * Returns an array of pixels, stored as integers, from a BufferedImage. The
     * pixels are grabbed from a rectangular area specified. Calling this method
     * on an image of type other than BufferedImage.TYPE_INT_ARGB,
     * BufferedImage.TYPE_INT_ARGB_PRE, or BufferedImage.TYPE_INT_RGB will
     * unmanage the image.
     *
     * @param bufferedImage the source image
     * @param x the x location at which to start reading pixels
     * @param y the y location at which to start reading pixels
     * @param width the width of the rectangle of pixels to read
     * @param height the height of the rectangle of pixels to read
     * @param pixels a pre-allocated array of pixels of size width * height; can
     * be null
     * @return an array of pixels stored as integers
     * @throws IllegalArgumentException if pixels is non-null and of length &lt;
     * width * height;
     */
    public static int[] getPixelsFrom(final BufferedImage bufferedImage, final int x, final int y, final int width, final int height, int[] pixels) {
        if (width == 0 || height == 0) {
            return new int[0];
        }

        if (pixels == null) {
            pixels = new int[width * height];
        } else if (pixels.length < width * height) {
            throw new IllegalArgumentException("Pixels array must have a length >= width * height");
        }

        final int imageType = bufferedImage.getType();

        if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB || imageType == BufferedImage.TYPE_INT_ARGB_PRE) {
            final Raster raster = bufferedImage.getRaster();
            return (int[]) raster.getDataElements(x, y, width, height, pixels);
        } else {
            Logging.trace(CompatibleImageUtil.class, "getPixelsFrom(...) had to unmanage image to read pixels. Image is no longer accelerated.");
            return bufferedImage.getRGB(x, y, width, height, pixels, 0, width);
        }
    }

    /**
     * Writes a rectangular area of pixels on the destination BufferedImage.
     * Calling this method on an image of type other than
     * BufferedImage.TYPE_INT_ARGB or BufferedImage.TYPE_INT_RGB will unmanage
     * the image.
     *
     * @param bufferedImage the destination image
     * @param pixels an array of pixels stored as integers.
     * @throws IllegalArgumentException if pixels is non-null and of length &lt;
     * width * height
     */
    public static void setPixelsOn(final BufferedImage bufferedImage, final int[] pixels) {
        setPixelsOn(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels);
    }

    /**
     * Writes a rectangular area of pixels on the destination BufferedImage.
     * Calling this method on an image of type other than
     * BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_ARGB_PRE, or
     * BufferedImage.TYPE_INT_RGB will unmanage the image.
     *
     * @param bufferedImage the destination image
     * @param x the x location at which to start writing pixels
     * @param y the y location at which to start writing pixels
     * @param width the width of the rectangle of pixels to write
     * @param height the height of the rectangle of pixels to write
     * @param pixels an array of pixels stored as integers.
     * @throws IllegalArgumentException if pixels is non-null and of length &lt;
     * width * height
     */
    public static void setPixelsOn(final BufferedImage bufferedImage, final int x, final int y, final int width, final int height, final int[] pixels) {
        if (pixels == null || width == 0 || height == 0) {
            return;
        } else if (pixels.length < width * height) {
            throw new IllegalArgumentException("Pixels array must have a length >= width * height");
        }

        final int imageType = bufferedImage.getType();

        if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB || imageType == BufferedImage.TYPE_INT_ARGB_PRE) {
            final WritableRaster raster = bufferedImage.getRaster();
            raster.setDataElements(x, y, width, height, pixels);
        } else {
            Logging.trace(CompatibleImageUtil.class, "setPixelsOn(...) had to unmanage image to write pixels. Image is no longer accelerated.");
            bufferedImage.setRGB(x, y, width, height, pixels, 0, width);
        }
    }

    /**
     * Compare two images, attempting to establish if they have the same
     * contents. This operation eventually iterates through every pixel to check
     * equality, but first performs basic checks to see if the images match. As
     * soon as a difference is found, false is returned.
     *
     * @param imageA
     * @param imageB
     * @return
     */
    public static boolean areEqual(BufferedImage imageA, BufferedImage imageB) {
        // If both images are the same object, they match.
        if (Objects.equals(imageA, imageB)) {
            return true;
        }

        // If only one of the images in null, they don't match.
        if (imageA == null ^ imageB == null) {
            return false;
        }

        // If the types don't match, the images don't match.
        if (imageA.getType() != imageB.getType()) {
            return false;
        }

        // The images must be the same size.
        if (imageA.getWidth() != imageB.getWidth() || imageA.getHeight() != imageB.getHeight()) {
            return false;
        }

        int width = imageA.getWidth();
        int height = imageA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imageA.getRGB(x, y) != imageB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}
