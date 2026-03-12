package com.errandly.Errandly.errandtest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.errandly.Errandly.errand.entity.ErrandStatus;
import com.errandly.Errandly.errand.errand_state.ErrandStateManager;


public class ErrandStateManagerTest {
    private ErrandStateManager stateManager;

    @BeforeEach
    public void setup(){
        stateManager=new ErrandStateManager();
    }

    @Test
    public void shouldReturnTrue(){
        ErrandStatus state1=ErrandStatus.CREATED;
        ErrandStatus state2=ErrandStatus.PAID;

        assertTrue(stateManager.checkTransition(state1, state2));
    }

}
