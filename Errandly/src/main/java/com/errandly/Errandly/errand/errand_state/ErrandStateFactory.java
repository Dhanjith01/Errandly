package com.errandly.Errandly.errand.errand_state;

import java.util.HashMap;
import java.util.Map;

import com.errandly.Errandly.errand.entity.Errand;
import com.errandly.Errandly.errand.entity.ErrandStatus;

public class ErrandStateFactory {
    
    private final Map<ErrandStatus,ErrandState> statemap=new HashMap<>();
    ErrandStateFactory(){
        statemap.put(ErrandStatus.CREATED,new ErrandCreatedState());
        statemap.put(ErrandStatus.PAID,new ErrandPaidState());
        statemap.put(ErrandStatus.ACCEPTED,new ErrandAcceptedState());
        statemap.put(ErrandStatus.DELIVERED,new ErrandDeliveredState());
        statemap.put(ErrandStatus.COMPLETED,new ErrandCompletedState());
        statemap.put(ErrandStatus.IN_PROGRESS,new ErrandInProgressState());
        statemap.put(ErrandStatus.CANCELLED,new ErrandCancelledState());
    }
    public void init_state(Errand errand){
        errand.setState(statemap.get(errand.getStatus()));
    }
}
