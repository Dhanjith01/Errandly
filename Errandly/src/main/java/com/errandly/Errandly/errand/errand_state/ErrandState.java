package com.errandly.Errandly.errand.errand_state;

import com.errandly.Errandly.errand.entity.Errand;

public interface ErrandState {
    public void next(Errand errand);
}
