public class MessageCreator {
    private static final String SN_HEARED = "I'm SN:";
    private static final String PORT_HEARED = "Please reply Port :";

    public static String buildWithPort(int port) {
        return PORT_HEARED + port;
    }

    public static int parsePort(String data) {
        if (data.startsWith(PORT_HEARED)) {
            return Integer.parseInt(data.substring(PORT_HEARED.length()));
        }

        return -1;
    }

    public static String buildWithSn(String sn) {
        return SN_HEARED + sn;
    }

    public static String parseSn(String data) {
        if (data.startsWith(SN_HEARED)) {
            return data.substring(SN_HEARED.length());
        }
        return null;
    }
}