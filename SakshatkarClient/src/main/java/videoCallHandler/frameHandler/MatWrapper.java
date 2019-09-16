package videoCallHandler.frameHandler;

import org.opencv.core.Mat;

import java.io.Serializable;

public class MatWrapper implements Serializable {
    private byte[] matArray;
    private int rows,cols,type;

    public MatWrapper(Mat mat) {
        rows=mat.rows();
        cols=mat.cols();
        type=mat.type();
        matArray=new byte[(int) (mat.total()*mat.elemSize())];
        mat.get(0,0,matArray);
    }

    public byte[] getMatArray() {
        return matArray;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getType() {
        return type;
    }
}