package com.emp.management.employeemanagement.serviceimpl;

import com.emp.management.employeemanagement.model.Employee;
import com.emp.management.employeemanagement.model.EmployeeEvents;
import com.emp.management.employeemanagement.model.EmployeeStates;
import com.emp.management.employeemanagement.repository.EmployeeRepository;
import com.emp.management.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Optional;
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository emprepo;
    private static final String EMPLOYEE_ID_HEADER = "employeeId";
    private final StateMachineFactory<EmployeeStates, EmployeeEvents> factory;
    @Override
    public Employee savOrUpdateEmployee(Employee emp) {

        return emprepo.save(emp);
    }
    @Override
    public Boolean isEmployeeExistsByEmail(String emailId) {

        return emprepo.existsByEmployeeEmail(emailId);
    }
    @Override
    public Optional<Employee> findByEmailId(String emailId) {
        return emprepo.findByEmployeeEmail(emailId);
    }
    @Override
    public void updateStatus(Long empId,String event){
        if ((event.equalsIgnoreCase("APPROVE"))) {
            markapprove(empId);
        } else if ((event.equalsIgnoreCase("CHECKED"))) {
                markchecked(empId);
            } else if ((event.equalsIgnoreCase("MAKE_ACTIVE"))) {
                makactive(empId);
            }

    }
    public StateMachine<EmployeeStates,EmployeeEvents>markapprove(Long empId){
        StateMachine<EmployeeStates,EmployeeEvents>sm=this.build(empId);
        Message<EmployeeEvents>makeactivemsg= MessageBuilder.withPayload(EmployeeEvents.APPROVE)
                .setHeader(EMPLOYEE_ID_HEADER,empId).build();
        sm.sendEvent(makeactivemsg);
        return sm;
    }

    public StateMachine<EmployeeStates,EmployeeEvents>markchecked(Long empId){
        StateMachine<EmployeeStates,EmployeeEvents>sm=this.build(empId);
        Message<EmployeeEvents>makeactivemsg= MessageBuilder.withPayload(EmployeeEvents.CHECKED)
                .setHeader(EMPLOYEE_ID_HEADER,empId).build();
        sm.sendEvent(makeactivemsg);
        return sm;
    }

    public StateMachine<EmployeeStates,EmployeeEvents>makactive(Long empId){
        StateMachine<EmployeeStates,EmployeeEvents>sm=this.build(empId);
        Message<EmployeeEvents>makeactivemsg= MessageBuilder.withPayload(EmployeeEvents.MAKE_ACTIVE)
                .setHeader(EMPLOYEE_ID_HEADER,empId).build();
        sm.sendEvent(makeactivemsg);
                return sm;
    }
    private StateMachine<EmployeeStates,EmployeeEvents>build (Long empId){
        Employee emp=emprepo.findById(empId).get();
        String empIdKey = Long.toString(emp.getEmployeeId());
        StateMachine<EmployeeStates,EmployeeEvents>sm=this.factory.getStateMachine(empIdKey);
        sm.stop();
        sm.getStateMachineAccessor()
                .doWithAllRegions( sma->{
                    sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<EmployeeStates,EmployeeEvents>(){
                                                       @Override
                                                       public void preStateChange(State<EmployeeStates, EmployeeEvents> state, Message<EmployeeEvents> message, Transition<EmployeeStates, EmployeeEvents> transition, StateMachine<EmployeeStates, EmployeeEvents> stateMachine) {
                                                           Optional.ofNullable(message).ifPresent(msg->{
                                                               Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(EMPLOYEE_ID_HEADER,-1L)))
                                                                       .ifPresent(empId1->{
                                                                           Employee emp1=emprepo.findById(empId1).get();
                                                                           emp1.setStatus(state.getId());
                                                                           emprepo.save(emp1);
                                                                               });
                                                                   });
                                                       }
                                                   });
                });

                sm.start();
        return sm;
    }
}
