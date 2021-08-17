import android.opengl.Matrix

class MatrixUtils{
    companion object {
        val TYPE_FITXY: Int = 0
        val TYPE_CENTERCROP = 1
        val TYPE_CENTERINSIDE = 2
        val TYPE_FITSTART = 3
        val TYPE_FITEND = 4


        fun getShowMatrix(
            matrix: FloatArray,
            imgWidth: Int,
            imgHeight: Int,
            viewWidth: Int,
            viewHeight: Int
        ) {
            if ((imgWidth > 0) and (imgHeight > 0) and (viewWidth > 0) and (viewHeight > 0)) {
                val sWhView = viewWidth.toFloat() / viewHeight
                val sWhImg = imgWidth.toFloat() / imgHeight
                val projection = FloatArray(16)
                val camera = FloatArray(16)
                if (sWhImg > sWhView) {
                    Matrix.orthoM(matrix, 0, -sWhView / sWhImg, sWhView / sWhImg, -1f, 1f, 1f, 3f)
                } else {
                    Matrix.orthoM(matrix, 0, -1f, 1f, -sWhImg / sWhView, sWhImg / sWhView, 1f, 3f)
                }
                Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
                Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
            }
        }

        fun getOriginalMatrix(): FloatArray {
            return floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            )
        }
    }
}