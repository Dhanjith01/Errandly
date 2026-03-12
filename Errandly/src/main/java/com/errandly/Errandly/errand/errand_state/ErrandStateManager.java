package com.errandly.Errandly.errand.errand_state;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.errandly.Errandly.errand.entity.ErrandStatus;

public class ErrandStateManager {
    private final Map<ErrandStatus,Set<ErrandStatus>> statemap=new HashMap<>();
    public ErrandStateManager(){
        statemap.put(ErrandStatus.CREATED,Set.of(ErrandStatus.PAID,ErrandStatus.CANCELLED));
        statemap.put(ErrandStatus.PAID,Set.of(ErrandStatus.ACCEPTED,ErrandStatus.CANCELLED));
        statemap.put(ErrandStatus.ACCEPTED,Set.of(ErrandStatus.IN_PROGRESS,ErrandStatus.CANCELLED));
        statemap.put(ErrandStatus.IN_PROGRESS,Set.of(ErrandStatus.DELIVERED));
        statemap.put(ErrandStatus.DELIVERED,Set.of(ErrandStatus.COMPLETED));
        statemap.put(ErrandStatus.COMPLETED,Set.of());
        statemap.put(ErrandStatus.CANCELLED,Set.of());
    }
    
    public boolean checkTransition(ErrandStatus x,ErrandStatus y){
        return statemap.get(x).contains(y);
    }
}
