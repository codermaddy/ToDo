package android.example.com.todo;

import java.util.UUID;

public class Task {

    private UUID mUUID;
    private String mHeading;
    private String mDetail;
    private boolean mPending;

    public Task(String heading, String detail, boolean pending) {
        setHeading(heading);
        setDetail(detail);
        setPending(pending);
        mUUID =  UUID.randomUUID();
    }

    public UUID getUUID(){
        return mUUID;
    }

    public String getHeading() {
        return mHeading;
    }

    public void setHeading(String heading) {
        mHeading = heading;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public boolean isPending() {
        return mPending;
    }

    public void setPending(boolean pending) {
        mPending = pending;
    }
}
