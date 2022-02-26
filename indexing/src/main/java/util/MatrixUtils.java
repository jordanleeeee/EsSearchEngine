package util;

import java.util.Arrays;

/**
 * @author Jordan
 */
public class MatrixUtils {
    public static double[] vectorTimesMatrix(double[] vector, double[][] matrix) {
        double[] result = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i] += vector[j] * matrix[j][i];
            }
        }
        return result;
    }

    public static void addValueToVector(double value, double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] += value;
        }
    }

    public static void multiplyValueToVector(double value, double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] *= value;
        }
    }

    public static void normalizeMatrixRow(double[][] matrix) {
        for (double[] doubles : matrix) {
            normalizeVector(doubles);
        }
    }

    public static void normalizeVector(double[] vector) {
        double rowTotal = Arrays.stream(vector).reduce(0, Double::sum);
        for (int i = 0; i < vector.length; i++) {
            if (rowTotal > 0) {
                vector[i] /= rowTotal;
            }
        }
    }
}
