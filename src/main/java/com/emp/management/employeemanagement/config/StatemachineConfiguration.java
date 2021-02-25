package com.emp.management.employeemanagement.config;

import com.emp.management.employeemanagement.model.EmployeeEvents;
import com.emp.management.employeemanagement.model.EmployeeStates;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Log
@Configuration
@EnableStateMachineFactory
public class StatemachineConfiguration extends StateMachineConfigurerAdapter<EmployeeStates, EmployeeEvents> {
    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeStates, EmployeeEvents> transitions) throws Exception {
        transitions
                .withExternal().source(EmployeeStates.ADDED).target(EmployeeStates.IN_CHECK).event(EmployeeEvents.CHECKED)
                .and()
                .withExternal().source(EmployeeStates.IN_CHECK).target(EmployeeStates.APPROVED).event(EmployeeEvents.APPROVE)
                .and().
                 withExternal().source(EmployeeStates.ADDED).target(EmployeeStates.APPROVED).event(EmployeeEvents.APPROVE)
                .and()
                .withExternal().source(EmployeeStates.APPROVED).target(EmployeeStates.ACTIVE).event(EmployeeEvents.MAKE_ACTIVE)
                .and()
                .withExternal().source(EmployeeStates.ADDED).target(EmployeeStates.ACTIVE).event(EmployeeEvents.MAKE_ACTIVE)
                .and()
                .withExternal().source(EmployeeStates.IN_CHECK).target(EmployeeStates.ACTIVE).event(EmployeeEvents.MAKE_ACTIVE);

    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeStates, EmployeeEvents> states) throws Exception {
        states
                .withStates()
                .initial(EmployeeStates.ADDED)
                .states(EnumSet.allOf(EmployeeStates.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeStates, EmployeeEvents> config) throws Exception {
        StateMachineListenerAdapter<EmployeeStates, EmployeeEvents>adapter=new StateMachineListenerAdapter<EmployeeStates, EmployeeEvents>(){
            @Override
            public void stateChanged(State<EmployeeStates, EmployeeEvents> from, State<EmployeeStates, EmployeeEvents> to) {
                log.info(String.format("stateChanged(from: %s, to: %s)", from + "", to + ""));
            }
        };
        config.withConfiguration()
                .autoStartup(false)
                .listener(adapter);
    }
}
