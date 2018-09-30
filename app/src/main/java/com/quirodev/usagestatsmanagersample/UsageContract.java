package com.quirodev.usagestatsmanagersample;

import java.util.List;

public interface UsageContract {

    interface View{

        void onUserHasNoPermission();
    }

    interface Presenter{
        List<UsageStatsWrapper> retrieveUsageStats();
    }
}
