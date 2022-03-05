package ch.bailu.gtk.bridge;

import java.awt.geom.AffineTransform;

public class Matrix {


    /**
     * Translate awt.geom.AffineTransform to cairo.Matrix
     *
     * names and relations:
     *   flatmatrix[0]  m00 xx scaleX
     *   flatmatrix[1]  m10 yx shearY
     *   flatmatrix[2]  m01 xy shearX
     *   flatmatrix[3]  m11 yy scaleY
     */
    public static ch.bailu.gtk.cairo.Matrix toCairoMatrix(AffineTransform transform) {
        var matrix = new ch.bailu.gtk.cairo.Matrix();
        matrix.init(
                transform.getScaleX(),
                transform.getShearY(),
                transform.getShearX(),
                transform.getScaleY(),
                transform.getTranslateX(),
                transform.getTranslateY());
        return matrix;
    }
}
