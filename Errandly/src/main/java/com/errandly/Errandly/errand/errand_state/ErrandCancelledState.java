package com.errandly.Errandly.errand.errand_state;

import com.errandly.Errandly.errand.entity.Errand;

public class ErrandCancelledState implements ErrandState {
    public void next(Errand errand){
        throw new IllegalStateException("Errand already cancelled");
    }
}
