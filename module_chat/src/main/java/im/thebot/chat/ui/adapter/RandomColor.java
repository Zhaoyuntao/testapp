package im.thebot.chat.ui.adapter;

public class RandomColor {

    private static final int[] ColorArray = {
            0xFFFF7473,
            0xFFFFB44B,
            0xFFFBD84F,
            0xFF79D589,
            0xFF75CCF4,
            0xFF58A7FD,
            0xFF7775EB,
            0xFFF26781,
            0xFFFF7585,
            0xFFf78259,
            0xFFffbd69,
            0xFF75daad,
            0xFF90DCFF,
            0xFF69B6FF,
            0xFF9399ff,
            0xFFff80b0,
            0xFFff7761,
            0xFFf9a11b,
            0xFFfdc23e,
            0xFF8CD790,
            0xFFa3daff,
            0xFF84B1ED,
            0xFFA593E0,
            0xFFFFBBD6
    };

    public static int getColor(String uid) {
        float hashCode = Math.abs(uid.hashCode());
        float i = hashCode / Integer.MAX_VALUE;
        int index = ((int) (i * ColorArray.length)) - 1;
        if (index < 0) {
            index = 0;
        }
        return ColorArray[index];
    }

}
