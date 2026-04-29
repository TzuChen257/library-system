package util;

/**
 * Settings：存放跟規則有關的設定（可直接取用）
 */
public class Settings {

    // 借閱天數
    public static final int BORROW_DAYS = 30;
    public static final int RENEW_MAX_DAYS = 60;

    // 借閱上限（依等級）
    public static int borrowLimitByLevel(int readerLevel) {
        // -1：失誤未處理；0：負分禁權益；1：一般；2：優良；3：愛書人士
        if (readerLevel <= 0) return 0;
        if (readerLevel == 1) return 3;
        if (readerLevel == 2) return 7;
        return 10;
    }

    // 點數規則（基礎示範：你可再細化成「每月最多10」與「季結算」）
    public static final int POINT_BORROW_RETURN_OK = 1;
    public static final int POINT_REVIEW_APPROVED = 3;

    public static final int MESSAGE_PICKUP_READY = 3;
    public static final int MESSAGE_OVERDUE_REMIND = 5;
    public static final int MESSAGE_OVERDUE_TODAY = 6;
}
