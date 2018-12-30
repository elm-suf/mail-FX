package logger;

public class Log {
    private String level;
    private String TAG;
    private String message;

    @Override
    public String toString() {
        return "Log{ level='" + level + '\'' + ", TAG='" + TAG + '\'' + ", message='" + message + '\'' + '}';
    }

    public Log(String level, String tag, String message) {
        this.level = level;
        this.TAG = tag;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getTag() {
        return TAG;
    }

    public String getLevel() {
        return level;
    }
}
