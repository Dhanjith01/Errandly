package com.errandly.Errandly.errand.errand_state;

import com.errandly.Errandly.errand.entity.Errand;
import com.errandly.Errandly.errand.entity.ErrandStatus;

public class ErrandCreatedState implements ErrandState {
    public void next(Errand errand){
        errand.setStatus(ErrandStatus.PAID);
    }
}
