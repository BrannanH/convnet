package com.brannanhancock.convnet.fundamentals;

import com.brannanhancock.convnet.fundamentals.layers.Layer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConvNet {

    private final int[] inputDimensions;
    private final List<Layer> layers;

    private ConvNet(int[] inputDimensions, List<Layer> layers) {
        this.inputDimensions = inputDimensions;
        this.layers = layers;
    }

    public int[] getInputDimensions() {
        return inputDimensions;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvNet convNet = (ConvNet) o;
        return Objects.deepEquals(inputDimensions, convNet.inputDimensions) && Objects.equals(layers, convNet.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(inputDimensions), layers);
    }

    @Override
    public String toString() {
        return "ConvNet{" +
                "inputDimensions=" + Arrays.toString(inputDimensions) +
                ", layers=" + layers +
                '}';
    }

    static class Builder {

        int[] inputDimensions;
        List<Layer> layers = new ArrayList<>();

        private Builder() {
            // no instances for you!
        }

        static Builder builder() {
            return new Builder();
        }

        Builder withInputDimensions(int[] inputDimensions) {
            this.inputDimensions = inputDimensions;
            return this;
        }

        Builder withNextLayer(Layer layer) {
            layers.add(layer);
            return this;
        }

        ConvNet build() {
            return new ConvNet(inputDimensions, List.copyOf(layers));
        }
    }
}
