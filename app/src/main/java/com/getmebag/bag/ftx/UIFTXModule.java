package com.getmebag.bag.ftx;

import dagger.Module;

/**
 * Created by karthiktangirala on 1/2/15.
 */
@Module(
        injects = {
                FTXTutorialActivity.class,
                FTXTutorialFragment.class,
                FTXSlideOneFragment.class,
                FTXSlideTwoFragment.class,
                FTXSlideThreeFragment.class,
        },
        complete = false
)
public class UIFTXModule {
    // TODO put your application-specific providers here!
}
