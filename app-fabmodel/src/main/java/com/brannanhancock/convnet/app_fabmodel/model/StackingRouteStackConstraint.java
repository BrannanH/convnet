package com.brannanhancock.convnet.app_fabmodel.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class StackingRouteStackConstraint implements RouteStackConstraint {
    @Override
    public String where() {
        return "Prospective machine id must match the id of the most recent machine of that type this path has seen";
    }

    @Override
    public boolean isMachineWithConfigurationPlausibleNextStep(MachineWithConfigurationRequirements possibleNextStep, Deque<MachineWithConfigurationRequirements> routeStack) {
        Deque<MachineWithConfigurationRequirements> defensiveCopy = new LinkedList<>(routeStack);
        while (!defensiveCopy.isEmpty()) {
            MachineWithConfigurationRequirements top = defensiveCopy.removeFirst();
            if (Objects.equals(top.machine().machineType(), possibleNextStep.machine().machineType())) {
                return top.machine().id() == possibleNextStep.machine().id();
            }
        }
        return true;
    }
}
